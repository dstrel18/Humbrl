package com.example.humblr.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.humblr.api.Api
import com.example.humblr.data.post.PostDto
import com.example.humblr.data.post.PostListingDto
import com.example.humblr.data.profile.ClickedUserProfileDto
import com.example.humblr.data.subredditsDto.SubredditDto
import com.example.humblr.data.subredditsDto.SubredditListingDto
import com.example.humblr.paging_source.PagingSourceSearch
import com.example.humblr.paging_source.PagingSourceSubreddit
import com.example.humblr.paging_source.PagingSourceSubredditInfo
import com.example.humblr.utils.PAGE_SIZE_SUBREDDITS
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class PostRepository @Inject constructor(private val apiPost: Api) {


    fun getSubredditsList(
        source: String?,
    ): Flow<PagingData<SubredditDto>> =
        Pager(config = PagingConfig(pageSize = PAGE_SIZE_SUBREDDITS),
            pagingSourceFactory = { PagingSourceSubreddit(apiPost, source) }).flow


    fun getSearchList(
        source: String?,
    ): Flow<PagingData<SubredditDto>> =
        Pager(config = PagingConfig(pageSize = PAGE_SIZE_SUBREDDITS),
            pagingSourceFactory = { PagingSourceSearch(apiPost, source) }).flow


    fun getSubredditsInfo(
        id: String?,
    ): Flow<PagingData<PostDto>> = Pager(config = PagingConfig(pageSize = PAGE_SIZE_SUBREDDITS),
        pagingSourceFactory = { PagingSourceSubredditInfo(apiPost, id) }).flow


    suspend fun getPostInfo(source: String): List<PostListingDto> = apiPost.getSubredditInfo(source)

    suspend fun setVote(dir: Int, name: String) = apiPost.votePost(dir, name)

    suspend fun getComment(url: String): List<PostListingDto> = apiPost.getComment(url)
    suspend fun getAnotherUserProfile(name: String): ClickedUserProfileDto =
        apiPost.getAnotherUserProfile(name)

    suspend fun savePost(postName: String) = apiPost.savePost(postName)

    suspend fun subscribeOnSubreddit(action: String, name: String) =
        apiPost.subscribeOnSubreddit(action, name)


}