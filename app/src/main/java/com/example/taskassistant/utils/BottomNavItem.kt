// utils/BottomNavItem.kt
package com.example.taskassistant.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val routes: String, val label: String, val icon: ImageVector) {
    object Task : BottomNavItem("task", "Task", Icons.Filled.List)
    object Groups : BottomNavItem("groups", "Groups", Icons.Filled.Person)
    object Pomodoro : BottomNavItem("pomodoro", "Pomodoro", Icons.Filled.Star)
    object Plan : BottomNavItem("plan", "Plan", Icons.Filled.Info)
}
