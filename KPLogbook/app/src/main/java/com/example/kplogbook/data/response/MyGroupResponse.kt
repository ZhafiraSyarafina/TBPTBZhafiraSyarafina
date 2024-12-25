package com.example.kplogbook.data.response

import com.google.gson.annotations.SerializedName

data class MyGroupResponse(

	@field:SerializedName("data")
	val data: MyGroupData,

	@field:SerializedName("success")
	val success: Boolean
)

data class MyGroupMembersItem(

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
	val groupMembers: MyGroupMembers,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class MyGroupMembers(

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

data class MyGroupData(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("Members")
	val members: List<MyGroupMembersItem>,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
