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
import id.co.dsip.propampoldakalbar.model.Instruksi;
import id.co.dsip.propampoldakalbar.model.Polisi;

/**
 * Created by japra_awok on 27/04/2017.
 */

public class InstruksiAdapter extends RecyclerView.Adapter<InstruksiAdapter.ViewHolder>{

    private Context context;
    private List<Instruksi> instruksiList;
    private OnItemClickListener clickListener;
    private int layoutWidth, layoutHeight;

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public InstruksiAdapter(Context context, List<Instruksi> instruksiList, int layoutWidth, int layoutHeight) {
        this.context = context;
        this.instruksiList = instruksiList;
        this.layoutWidth = layoutWidth;
        this.layoutHeight = layoutHeight;
    }

    @Override
    public InstruksiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.instruksi_list_row, parent, false);
        return new InstruksiAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InstruksiAdapter.ViewHolder holder, int position) {
        final Instruksi instruksi = instruksiList.get(position);

        Polisi polisi = instruksi.instruktor.polisi;
        Picasso.with(context)
                .load(polisi.pas_foto)
                //.resize(layoutWidth, 0)
                //.onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                .transform(new FitXyTransformation(layoutWidth / 7, false))
                .into(holder.ivGbr);

        holder.tvNama.setText(polisi.nama);
        holder.tvPangkatNrp.setText(polisi.pangkat.singkatan + " NRP " + polisi.nrp);
        holder.tvPerihal.setText(instruksi.perihal);
        holder.tvTanggal.setText(instruksi.created_at);
    }

    @Override
    public int getItemCount() {
        return instruksiList == null ? 0 : instruksiList.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        instruksiList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Instruksi> list) {
        instruksiList.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        @BindView(R.id.ivGbr) ImageView ivGbr;
        @BindView(R.id.tvNama) TextView tvNama;
        @BindView(R.id.tvPangkatNrp) TextView tvPangkatNrp;
        @BindView(R.id.tvPerihal) TextView tvPerihal;
        @BindView(R.id.tvTanggal) TextView tvTanggal;

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
