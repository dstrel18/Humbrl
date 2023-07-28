package com.example.humblr.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.humblr.api.Api
import com.example.humblr.data.post.PostDto
import com.example.humblr.data.post.PostListingDto
import com.example.humblr.data.profile.ClickedUserProfileDto
import com.example.humblr.data.profile.FriendsListingDto
import com.example.humblr.data.profile.ProfileDto
import com.example.humblr.paging_source.PagingSourceSubscribed
import com.example.humblr.paging_source.PagingSourceUserSubreddit
import com.example.humblr.utils.PAGE_SIZE_SUBREDDITS
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val api: Api) {

    suspend fun getUserProfile(): ProfileDto = api.getUserProfile()

    suspend fun getFriends(): FriendsListingDto {
        return api.meFriends()
    }

    suspend fun getAnotherUserProfile(name: String): ClickedUserProfileDto =
        api.getAnotherUserProfile(name)

    suspend fun makeFriends(name: String) = api.makeFriends(name)

    fun getUserSubreddits(
        id: String?,
    ): Flow<PagingData<PostDto>> = Pager(config = PagingConfig(pageSize = PAGE_SIZE_SUBREDDITS),
        pagingSourceFactory = { PagingSourceUserSubreddit(api, id!!) }).flow


    fun getSubscribed(
        source: String?,
        type: String,
    ): Flow<PagingData<PostDto>> = Pager(config = PagingConfig(pageSize = PAGE_SIZE_SUBREDDITS),
        pagingSourceFactory = { PagingSourceSubscribed(api, source!!, type) }).flow

    suspend fun getSubscribedAll(): PostListingDto {
        val name = getUserProfile().name
        return api.getSubscribed(name, "", "")
    }

    suspend fun unSave(id: String) = api.unsavePost(id)


}