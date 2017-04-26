package id.co.dsip.propampoldakalbar.rest_api;

import java.util.Map;

import id.co.dsip.propampoldakalbar.model.UserSession;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by japra_awok on 13/03/2017.
 */

public interface UserEndpoint {
    @FormUrlEncoded
    @POST("site/login-polisi")
    Call<UserSession> login(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user/change-password-polisi")
    Call<UserSession> changePassword(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user/send-fcm-key")
    Call<UserSession> sendFcmKey(@Field("fcm_key") String fcm_key);

    /*@POST("mst-person")
    Call<Person> register(@Body Person person);

    @GET("mst-person/login")
    Call<Person> login(
            @Query("email") String email,
            @Query("hp") String hp
    );

    @GET("trn-person-order")
    Call<List<PersonOrder>> getOrders(@Query("TrnPersonOrderSearch[person_id]") String personId);

    @POST("trn-person-order")
    Call<PersonOrder> order(@Body OrderForm orderForm);*/
}
