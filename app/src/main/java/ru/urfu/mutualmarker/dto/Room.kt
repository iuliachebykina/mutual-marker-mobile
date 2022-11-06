package ru.urfu.mutualmarker.dto

data class Room(
    var id: Long,
    var title: String?,
    var code: String,
    var membersCount: Int
)
