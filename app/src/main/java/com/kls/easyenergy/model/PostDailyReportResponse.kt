package com.kls.easyenergy.model
import com.google.gson.annotations.SerializedName


data class PostDailyReportResponse(
    @SerializedName("data")
    val `data`: DataPost,
    @SerializedName("status")
    val status: String
)

data class DataPost(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("current_kwh_usage")
    val currentKwhUsage: Int,
    @SerializedName("id_room")
    val idRoom: String,
    @SerializedName("id_user")
    val idUser: String,
    @SerializedName("total_kwh_usage")
    val totalKwhUsage: Int
)