package com.example.kplogbook.data.response


import com.google.gson.annotations.SerializedName

data class User(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("email")
    val email: String
)