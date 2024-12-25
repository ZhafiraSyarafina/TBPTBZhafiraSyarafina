package com.example.kplogbook.data.request

data class RegisterRequest(
    var name: String? = null,
    var email: String? = null,
    var noHp: String? = null,
    var institusi: String? = null,
    var password: String? = null,
    var password_confirmation: String? = null
)