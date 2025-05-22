package com.example.taskassistant.user.interfaces.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskassistant.ui.theme.TaskassistantTheme
import com.example.taskassistant.viewmodel.AuthViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController


@Composable
fun CreateCredentialsScreen(
    modifier: Modifier = Modifier,
    userId: String,
    navController: NavController,
    viewModel: AuthViewModel = viewModel(),

) {
    val context = LocalContext.current
    val username = viewModel.username
    val password = viewModel.password
    val confirmPassword = viewModel.confirmPassword

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
            Text(
                text = "Set Your Credentials",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { viewModel.username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { viewModel.confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // TODO: Validate and finish registration, then navigate to HomeScreen
                    if(viewModel.password != viewModel.confirmPassword) {
                        Toast.makeText(context, "Password do not match!", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    viewModel.createCredentials(
                        userId = userId,
                        onSuccess = {
                            Toast.makeText(context, "Credentials Created", Toast.LENGTH_LONG).show()
                            navController.navigate("getStartedScreen") {
                                popUpTo("createCredentials") {inclusive = true}
                            }

                        },
                        onFailure = {exception ->
                            Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
                            Log.e("Credentials", "Error: ${exception.message}")
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Continue", fontSize = 18.sp)
            }
        }
    }
}

