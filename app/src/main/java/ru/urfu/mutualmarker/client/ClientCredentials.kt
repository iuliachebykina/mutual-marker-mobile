package ru.urfu.mutualmarker.client

object ClientCredentials {

    val loginService: LoginService
        get() = RetrofitClient.getClient().create(LoginService::class.java)
}