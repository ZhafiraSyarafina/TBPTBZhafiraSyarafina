package com.example.kplogbook.data.response

import com.google.gson.annotations.SerializedName

data class RepliesResponse(

	@field:SerializedName("data")
	val data: List<DataReply> ,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class KPRequest(

	@field:SerializedName("proposalUrl")
	val proposalUrl: String? = null,

	@field:SerializedName("reason")
	val reason: Any? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("Group")
	val group: Group? = null,

	@field:SerializedName("endDate")
	val endDate: String? = null,

	@field:SerializedName("groupId")
	val groupId: Int? = null,

	@field:SerializedName("company")
	val company: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("startDate")
	val startDate: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class DataReply(

	@field:SerializedName("idKP")
	val idKP: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("KPRequest")
	val kPRequest: KPRequest? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("responseLetterUrl")
	val responseLetterUrl: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class ReplyResponse(

	@field:SerializedName("data")
	val data: DataReply? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)
