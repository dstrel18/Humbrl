package com.example.humblr.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.humblr.data.post.PostDto
import com.example.humblr.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: ProfileRepository) :
    ViewModel() {

    private val _subscribedSave = MutableStateFlow<PagingData<PostDto>>(
        (PagingData.empty())
    )
    private val _commentSave = MutableStateFlow<PagingData<PostDto>>(
        (PagingData.empty())
    )

    val subscribedSave = _subscribedSave
    val commentSave = _commentSave


    fun getCommentSave(type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val username = repository.getUserProfile().name
            repository.getSubscribed(username, type).cachedIn(viewModelScope).collect {
                _commentSave.value = it
            }
        }
    }


    fun getSubredditSave(type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val username = repository.getUserProfile().name
            repository.getSubscribed(username, type).cachedIn(viewModelScope).collect {
                _subscribedSave.value = it
            }
        }
    }
}
