package ru.urfu.mutualmarker.client

object ClientCredentials {

    val BASE_URL = "http://5.181.253.200:8090"
    val authorizationService: AuthorizationService
        get() = RetrofitClient.getClient(BASE_URL).create(AuthorizationService::class.java)
}