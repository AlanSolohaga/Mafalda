package com.project.mafalda.interfaz;

import com.project.mafalda.model.User;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApi {

    @Headers({"Content-Type: application/json","Authorization: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDI1NDA0NjQyMjkxNzAwNjcxNjkifQ.lSiE0hRf7M_PMPFPjh4M3Q7MEEuwI3xGW2zhoFf0FcU"})
    @GET("api/v1/quizzes/{id}")
    public Call<JSONObject> find(@Path("id")String id);

    //TODO: authorization poner el token original
    @Headers({"Content-Type: application/json"})
    @GET("api/v1/quizzes/{id}")
    public Call<JSONObject> find(@Header ("Authorization")String authorization,@Path("id")String id, @Query("page")String page);
}
