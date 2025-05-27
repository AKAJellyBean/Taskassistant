package com.example.taskassistant

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.taskassistant.utils.BottomNavItem



@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Task,
        BottomNavItem.Groups,
        BottomNavItem.Pomodoro,
        BottomNavItem.Plan
    )

    BottomAppBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = navController.currentDestination?.route == item.routes,
                onClick = {
                    if (navController.currentDestination?.route != item.routes) {
                        navController.navigate(item.routes)
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

