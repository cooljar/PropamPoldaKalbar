package id.co.dsip.propampoldakalbar.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.helpers.FitXyTransformation;
import id.co.dsip.propampoldakalbar.model.Attachment;
import id.co.dsip.propampoldakalbar.ui.CustomTextureVideoView;
import id.co.dsip.propampoldakalbar.util.UIUtils;

/**
 * Created by japra_awok on 15/04/2017.
 */

public class ListAttachmentAdapter extends RecyclerView.Adapter<ListAttachmentAdapter.VideoViewHolder>{

    private Context context;
    private List<Attachment> mAttachment;
    private int layoutWidth;

    WeakReference<Activity> activityWeakReference;

    private Activity getActivity() {
        return activityWeakReference.get();
    }

    public ListAttachmentAdapter(Context context, List<Attachment> att, Activity activity, int layoutWidth) {
        this.context = context;
        this.mAttachment = att;
        this.activityWeakReference = new WeakReference<>(activity);
        this.layoutWidth = layoutWidth;
    }

    @Override
    public ListAttachmentAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attachment_list_item, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListAttachmentAdapter.VideoViewHolder holder, int position) {
        Attachment attachment = mAttachment.get(position);

        String attType = attachment.file_type;
        int slashIndex = attType.indexOf("/");
        String fileType = attType.substring(0, slashIndex);

        switch (fileType){
            case "image":
                holder.FlVideo.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(attachment.file_location)
                        .transform(new FitXyTransformation(layoutWidth, false))
                        //.resize(100, 100)
                        //.onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                        .placeholder(R.drawable.loading) // can also be a drawable
                        .into(holder.ivImage);
                break;
            case "video":
                holder.ivImage.setVisibility(View.GONE);

                holder.videoUrl = attachment.file_location;
                holder.imageLoaderProgressBar.setVisibility(View.INVISIBLE);
                holder.videoImageView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onViewRecycled(VideoViewHolder holder) {
        if (holder == currentVideoViewHolder) {
            currentVideoViewHolder = null;
            holder.stopVideo();
        }
        holder.videoView.stopPlayback();
        super.onViewRecycled(holder);

    }

    @Override
    public int getItemCount() {
        return mAttachment == null ? 0 : mAttachment.size();
    }

    public void onScrolled(RecyclerView recyclerView) {
        if (currentVideoViewHolder != null) {
            currentVideoViewHolder.onScrolled(recyclerView);
        }
    }

    VideoViewHolder currentVideoViewHolder;

    public class VideoViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivImage) ImageView ivImage;

        @BindView(R.id.FlVideo) FrameLayout FlVideo;
        @BindView(R.id.video_play_img_btn) ImageView videoPlayImageButton;
        @BindView(R.id.lyt_image_loader_progress_bar) ProgressBar imageLoaderProgressBar;
        @BindView(R.id.video_feed_item_video) CustomTextureVideoView videoView;
        @BindView(R.id.video_feed_item_video_image) ImageView videoImageView;

        String videoUrl;

        public String getVideoUrl() {
            return videoUrl;
        }

        public VideoViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mp) {
                    Log.v("Video", "onPrepared" + videoView.getVideoPath());
                    int width = mp.getVideoWidth();
                    int height = mp.getVideoHeight();
                    videoView.setIsPrepared(true);
                    UIUtils.resizeView(videoView, UIUtils.getScreenWidth(getActivity()), UIUtils.getScreenWidth(getActivity()) * height / width);
                    if (currentVideoViewHolder == VideoViewHolder.this) {
                        videoImageView.setVisibility(View.GONE);
                        imageLoaderProgressBar.setVisibility(View.INVISIBLE);
                        videoView.setVisibility(View.VISIBLE);
                        videoView.seekTo(0);
                        videoView.start();
                    }
                }
            });
            videoView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.v("Video", "onFocusChange" + hasFocus);
                    if (!hasFocus && currentVideoViewHolder == VideoViewHolder.this) {
                        stopVideo();
                    }

                }
            });
            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    Log.v("Video", "onInfo" + what + " " + extra);

                    return false;
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.v("Video", "onCompletion");

                    videoImageView.setVisibility(View.VISIBLE);
                    videoPlayImageButton.setVisibility(View.VISIBLE);

                    if (videoView.getVisibility() == View.VISIBLE)
                        videoView.setVisibility(View.INVISIBLE);


                    imageLoaderProgressBar.setVisibility(View.INVISIBLE);
                    currentVideoViewHolder = null;
                }
            });
            videoPlayImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentVideoViewHolder != null && currentVideoViewHolder != VideoViewHolder.this) {
                        currentVideoViewHolder.videoView.pause();
                        currentVideoViewHolder.videoImageView.setVisibility(View.INVISIBLE);
                        currentVideoViewHolder.videoPlayImageButton.setVisibility(View.VISIBLE);
                        currentVideoViewHolder.imageLoaderProgressBar.setVisibility(View.INVISIBLE);
                        if (currentVideoViewHolder.videoView.getVisibility() == View.VISIBLE)
                            currentVideoViewHolder.videoView.setVisibility(View.INVISIBLE);


                        currentVideoViewHolder = null;
                    }

                    currentVideoViewHolder = VideoViewHolder.this;

                    videoPlayImageButton.setVisibility(View.INVISIBLE);
                    imageLoaderProgressBar.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.VISIBLE);
                    videoImageView.setVisibility(View.INVISIBLE);
                    if (!getVideoUrl().equals(videoView.getVideoPath())) {
                        videoView.setIsPrepared(false);
                        videoView.setVideoPath(getVideoUrl());
                        videoView.requestFocus();
                    } else {
                        if (videoView.isPrepared()) {
                            imageLoaderProgressBar.setVisibility(View.INVISIBLE);
                        } else {
                            imageLoaderProgressBar.setVisibility(View.VISIBLE);
                        }
                        videoView.requestFocus();
                        videoView.seekTo(0);
                        videoView.start();
                    }
                }
            });
        }

        public void stopVideo() {
            Log.v("Video", "stopVideo");

            //imageView is within the visible window
            videoView.pause();
            if (videoView.getVisibility() == View.VISIBLE) {
                videoView.setVisibility(View.INVISIBLE);
            }
            videoImageView.setVisibility(View.VISIBLE);
            videoPlayImageButton.setVisibility(View.VISIBLE);
            imageLoaderProgressBar.setVisibility(View.INVISIBLE);
            currentVideoViewHolder = null;
        }

        public void onScrolled(RecyclerView recyclerView) {
            if (isViewNotVisible(videoPlayImageButton, recyclerView) || isViewNotVisible(imageLoaderProgressBar, recyclerView)) {
                //imageView is within the visible window
                stopVideo();
            }
        }

        public boolean isViewNotVisible(View view, RecyclerView recyclerView) {
            Rect scrollBounds = new Rect();
            recyclerView.getHitRect(scrollBounds);
            return view.getVisibility() == View.VISIBLE && !view.getLocalVisibleRect(scrollBounds);
        }
    }
}
