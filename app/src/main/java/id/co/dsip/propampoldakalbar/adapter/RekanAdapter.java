package id.co.dsip.propampoldakalbar.adapter;

import android.content.Context;
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
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.helpers.FitXyTransformation;
import id.co.dsip.propampoldakalbar.helpers.OnItemClickListener;
import id.co.dsip.propampoldakalbar.model.Polisi;

/**
 * Created by japra_awok on 22/04/2017.
 */

public class RekanAdapter extends RecyclerView.Adapter<RekanAdapter.ViewHolder>{

    private Context context;
    private List<Polisi> anggotaList;
    private OnItemClickListener clickListener;
    private int layoutWidth, layoutHeight;

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public RekanAdapter(Context context, List<Polisi> anggotaList, int layoutWidth, int layoutHeight) {
        this.context = context;
        this.anggotaList = anggotaList;
        this.layoutWidth = layoutWidth;
        this.layoutHeight = layoutHeight;
    }

    @Override
    public RekanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rekan_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RekanAdapter.ViewHolder holder, int position) {
        final Polisi anggota = anggotaList.get(position);

        Picasso.with(context)
                .load(anggota.pas_foto)
                //.resize(layoutWidth, 0)
                //.onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                .transform(new FitXyTransformation(layoutWidth / 7, false))
                .into(holder.ivGbr);

        holder.tvNama.setText(anggota.nama);
        holder.tvPangkatNrp.setText(anggota.pangkat.singkatan + " NRP " + anggota.nrp);
        holder.tvPenempatan.setText(anggota.penempatan.name);
        holder.tvJabatan.setText(anggota.jabatan.singkatan);
    }

    @Override
    public int getItemCount() {
        return anggotaList == null ? 0 : anggotaList.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        anggotaList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Polisi> list) {
        anggotaList.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        @BindView(R.id.ivGbr) ImageView ivGbr;
        @BindView(R.id.tvNama) TextView tvNama;
        @BindView(R.id.tvPangkatNrp) TextView tvPangkatNrp;
        @BindView(R.id.tvPenempatan) TextView tvPenempatan;
        @BindView(R.id.tvJabatan) TextView tvJabatan;

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
