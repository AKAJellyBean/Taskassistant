package com.example.taskassistant.user.interfaces.group

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskassistant.R
import com.example.taskassistant.viewmodel.GroupFileViewModel
import com.example.taskassistant.data.GroupFile

@Composable
fun GroupFileScreen(
    groupId: String,
    viewModel: GroupFileViewModel = viewModel()
) {
    val context = LocalContext.current
    val files by viewModel.files.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val name = uri.lastPathSegment?.substringAfterLast("/") ?: "UploadedFile"
            viewModel.uploadFile(
                groupId = groupId,
                fileUri =  uri,
                fileName = name,
                onSuccess = { viewModel.fetchGroupFiles(groupId) },
                onError = { /* Optional: Show error */ }
            )
        }
    }

    LaunchedEffect(groupId) {
        viewModel.fetchGroupFiles(groupId)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { launcher.launch("*/*") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(painter = painterResource(R.drawable.clip_file), contentDescription = "Upload File")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)) {

            if (files.isEmpty()) {
                Text(
                    text = "No files uploaded yet.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(files) { file ->
                        FileItem(file)
                    }
                }
            }
        }
    }
}

@Composable
fun FileItem(file: GroupFile) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = file.fileName,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Uploaded: ${file.uploadedAt}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
