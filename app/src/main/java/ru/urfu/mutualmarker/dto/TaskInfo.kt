package ru.urfu.mutualmarker.dto

import java.time.Instant

/**
{
    "id": 1,
    "title": "Анализ ЦА",
    "description": "Проанализировать и сделать схему",
    "openDate": "2022-11-28T17:04:41.911Z",
    "closeDate": "2022-11-28T17:04:41.911Z",
    "roomId": 3,
    "deleted": false
}
 */
data class TaskInfo (
    var id: Long,
    var title: String?,
    var description: String?,
    var openDate: String?,
    var closeDate: String?,
    var roomId: Int,
    var deleted: Boolean
)
