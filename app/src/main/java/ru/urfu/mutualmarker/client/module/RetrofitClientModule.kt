package ru.urfu.mutualmarker.client.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.urfu.mutualmarker.client.LoginService
import ru.urfu.mutualmarker.client.CustomCookieJar
import ru.urfu.mutualmarker.client.RoomService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitClientModule {
    private val baseUrl = "http://5.181.253.200:8090"

    @Singleton
    @Provides
    fun provideOkHttpClient(customCookieJar: CustomCookieJar): OkHttpClient = OkHttpClient
        .Builder()
        .cookieJar(customCookieJar)
        .build();

    @Singleton
    @Provides
    fun provideCustomCookieJar(): CustomCookieJar = CustomCookieJar()


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit
        .Builder()
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    @Singleton
    @Provides
    fun provideLoginService(retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    @Singleton
    @Provides
    fun provideRoomService(retrofit: Retrofit): RoomService =
        retrofit.create(RoomService::class.java)
}