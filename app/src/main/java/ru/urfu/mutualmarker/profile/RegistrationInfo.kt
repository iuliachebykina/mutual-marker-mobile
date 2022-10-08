package ru.urfu.mutualmarker.profile

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationInfo(
    val email: String,
    val password: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val university: String,
    val institute: String,
    val studentGroup: String,
    val socialNetwork: String,
    val subject: String
)
