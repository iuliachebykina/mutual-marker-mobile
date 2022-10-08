package ru.urfu.mutualmarker.profile

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val firstName: String,
    val lastName: String,
    val patronymic: String
)
