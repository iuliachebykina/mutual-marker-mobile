package ru.urfu.mutualmarker.dto

import java.time.Instant

/**
{
    "id": 1,
    "title": "Анализ ЦА",
    "description": "Проанализировать и сделать схему",
    "openDate": "2022-11-28T17:04:41.889Z",
    "closeDate": "2022-11-28T17:04:41.889Z",
    "roomId": 3,
    "minNumberOfGraded": 5,
    "markSteps": [
    {
        "id": 0,
        "owner": {
            "id": 0,
            "email": "string",
            "role": "ROLE_ADMIN",
            "name": {
                "firstName": "string",
                "lastName": "string",
                "patronymic": "string"
            },
            "subject": "string",
            "university": "string",
            "institute": "string",
            "studentGroup": "string",
            "phoneNumber": "string",
            "socialNetwork": "string"
        },
        "title": "string",
        "description": "string",
        "values": [
            {
            "id": 0,
            "value": 0
            }
        ]
    }
    ],
    "deleted": true
}
 */
data class Task (
    var id: Long,
    var title: String?,
    var description: String?,
    var openDate: String?, // TODO: Instant
    var closeDate: String?,
    var roomId: Int,
    var deleted: Boolean
        )

// TODO add mark step