package com.kls.easyenergy.model
import com.google.gson.annotations.SerializedName


data class JoinRoomResponse(
    @SerializedName("data")
    val `data`: DataJoinRoom,
    @SerializedName("status")
    val status: String
)

data class DataJoinRoom(
    @SerializedName("id")
    val id: String
)