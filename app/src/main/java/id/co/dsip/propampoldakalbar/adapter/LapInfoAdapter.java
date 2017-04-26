package id.co.dsip.propampoldakalbar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.helpers.FitXyTransformation;
import id.co.dsip.propampoldakalbar.helpers.OnItemClickListener;
import id.co.dsip.propampoldakalbar.model.LapInfo;

/**
 * Created by japra_awok on 18/04/2017.
 */

public class LapInfoAdapter extends RecyclerView.Adapter<LapInfoAdapter.ViewHolder>{

    private Context context;
    private List<LapInfo> lapInfoList;
    private OnItemClickListener clickListener;
    private int layoutWidth;

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public LapInfoAdapter(Context context, List<LapInfo> lapInfoList, int layoutWidth) {
        this.context = context;
        this.lapInfoList = lapInfoList;
        this.layoutWidth = layoutWidth;
    }

    @Override
    public LapInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lap_info_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LapInfoAdapter.ViewHolder holder, int position) {
        final LapInfo lapInfo = lapInfoList.get(position);

        Picasso.with(context)
                .load(lapInfo.trnLapInfoAttachments.get(0).file_location)
                //.resize(layoutWidth, 0)
                //.onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                .placeholder(R.drawable.loading) // can also be a drawable
                .transform(new FitXyTransformation(layoutWidth, false))
                .into(holder.ivAttachment);

        Picasso.with(context)
                .load(lapInfo.creator.polisi.pas_foto)
                .resize(50, 50)
                .onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                //.fit()
                .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                .into(holder.ivProfile);

        holder.tvNama.setText(lapInfo.creator.polisi.nama);
        holder.tvTanggal.setText(lapInfo.created_at);
        holder.tvJenis.setText(lapInfo.jenis.nama);
        holder.tvJudul.setText(lapInfo.judul);

        holder.tvComentCount.setText(String.valueOf(lapInfo.trnLapInfoComents.size()));
        holder.tvLikeCount.setText(String.valueOf(lapInfo.trnLapInfoLikes.size()));

        holder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Comment...", Toast.LENGTH_SHORT).show();
            }
        });

        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Like...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lapInfoList == null ? 0 : lapInfoList.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        lapInfoList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<LapInfo> list) {
        lapInfoList.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        @BindView(R.id.ivProfile) CircleImageView ivProfile;
        @BindView(R.id.ivAttachment) ImageView ivAttachment;
        @BindView(R.id.ivComment) CircleImageView ivComment;
        @BindView(R.id.ivLike) CircleImageView ivLike;
        @BindView(R.id.tvNama) TextView tvNama;
        @BindView(R.id.tvTanggal) TextView tvTanggal;
        @BindView(R.id.tvJenis) TextView tvJenis;
        @BindView(R.id.tvJudul) TextView tvJudul;
        @BindView(R.id.tvComentCount) TextView tvComentCount;
        @BindView(R.id.tvLikeCount) TextView tvLikeCount;

        public ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
