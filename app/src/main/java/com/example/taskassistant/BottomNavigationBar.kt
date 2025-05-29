package com.example.taskassistant

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.taskassistant.utils.BottomNavItem



@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Task,
        BottomNavItem.Plan,
        BottomNavItem.Pomodoro,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.routes,
                onClick = {
                    if (currentRoute != item.routes) {
                        navController.navigate(item.routes) {
                            // Avoid multiple copies of the same destination
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 14.sp
                    ) }
            )
        }
    }
}

