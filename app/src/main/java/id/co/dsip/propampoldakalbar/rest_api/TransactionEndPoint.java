package id.co.dsip.propampoldakalbar.rest_api;


import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import id.co.dsip.propampoldakalbar.model.Berita;
import id.co.dsip.propampoldakalbar.model.Coment;
import id.co.dsip.propampoldakalbar.model.Jenis;
import id.co.dsip.propampoldakalbar.model.LapGiat;
import id.co.dsip.propampoldakalbar.model.LapInfo;
import id.co.dsip.propampoldakalbar.model.Like;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by japra_awok on 12/04/2017.
 */

public interface TransactionEndPoint {
    @GET("trn-berita")
    Call<JsonObject> getBerita(
            @Query("page") String page,
            @Query("per-page") String perPage
    );

    @GET("trn-lap-info")
    Call<JsonObject> getLapInfo(
            @Query("page") String page,
            @Query("per-page") String perPage
    );

    @GET("trn-lap-giat")
    Call<JsonObject> getLapGiat(
            @Query("page") String page,
            @Query("per-page") String perPage
    );

    @FormUrlEncoded
    @POST("trn-berita/create-comment")
    Call<Coment> createComentBerita(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("trn-berita/create-like")
    Call<Like> createLikeBerita(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("trn-lap-info/create-comment")
    Call<Coment> createComentLapInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("trn-lap-info/create-like")
    Call<Like> createLikeLapInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("trn-lap-giat/create-comment")
    Call<Coment> createComentLapGiat(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("trn-lap-giat/create-like")
    Call<Like> createLikeLapGiat(@FieldMap Map<String, String> params);

    @GET("drop-down/jenis-berita")
    Call<List<Jenis>> getJenisBerita();

    @GET("drop-down/jenis-lap-info")
    Call<List<Jenis>> getJenisLapInfo();

    @GET("drop-down/jenis-lap-giat")
    Call<List<Jenis>> getJenisLapGiat();

    @POST("trn-berita")
    Call<Berita> createBerita(
            @Body RequestBody data
    );

    @POST("trn-lap-info")
    Call<LapInfo> createLapInfo(
            @Body RequestBody data
    );

    @POST("trn-lap-giat")
    Call<LapGiat> createLapGiat(
            @Body RequestBody data
    );

    @GET("user/list-rekan")
    Call<JsonObject> getRekanList(
            @Query("page") String page,
            @Query("per-page") String perPage,
            @Query("MstAnggotaPolisiSearch[nama]") String queryNama
    );
}
