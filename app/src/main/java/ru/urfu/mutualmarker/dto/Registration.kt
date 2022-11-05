package ru.urfu.mutualmarker.dto

data class Registration(
    val password: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val email: String
)
