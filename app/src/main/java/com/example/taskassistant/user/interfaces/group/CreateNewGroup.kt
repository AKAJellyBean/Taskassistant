package com.example.taskassistant.user.interfaces.group

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.taskassistant.R
import com.example.taskassistant.viewmodel.GroupViewModel

@Composable
fun CreateNewGroupScreen(
    navController: NavController,
    userId: String,
    viewModel: GroupViewModel = viewModel()
) {

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                IconButton(onClick = {
                    // TODO: Handle back navigation
                }) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_small_left),
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Create New Group",
                    fontSize = 20.sp
                )
            }

            // Group Name Input
            OutlinedTextField(
                value = viewModel.groupName,
                onValueChange = { viewModel.groupName = it },
                label = { Text("Group Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Group Description Input
            OutlinedTextField(
                value = viewModel.groupDescription,
                onValueChange = { viewModel.groupDescription = it },
                label = { Text("Group Description") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // FAB in bottom-right corner
        FloatingActionButton(
            onClick = {
                viewModel.createGroup(
                    userId = userId,
                    onSuccess = {
                        Toast.makeText(context, "Group successfully created!", Toast.LENGTH_LONG).show()
                    },
                    onFailure = {
                        Toast.makeText(context, "Group creation failed.", Toast.LENGTH_LONG).show()
                    }
                )
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.check),
                contentDescription = "Create Group"
            )
        }
    }
}

@Preview
@Composable
fun PreviewCreateNewGroupScreen() {
    CreateNewGroupScreen(
        userId = "",
        navController = rememberNavController()
    )
}
