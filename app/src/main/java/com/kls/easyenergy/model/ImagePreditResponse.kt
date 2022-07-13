package com.kls.easyenergy.model
import com.google.gson.annotations.SerializedName


data class ImagePreditResponse(
    @SerializedName("electricity_usage")
    val electricityUsage: String,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)