package com.example.taskassistant.user.interfaces.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskassistant.AppTopBar
import com.example.taskassistant.BottomNavigationBar
import com.example.taskassistant.ui.theme.AppTheme
import com.example.taskassistant.ui.theme.bodyFontFamily
import com.example.taskassistant.user.interfaces.group.CreateNewGroupScreen
import com.example.taskassistant.user.interfaces.group.GroupListScreen
import com.example.taskassistant.user.interfaces.profile.ProfileScreen
import com.example.taskassistant.user.interfaces.task.TaskListScreen
import com.example.taskassistant.utils.BottomNavItem


@Composable
fun MainScreen(userId: String, navController: NavHostController) {
    val bottomNavController = rememberNavController() // used just for bottom nav switching

    AppTheme {
        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Mindly",
                    showBackButton = false,
                    navController = navController,

                    )
            },
            bottomBar = { BottomNavigationBar(bottomNavController) }
        ) { innerPadding ->
            NavHost(
                navController = bottomNavController,
                startDestination = BottomNavItem.Task.routes,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomNavItem.Task.routes) {

                    TaskListScreen(userId = userId, navController = navController)
                }

                composable(BottomNavItem.Group.routes) {
                    GroupListScreen(userId = userId, navController = navController)
                }

                composable(BottomNavItem.Profile.routes){
                    ProfileScreen(userId = userId)
                }
            }
        }
    }

}

