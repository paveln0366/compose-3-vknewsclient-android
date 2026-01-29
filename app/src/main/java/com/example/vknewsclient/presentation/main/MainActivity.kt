package com.example.vknewsclient.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vknewsclient.domain.entity.AuthState
import com.example.vknewsclient.presentation.NewsFeedApplication
import com.example.vknewsclient.presentation.ViewModelFactory
import com.example.vknewsclient.ui.theme.VkNewsClientTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as NewsFeedApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VkNewsClientTheme {
                val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
                val authState = viewModel.authState.observeAsState(AuthState.Initial)

//                val launcher = rememberLauncherForActivityResult(
//                    contract = VK.getVKAuthActivityResultContract()
//                ) {
//                    viewModel.performAuthResult(it)
//                }

                when (authState.value) {
                    is AuthState.Authorized -> {
                        MainScreen(viewModelFactory)
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