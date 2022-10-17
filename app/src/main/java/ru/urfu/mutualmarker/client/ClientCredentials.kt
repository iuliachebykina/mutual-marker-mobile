package ru.urfu.mutualmarker.client

object ClientCredentials {

    private val BASE_URL = "http://5.181.253.200:8090"
    val loginService: LoginService
        get() = RetrofitClient.getClient(BASE_URL).create(LoginService::class.java)
}