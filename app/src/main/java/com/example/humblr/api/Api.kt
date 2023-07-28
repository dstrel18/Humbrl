package com.example.humblr.api

import com.example.humblr.data.post.PostDto
import com.example.humblr.data.post.PostListingDto
import com.example.humblr.data.post.SinglePostListingDto
import com.example.humblr.data.profile.ClickedUserProfileDto
import com.example.humblr.data.profile.FriendsListingDto
import com.example.humblr.data.profile.ProfileDto
import com.example.humblr.data.subredditsDto.SubredditDto
import com.example.humblr.data.subredditsDto.SubredditListingDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    //User
    @GET("/api/v1/me")
    suspend fun getUserProfile(): ProfileDto

    @GET("/api/v1/me/friends")
    suspend fun meFriends(): FriendsListingDto

    @PUT("/api/v1/me/friends/{username}")
    suspend fun makeFriends(
        @Path("username") username: String,
    ): PostListingDto

    @GET("/user/{username}/submitted")
    suspend fun getUserContent(
        @Path("username") username: String,
        @Query("after") page: String?,
    ): PostListingDto

    @GET("/user/{username}/saved/")
    suspend fun getSubscribed(
        @Path("username") username: String?,
        @Query("after") page: String,
        @Query("type") type: String,
    ): PostListingDto

    @POST("/api/unsave")
    suspend fun unsavePost(
        @Query("id") postName: String
    )

    @GET("/user/{username}/upvoted/")
    suspend fun getUpVoted(
        @Path("username") username: String?,
        @Query("after") page: String,
        @Query("type") type: String,
    ): PostListingDto

    @GET("/user/{username}/about")
    suspend fun getAnotherUserProfile(
        @Path("username") username: String
    ): ClickedUserProfileDto

    //    Subreddit
    @GET("/subreddits/{source}")
    suspend fun getSubredditListing(
        @Path("source") source: String?,
        @Query("after") page: String
    ): SubredditListingDto

    @POST("/api/subscribe")
    suspend fun subscribeOnSubreddit(
        @Query("action") action: String,
        @Query("sr_name") name: String
    )

    @GET("/subreddits/search")
    suspend fun searchSubredditsPaging(
        @Query("q") source: String?,
        @Query("after") page: String?
    ): SubredditListingDto


    @GET("/{source}")
    suspend fun getSubredditPosts(
        @Path("source") source: String?,
        @Query("after") page: String
    ): PostListingDto

    @GET("/comments/{url}/")
    suspend fun getSubredditInfo(
        @Path("url") url: String
    ): List<PostListingDto>

    @POST("/api/vote")
    suspend fun votePost(
        @Query("dir") dir: Int,
        @Query("id") postName: String
    )


    @GET("/comments/{url}")
    suspend fun getComment(
        @Path("url") url: String,
        @Query("sort") sort: String = "controversial",
        @Query("threded") threded: Boolean = false
    ): List<PostListingDto>

    @POST("/api/save")
    suspend fun savePost(
        @Query("id") postName: String
    )
}