package com.example.taskassistant.user.interfaces.group

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskassistant.data.GroupTask

@Composable
fun GroupTaskDetailDialog(
    task: GroupTask,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        title = {
            Text(text = task.groupTaskTitle, fontSize = 20.sp)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Description: ${task.groupTaskDescription}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Assigned To: ${task.assignTo.joinToString(", ")}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Created By: ${task.createdBy}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Created At: ${task.createdAt}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Due Date: ${task.dueDate}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Completed: ${if (task.isCompleted) "Yes" else "No"}")
            }
        },
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewGroupTaskDetailDialog() {
    val mockTask = GroupTask(
        groupTaskId = "1",
        groupId = "g1",
        groupTaskTitle = "Mock Task",
        groupTaskDescription = "Do something important.",
        createdBy = "Jane Doe",
        createdAt = "2025-05-30",
        dueDate = "2025-06-01",
        isCompleted = false,
        assignTo = listOf("user1", "user2")
    )
    GroupTaskDetailDialog(task = mockTask, onDismiss = {})
}
