package com.example.kplogbook.data.request

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)