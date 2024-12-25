package com.example.kplogbook.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)