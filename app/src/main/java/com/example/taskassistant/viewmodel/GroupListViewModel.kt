package com.example.taskassistant.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.taskassistant.data.Group
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class GroupListViewModel: ViewModel() {

    private val db = Firebase.firestore

    var groups = mutableStateListOf<Group>()
        private set

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun loadGroupForUser(userId: String) {
        isLoading.value = true
        errorMessage.value = null

        db.collection("groupMembers")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapShot ->
                val groupIds = querySnapShot.documents.mapNotNull { it.getString("groupId") }

                groups.clear()

                if (groupIds.isEmpty()) {
                    isLoading.value = false
                    return@addOnSuccessListener
                }

                groupIds.forEach {groupId ->
                    db.collection("groups")
                        .document(groupId)
                        .get()
                        .addOnSuccessListener { groupDoc ->
                            groupDoc.toObject(Group::class.java)?.let { group ->
                                groups.add(group)
                            }
                            isLoading.value = false
                        }
                        .addOnFailureListener {
                            errorMessage.value = it.message
                            isLoading.value = false
                        }
                }
            }
            .addOnFailureListener {
                errorMessage.value = it.message
                isLoading.value = false
            }
    }
}