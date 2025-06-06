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
import com.example.taskassistant.user.interfaces.group.CreateGroupTaskScreen
import com.example.taskassistant.user.interfaces.group.CreateNewGroupScreen
import com.example.taskassistant.user.interfaces.group.GroupMainScreen
import com.example.taskassistant.user.interfaces.group.GroupTaskListScreen
import com.example.taskassistant.user.interfaces.home.GetStartedScreen
import com.example.taskassistant.user.interfaces.home.MainScreen
import com.example.taskassistant.user.interfaces.profile.ProfileScreen
import com.example.taskassistant.user.interfaces.task.CreateSubTaskScreen
import com.example.taskassistant.user.interfaces.task.CreateTaskGroupScreen
import com.example.taskassistant.user.interfaces.task.SubTaskListScreen
import com.example.taskassistant.user.interfaces.task.TaskListScreen
import com.example.taskassistant.user.interfaces.task.UpdateSubTaskScreen
import com.example.taskassistant.user.interfaces.task.UpdateTaskGroupScreen
import com.example.taskassistant.utils.BottomNavItem



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

        composable("mainScreen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            MainScreen(userId = userId, navController = navController)
        }

        composable("createTaskGroup/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            CreateTaskGroupScreen(navController = navController, userId = userId)
        }

        composable("mainScreen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            MainScreen(userId = userId, navController = navController) // pass it here
        }

        composable("subTaskList/{taskGroupId}") {backStackEntry ->
            val taskGroupId = backStackEntry.arguments?.getString("taskGroupId") ?: ""
            SubTaskListScreen(taskGroupId = taskGroupId, navController = navController)
        }

        composable("createSubTask/{taskGroupId}") {backStackEntry ->
            val taskGroupId = backStackEntry.arguments?.getString("taskGroupId") ?: ""
            CreateSubTaskScreen(taskGroupId = taskGroupId, navController = navController)
        }

        composable("updateSubTask/{subTaskId}") {backStackEntry ->
            val subTaskId = backStackEntry.arguments?.getString("subTaskId") ?: ""
            UpdateSubTaskScreen(subTaskId = subTaskId, navController = navController)
        }

        composable("editTaskGroup/{taskGroupId}") {backStackEntry ->
            val taskGroupId = backStackEntry.arguments?.getString("taskGroupId") ?: ""
            UpdateTaskGroupScreen(taskGroupId = taskGroupId, navController = navController)
        }

        // Group Main Screen
        composable("groupMain/{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            GroupMainScreen(groupId = groupId, navController = navController, userId = userId)
        }

        composable("createGroup/{userId}") { backstackEntry ->
            val userId = backstackEntry.arguments?.getString("userId") ?: ""
            CreateNewGroupScreen(userId = userId, navController = navController)
        }

        composable("groupTaskList{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            GroupTaskListScreen(groupId = groupId, navController = navController)
        }

        composable("createGroupTask/{groupId}") {
                backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            CreateGroupTaskScreen(groupId = groupId, userId = userId, onTaskCreated = {})
        }

        composable("profile/{userId}") { backStackEntry ->

            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfileScreen(userId = userId)
        }

    }
}