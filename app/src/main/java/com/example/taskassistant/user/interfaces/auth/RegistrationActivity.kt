package com.example.taskassistant.user.interfaces.auth

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taskassistant.viewmodel.AuthViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel = viewModel(),
) {
    val context = LocalContext.current

    val firstName = viewModel.firstName
    val lastName = viewModel.lastName
    val dateOfBirth = viewModel.dateOfBirth
    val email = viewModel.email
    val mobileNumber = viewModel.mobileNumber

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineLarge,
                )
                Text(
                    text = "Please fill in your personal details below.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(5.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StyledTextField(value = firstName, label = "First Name", onValueChange = { viewModel.firstName = it })
                        StyledTextField(value = lastName, label = "Last Name", onValueChange = { viewModel.lastName = it })
                        DateOfBirthPicker(viewModel)
                        StyledTextField(value = email, label = "Email", keyboardType = KeyboardType.Email, onValueChange = { viewModel.email = it })
                        StyledTextField(value = mobileNumber, label = "Mobile Number", keyboardType = KeyboardType.Phone, onValueChange = { viewModel.mobileNumber = it })
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = {
                    navController.navigate("login")
                }) {
                    Text("Already have an account? Log in")
                }
                Button(
                    onClick = {
                        viewModel.registerUser(
                            onSuccess = { userId ->
                                if (userId.isNotEmpty()) {
                                    navController.navigate("createCredentials/$userId")
                                } else {
                                    Toast.makeText(context, "Invalid user ID, try again.", Toast.LENGTH_LONG).show()
                                }
                            },
                            onFailure = { exception ->
                                Log.e("Registration", "Error: ${exception.message}")
                                Toast.makeText(context, "Registration failed: ${exception.message}", Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Register", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun StyledTextField(
    value: String,
    label: String,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = if (placeholder.isNotEmpty()) { { Text(placeholder) } } else null,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {

    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun DateOfBirthPicker(viewModel: AuthViewModel = viewModel()) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val formattedDate = selectedDate?.let {
        val date = Date(it)
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
    } ?: "No Date Selected"

    Column {
        Text(text = "Selected Date, $formattedDate")
        Button(onClick = {showDialog = true}) {
            Text("Pick a Date")
        }

        if (showDialog) {
            DatePickerModal(
                onDateSelected = { millis ->
                    viewModel.updateSelectedDate(millis)
                    viewModel.dateOfBirth = millis?.let {
                        SimpleDateFormat("MM/dd/yyyy", Locale.US).format(Date(it))
                    } ?: ""
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}