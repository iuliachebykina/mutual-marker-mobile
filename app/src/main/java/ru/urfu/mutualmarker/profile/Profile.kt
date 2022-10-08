package ru.urfu.mutualmarker.profile

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val email: String,
    val password: String,
    //Role???
    val name: Name,
    val subject: String,
    val university: String,
    val studentGroup: String,
    val phoneNumber: String,
    val socialNetwork: String
)
