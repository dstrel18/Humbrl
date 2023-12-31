package com.example.humblr.signIn

import android.util.Base64
import com.example.humblr.utils.CLIENT_ID
import com.example.humblr.utils.CLIENT_SECRET
import com.example.humblr.utils.REDIRECT_URI
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiToken {

    @POST("api/v1/access_token")
    suspend fun getToken(
        @Header("Authorization") authString: String = "Basic $encodedAuthString",
        @Query("grant_type") grantType: String = "authorization_code",
        @Query("code") code: String,
        @Query("redirect_uri") redirectUri: String = REDIRECT_URI
    ): ResponseToken

    companion object {
        private const val authString = "$CLIENT_ID:$CLIENT_SECRET"
        val encodedAuthString: String =
            Base64.encodeToString(authString.toByteArray(), Base64.NO_WRAP)

    }
}

val retrofit = Retrofit.Builder().baseUrl("https://www.reddit.com/")
    .addConverterFactory(MoshiConverterFactory.create()).build().create(ApiToken::class.java)

@JsonClass(generateAdapter = true)
class ResponseToken(
    @Json(name = "access_token") val access_token: String
)

