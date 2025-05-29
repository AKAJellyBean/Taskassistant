package com.example.taskassistant.user.interfaces.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taskassistant.viewmodel.HomeViewModel

@Composable
fun GetStartedScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = viewModel(),
) {
    val welcomeMessage = viewModel.welcomeMessage

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Add Illustration
                Icon(
                    imageVector = Icons.Default.Star, // You can use Image(painterResource(...)) or Lottie here
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(96.dp)
                )

                // Cool Title
                Text(
                    text = "Welcome to Task Assistant!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                // Subtitle / Tagline
                Text(
                    text = "Organize, Focus, Achieve ✨",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )

                // Spacer
                Spacer(modifier = Modifier.height(8.dp))

                // Button
                Button(
                    onClick = {
                        navController.navigate("registration")
                    },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp)
                ) {
                    Text("🚀 Get Started", fontSize = MaterialTheme.typography.titleMedium.fontSize)
                }
            }
        }
    }
}


