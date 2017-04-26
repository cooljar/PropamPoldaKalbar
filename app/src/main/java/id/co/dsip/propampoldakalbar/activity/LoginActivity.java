package id.co.dsip.propampoldakalbar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.helpers.SessionManager;
import id.co.dsip.propampoldakalbar.model.UserSession;
import id.co.dsip.propampoldakalbar.rest_api.ErrorMessage;
import id.co.dsip.propampoldakalbar.rest_api.MyOkHttpInterceptor;
import id.co.dsip.propampoldakalbar.rest_api.ServiceGenerator;
import id.co.dsip.propampoldakalbar.rest_api.UserEndpoint;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog progress;
    SessionManager session;

    @BindView(R.id.etNrp)EditText etNrp;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.btLogin) Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("LOGIN");

        // Session Manager
        session = new SessionManager(getApplicationContext());

        progress = new ProgressDialog(this);
        progress.setTitle("Mengirim data");
        progress.setMessage("Mohon tunggu......");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCancelable(false);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btLogin)
    public void login() {
        String nrp = etNrp.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(nrp)) {
            Toast.makeText(this, "NRP harus diisi", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password harus diisi", Toast.LENGTH_LONG).show();
        }else{
            progress.show();

            OkHttpClient httpClient = new MyOkHttpInterceptor().getOkHttpClient();
            UserEndpoint service = ServiceGenerator.createService(UserEndpoint.class, httpClient);

            Map<String,String> params = new HashMap<String, String>();
            params.put("LoginForm[username]", nrp);
            params.put("LoginForm[password]", password);

            Call<UserSession> call = service.login(params);

            call.enqueue(new Callback<UserSession>() {
                @Override
                public void onResponse(Call<UserSession> call, Response<UserSession> response) {
                    Gson gson = new Gson();

                    if(response.isSuccessful()){
                        UserSession user = response.body();
                        String userDataToString = gson.toJson(user);

                        session.createLoginSession(userDataToString);

                        String token = FirebaseInstanceId.getInstance().getToken();
                        if (token != null && !token.isEmpty() && !token.equals("null")){
                            sendTokenToServer();
                        }else{
                            progress.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                    }else{
                        progress.dismiss();

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
                                Toast.makeText(LoginActivity.this, errMsg, Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserSession> call, Throwable t) {
                    progress.dismiss();
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendTokenToServer(){
        UserSession user = session.getUserDetails();
        String token = FirebaseInstanceId.getInstance().getToken();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + user.user.auth_key);

        OkHttpClient httpClient = new MyOkHttpInterceptor(headers).getOkHttpClient();
        UserEndpoint service = ServiceGenerator.createService(UserEndpoint.class, httpClient);
        Call<UserSession> call = service.sendFcmKey(token);
        call.enqueue(new Callback<UserSession>() {
            @Override
            public void onResponse(Call<UserSession> call, Response<UserSession> response) {
                progress.dismiss();
                if(response.isSuccessful()){
                    session.logoutUser();

                    Gson gson = new Gson();

                    UserSession user = response.body();
                    String userDataToString = gson.toJson(user);

                    session.createLoginSession(userDataToString);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<UserSession> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
