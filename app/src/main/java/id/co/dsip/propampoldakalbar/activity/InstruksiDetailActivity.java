package id.co.dsip.propampoldakalbar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.helpers.FitXyTransformation;
import id.co.dsip.propampoldakalbar.model.Instruksi;
import id.co.dsip.propampoldakalbar.model.Polisi;
import id.co.dsip.propampoldakalbar.model.UserSession;

public class InstruksiDetailActivity extends AppCompatActivity {

    public static final String INSTRUKSI_PARCEL = "instruksi_parcel";
    public static final String USER_SESSION_PARCEL = "user_session_parcel";
    public static final String LAYOUT_WIDTH_PARCEL = "layout_width_parcel";
    public static final String LAYOUT_HEIGHT_PARCEL = "layout_height_parcel";

    private Instruksi mInstruksi;
    private UserSession user;
    private int layoutWidth, layoutHeight;

    @BindView(R.id.ivGbr) ImageView ivGbr;
    @BindView(R.id.tvNama) TextView tvNama;
    @BindView(R.id.tvPangkatNrp) TextView tvPangkatNrp;
    @BindView(R.id.tvPenempatan) TextView tvPenempatan;
    @BindView(R.id.tvJabatan) TextView tvJabatan;
    @BindView(R.id.tvTanggal) TextView tvTanggal;
    @BindView(R.id.tvPerihal) TextView tvPerihal;
    @BindView(R.id.tvIsiInstruksi) TextView tvIsiInstruksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruksi_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Instruksi");

        Intent intent = getIntent();
        mInstruksi = Parcels.unwrap(intent.getParcelableExtra(INSTRUKSI_PARCEL));
        user = Parcels.unwrap(intent.getParcelableExtra(USER_SESSION_PARCEL));
        layoutWidth = intent.getIntExtra(LAYOUT_WIDTH_PARCEL, 0);
        layoutHeight = intent.getIntExtra(LAYOUT_HEIGHT_PARCEL, 0);

        Polisi polisi = mInstruksi.instruktor.polisi;

        ButterKnife.bind(this);

        Picasso.with(this)
                .load(polisi.pas_foto)
                //.resize(layoutWidth, 0)
                //.onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                .transform(new FitXyTransformation(layoutWidth / 7, false))
                .into(ivGbr);

        tvNama.setText(polisi.nama);
        tvPangkatNrp.setText(polisi.pangkat.singkatan + " NRP " + polisi.nrp);
        tvPenempatan.setText(polisi.penempatan.name);
        tvJabatan.setText(polisi.jabatan.singkatan);

        tvTanggal.setText(mInstruksi.created_at);
        tvPerihal.setText(mInstruksi.perihal);
        tvIsiInstruksi.setText(mInstruksi.deskripsi);
    }

}
