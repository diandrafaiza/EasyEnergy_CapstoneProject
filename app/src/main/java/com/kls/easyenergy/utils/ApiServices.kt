package com.kls.jnecourierapps.utils

import com.kls.easyenergy.model.ImagePreditResponse
import com.kls.easyenergy.model.LoginResponse
import com.kls.easyenergy.model.RoomDetailModel
import com.kls.easyenergy.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @GET("rooms")
    fun getRoomTask() : Call<RoomResponse>

    @GET("rooms/{id}")
    fun getDetailRoom(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) idRoom: String
    ): Call<RoomDetailModel>

    @POST("join/{id}")
    fun joinRoomTask(
        @Header("Authorization") token: String,
        @Path(value = "id", encoded = true) idRoom: String
    ): Call<JoinRoomResponse>

    @Multipart
    @POST("predict")
    fun kwhPredict(
        @Part file: MultipartBody.Part,
    ): Call<ImagePreditResponse>

    @FormUrlEncoded
    @POST("authentications")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ) : Call<LoginResponse>

    @FormUrlEncoded
    @POST("users")
    fun createAccount(
        @Field("username") name: String,
        @Field("password") password: String,
        @Field("fullname") full_name: String,
        @Field("email") email: String,
    ) : Call<RegisterResponse>

    @GET("reports/{roomid}/{date}")
    fun getDailyReportTask(
        @Header("Authorization") token: String,
        @Path(value = "roomid", encoded = true) idRoom: String,
        @Path(value = "date", encoded = true) date: String
    ): Call<DailyReportResponse>

    @Multipart
    @POST("reports/{roomid}/{date}")
    fun postDailyReportTask(
        @Header("Authorization") token: String,
        @Path(value = "roomid", encoded = true) idRoom: String,
        @Path(value = "date", encoded = true) date: String,
        @Query("kwhusage") usage: String,
        @Part file: MultipartBody.Part,
    ): Call<PostDailyReportResponse>

}