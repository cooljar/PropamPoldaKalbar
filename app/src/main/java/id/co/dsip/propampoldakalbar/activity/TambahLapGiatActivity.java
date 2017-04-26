package id.co.dsip.propampoldakalbar.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.CameraVideoPicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.adapter.AttachmentItemAdapter;
import id.co.dsip.propampoldakalbar.helpers.LocationHelper;
import id.co.dsip.propampoldakalbar.helpers.ScrollableGridView;
import id.co.dsip.propampoldakalbar.model.AttachmentGridItem;
import id.co.dsip.propampoldakalbar.model.Jenis;
import id.co.dsip.propampoldakalbar.model.LapGiat;
import id.co.dsip.propampoldakalbar.model.UserSession;
import id.co.dsip.propampoldakalbar.rest_api.ErrorMessage;
import id.co.dsip.propampoldakalbar.rest_api.MyOkHttpInterceptor;
import id.co.dsip.propampoldakalbar.rest_api.ServiceGenerator;
import id.co.dsip.propampoldakalbar.rest_api.TransactionEndPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahLapGiatActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "TambahLapGiatActivity";
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 9091;
    public static final String USER_PARCEL = "user_parcel";
    public static final String LAYOUT_WIDTH_PARCEL = "layout_width_parcel";
    public static final String LAYOUT_HEIGHT_PARCEL = "layout_height_parcel";

    private UserSession user;
    private MapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Geocoder geocoder;
    private LocationHelper locationHelper;
    private ProgressDialog progress;
    private List<Jenis> jenisLapGiatList = new ArrayList<Jenis>();
    private Map<String, String> jenisMap = new HashMap<String, String>();
    boolean locationUpdateRunning = false;
    private int layoutWidth, layoutHeight;

    @State
    String fotoOutputPath;
    @State String videoOutputPath;

    ImagePicker imagePicker;
    CameraImagePicker cameraImagePicker;
    VideoPickerCallback videoPickerCallback;
    VideoPicker videoPicker;
    CameraVideoPicker cameraVideoPicker;
    ImagePickerCallback imagePickerCallback;

    AttachmentItemAdapter attachmentItemAdapter;

    List<AttachmentGridItem> attachmentItems = new ArrayList<AttachmentGridItem>();

    @BindView(R.id.llProgress) LinearLayout llProgress;
    @BindView(R.id.pbHorizontal) ProgressBar pbHorizontal;
    @BindView(R.id.tvLoading) TextView tvLoading;
    @BindView(R.id.tvLokasi) TextView tvLokasi;
    @BindView(R.id.llForm) LinearLayout llForm;
    @BindView(R.id.spinnerJenisLapGiat) Spinner spinnerJenisLapGiat;
    @BindView(R.id.etJudul) EditText etJudul;
    @BindView(R.id.etDeskripsi) EditText etDeskripsi;
    @BindView(R.id.ibPhoto) ImageButton ibPhoto;
    @BindView(R.id.ibVideo) ImageButton ibVideo;
    @BindView(R.id.ibRemoveAtt) ImageButton ibRemoveAtt;
    @BindView(R.id.gv) ScrollableGridView gv;
    @BindView(R.id.btSubmit) Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_lap_giat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Buat Laporan Kegiatan");

        Icepick.restoreInstanceState(this, savedInstanceState);

        ButterKnife.bind(this);

        progress = new ProgressDialog(this);

        user = Parcels.unwrap(getIntent().getParcelableExtra(USER_PARCEL));
        layoutWidth = getIntent().getIntExtra(LAYOUT_WIDTH_PARCEL, 0);
        layoutHeight = getIntent().getIntExtra(LAYOUT_HEIGHT_PARCEL, 0);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
        params.height = layoutHeight / 4;
        mapFragment.getView().setLayoutParams(params);

        locationHelper = new LocationHelper(this);
        geocoder = new Geocoder(this, Locale.getDefault());

        registerForContextMenu(ibPhoto);
        registerForContextMenu(ibVideo);

        setupLayout();
        setupAttachmentPicker();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setupLayout(){
        llProgress.setVisibility(View.VISIBLE);
        tvLoading.setText("Mendapatkan lokasi terkini anda...");

        llForm.setVisibility(View.GONE);

        boolean playServicesSuported = locationHelper.checkPlayServices();
        boolean locationPermited = true;
        boolean gpsActive = locationHelper.checkGpsActive();
        boolean writeExternalStoragePermited = true;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            locationPermited = locationHelper.checkLocationPermission();

            writeExternalStoragePermited =
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

            if (!writeExternalStoragePermited) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_STORAGE);
            }
        }

        if (playServicesSuported && locationPermited && gpsActive && writeExternalStoragePermited) {
            mapFragment.getMapAsync(this);
        }

        attachmentItemAdapter = new AttachmentItemAdapter(this, attachmentItems, layoutWidth);
        gv.setAdapter(attachmentItemAdapter);
        gv.setExpanded(true);
    }

    private void setupAttachmentPicker(){
        imagePickerCallback = new ImagePickerCallback() {
            @Override
            public void onImagesChosen(List<ChosenImage> list) {
                Log.e("IMG", String.valueOf(list.size()));
                for (ChosenImage img : list){
                    AttachmentGridItem gridItem = new AttachmentGridItem(
                            img.getId(), img.getType(), img.getMimeType(), img.getOriginalPath(), img.getThumbnailPath(), img.getDisplayName()
                    );
                    attachmentItems.add(gridItem);
                }

                // Update the GridView
                attachmentItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String s) {
                Log.e("IMG_ERROR", s);
            }
        };

        videoPickerCallback = new VideoPickerCallback() {
            @Override
            public void onVideosChosen(List<ChosenVideo> list) {
                for (ChosenVideo vid : list){
                    AttachmentGridItem gridItem = new AttachmentGridItem(
                            vid.getId(), vid.getType(), vid.getMimeType(), vid.getOriginalPath(), vid.getPreviewThumbnail(), vid.getDisplayName()
                    );
                    attachmentItems.add(gridItem);

                    Log.e("FOTO_SUCCESS", vid.getMimeType());
                    Log.e("FOTO_SUCCESS", vid.getFileExtensionFromMimeType());
                }

                // Update the GridView
                attachmentItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String s) {
                Log.e("VIDEO_ERROR", s);
            }
        };

        imagePicker = new ImagePicker(TambahLapGiatActivity.this);
        imagePicker.setImagePickerCallback(imagePickerCallback);
        //imagePicker.allowMultiple();// Default is false
        imagePicker.shouldGenerateMetadata(true); // Default is true
        imagePicker.shouldGenerateThumbnails(true); // Default is true

        cameraImagePicker = new CameraImagePicker(TambahLapGiatActivity.this);
        cameraImagePicker.setImagePickerCallback(imagePickerCallback);
        cameraImagePicker.shouldGenerateMetadata(true); // Default is true
        cameraImagePicker.shouldGenerateThumbnails(true); // Default is true

        videoPicker = new VideoPicker(TambahLapGiatActivity.this);
        videoPicker.setVideoPickerCallback(videoPickerCallback);
        videoPicker.shouldGenerateMetadata(true); // Default is true

        cameraVideoPicker = new CameraVideoPicker(TambahLapGiatActivity.this);
        cameraVideoPicker.setVideoPickerCallback(videoPickerCallback);
        cameraVideoPicker.shouldGenerateMetadata(true); // Default is true
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == Picker.PICK_IMAGE_DEVICE) {
                if(imagePicker == null) {
                    imagePicker = new ImagePicker(TambahLapGiatActivity.this);
                    imagePicker.setImagePickerCallback(imagePickerCallback);
                }
                imagePicker.submit(data);
            }else if(requestCode == Picker.PICK_IMAGE_CAMERA) {
                if(cameraImagePicker == null) {
                    cameraImagePicker = new CameraImagePicker(TambahLapGiatActivity.this);
                    cameraImagePicker.reinitialize(fotoOutputPath);
                    // OR in one statement
                    // imagePicker = new CameraImagePicker(Activity.this, outputPath);
                    cameraImagePicker.setImagePickerCallback(imagePickerCallback);
                }
                cameraImagePicker.submit(data);
            }else if(requestCode == Picker.PICK_VIDEO_DEVICE){
                if(videoPicker == null) {
                    videoPicker = new VideoPicker(TambahLapGiatActivity.this);
                    videoPicker.setVideoPickerCallback(videoPickerCallback);
                }
                videoPicker.submit(data);
            }else if(requestCode == Picker.PICK_VIDEO_CAMERA){
                if(videoPicker == null) {
                    videoPicker = new VideoPicker(TambahLapGiatActivity.this);
                    videoPicker.reinitialize(videoOutputPath);
                    videoPicker.setVideoPickerCallback(videoPickerCallback);
                }
                videoPicker.submit(data);
            }
        }
    }

    @OnClick(R.id.ibPhoto)
    public void takeFoto(View view) {
        openContextMenu(view);
    }

    @OnClick(R.id.ibVideo)
    public void takeVideo(View view) {
        openContextMenu(view);
    }

    @OnClick(R.id.ibRemoveAtt)
    public void clearAttachment(View view) {
        attachmentItems.clear();
        attachmentItemAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btSubmit)
    public void submitForm(View view) {
        if(attachmentItems.size() < 1){
            Toast.makeText(this, "Anda belum memasukan lampiran..!!", Toast.LENGTH_SHORT).show();
            return;
        }

        String jenisLapGiat = String.valueOf(spinnerJenisLapGiat.getSelectedItem());
        if(jenisLapGiat.equals("-- Pilih Jenis Lap. Giat --")){
            Toast.makeText(this, "Anda belummemilih jenis lap. giat..!!", Toast.LENGTH_SHORT).show();
            return;
        }

        String formJudul = etJudul.getText().toString();
        if (TextUtils.isEmpty(formJudul)) {
            Toast.makeText(this, "Anda belum memasukan judul lap. giat..!!", Toast.LENGTH_SHORT).show();
            return;
        }

        String formDeskripsi = etDeskripsi.getText().toString();
        if (TextUtils.isEmpty(formDeskripsi)) {
            Toast.makeText(this, "Anda belum memasukan deskripsi..!!", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.setTitle("Mengirim data");
        progress.setMessage("Mohon tunggu......");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        progress.show();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("TrnLapGiat[jenis_id]", jenisMap.get(jenisLapGiat));
        builder.addFormDataPart("TrnLapGiat[judul]", formJudul);
        builder.addFormDataPart("TrnLapGiat[deskripsi]", formDeskripsi);
        builder.addFormDataPart("TrnLapGiat[accuracy]", String.valueOf(mLastLocation.getAccuracy()));
        builder.addFormDataPart("TrnLapGiat[lat]", String.valueOf(mLastLocation.getLatitude()));
        builder.addFormDataPart("TrnLapGiat[lng]", String.valueOf(mLastLocation.getLongitude()));
        builder.addFormDataPart("TrnLapGiat[provider]", mLastLocation.getProvider());
        builder.addFormDataPart("TrnLapGiat[speed]", String.valueOf(mLastLocation.getSpeed()));

        for (int pos = 0; pos < attachmentItems.size(); pos++) {
            AttachmentGridItem att = attachmentItems.get(pos);
            String filePath = att.original_path;

            if (!TextUtils.isEmpty(filePath)) {
                File file = new File(filePath);
                builder.addFormDataPart("Attachment[]", file.getName(), RequestBody.create(MediaType.parse(att.mime_type), file));
            }
        }
        MultipartBody requestBody = builder.build();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + user.user.auth_key);

        OkHttpClient httpClient = new MyOkHttpInterceptor(headers).getOkHttpClient();
        TransactionEndPoint service = ServiceGenerator.createService(TransactionEndPoint.class, httpClient);
        Call<LapGiat> call = service.createLapGiat(requestBody);
        call.enqueue(new Callback<LapGiat>() {
            @Override
            public void onResponse(Call<LapGiat> call, Response<LapGiat> response) {
                progress.dismiss();

                Gson gson = new Gson();

                if(response.isSuccessful()){
                    final LapGiat lapGiat = response.body();

                    AlertDialog.Builder builder = new AlertDialog.Builder(TambahLapGiatActivity.this);
                    builder.setTitle("Berhasil...!!");
                    builder.setMessage("Lap. Giat berhasil dikirim. Apakah anda ingin manambah lap. giat baru?");
                    builder.setPositiveButton("Ya",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {
                                    dialog.dismiss();
                                    resetForm();
                                }
                            });
                    builder.setNegativeButton("Tidak",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {
                                    dialog.dismiss();

                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable(LapGiatDetailActivity.USER_SESSION_PARCEL, Parcels.wrap(user));
                                    bundle.putParcelable(LapGiatDetailActivity.LAP_GIAT_PARCEL, Parcels.wrap(lapGiat));
                                    bundle.putInt(LapGiatDetailActivity.LAYOUT_WIDTH_PARCEL, layoutWidth);
                                    bundle.putInt(LapGiatDetailActivity.LAYOUT_HEIGHT_PARCEL, layoutHeight);

                                    Intent i = new Intent(TambahLapGiatActivity.this, LapGiatDetailActivity.class);
                                    i.putExtras(bundle);
                                    startActivity(i);

                                    TambahLapGiatActivity.this.finish();
                                }
                            });
                    builder.show();
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
                            Toast.makeText(TambahLapGiatActivity.this, errMsg, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(TambahLapGiatActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(TambahLapGiatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LapGiat> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(TambahLapGiatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetForm(){
        attachmentItems.clear();
        attachmentItemAdapter.notifyDataSetChanged();

        spinnerJenisLapGiat.setSelection(0);
        etJudul.setText("");
        etDeskripsi.setText("");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        int btnId = v.getId();
        switch (btnId){
            case R.id.ibPhoto:
                menu.setHeaderTitle("Tambah Foto");
                break;
            case R.id.ibVideo:
                menu.setHeaderTitle("Tambah Video");
                break;
        }

        menu.add(0, v.getId(), 0, "Dari Galeri");
        menu.add(0, v.getId(), 0, "Dari Kamera");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int btnId = item.getItemId();

        switch (btnId){
            case R.id.ibPhoto:
                uploadFoto(item.getTitle());
                break;
            case R.id.ibVideo:
                uploadVideo(item.getTitle());
                break;
        }

        return true;
    }

    private void uploadFoto(CharSequence takeType){
        if (takeType == "Dari Galeri") {
            imagePicker.pickImage();
        }else{
            fotoOutputPath = cameraImagePicker.pickImage();
        }
    }

    private void uploadVideo(CharSequence takeType){
        if (takeType == "Dari Galeri") {
            videoPicker.pickVideo();
        }else{
            videoOutputPath = cameraVideoPicker.pickVideo();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();

        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LocationHelper.MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    TambahLapGiatActivity.this.finish();
                }
                break;
            case MY_PERMISSIONS_REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    TambahLapGiatActivity.this.finish();
                }
                break;
            case LocationHelper.PLAY_SERVICES_RESOLUTION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    TambahLapGiatActivity.this.finish();
                }
                break;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.e("--", "buildGoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (locationHelper.checkLocationPermission()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            startLocationUpdates();
        }
    }

    private void getJenisLapGiat(){
        tvLoading.setText("Memuat jenis lap. info...");

        OkHttpClient httpClient = new MyOkHttpInterceptor().getOkHttpClient();
        TransactionEndPoint service = ServiceGenerator.createService(TransactionEndPoint.class, httpClient);
        Call<List<Jenis>> call = service.getJenisLapGiat();
        call.enqueue(new Callback<List<Jenis>>() {
            @Override
            public void onResponse(Call<List<Jenis>> call, Response<List<Jenis>> response) {
                pbHorizontal.setIndeterminate(false);

                Gson gson = new Gson();

                if(response.isSuccessful()){
                    jenisLapGiatList.addAll(response.body());

                    ArrayList<String> items = new ArrayList<String>();
                    items.add("-- Pilih Jenis Lap. Giat --");
                    jenisMap.put("-- Pilih Jenis Lap. Giat --", "");

                    for(Jenis jenis: jenisLapGiatList){
                        items.add(jenis.nama);
                        jenisMap.put(jenis.nama, String.valueOf(jenis.id));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(TambahLapGiatActivity.this, R.layout.custom_spinner_item, items);
                    spinnerJenisLapGiat.setAdapter(adapter);

                    tvLoading.setVisibility(View.GONE);
                    llForm.setVisibility(View.VISIBLE);
                }else{
                    String failureMessage = response.message();

                    String errorMessage = "";

                    try {
                        String errorBody = response.errorBody().string();

                        if(response.code() == 422){
                            Type errorType = new TypeToken<List<ErrorMessage>>(){}.getType();
                            List<ErrorMessage> errorMessages = gson.fromJson(errorBody, errorType);

                            String errMsg = failureMessage + "\n";
                            for(ErrorMessage err : errorMessages){
                                errMsg = errMsg.concat(err.field + ": " + err.message + "\n");
                            }
                            errorMessage = errMsg;
                        }else{
                            errorMessage = response.message();
                        }
                    } catch (IOException e) {
                        errorMessage = e.getMessage();
                    }

                    tvLoading.setText("Error: " + errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<Jenis>> call, Throwable t) {
                pbHorizontal.setIndeterminate(false);
                tvLoading.setText("Error: " + t.getMessage());
            }
        });
    }

    protected void startLocationUpdates() {
        if (!locationHelper.checkLocationPermission()){
        }

        if(!locationUpdateRunning){
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            locationUpdateRunning = true;
        }
    }

    protected void stopLocationUpdates() {
        if(mGoogleApiClient != null){
            if(locationUpdateRunning){
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                locationUpdateRunning = false;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        stopLocationUpdates();

        mMap.clear();

        mLastLocation = location;

        String lokasiAnda = "";
        try {
            double lat = mLastLocation.getLatitude();
            double lng = mLastLocation.getLongitude();

            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            //String country = addresses.get(0).getCountryName();
            //String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            lokasiAnda = city + ", " + state + ", " + knownName;
            tvLokasi.setText(lokasiAnda);

            final String finalLokasiAnda = lokasiAnda;

            LatLng currentLocationPoint = new LatLng(lat, lng);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocationPoint, 15);
            mMap.animateCamera(cameraUpdate, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    LatLng currPos = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mMap.addMarker(
                            new MarkerOptions()
                                    .position(currPos)
                                    .title("Lokasi anda")
                                    .snippet(finalLokasiAnda)
                    );

                    getJenisLapGiat();
                }

                @Override
                public void onCancel() {
                    TambahLapGiatActivity.this.finish();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e(TAG, "onMapReady");
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);

        buildGoogleApiClient();
    }
}
