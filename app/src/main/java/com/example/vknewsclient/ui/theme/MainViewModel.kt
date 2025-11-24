package com.example.vknewsclient.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
    val authState: LiveData<AuthState> = _authState


    private val vkAuthCallback = object : VKIDAuthCallback {
        override fun onAuth(accessToken: AccessToken) {
            val token = accessToken.token
//            _authState.value = AuthState.Authorized(accessToken)
            _authState.value = AuthState.Authorized

        }

        override fun onFail(fail: VKIDAuthFail) {
            when (fail) {
                is VKIDAuthFail.Canceled -> {
                    //...
                }

                else -> {
                    //...
                }
            }
//            _authState.value = AuthState.NotAuthorized(fail)
            _authState.value = AuthState.NotAuthorized
        }
    }

    fun login() {
        viewModelScope.launch {
            VKID.instance.authorize(
                callback = vkAuthCallback,
                params = VKIDAuthParams {
                    scopes = setOf("offline")
                }
            )
        }
    }

//    init {
//        _authState.value = if (VK.isLoggedIn()) AuthState.Authorized else AuthState.NotAuthorized
//    }

//    fun performAuthResult(result: VKAuthenticationResult) {
//        if (result is VKAuthenticationResult.Success) {
//            _authState.value = AuthState.Authorized
//        } else {
//            _authState.value = AuthState.NotAuthorized
//        }
//    }
}