package id.co.dsip.propampoldakalbar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.model.Coment;
import id.co.dsip.propampoldakalbar.model.Polisi;

/**
 * Created by japra_awok on 13/04/2017.
 */

public class KomentarAdapter extends RecyclerView.Adapter<KomentarAdapter.ViewHolder> {

    private Context context;
    private List<Coment> comentList;

    public KomentarAdapter(Context context, List<Coment> comentList) {
        this.context = context;
        this.comentList = comentList;
    }

    @Override
    public KomentarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.komentar_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(KomentarAdapter.ViewHolder holder, int position) {
        Coment coment = comentList.get(position);

        if(coment.creator.polisi != null){
            Polisi polisi = coment.creator.polisi;
            String pangkat = polisi.pangkat.singkatan;
            holder.tvNama.setText(polisi.nama + ", " + pangkat + " NRP: " + polisi.nrp);

            Picasso.with(context)
                    .load(polisi.pas_foto)
                    //.resize(100, 100)
                    //.onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                    .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                    .into(holder.ivFoto);
        }else if(coment.creator.masyarakat != null){
            holder.tvNama.setText(coment.creator.masyarakat.nama);

            Picasso.with(context)
                    .load(coment.creator.masyarakat.pas_foto)
                    //.resize(100, 100)
                    //.onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                    .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                    .into(holder.ivFoto);
        }

        holder.tvWaktu.setText(coment.created_at);
        holder.tvKomentar.setText(coment.coment);
    }

    @Override
    public int getItemCount() {
        return comentList == null ? 0 : comentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView ivFoto;
        public TextView tvNama;
        public TextView tvWaktu;
        public TextView tvKomentar;

        public ViewHolder(View view) {
            super(view);

            ivFoto = (CircleImageView) view.findViewById(R.id.ivFoto);
            tvNama = (TextView) view.findViewById(R.id.tvNama);
            tvWaktu = (TextView) view.findViewById(R.id.tvWaktu);
            tvKomentar = (TextView) view.findViewById(R.id.tvKomentar);
        }
    }
}
