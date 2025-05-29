package com.example.taskassistant.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskassistant.ui.theme.baseline
import com.example.taskassistant.ui.theme.displayFontFamily

// screens/TasksScreen.kt
@Composable
fun TasksScreen() {
    Text("Tasks Screen")
}

// screens/PlansScreen.kt
@Composable
fun PlansScreen() {
    Text("Plans Screen")
}

// screens/PomodoroScreen.kt
@Composable
fun PomodoroScreen() {
    Text("Pomodoro Screen")
}

// screens/ProfileScreen.kt
@Composable
fun ProfileScreen() {
    Text(
        text = "Profile Screen",
        style = MaterialTheme.typography.bodyLarge

    )
}
@Preview
@Composable
fun Display() {
    ProfileScreen()
}