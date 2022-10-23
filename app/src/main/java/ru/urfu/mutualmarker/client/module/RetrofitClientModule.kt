package ru.urfu.mutualmarker.client.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.urfu.mutualmarker.client.ClientCredentials
import ru.urfu.mutualmarker.client.LoginService
import ru.urfu.mutualmarker.client.RetrofitClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitClientModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() : OkHttpClient =
        OkHttpClient().newBuilder().build()

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(RetrofitClient.BASE_URL)
            .build()

    @Singleton
    @Provides
    fun provideLoginService(retrofit: Retrofit) : LoginService =
        retrofit.create(LoginService::class.java)
}