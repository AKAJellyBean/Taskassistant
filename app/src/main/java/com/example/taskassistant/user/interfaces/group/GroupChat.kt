package com.example.taskassistant.user.interfaces.group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taskassistant.viewmodel.ChatViewModel
import com.example.taskassistant.utils.playNotificationSound
import androidx.compose.material3.TextFieldDefaults


@Composable
fun ChatScreen(
    groupId: String,
    userId: String,
    viewModel: ChatViewModel = viewModel(),
    navController: NavController
) {
    val messages by viewModel.messages.collectAsState()
    val latestMessage by viewModel.latestMessage.collectAsState()
    var input by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Play sound if a new message is received from another user
    LaunchedEffect(latestMessage) {
        if (latestMessage != null && latestMessage!!.senderId != userId) {
            playNotificationSound(context)
        }
    }

    // Start listening for messages
    LaunchedEffect(groupId) {
        viewModel.listenForMessages(groupId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(messages.reversed()) { msg ->
                val isSender = msg.senderId == userId
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isSender) Arrangement.End else Arrangement.Start
                ) {
                    ChatBubble(message = msg.message, isSender = isSender)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = { Text("Type your message...") },
                shape = RoundedCornerShape(16.dp)
            )

            IconButton(
                onClick = {
                    if (input.isNotBlank()) {
                        viewModel.sendMessage(groupId, userId, input)
                        input = ""
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
            }
        }

    }
}

@Composable
fun ChatBubble(message: String, isSender: Boolean) {
    val bubbleColor = if (isSender) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isSender) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    val alignment = if (isSender) Alignment.End else Alignment.Start

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .background(bubbleColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(
            text = message,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
