package id.co.dsip.propampoldakalbar.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.helpers.FitXyTransformation;
import id.co.dsip.propampoldakalbar.model.Polisi;
import id.co.dsip.propampoldakalbar.model.UserSession;

public class DetailRekanActivity extends AppCompatActivity {

    public static final String REKAN_PARCEL = "rekan_parcel";
    public static final String USER_SESSION_PARCEL = "user_session_parcel";
    public static final String LAYOUT_WIDTH_PARCEL = "layout_width_parcel";
    public static final String LAYOUT_HEIGHT_PARCEL = "layout_height_parcel";

    private Polisi mPolisi;
    private UserSession user;
    private int layoutWidth, layoutHeight;

    @BindView(R.id.ivKta) ImageView ivKta;
    @BindView(R.id.ivPasFoto) ImageView ivPasFoto;
    @BindView(R.id.tvNama) TextView tvNama;
    @BindView(R.id.tvPangkat) TextView tvPangkat;
    @BindView(R.id.tvNrp) TextView tvNrp;
    @BindView(R.id.tvPenempatan) TextView tvPenempatan;
    @BindView(R.id.tvJabatan) TextView tvJabatan;
    @BindView(R.id.tvHp) TextView tvHp;
    @BindView(R.id.tvEmail) TextView tvEmail;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.llHp) LinearLayout llHp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rekan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Rekan");

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mPolisi = Parcels.unwrap(intent.getParcelableExtra(REKAN_PARCEL));
        user = Parcels.unwrap(intent.getParcelableExtra(USER_SESSION_PARCEL));
        layoutWidth = intent.getIntExtra(LAYOUT_WIDTH_PARCEL, 0);
        layoutHeight = intent.getIntExtra(LAYOUT_HEIGHT_PARCEL, 0);

        Picasso.with(this)
                .load(mPolisi.pas_foto)
                .transform(new FitXyTransformation(layoutWidth, true))
                .placeholder(R.drawable.loading) // can also be a drawable
                .into(ivPasFoto)
        ;

        Picasso.with(this)
                .load(mPolisi.foto_kta)
                .transform(new FitXyTransformation(layoutWidth, true))
                .placeholder(R.drawable.loading) // can also be a drawable
                .into(ivKta)
        ;

        tvNama.setText(mPolisi.nama);
        tvPangkat.setText(mPolisi.pangkat.singkatan);
        tvNrp.setText(mPolisi.nrp);
        tvPenempatan.setText(mPolisi.penempatan.name);
        tvJabatan.setText(mPolisi.jabatan.singkatan);
        tvHp.setText(mPolisi.hp);
        tvEmail.setText(mPolisi.user.email);
    }

    @OnClick(R.id.fab)
    public void chatWithRekan(View view){
        Intent intent = new Intent(DetailRekanActivity.this, ChatActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable(ChatActivity.USER_SESSION_PARCEL, Parcels.wrap(user));
        bundle.putParcelable(ChatActivity.REKAN_PARCEL, Parcels.wrap(mPolisi));

        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.llHp)
    public void createPhoneCall(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mPolisi.hp));
        startActivity(intent);
    }
}
