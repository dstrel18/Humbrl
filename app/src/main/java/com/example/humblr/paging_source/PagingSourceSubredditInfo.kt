package com.example.humblr.paging_source

import android.annotation.SuppressLint
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.humblr.api.Api
import com.example.humblr.data.post.PostDto
import com.example.humblr.data.subredditsDto.SubredditDto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PagingSourceSubredditInfo @Inject constructor(
    private val api: Api,
    private val source: String?,
) : PagingSource<String, PostDto>() {

    override fun getRefreshKey(state: PagingState<String, PostDto>): String = FIRST_PAGE

    @SuppressLint("SuspiciousIndentation")
    override suspend fun load(params: LoadParams<String>): LoadResult<String, PostDto> {
        val page = params.key ?: FIRST_PAGE
        return try {
            val post = api.getSubredditPosts(source, page).data
            LoadResult.Page(
                data = post.children,
                prevKey = null,
                nextKey = if (post.children.isEmpty()) null else post.children.last().data.name,
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    private companion object {
        private const val FIRST_PAGE = ""
    }
}