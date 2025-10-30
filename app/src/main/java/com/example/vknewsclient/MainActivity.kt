package com.example.vknewsclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vknewsclient.ui.theme.MainScreen
import com.example.vknewsclient.ui.theme.VkNewsClientTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VkNewsClientTheme {
                MainScreen(viewModel)
            }
//            VkNewsClientTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Box(modifier = Modifier.padding(innerPadding)) {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(MaterialTheme.colorScheme.background)
//                                .padding(8.dp)
//                        ) {
//                            PostCard()
//                        }
//                    }
//                }
//            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Test() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet() {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NavigationDrawerItem(
                        { Text("Favorite") },
                        true,
                        {},
                        icon = { Icon(Icons.Filled.Favorite, null) }
                    )
                    NavigationDrawerItem(
                        { Text("Edit") },
                        false,
                        {},
                        icon = { Icon(Icons.Outlined.Edit, null) }
                    )
                    NavigationDrawerItem(
                        { Text("Delete") },
                        false,
                        {},
                        icon = { Icon(Icons.Outlined.Delete, null) }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "TopAppBar title") },
                    navigationIcon = {
                        IconButton({ scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu,
                                null
                            )
                        }
                    }
                )
            },

            bottomBar = {
                ShortNavigationBar {
                    ShortNavigationBarItem(
                        true,
                        {},
                        { Icon(Icons.Filled.Favorite, null) },
                        { Text("Favorite") }
                    )
                    ShortNavigationBarItem(
                        true,
                        {},
                        { Icon(Icons.Outlined.Edit, null) },
                        { Text("Edit") }
                    )
                    ShortNavigationBarItem(
                        true,
                        {},
                        { Icon(Icons.Outlined.Delete, null) },
                        { Text("Delete") }
                    )
                }
            }
        ) {
            Text(
                "This is scaffold content",
                Modifier.padding(it)
            )
        }
    }
}