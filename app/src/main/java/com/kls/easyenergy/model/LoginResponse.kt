package com.kls.easyenergy.model


import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("data")
    val dataLogin: DataLogin,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataLogin(

    @field:SerializedName("accessToken")
    val accessToken: String? = null,

    @field:SerializedName("refreshToken")
    val refreshToken: String? = null
)
