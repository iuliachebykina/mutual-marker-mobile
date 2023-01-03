package ru.urfu.mutualmarker.dto

/**
 * {
 *   "id": 0,
 *   "title": "string",
 *   "description": "string",
 *   "attachments": [
 *     "string"
 *   ]
 * }
 */
data class Project(
    var id: Long?,
    var title: String?,
    var description: String?,
    var attachments: List<String>?
)
