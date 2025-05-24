package com.example.taskassistant

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskassistant.user.interfaces.auth.CreateCredentialsScreen
import com.example.taskassistant.user.interfaces.auth.LoginScreen
import com.example.taskassistant.user.interfaces.auth.RegistrationScreen
import com.example.taskassistant.user.interfaces.home.GetStartedScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("registration") {
            RegistrationScreen(navController = navController)
        }


        composable("createCredentials/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            CreateCredentialsScreen(navController = navController, userId = userId)
        }

        composable("getStartedScreen") {
            GetStartedScreen(navController = navController)
        }

        composable("login") {
            LoginScreen(navController = navController)
        }
    }
}