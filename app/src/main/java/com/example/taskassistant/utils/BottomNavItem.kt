// utils/BottomNavItem.kt
package com.example.taskassistant.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.taskassistant.R

sealed class BottomNavItem(val routes: String, val label: String, val iconId: Int) {
    object Task : BottomNavItem("tasks", "Tasks", R.drawable.to)
    object Group : BottomNavItem("groups", "Groups", R.drawable.users)
    object Profile : BottomNavItem("profile", "Profile", R.drawable.user__1_)
}
