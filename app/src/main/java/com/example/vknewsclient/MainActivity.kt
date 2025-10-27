package com.example.vknewsclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vknewsclient.ui.theme.PostCard
import com.example.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VkNewsClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(8.dp)
                        ) {
                            PostCard()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Test() {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Example3()
    }
}

@Composable
private fun Example1() {
    OutlinedButton({}) {
        Text("Hello World")
    }
}

@Composable
private fun Example2() {
    TextField(
        value = "Value",
        onValueChange = {},
        label = { Text("Label") }
    )
}

@Composable
private fun Example3() {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = { Text("Yes", Modifier.padding(8.dp)) },
        title = { Text("Are your sure?") },
        text = { Text("Do you want to delete this file?") },
        dismissButton = { Text("No", Modifier.padding(8.dp)) }
    )
}