package com.example.kplogbook.data.response

import com.google.gson.annotations.SerializedName

data class InfosResponse(

	@field:SerializedName("data")
	val data: List<DataInfo>,

	@field:SerializedName("success")
	val success: Boolean
)

data class InfoResponse(

	@field:SerializedName("data")
	val data: DataInfo,

	@field:SerializedName("success")
	val success: Boolean
)

data class DataInfo(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
