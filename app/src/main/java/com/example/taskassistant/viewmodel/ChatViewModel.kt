package com.example.taskassistant.viewmodel

import androidx.lifecycle.ViewModel
import com.example.taskassistant.data.ChatMessage
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class ChatViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val db2 = Firebase.firestore

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _latestMessage = MutableStateFlow<ChatMessage?>(null)
    val latestMessage: StateFlow<ChatMessage?> = _latestMessage

    fun listenForMessages(groupId: String) {
        Firebase.firestore.collection("groupChats")
            .document(groupId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, _ ->
                val msgs = snapshots?.documents?.mapNotNull { it.toObject(ChatMessage::class.java) } ?: emptyList()
                _messages.value = msgs

                // Set the latest message if it's new
                if (msgs.isNotEmpty()) {
                    _latestMessage.value = msgs.last()
                }
            }
    }

    fun sendMessage(groupId: String, senderId: String, message: String) {
        val newMessage = ChatMessage(
            messageId = UUID.randomUUID().toString(),
            senderId = senderId,
            groupId = groupId,
            message = message,
            timestamp = System.currentTimeMillis()
        )
        db.collection("groupChats")
            .document(groupId)
            .collection("messages")
            .document(newMessage.messageId)
            .set(newMessage)
    }


}
