package ru.urfu.mutualmarker.client.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.urfu.mutualmarker.client.*
import ru.urfu.mutualmarker.service.AttachmentDownloadService
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
        .build()


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
    fun provideAuthorizationService(retrofit: Retrofit): AuthorizationService =
        retrofit.create(AuthorizationService::class.java)

    @Singleton
    @Provides
    fun provideRoomService(retrofit: Retrofit): RoomService =
        retrofit.create(RoomService::class.java)

    @Singleton
    @Provides
    fun provideProfileService(retrofit: Retrofit): ProfileService =
        retrofit.create(ProfileService::class.java)

    @Singleton
    @Provides
    fun provideTaskService(retrofit: Retrofit): TaskService =
        retrofit.create(TaskService::class.java)

    @Singleton
    @Provides
    fun provideProjectService(retrofit: Retrofit): ProjectService =
        retrofit.create(ProjectService::class.java)

    @Singleton
    @Provides
    fun provideAttachmentService(retrofit: Retrofit): AttachmentService =
        retrofit.create(AttachmentService::class.java)

    @Singleton
    @Provides
    fun provideAttachmentDownloadService(): AttachmentDownloadService = AttachmentDownloadService()

    @Singleton
    @Provides
    fun provideMarkService(retrofit: Retrofit): MarkService =
        retrofit.create(MarkService::class.java)
}