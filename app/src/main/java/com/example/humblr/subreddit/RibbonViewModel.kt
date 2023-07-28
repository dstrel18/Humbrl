package com.example.humblr.subreddit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.humblr.data.post.PostDto
import com.example.humblr.data.post.PostListingDto
import com.example.humblr.data.profile.ClickedUserProfileDto
import com.example.humblr.data.subredditsDto.SubredditDto
import com.example.humblr.repository.PostRepository
import com.example.humblr.repository.ProfileRepository
import com.example.humblr.utils.TAG_T
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RibbonViewModel @Inject constructor(
    private val repository: PostRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _subreddit = MutableStateFlow<PagingData<SubredditDto>>(PagingData.empty())
    val subreddit = _subreddit

    private val _subredditInfo = MutableStateFlow<PagingData<PostDto>>(PagingData.empty())
    val subredditInfo = _subredditInfo

    private val _postInfo = MutableLiveData<List<PostListingDto>>()
    val postInfo: LiveData<List<PostListingDto>> get() = _postInfo

    private val _profile = MutableLiveData<ClickedUserProfileDto>()
    val profile: LiveData<ClickedUserProfileDto> get() = _profile


    private val likeLiveData = MutableLiveData<Boolean>()
    val like: LiveData<Boolean>
        get() = likeLiveData

    fun getSubreddit(source: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val it =
                repository.getSubredditsList(source = source).cachedIn(viewModelScope).collect {
                    _subreddit.value = it
                    Log.d(TAG_T, "post - ${it}}")
                }
        }
    }

    fun getSearch(source: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val it = repository.getSearchList(source = source).cachedIn(viewModelScope).collect {
                _subreddit.value = it
            }
            Log.d(TAG_T, "post - $it")
        }
    }


    fun getSubredditInfo(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val it = repository.getSubredditsInfo(id = id).cachedIn(viewModelScope).collect {
                _subredditInfo.value = it
            }
            Log.d(TAG_T, "post - $it")
        }
    }

    fun getInfoPost(source: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val postInfo = repository.getPostInfo(source = source)
            _postInfo.postValue(postInfo)
        }
    }

    fun setVote(name: String, dir: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setVote(dir, name)
        }
    }

    fun getAnotherUserProfile(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = repository.getAnotherUserProfile(name = name)
            _profile.postValue(profile)
        }
    }

    fun savePost(namePost: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.savePost(namePost)
        }
    }

    fun subscribe(action: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.subscribeOnSubreddit(action, name)
        }
    }

    fun makeFriends(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileRepository.makeFriends(name)
            } catch (e: Exception) {
            }
        }
    }
}
