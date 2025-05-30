package com.example.taskassistant.user.interfaces.group

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taskassistant.data.Group
import com.example.taskassistant.viewmodel.GroupListViewModel

@Composable
fun GroupListScreen(
    viewModel: GroupListViewModel = viewModel(),
    userId: String,
    navController: NavController,
    onBackPressed: (() -> Unit)? = null
) {
    LaunchedEffect(Unit) {
        viewModel.loadGroupForUser(userId = userId)
    }

    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    val groups = viewModel.groups

    BackHandler(enabled = true) {
        onBackPressed?.invoke() ?: navController.popBackStack()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            when {
                isLoading -> {
                    Spacer(modifier = Modifier.height(20.dp))
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                errorMessage != null -> {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                groups.isEmpty() -> {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "No groups found.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(groups) { group ->
                            GroupCard(
                                group = group,
                                onClick = { navController.navigate("groupMain/${group.groupId}") }
                            )
                        }
                    }
                }
            }
        }

        // Floating Add Group Button
        FloatingActionButton(
            onClick = { navController.navigate("createGroup/$userId") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Group")
        }
    }
}


@Composable
fun GroupCard(group: Group, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar or Initials Circle (optional)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.groupName.take(1).uppercase(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.groupName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = group.groupDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }

            // Optional forward arrow icon
            Icon(
                imageVector = Icons.Default.ArrowBack, // Replace with a forward arrow if needed
                contentDescription = "Open",
                modifier = Modifier
                    .size(20.dp)
                    .rotate(180f), // Flip ArrowBack to act like ArrowForward
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
