package id.co.dsip.propampoldakalbar.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.fragment.ComentFragmentDialog;
import id.co.dsip.propampoldakalbar.fragment.LikeFragmentDialog;
import id.co.dsip.propampoldakalbar.helpers.FitXyTransformation;
import id.co.dsip.propampoldakalbar.helpers.OnItemClickListener;
import id.co.dsip.propampoldakalbar.model.Berita;
import id.co.dsip.propampoldakalbar.model.Coment;
import id.co.dsip.propampoldakalbar.model.Like;
import id.co.dsip.propampoldakalbar.model.UserSession;

/**
 * Created by japra_awok on 12/04/2017.
 */

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.ViewHolder> {

    private Context context;
    private List<Berita> beritaList;
    private UserSession user;
    private OnItemClickListener clickListener;
    private int layoutWidth;

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public BeritaAdapter(Context context, List<Berita> beritaList, int layoutWidth, UserSession user) {
        this.context = context;
        this.beritaList = beritaList;
        this.layoutWidth = layoutWidth;
        this.user = user;
    }

    @Override
    public BeritaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.berita_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BeritaAdapter.ViewHolder holder, int position) {
        final Berita berita = beritaList.get(position);

        Picasso.with(context)
                .load(berita.trnBeritaAttachments.get(0).file_location)
                //.resize(layoutWidth, 0)
                //.onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                .placeholder(R.drawable.loading) // can also be a drawable
                .transform(new FitXyTransformation(layoutWidth, false))
                .into(holder.ivAttachment);

        Picasso.with(context)
                .load(berita.creator.polisi.pas_foto)
                .resize(50, 50)
                .onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                //.fit()
                .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                .into(holder.ivProfile);

        holder.tvNama.setText(berita.creator.polisi.nama);
        holder.tvTanggal.setText(berita.created_at);
        holder.tvJenis.setText(berita.jenis.nama);
        holder.tvJudul.setText(berita.judul);

        holder.tvComentCount.setText(String.valueOf(berita.trnBeritaComents.size()));
        holder.tvLikeCount.setText(String.valueOf(berita.trnBeritaLikes.size()));

        holder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Coment> paramComent = berita.trnBeritaComents;
                if(paramComent.size() > 0){
                    AppCompatActivity activity = (AppCompatActivity) context;
                    FragmentManager fm = activity.getSupportFragmentManager();
                    ComentFragmentDialog comentFragment = ComentFragmentDialog.newInstance(paramComent);
                    comentFragment.show(fm, "fragment_komentar");
                }
            }
        });

        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Like> paramLikes = berita.trnBeritaLikes;
                if(paramLikes.size() > 0){
                    AppCompatActivity activity = (AppCompatActivity) context;
                    FragmentManager fm = activity.getSupportFragmentManager();
                    LikeFragmentDialog likeFragment = LikeFragmentDialog.newInstance(paramLikes);
                    likeFragment.show(fm, "fragment_like");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return beritaList == null ? 0 : beritaList.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        beritaList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Berita> list) {
        beritaList.addAll(list);
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
