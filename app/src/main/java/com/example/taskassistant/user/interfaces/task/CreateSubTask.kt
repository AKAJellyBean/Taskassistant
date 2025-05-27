package com.example.taskassistant.user.interfaces.task

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.taskassistant.user.interfaces.auth.DatePickerModal
import com.example.taskassistant.viewmodel.SubTaskViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.exp

@Composable
fun CreateSubTaskScreen(
    taskGroupId: String,
    viewModel: SubTaskViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Add new task to your task group",
                fontFamily = FontFamily.Monospace,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Input Fields
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = {viewModel.title = it},
                label = { Text(text = "Subtask Title") },
                modifier = Modifier.fillMaxWidth()
            )

            // Date picker text field goes here
            DueDatePickerTextField()
            // Priority picker goes here
            PriorityPickerTextField()

            // Create Task Button goes here
            Button(
                onClick = {
                    viewModel.createSubTask(
                        taskGroupId = taskGroupId,
                        onSuccess = {
                            Toast.makeText(context, "Task Successfully Created", Toast.LENGTH_LONG).show()
                            navController.popBackStack()
                        },
                        onFailure = {
                            Toast.makeText(context, "Task Creation Failed:", Toast.LENGTH_LONG).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Add Sub Task"
                )
            }

        }

    }
}

@Composable
fun DueDatePickerTextField(viewModel: SubTaskViewModel = viewModel()) {
    val context = LocalContext.current
    val selectedDate by viewModel.selectedDate.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val formattedDate = selectedDate?.let {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(it))
    } ?: ""


    OutlinedTextField(
        value = formattedDate,
        onValueChange = {},
        label = { Text(text = "Due Date") },
        readOnly = true,
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { showDialog = true}) {
                Icon(Icons.Default.DateRange, contentDescription = "Select Due Date")
            }
        },
        modifier = Modifier.fillMaxWidth()
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
            onDismiss = { showDialog = false}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityPickerTextField(
    viewModel: SubTaskViewModel = viewModel(),
) {
    val priorities = listOf("High", "Medium", "Low")
    val selectedPriority by viewModel.selectedPriority.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded}
    ) {
        OutlinedTextField(
            value = selectedPriority ?: "",
            onValueChange = {},
            label = { Text("Priority") },
            readOnly = true,
            trailingIcon =  {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Info, contentDescription = "Select Priority")
                }
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },

        ) {
            priorities.forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority) },
                    onClick = {
                        viewModel.updateSelectedPriority(priority)
                        expanded = false
                    }
                )
            }
        }
    }



}

@Preview
@Composable
fun PreviewCreateTaskScreen() {
    CreateSubTaskScreen(taskGroupId = "", navController = rememberNavController())
}