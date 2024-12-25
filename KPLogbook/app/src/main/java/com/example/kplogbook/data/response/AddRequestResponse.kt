package com.example.kplogbook.data.response

import com.google.gson.annotations.SerializedName

data class AddRequestResponse(

	@field:SerializedName("data")
	val data: DataAddRequest,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)


data class DataAddRequest(

	@field:SerializedName("proposalUrl")
	val proposalUrl: String,

	@field:SerializedName("reason")
	val reason: Any,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("Group")
	val group: Group,

	@field:SerializedName("endDate")
	val endDate: String,

	@field:SerializedName("groupId")
	val groupId: Int,

	@field:SerializedName("company")
	val company: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("startDate")
	val startDate: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
