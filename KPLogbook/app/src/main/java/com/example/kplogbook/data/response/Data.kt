package com.example.kplogbook.data.response

import com.google.gson.annotations.SerializedName

data class Data(

    @field:SerializedName("user")
    val user: User,

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("refreshToken")
    val refreshToken: String? = null
)