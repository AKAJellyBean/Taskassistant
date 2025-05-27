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
fun CreateTaskGroupScreen(
    navController: NavController,
    userId: String,
    viewModel: TaskGroupViewModel = viewModel()
) {
    val context = LocalContext.current
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
                viewModel.createTaskGroup(
                    userId = userId,
                    onSuccess = {
                        Toast.makeText(context, "Task Group Successfully created", Toast.LENGTH_LONG).show()
                        navController.popBackStack()
                    },
                    onFailure = {
                        Toast.makeText(context, "Task Group Creation Failed", Toast.LENGTH_LONG).show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Task Group")
        }


    }

}

@Composable
fun DatePickerTextField(viewModel: TaskGroupViewModel = viewModel()) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val formattedDate = selectedDate?.let {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(it))
    } ?: ""

    OutlinedTextField(
        value = formattedDate,
        onValueChange = {},
        label = { Text("Due Date") },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.DateRange, contentDescription = "Select Date")
            }
        }
    )

    if (showDialog) {
        DatePickerModal(
            onDateSelected = { millis ->
                viewModel.updateSelectedDate(millis)
                viewModel.dueDate = millis?.let {
                    SimpleDateFormat("MM/dd/yyyy", Locale.US).format(Date(it))
                } ?: ""
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}




@Preview
@Composable
fun PreviewCreateTaskGroupScreen(){
    CreateTaskGroupScreen(
        navController = rememberNavController(),
        userId = ""
    )
}