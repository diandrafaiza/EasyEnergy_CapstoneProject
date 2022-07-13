package com.kls.easyenergy.model
import com.google.gson.annotations.SerializedName


data class RoomDetailModel(
    @SerializedName("data")
    val `data`: DataDetail,
    @SerializedName("status")
    val status: String
)

data class DataDetail(
    @SerializedName("room")
    val room: Room
)

data class Room(
    @SerializedName("category")
    val category: String,
    @SerializedName("challenge")
    val challenge: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("image_header")
    val imageHeader: String,
    @SerializedName("is_finished")
    val isFinished: Boolean,
    @SerializedName("participants")
    val participants: List<Participant>,
    @SerializedName("reward")
    val reward: Int,
    @SerializedName("room_name")
    val roomName: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("is_joined")
    val is_joined: Boolean
)

data class Participant(
    @SerializedName("fullname")
    val fullname: String
)