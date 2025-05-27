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
import androidx.compose.runtime.LaunchedEffect
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
fun UpdateSubTaskScreen(
    subTaskId: String,
    viewModel: SubTaskViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val selectedPriority by viewModel.selectedPriority.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()

    // Fetch subtask once when screen launches
    LaunchedEffect (Unit) {
        viewModel.loadSubTaskDetails(subTaskId)
    }

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
                text = "Update your subtask",
                fontFamily = FontFamily.Monospace,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
                label = { Text(text = "Subtask Title") },
                modifier = Modifier.fillMaxWidth()
            )

            DueDatePickerTextField()
            PriorityPickerTextField()

            Button(
                onClick = {
                    viewModel.updateSubTask(
                        subTaskId = subTaskId,
                        updatedTitle = viewModel.title,
                        updatedDueDate = viewModel.dueDate,
                        updatedPriority = selectedPriority ?: "",
                        updatedIsCompleted = false, // or keep current if fetched
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
                Text(text = "Update Subtask")
            }
        }
    }
}



@Preview
@Composable
fun PreviewUpdateTaskScreen() {
    UpdateSubTaskScreen(subTaskId = "", navController = rememberNavController())
}