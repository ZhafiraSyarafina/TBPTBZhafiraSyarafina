package com.example.kplogbook.data.response

import com.google.gson.annotations.SerializedName

data class ArsipResponse(

	@field:SerializedName("data")
	val data: List<DataArsip>,

	@field:SerializedName("success")
	val success: Boolean
)

data class DataArsip(

	@field:SerializedName("idKP")
	val idKP: Int,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("KPRequest")
	val kPRequest: KPRequest,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("responseLetterUrl")
	val responseLetterUrl: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

