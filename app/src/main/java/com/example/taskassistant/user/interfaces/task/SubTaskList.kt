package com.example.taskassistant.user.interfaces.task

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        LaunchedEffect(key1 = taskGroupId) {
            viewModel.getUserSubTaskList(
                taskGroupId = taskGroupId,
                onSuccess = {},
                onFailure = {}
            )
        }
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Task Group Heading",
                fontFamily = Inter_Regular,
                fontSize = 25.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

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
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Floating Action Button
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate("createSubTask/$taskGroupId")
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
                Spacer(modifier = Modifier.width(8.dp))
                Text("New Subtask")
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = subTaskTitle,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 15.sp
                )
                Text(
                    text = "Due Date: $subTaskDueDate",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp
                )
                Text(
                    text = "Priority: $priority",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Option Menu"
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

                RadioButton(
                    selected = isCompleted,
                    onClick = { onToggleComplete() }
                )


            }

        }

    }
}



@Preview()
@Composable
fun PreviewTaskList() {
    SubTaskListScreen(taskGroupId = "", navController = rememberNavController())
}