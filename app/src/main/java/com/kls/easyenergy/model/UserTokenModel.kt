package com.kls.easyenergy.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val token: String,
    val isLogin: Boolean
) : Parcelable
