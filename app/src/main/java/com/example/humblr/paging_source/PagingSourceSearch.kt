package com.example.humblr.paging_source

import android.annotation.SuppressLint
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.humblr.api.Api
import com.example.humblr.data.subredditsDto.SubredditDto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PagingSourceSearch @Inject constructor(
    private val api: Api,
    private val source: String?,
) : PagingSource<String, SubredditDto>() {

    override fun getRefreshKey(state: PagingState<String, SubredditDto>): String = FIRST_PAGE

    @SuppressLint("SuspiciousIndentation")
    override suspend fun load(params: LoadParams<String>): LoadResult<String, SubredditDto> {
        val page = params.key ?: FIRST_PAGE
        return try {
            val search = api.searchSubredditsPaging(source, page).data
            LoadResult.Page(
                data = search.children,
                prevKey = null,
                nextKey = if (search.children.isEmpty()) null else search.children.last().data.name,
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