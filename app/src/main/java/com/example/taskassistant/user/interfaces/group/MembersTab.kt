package com.example.taskassistant.user.interfaces.group

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskassistant.R
import com.example.taskassistant.viewmodel.GroupMembersViewModel
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.itemsIndexed



@Composable
fun MembersTab(
    viewModel: GroupMembersViewModel,
    groupId: String,
    onAddMemberClick: () -> Unit = {}
) {
    val membersWithUsernames by viewModel.membersWithUsernames.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()
    val showAddUserDialog = viewModel.showAddUserDialog
    val searchQuery = remember { mutableStateOf("") }

    LaunchedEffect(groupId) {
        viewModel.fetchGroupMembersWithUsernames(groupId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            error != null -> {
                Text(
                    text = "Error: $error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(membersWithUsernames, key = { index, item -> item.first.groupMemberId }) { _, (member, username) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.users),
                                        contentDescription = "User Icon",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                            .padding(8.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = username,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "Joined: ${member.joinedAt}",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Surface(
                                    color = if (member.roleId.lowercase() == "admin") Color(0xFF3F51B5) else Color.Gray,
                                    shape = RoundedCornerShape(20.dp),
                                    tonalElevation = 2.dp
                                ) {
                                    Text(
                                        text = member.roleId.uppercase(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.showAddUserDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "Add Member"
            )
        }

        if (showAddUserDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.showAddUserDialog = false },
                title = { Text("Add Member") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = searchQuery.value,
                            onValueChange = {
                                searchQuery.value = it
                                viewModel.searchUsersByUsername(it)
                            },
                            label = { Text("Search by username") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (viewModel.isSearching) {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }

                        viewModel.searchError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }

                        LazyColumn {
                            items(viewModel.searchResults) { (userId, username) ->
                                ListItem(
                                    headlineContent = { Text(username) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.addUserToGroup(groupId, userId)
                                            viewModel.showAddUserDialog = false
                                        }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.showAddUserDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}
