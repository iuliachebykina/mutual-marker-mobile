package ru.urfu.mutualmarker.dto

data class RegistrationRequest(
    val password: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val email: String
)
