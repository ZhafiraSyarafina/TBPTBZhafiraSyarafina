package com.example.kplogbook.data.response

import com.google.gson.annotations.SerializedName

data class CreateGroupResponse(

	@field:SerializedName("data")
	val data: DataCreateGroup,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataCreateGroup(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("Members")
	val members: List<NewMembersItem>,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class NewGroupMembers(

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

data class NewMembersItem(

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

	@field:SerializedName("GroupMembers")
	val groupMembers: NewGroupMembers,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
