package id.co.dsip.propampoldakalbar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.fragment.ComentLapGiatDialogFragment;
import id.co.dsip.propampoldakalbar.fragment.ListAttacmentDialogFragment;
import id.co.dsip.propampoldakalbar.model.Coment;
import id.co.dsip.propampoldakalbar.model.LapGiat;
import id.co.dsip.propampoldakalbar.model.Like;
import id.co.dsip.propampoldakalbar.model.Masyarakat;
import id.co.dsip.propampoldakalbar.model.Polisi;
import id.co.dsip.propampoldakalbar.model.UserSession;
import id.co.dsip.propampoldakalbar.rest_api.ErrorMessage;
import id.co.dsip.propampoldakalbar.rest_api.MyOkHttpInterceptor;
import id.co.dsip.propampoldakalbar.rest_api.ServiceGenerator;
import id.co.dsip.propampoldakalbar.rest_api.TransactionEndPoint;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LapGiatDetailActivity extends AppCompatActivity implements OnMapReadyCallback,
        ComentLapGiatDialogFragment.OnComentPostedListener{

    public static final String LAP_GIAT_PARCEL = "lap_giat_parcel";
    public static final String USER_SESSION_PARCEL = "user_session_parcel";
    public static final String LAYOUT_WIDTH_PARCEL = "layout_width_parcel";
    public static final String LAYOUT_HEIGHT_PARCEL = "layout_height_parcel";

    private UserSession user;
    private int layoutWidth, layoutHeight;
    private GoogleMap mMap;
    private MapFragment mapFragment;
    private LapGiat mLapGiat;

    @BindView(R.id.tvJenis) TextView tvJenis;
    @BindView(R.id.tvJudul) TextView tvJudul;
    @BindView(R.id.tvNama) TextView tvNama;
    @BindView(R.id.tvWaktu) TextView tvWaktu;
    @BindView(R.id.tvDeskripsi) TextView tvDeskripsi;
    @BindView(R.id.tvComentCount) TextView tvComentCount;
    @BindView(R.id.tvLikeCount) TextView tvLikeCount;
    @BindView(R.id.btAttachment) Button btAttachment;

    @BindView(R.id.ivProfile) CircleImageView ivProfile;
    @BindView(R.id.ivComment) CircleImageView ivComment;
    @BindView(R.id.ivLike) CircleImageView ivLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lap_giat_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Lap. Giat");

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mLapGiat = Parcels.unwrap(intent.getParcelableExtra(LAP_GIAT_PARCEL));
        user = Parcels.unwrap(intent.getParcelableExtra(USER_SESSION_PARCEL));
        layoutWidth = intent.getIntExtra(LAYOUT_WIDTH_PARCEL, 0);
        layoutHeight = intent.getIntExtra(LAYOUT_HEIGHT_PARCEL, 0);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
        params.height = layoutHeight / 4;
        mapFragment.getView().setLayoutParams(params);

        assignViewData();
    }

    @OnClick(R.id.btAttachment)
    public void viewAttahment(View view) {
        FragmentManager fm = getSupportFragmentManager();
        ListAttacmentDialogFragment liastAttachmentFragment = ListAttacmentDialogFragment.newInstance(
                mLapGiat.trnLapGiatAttachments,
                user,
                layoutWidth
        );
        liastAttachmentFragment.show(fm, "fragment_list_attachment");
    }

    @Override
    protected void onStart() {
        mapFragment.getMapAsync(this);
        super.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double lat = mLapGiat.lat;
        double lng = mLapGiat.lng;

        final LatLng currentLocationPoint = new LatLng(lat, lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocationPoint, 15);
        mMap.animateCamera(cameraUpdate, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                mMap.addMarker(new MarkerOptions().position(currentLocationPoint).title(mLapGiat.creator.polisi.nama));
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void assignViewData(){
        tvJenis.setText(mLapGiat.jenis.nama);
        tvJudul.setText(mLapGiat.judul);

        if(mLapGiat.creator.polisi != null){
            Polisi polisi = mLapGiat.creator.polisi;
            String pangkat = polisi.pangkat.singkatan;
            tvNama.setText(polisi.nama + ", " + pangkat + " NRP: " + polisi.nrp);

            Picasso.with(this)
                    .load(polisi.pas_foto)
                    //.resize(100, 100)
                    //.onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                    .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                    .into(ivProfile);
        }else if(mLapGiat.creator.masyarakat != null){
            Masyarakat masyarakat = mLapGiat.creator.masyarakat;
            tvNama.setText(masyarakat.nama);
        }

        tvWaktu.setText(mLapGiat.created_at);
        tvDeskripsi.setText(mLapGiat.deskripsi);
        tvComentCount.setText(String.valueOf(mLapGiat.trnLapGiatComents.size()));
        tvLikeCount.setText(String.valueOf(mLapGiat.trnLapGiatLikes.size()));

        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                ComentLapGiatDialogFragment comentFragment = ComentLapGiatDialogFragment.newInstance(mLapGiat, user);
                comentFragment.show(fm, "fragment_komentar");
            }
        });

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postLike(mLapGiat.id);
            }
        });
    }

    @Override
    public void onComentPosted(Coment coment) {
        mLapGiat.trnLapGiatComents.add(coment);
        tvComentCount.setText(String.valueOf(mLapGiat.trnLapGiatComents.size()));
    }

    private void postLike(String lapGiatId){
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Mengirim data");
        progress.setMessage("Mohon tunggu......");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        progress.show();

        Map<String,String> params = new HashMap<String, String>();
        params.put("TrnLapGiatLike[lap_giat_id]", lapGiatId);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + user.user.auth_key);

        OkHttpClient httpClient = new MyOkHttpInterceptor(headers).getOkHttpClient();
        TransactionEndPoint service = ServiceGenerator.createService(TransactionEndPoint.class, httpClient);
        Call<Like> call = service.createLikeLapGiat(params);

        call.enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                progress.dismiss();
                Gson gson = new Gson();

                if(response.isSuccessful()){
                    mLapGiat.trnLapGiatLikes.add(response.body());
                    tvLikeCount.setText(String.valueOf(mLapGiat.trnLapGiatLikes.size()));
                    ivLike.setImageResource(R.drawable.heart_on);
                    ivLike.setOnClickListener(null);
                }else{
                    String failureMessage = response.message();

                    try {
                        String errorBody = response.errorBody().string();

                        if(response.code() == 422){
                            Type errorType = new TypeToken<List<ErrorMessage>>(){}.getType();
                            List<ErrorMessage> errorMessages = gson.fromJson(errorBody, errorType);

                            String errMsg = failureMessage + "\n";
                            for(ErrorMessage err : errorMessages){
                                errMsg = errMsg.concat(err.field + ": " + err.message + "\n");
                            }
                            Toast.makeText(LapGiatDetailActivity.this, errMsg, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(LapGiatDetailActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(LapGiatDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(LapGiatDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
