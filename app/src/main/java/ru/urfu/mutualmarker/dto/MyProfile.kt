package ru.urfu.mutualmarker.dto

data class MyProfile(
    val id: Long,
    val email: String,
    val role: String,
    val name: Name,
    val subject: String,
    val university: String,
    val institute: String,
    val studentGroup: String,
    val phoneNumber: String,
    val socialNetwork: String
)
