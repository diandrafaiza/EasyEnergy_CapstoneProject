package com.kls.easyenergy.model
import com.google.gson.annotations.SerializedName


data class DailyReportResponse(
    @SerializedName("data")
    val `data`: DataReport,
    @SerializedName("status")
    val status: String
)

data class DataReport(
    @SerializedName("detected_kwh_usage")
    val detectedKwhUsage: Int,
    @SerializedName("id_room")
    val idRoom: String,
    @SerializedName("id_user")
    val idUser: String,
    @SerializedName("is_done")
    val isDone: Boolean,
    @SerializedName("kwh_image")
    val kwhImage: String,
    @SerializedName("today_kwh_usage")
    val todayKwhUsage: Int,
    @SerializedName("total_kwh_usage")
    val totalKwhUsage: Int
)