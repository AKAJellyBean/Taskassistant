package com.example.taskassistant.data

data class ChatMessage(
    val messageId: String = "",
    val senderId: String = "",
    val groupId: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
