package id.co.dsip.propampoldakalbar.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.parceler.Parcels;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.dsip.propampoldakalbar.R;
import id.co.dsip.propampoldakalbar.activity.LoginActivity;
import id.co.dsip.propampoldakalbar.activity.MainActivity;
import id.co.dsip.propampoldakalbar.helpers.SessionManager;
import id.co.dsip.propampoldakalbar.model.UserSession;
import id.co.dsip.propampoldakalbar.rest_api.ErrorMessage;
import id.co.dsip.propampoldakalbar.rest_api.MyOkHttpInterceptor;
import id.co.dsip.propampoldakalbar.rest_api.ServiceGenerator;
import id.co.dsip.propampoldakalbar.rest_api.UserEndpoint;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GantiPasswordFragment extends Fragment {
    private SessionManager session;
    private UserSession user;
    private ProgressDialog progress;

    @BindView(R.id.etPassword)EditText etPassword;
    @BindView(R.id.etConfirmPassword) EditText etConfirmPassword;
    @BindView(R.id.btSubmit) Button btSubmit;

    public GantiPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ganti_password, container, false);

        session = new SessionManager(getActivity());
        user = session.getUserDetails();

        ButterKnife.bind(this, view);

        progress = new ProgressDialog(getActivity());

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                changePassword(password, confirmPassword);
            }
        });

        return view;
    }

    private void changePassword(String password, String confirm_password){

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Password harus diisi", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(confirm_password)){
            Toast.makeText(getActivity(), "Confirm Password harus diisi", Toast.LENGTH_SHORT).show();
        }else if(!TextUtils.equals(password, confirm_password)){
            Toast.makeText(getActivity(), "Password tidak sama!!", Toast.LENGTH_SHORT).show();
        }else{

            /*View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }*/

            progress.setTitle("Mengirim data");
            progress.setMessage("Mohon tunggu......");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(false);
            progress.setCancelable(false);
            progress.show();

            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "Bearer " + user.user.auth_key);

            OkHttpClient httpClient = new MyOkHttpInterceptor(headers).getOkHttpClient();
            UserEndpoint service = ServiceGenerator.createService(UserEndpoint.class, httpClient);

            Map<String,String> params = new HashMap<String, String>();
            params.put("PasswordFormPolisi[password]", password);
            params.put("PasswordFormPolisi[confirm_password]", confirm_password);

            Call<UserSession> call = service.changePassword(params);
            call.enqueue(new Callback<UserSession>() {
                @Override
                public void onResponse(Call<UserSession> call, Response<UserSession> response) {
                    progress.dismiss();

                    Gson gson = new Gson();

                    if(response.isSuccessful()){
                        UserSession user = response.body();
                        String userDataToString = gson.toJson(user);

                        session.logoutUser();

                        //session.createLoginSession(userDataToString);
                        //user = session.getUserDetails();

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Berhasil!!!");
                        builder.setMessage("Password anda telah berubah. Anda akan diarahkan ke halaman login, silahkan masuk kembali dengan password baru anda.");
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int which) {
                                        etPassword.setText("");
                                        etConfirmPassword.setText("");
                                        dialog.dismiss();

                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        getActivity().startActivity(intent);
                                        getActivity().finish();
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
                                Toast.makeText(getActivity(), errMsg, Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserSession> call, Throwable t) {
                    progress.dismiss();
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
