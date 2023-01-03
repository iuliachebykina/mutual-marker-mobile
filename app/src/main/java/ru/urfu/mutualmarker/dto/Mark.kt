package ru.urfu.mutualmarker.dto

data class Mark(
    val projectId: Long?,
    val profileId: Long?,
    val comment: String?,
    val markStepValues: List<Int>
)
