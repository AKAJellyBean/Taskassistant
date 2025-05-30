package com.example.taskassistant.user.interfaces.group

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.taskassistant.R
import com.example.taskassistant.viewmodel.GroupMembersViewModel

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun GroupMainScreen(groupId: String, navController: NavController, userId: String) {
    var selectedTab by remember { mutableStateOf("Members") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
//        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_small_left),
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Study Group 1",
                fontSize = 20.sp
            )
        }

        // Body content area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (selectedTab) {
                "Members" -> MembersTab(viewModel = GroupMembersViewModel(), groupId = groupId)
                "TaskList" -> GroupTaskListScreen(groupId = groupId, navController = navController)
                "Resources" -> GroupFileScreen(groupId = groupId)
                "Chat" -> ChatScreen(groupId = groupId, userId = userId, navController = navController)
            }
        }

        // Bottom Navigation (custom icons)
        GroupBottomNavBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}

@Composable
fun GroupBottomNavBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf("Members", "TaskList", "Resources", "Chat")
    val icons = listOf(
        R.drawable.users,
        R.drawable.responsability,
        R.drawable.clip_file,
        R.drawable.messages
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, tab ->
            IconButton(onClick = { onTabSelected(tab) }) {
                Icon(
                    painter = painterResource(id = icons[index]),
                    contentDescription = tab,
                    tint = if (tab == selectedTab)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewGroupMainScreen() {
    GroupMainScreen(groupId = "", navController = rememberNavController(), userId = "")
}
