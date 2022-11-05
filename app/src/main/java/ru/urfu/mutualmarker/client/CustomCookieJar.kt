package ru.urfu.mutualmarker.client

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.*

class CustomCookieJar : CookieJar {

    private val cookies = mutableListOf<Cookie>()

    override fun saveFromResponse(url: HttpUrl, cookieList: List<Cookie>) {
        if(url.encodedPath().endsWith("login")){
            cookies.clear()
            cookies.addAll(cookieList)

        }
        if(url.encodedPath().endsWith("logout")){
            cookies.clear()
        }

    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        if (!url.encodedPath().endsWith("login")) {
            return cookies
        }
        return Collections.emptyList()
    }

}