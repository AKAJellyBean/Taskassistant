package com.example.taskassistant.user.interfaces.task

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.taskassistant.ui.theme.Inter_Regular
import com.example.taskassistant.viewmodel.SubTaskViewModel

@Composable
fun SubTaskListScreen(
    taskGroupId: String,
    navController: NavController,
    viewModel: SubTaskViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = taskGroupId) {
        viewModel.getUserSubTaskList(
            taskGroupId = taskGroupId,
            onSuccess = {},
            onFailure = {}
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Task Group Details",
            fontFamily = Inter_Regular,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(0.7f)
                .align(Alignment.CenterHorizontally)
        )

        // Subtask List
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (viewModel.subTaskList.isNotEmpty()) {
                for (tasks in viewModel.subTaskList) {
                    SubTaskCard(
                        navController = navController,
                        subTaskId = tasks.subTaskId,
                        subTaskTitle = tasks.subTaskTitle,
                        subTaskDueDate = tasks.dueDate,
                        priority = tasks.priority,
                        isCompleted = tasks.isCompleted,
                        onEditClick = {},
                        onDeleteClick = {
                            viewModel.deleteSubTask(
                                subTaskId = tasks.subTaskId,
                                onSuccess = {
                                    viewModel.subTaskList.remove(tasks)
                                    Toast.makeText(context, "Task Successfully deleted", Toast.LENGTH_LONG).show()
                                },
                                onFailure = {
                                    Toast.makeText(context, "Unable to delete Task. Try Again", Toast.LENGTH_LONG).show()
                                }
                            )
                        },
                        onToggleComplete = {
                            val newStatus = !tasks.isCompleted
                            viewModel.updateSubTaskStatus(
                                subTaskId = tasks.subTaskId,
                                updatedIsCompleted = newStatus,
                                onSuccess = {},
                                onFailure = {}
                            )
                        }
                    )
                }
            } else {
                Text(
                    text = "You don't have any Subtasks to display",
                    fontSize = 16.sp,
                    fontFamily = Inter_Regular,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Gray
                )
            }
        }

        // Floating Action Button
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate("createSubTask/$taskGroupId")
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
                Spacer(modifier = Modifier.width(8.dp))
                Text("New Subtask", fontFamily = Inter_Regular)
            }
        }
    }
}

@Composable
fun SubTaskCard(
    navController: NavController,
    subTaskId: String,
    subTaskTitle: String,
    subTaskDueDate: String,
    priority: String,
    isCompleted: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onToggleComplete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subTaskTitle,
                    fontFamily = Inter_Regular,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = if (isCompleted) Color.Gray else Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Due: $subTaskDueDate",
                    fontFamily = Inter_Regular,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "Priority: $priority",
                    fontFamily = Inter_Regular,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options"
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            menuExpanded = false
                            navController.navigate("updateSubTask/$subTaskId")
                            onEditClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            menuExpanded = false
                            onDeleteClick()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                RadioButton(
                    selected = isCompleted,
                    onClick = { onToggleComplete() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSubTaskList() {
    SubTaskListScreen(taskGroupId = "exampleId", navController = rememberNavController())
}
