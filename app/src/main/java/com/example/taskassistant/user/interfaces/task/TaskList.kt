package com.example.taskassistant.user.interfaces.task

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taskassistant.ui.theme.Inter_Regular
import com.example.taskassistant.viewmodel.TaskGroupViewModel

@Composable
fun TaskListScreen(userId: String, navController: NavController, viewModel: TaskGroupViewModel = viewModel()) {

    val context = LocalContext.current
    LaunchedEffect(key1 = userId) {
        viewModel.getUserTaskList(
            userId = userId,
            onSuccess = {},
            onFailure = {}
        )
    }
    Surface(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Your Uncompleted Task Groups",
                    fontSize = 20.sp,
                    fontFamily = Inter_Regular,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                if (viewModel.taskGroupList.isNotEmpty()) {
                    for (tasks in viewModel.taskGroupList) {
                        TaskGroupCard(
                            taskGroupId = tasks.taskGroupId,
                            taskTitle = tasks.taskGroupTitle,
                            description = tasks.taskGroupDescription,
                            dueDate = tasks.dueDate,
                            isCompleted = tasks.isCompleted,
                            onEditClick = {
                                navController.navigate("editTaskGroup/${tasks.taskGroupId}")
                            },
                            onDeleteClick = {
                                viewModel.deleteTaskGroup(
                                    taskGroupId = tasks.taskGroupId,
                                    onSuccess = {
                                        viewModel.taskGroupList.remove(tasks)
                                        Toast.makeText(context, "Task Group Successfully Deleted",Toast.LENGTH_LONG).show()
                                    },
                                    onFailure = {
                                        Toast.makeText(context, "Task Group Deletion Failed", Toast.LENGTH_LONG).show()
                                    }
                                )
                            },
                            navController = navController,
                            onToggleComplete = {
                                val newStatus = !tasks.isCompleted
                                viewModel.updateTaskGroupStatus(
                                    taskGroupId = tasks.taskGroupId,
                                    updatedCompleted = newStatus,
                                    onSuccess = {},
                                    onFailure = {}
                                )
                            }

                        )
                    }
            }   else {
                    Text(
                        text = "You Don't have any Task Group to display"
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        navController.navigate("createTaskGroup/$userId")
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task Group")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New Group")
                }
            }
        }
    }
}

@Composable
fun TaskGroupCard(
    taskGroupId: String,
    taskTitle: String,
    description: String,
    dueDate: String,
    isCompleted: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onToggleComplete: () -> Unit,
    navController: NavController,
) {
    val menuExpanded = remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("subTaskList/$taskGroupId") },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = androidx.compose.material3.CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = taskTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )

                Text(
                    text = description.ifBlank { "No description available." },
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )

                Text(
                    text = "Due: $dueDate",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = { menuExpanded.value = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded.value,
                    onDismissRequest = { menuExpanded.value = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            menuExpanded.value = false
                            onEditClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            menuExpanded.value = false
                            onDeleteClick()
                        }
                    )
                }

                RadioButton(
                    selected = isCompleted,
                    onClick = { onToggleComplete() },
                    colors = androidx.compose.material3.RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

