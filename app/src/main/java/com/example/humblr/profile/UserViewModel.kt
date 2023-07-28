package com.example.humblr.profile

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.humblr.data.post.PostDto
import com.example.humblr.data.post.PostListingDto
import com.example.humblr.data.profile.ClickedUserProfileDto
import com.example.humblr.data.profile.ProfileDto
import com.example.humblr.enty.MySharedPreferences
import com.example.humblr.repository.ProfileRepository
import com.example.humblr.signIn.TokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repositoryProfile: ProfileRepository,
    private val sharedPreferences: MySharedPreferences
) : ViewModel() {

    private val _profileMy = MutableLiveData<ProfileDto>()
    val profile: LiveData<ProfileDto>
        get() = _profileMy


    private val _profile = MutableLiveData<ClickedUserProfileDto>()
    val profileUser: LiveData<ClickedUserProfileDto>
        get() = _profile

    var list = mutableListOf<ClickedUserProfileDto>()


    private val _friends: MutableLiveData<List<ClickedUserProfileDto>> = MutableLiveData(list)
    val friends: MutableLiveData<List<ClickedUserProfileDto>>
        get() = _friends


    private val _subredditUserInfo = MutableStateFlow<PagingData<PostDto>>(PagingData.empty())
    val subredditUserInfo = _subredditUserInfo

    private val _listSaved: MutableLiveData<PostListingDto> = MutableLiveData()
    val listSaved: LiveData<PostListingDto> get() = _listSaved


    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = repositoryProfile.getUserProfile()
            _profileMy.postValue(profile)
        }
    }

    fun getFriends() {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = repositoryProfile.getFriends()

            profile.data.children.forEach {
                getAnotherUserProfile(it.name)


            }
        }
    }

    fun getAnotherUserProfile(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = repositoryProfile.getAnotherUserProfile(name = name)
            _profile.postValue(profile)
            list.add(profile)
            _friends.postValue(list)
        }
    }

    fun makeFriends(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repositoryProfile.makeFriends(name)

            } catch (e: Exception) {

            }
        }
    }

    fun getUserContent(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val subredditUser =
                repositoryProfile.getUserSubreddits(name).cachedIn(viewModelScope).collect {
                    _subredditUserInfo.value = it
                }
        }
    }

    fun getSubscribedAll() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repositoryProfile.getSubscribedAll()
            _listSaved.postValue(list)
        }
    }

    fun unSave(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repositoryProfile.unSave(id)
        }
    }

    fun logout(fragment: Fragment) {
        sharedPreferences.saveToken(fragment.requireContext(), null)
        fragment.requireActivity().intent = null
        TokenStorage.accessToken = null
        fragment.findNavController()
            .navigate(UserFragmentDirections.actionUserFragmentToSignInFragment())
    }
}