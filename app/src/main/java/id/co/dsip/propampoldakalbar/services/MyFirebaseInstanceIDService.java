package id.co.dsip.propampoldakalbar.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import id.co.dsip.propampoldakalbar.helpers.SessionManager;
import id.co.dsip.propampoldakalbar.model.UserSession;
import id.co.dsip.propampoldakalbar.rest_api.MyOkHttpInterceptor;
import id.co.dsip.propampoldakalbar.rest_api.ServiceGenerator;
import id.co.dsip.propampoldakalbar.rest_api.TransactionEndPoint;
import id.co.dsip.propampoldakalbar.rest_api.UserEndpoint;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    SessionManager session;
    UserSession user;

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        session = new SessionManager(getApplicationContext());

        if(session.isLoggedIn()){
            user = session.getUserDetails();

            // If you want to send messages to this application instance or
            // manage this apps subscriptions on the server side, send the
            // Instance ID token to your app server.
            sendRegistrationToServer(refreshedToken);
        }
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + user.user.auth_key);

        OkHttpClient httpClient = new MyOkHttpInterceptor(headers).getOkHttpClient();
        UserEndpoint service = ServiceGenerator.createService(UserEndpoint.class, httpClient);
        Call call = service.sendFcmKey(token);
        try {
            Response response = call.execute();
            if(response.isSuccessful()){
                session.logoutUser();

                UserSession user = (UserSession) response.body();

                Gson gson = new Gson();
                String userDataToString = gson.toJson(user);
                session.createLoginSession(userDataToString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
