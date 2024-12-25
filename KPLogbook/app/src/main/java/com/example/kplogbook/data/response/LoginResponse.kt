package com.example.kplogbook.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: DataLogin,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class UserLogin(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("noHp")
	val noHp: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String
)

data class DataLogin(

	@field:SerializedName("user")
	val user: UserLogin,

	@field:SerializedName("token")
	val token: String
)
