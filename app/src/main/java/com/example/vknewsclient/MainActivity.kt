package com.example.vknewsclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vknewsclient.ui.theme.AuthState
import com.example.vknewsclient.ui.theme.LoginScreen
import com.example.vknewsclient.ui.theme.MainScreen
import com.example.vknewsclient.ui.theme.MainViewModel
import com.example.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VkNewsClientTheme {
                val viewModel: MainViewModel = viewModel()
                val authState = viewModel.authState.observeAsState(AuthState.Initial)

//                val launcher = rememberLauncherForActivityResult(
//                    contract = VK.getVKAuthActivityResultContract()
//                ) {
//                    viewModel.performAuthResult(it)
//                }

                when (authState.value) {
                    is AuthState.Authorized -> {
                        MainScreen()
                    }

                    is AuthState.NotAuthorized, AuthState.Initial -> {
                        LoginScreen {
                            viewModel.login()
//                            launcher.launch(listOf(VKScope.WALL))
                        }
                    }

//                    else -> {}
                }
            }
        }
    }
}