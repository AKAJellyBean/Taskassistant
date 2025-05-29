// utils/BottomNavItem.kt
package com.example.taskassistant.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.taskassistant.R

sealed class BottomNavItem(val routes: String, val label: String, val iconId: Int) {
    object Task : BottomNavItem("tasks", "Tasks", R.drawable.to)
    object Plan : BottomNavItem("plans", "Plans", R.drawable.plan_strategy)
    object Pomodoro : BottomNavItem("pomodoro", "Pomodoro", R.drawable.duration)
    object Profile : BottomNavItem("profile", "Profile", R.drawable.user__1_)
}
