package com.example.kplogbook.data.response

import com.google.gson.annotations.SerializedName

data class MembersResponse(

	@field:SerializedName("data")
	val data: List<DataMember>,

	@field:SerializedName("success")
	val success: Boolean
)

data class DataMember(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("nim")
	val nim: String,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("sks")
	val sks: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
