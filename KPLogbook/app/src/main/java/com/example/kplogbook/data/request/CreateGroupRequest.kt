package com.example.kplogbook.data.request

data class CreateGroupRequest(
    val name: String,
    val memberIds: List<Int>
)