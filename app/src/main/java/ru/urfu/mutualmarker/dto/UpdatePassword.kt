package ru.urfu.mutualmarker.dto

data class UpdatePassword (
    val email: String,
    val oldPassword: String,
    val newPassword: String

)
