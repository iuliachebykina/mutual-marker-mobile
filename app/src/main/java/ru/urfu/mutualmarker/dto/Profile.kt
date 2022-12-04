package ru.urfu.mutualmarker.dto

data class Profile(
    val id: Long?,
    val email: String?,
    val role: String?,
    val firstName: String?,
    val lastName: String?,
    val patronymic: String?,
    val subject: String?,
    val university: String,
    val institute: String,
    val studentGroup: String?,
    val phoneNumber: String,
    val socialNetwork: String,
    val name: Name
)
