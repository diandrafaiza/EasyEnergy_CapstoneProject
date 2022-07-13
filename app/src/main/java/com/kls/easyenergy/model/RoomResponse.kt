package com.kls.easyenergy.model
import com.google.gson.annotations.SerializedName


data class RoomResponse(
    @SerializedName("data")
    val `data`: ArrayList<RoomListModel>,
    @SerializedName("status")
    val status: String
)

data class RoomListModel(
    @SerializedName("category")
    val category: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("image_header")
    val imageHeader: String,
    @SerializedName("number_of_participants")
    val numberOfParticipants: Int,
    @SerializedName("room_name")
    val roomName: String
)