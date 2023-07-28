package com.example.humblr.signIn

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.humblr.utils.TAG_T
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {

    private val authSuccessEventChannel = Channel<Unit>(Channel.BUFFERED)
    val authSuccessFlow: Flow<Unit>
        get() = authSuccessEventChannel.receiveAsFlow()

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token

    fun getToken(authCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                retrofit.getToken(

                    code = authCode
                )
            }.fold(onSuccess = {

                TokenStorage.accessToken = it.access_token
                _token.postValue(it.access_token)

                val auth = authSuccessEventChannel.send(Unit)

            }, onFailure = { })
        }
    }


    override fun onCleared() {
        super.onCleared()
    }
}