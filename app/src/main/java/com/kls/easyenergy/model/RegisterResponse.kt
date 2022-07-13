package com.kls.easyenergy.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val dataRegister: DataRegister,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataRegister(

	@field:SerializedName("addedUser")
	val addedUser: AddedUser
)

data class AddedUser(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("fullname")
	val full_name: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
