package com.example.humblr.di

import android.util.Log
import com.example.humblr.api.Api
import com.example.humblr.network.AuthorizationInterceptor
import com.example.humblr.repository.PostRepository
import com.example.humblr.repository.ProfileRepository
import com.example.humblr.utils.BASE_URL
import com.example.humblr.utils.TAG_T
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideBaseUrl() = BASE_URL


    @Provides
    @Singleton
    fun provideRetrofitInstance(BASE_URL: String): Api {
        val okhttpClient = OkHttpClient.Builder().addNetworkInterceptor(HttpLoggingInterceptor() {
//            Log.d(TAG_T, "Network$it")
        }.setLevel(HttpLoggingInterceptor.Level.BODY)).followRedirects(true)
            .addNetworkInterceptor(AuthorizationInterceptor()).build()

        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(MoshiConverterFactory.create())
                .client(okhttpClient).build()

        return retrofit.create() ?: error("retrofit is not initialized")
    }


    @Provides
    @Singleton
    fun provideProfileRepository(api: Api): ProfileRepository = ProfileRepository(api)

    @Provides
    @Singleton
    fun providePostRepository(api: Api): PostRepository = PostRepository(api)
}