package com.example.kplogbook.data.response

import com.google.gson.annotations.SerializedName

data class RequestResponse(

	@field:SerializedName("data")
	val data: DataDetailRequest,

	@field:SerializedName("success")
	val success: Boolean
)

data class Request(

	@field:SerializedName("proposalUrl")
	val proposalUrl: String,

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

	@field:SerializedName("reason")
	val reason: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)






data class DataDetailRequest(

	@field:SerializedName("request")
	val request: Request
)
