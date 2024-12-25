package com.example.kplogbook.data.response

import com.google.gson.annotations.SerializedName

data class RequestsResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("success")
	val success: Boolean
)



data class GroupMembers(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("groupId")
	val groupId: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("memberId")
	val memberId: Int,

	@field:SerializedName("updatedAt")
	val updatedAt: String,

)

data class DataItem(

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
	val id: String,

	@field:SerializedName("startDate")
	val startDate: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class Group(

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

data class MembersItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("nim")
	val nim: String,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("GroupMembers")
	val groupMembers: GroupMembers,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
