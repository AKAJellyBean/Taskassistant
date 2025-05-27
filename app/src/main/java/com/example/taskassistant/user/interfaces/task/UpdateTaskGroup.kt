package com.example.taskassistant.user.interfaces.task

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.taskassistant.user.interfaces.auth.DatePickerModal
import com.example.taskassistant.viewmodel.TaskGroupViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UpdateTaskGroupScreen(
    taskGroupId: String,
    navController: NavController,
    viewModel: TaskGroupViewModel = viewModel()
) {
    val context = LocalContext.current

    // Fetch taskGroup data once when screen load
    LaunchedEffect(Unit) {
        viewModel.loadTaskGroupDetails(taskGroupId = taskGroupId)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Create New Task Group", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = viewModel.title,
            onValueChange = { viewModel.title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.description,
            onValueChange = { viewModel.description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        DatePickerTextField()

        Button(
            onClick = {
                viewModel.updateTaskGroup(
                    taskGroupId = taskGroupId,
                    updatedTaskGroupTitle = viewModel.title,
                    updatedTaskGroupDescription = viewModel.description,
                    updatedDueDate = viewModel.dueDate,
                    onSuccess = {
                        Toast.makeText(context, "Task Successfully Updated", Toast.LENGTH_LONG).show()
                        navController.popBackStack()
                    },
                    onFailure = {
                        Toast.makeText(context, "Task Update Failed", Toast.LENGTH_LONG).show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Task Group")
        }


    }

}




@Preview
@Composable
fun PreviewUpdateTaskGroupScreen(){
    CreateTaskGroupScreen(
        navController = rememberNavController(),
        userId = ""
    )
}