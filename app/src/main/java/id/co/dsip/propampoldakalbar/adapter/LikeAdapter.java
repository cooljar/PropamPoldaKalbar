package id.co.dsip.propampoldakalbar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.model.Like;
import id.co.dsip.propampoldakalbar.model.Polisi;

/**
 * Created by japra_awok on 02/05/2017.
 */

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder>{

    private Context context;
    private List<Like> likeList;

    public LikeAdapter(Context context, List<Like> likeList) {
        this.context = context;
        this.likeList = likeList;
    }

    @Override
    public LikeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.like_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LikeAdapter.ViewHolder holder, int position) {
        Like like = likeList.get(position);

        if(like.creator.polisi != null){
            Polisi polisi = like.creator.polisi;
            String pangkat = polisi.pangkat.singkatan;
            holder.tvNama.setText(polisi.nama + ", " + pangkat + " NRP: " + polisi.nrp);

            Picasso.with(context)
                    .load(polisi.pas_foto)
                    //.resize(100, 100)
                    //.onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                    .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                    .into(holder.ivFoto);
        }else if(like.creator.masyarakat != null){
            holder.tvNama.setText(like.creator.masyarakat.nama);

            Picasso.with(context)
                    .load(like.creator.masyarakat.pas_foto)
                    //.resize(100, 100)
                    //.onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                    .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                    .into(holder.ivFoto);
        }

        holder.tvWaktu.setText(like.created_at);
    }

    @Override
    public int getItemCount() {
        return likeList == null ? 0 : likeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivFoto) CircleImageView ivFoto;
        @BindView(R.id.tvNama) TextView tvNama;
        @BindView(R.id.tvWaktu) TextView tvWaktu;

        public ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }
}
