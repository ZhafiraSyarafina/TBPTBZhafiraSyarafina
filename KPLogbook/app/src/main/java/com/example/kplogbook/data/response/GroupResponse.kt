package com.example.kplogbook.data.response

import com.google.gson.annotations.SerializedName

data class GroupResponse(

	@field:SerializedName("data")
	val data: DataGroup,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)


data class DataGroup(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("Members")
	val members: List<MembersItem>,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)


