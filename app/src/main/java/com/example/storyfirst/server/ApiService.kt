package com.example.storyfirst.server

import com.example.storyfirst.helper.PreferencesHelper
import com.example.storyfirst.view.camera.FileUploadResponse
import com.example.storyfirst.model.GetStoriesResponse
import com.example.storyfirst.model.LoginResponse
import com.example.storyfirst.model.RegisterResponse
import com.google.android.gms.auth.api.Auth
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

    interface ApiService {

    @FormUrlEncoded
    @POST("register")

    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")

    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

//    @GET("stories")
//    @Headers("Authorization: Bearer ")
//    fun getListUser(): Call<List<UserResponse>>

//    @GET("stories")
//    @Headers("Authorization: ")
//    fun getUserDetail(): Call<DetailUserResponse>


//    @GET("/stories")
//    open fun getUser(
//        @Path("id") id: String?,
//        @Header("Authorization") authHeader: String?
//    ): Call<DetailUserResponse?>?

    @GET("stories")
    fun getStories(): Call<GetStoriesResponse>

    @Multipart
    @POST("")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>

}