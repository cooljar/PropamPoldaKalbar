package id.co.dsip.propampoldakalbar.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.fragment.BerandaFragment;
import id.co.dsip.propampoldakalbar.fragment.FragmentBerita;
import id.co.dsip.propampoldakalbar.fragment.FragmentInstruksi;
import id.co.dsip.propampoldakalbar.fragment.FragmentLapGiat;
import id.co.dsip.propampoldakalbar.fragment.FragmentLapInfo;
import id.co.dsip.propampoldakalbar.fragment.FragmentObrolan;
import id.co.dsip.propampoldakalbar.fragment.FragmentRekan;
import id.co.dsip.propampoldakalbar.fragment.GantiPasswordFragment;
import id.co.dsip.propampoldakalbar.helpers.SessionManager;
import id.co.dsip.propampoldakalbar.model.UserSession;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_FRAGMENT_BERANDA = "fragment_beranda";

    SessionManager session;
    UserSession user;

    DrawerLayout drawer;
    NavigationView navigationView;

    int layoutWidth, layoutHeight;

    @BindView(R.id.container) FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("BERANDA");

        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();

        ButterKnife.bind(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        ViewTreeObserver vto = container.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                layoutWidth = container.getWidth();
                layoutHeight = container.getHeight();

                //drawer.setDrawerListener(toggle);
                drawer.addDrawerListener(toggle);
                toggle.syncState();

                navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(MainActivity.this);

                View hView =  navigationView.getHeaderView(0);
                ImageView ivProfile = (ImageView) hView.findViewById(R.id.ivProfile);
                TextView tvNama = (TextView) hView.findViewById(R.id.tvNama);
                TextView tvNrp = (TextView) hView.findViewById(R.id.tvNrp);

                Picasso.with(MainActivity.this)
                        .load(user.pas_foto)
                        .resize(200, 200)
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(ivProfile)
                ;

                tvNama.setText(user.nama);

                String nrp = user.nrp;
                String pangkat = user.pangkat.singkatan;
                tvNrp.setText(pangkat + " NRP: " + nrp);

                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    proccessNotification(extras);
                }else{
                    navigationView.setCheckedItem(R.id.nav_beranda);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, new BerandaFragment());
                    transaction.commit();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            proccessNotification(extras);
        }else{
            navigationView.setCheckedItem(R.id.nav_beranda);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new BerandaFragment());
            transaction.commit();
        }
    }

    private void proccessNotification(Bundle data){
        if(session.isLoggedIn()){
            String actionType = data.getString("action_type", "");
            switch (actionType){
                case "instruksi":
                    navigationView.setCheckedItem(R.id.nav_instruksi);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(
                            R.id.container,
                            FragmentInstruksi.newInstanceWithNotif(layoutWidth, layoutHeight, user, data)
                    );
                    transaction.commit();
                    break;
            }
            /*for (String key: data.keySet())
            {
                Log.d ("myApplication", key + " is a key in the bundle");
            }*/
        }else{
            MainActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Konfirmasi!!");
            builder.setMessage("Aplikasi akan ditutup. Lanjutkan?");
            builder.setPositiveButton("Ya",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.dismiss();
                            MainActivity.this.finish();
                        }
                    });
            builder.setNegativeButton("Batal",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        int id = item.getItemId();

        switch (id){
            case R.id.nav_beranda:
                getSupportActionBar().setTitle("Beranda");
                transaction.replace(R.id.container, new BerandaFragment());
                break;
            case R.id.nav_berita:
                getSupportActionBar().setTitle("Berita");
                transaction.replace(R.id.container, FragmentBerita.newInstance(layoutWidth, layoutHeight, user));
                break;
            case R.id.nav_lap_info:
                getSupportActionBar().setTitle("Laporan Informasi");
                transaction.replace(R.id.container, FragmentLapInfo.newInstance(layoutWidth, layoutHeight, user));
                break;
            case R.id.nav_lap_giat:
                getSupportActionBar().setTitle("Laporan Kegiatan");
                transaction.replace(R.id.container, FragmentLapGiat.newInstance(layoutWidth, layoutHeight, user));
                break;
            case R.id.nav_rekan:
                getSupportActionBar().setTitle("Rekan Anda");
                transaction.replace(R.id.container, FragmentRekan.newInstance(layoutWidth, layoutHeight, user));
                break;
            case R.id.nav_obrolan:
                getSupportActionBar().setTitle("Obrolan");
                transaction.replace(R.id.container, new FragmentObrolan());
                break;
            case R.id.nav_instruksi:
                getSupportActionBar().setTitle("Instruksi");
                transaction.replace(R.id.container, FragmentInstruksi.newInstance(layoutWidth, layoutHeight, user));
                break;
            case R.id.nav_change_password:
                getSupportActionBar().setTitle("Ganti Password");
                transaction.replace(R.id.container, new GantiPasswordFragment());
                break;
            case R.id.nav_logout:

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Konfirmasi!!");
                builder.setMessage("Anda akan diminta memasukan NRP dan Password saat kembali membuka aplikasi. Lanjutkan?");
                builder.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                session.logoutUser();

                                dialog.dismiss();

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);

                                MainActivity.this.finish();
                            }
                        });
                builder.setNegativeButton("Batal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        transaction.commit();
        return true;
    }
}
