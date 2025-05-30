package com.example.taskassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskassistant.data.GroupTask
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class GroupTaskViewModel: ViewModel() {

    private val db = Firebase.firestore

    private val _groupTask = MutableStateFlow<List<GroupTask>>(emptyList())
    val taskGroup: StateFlow<List<GroupTask>> = _groupTask

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private  val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchGroupTask(groupId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            db.collection("groupTasks")
                .whereEqualTo("groupId", groupId)
                .get()
                .addOnSuccessListener { querySnapShot ->
                    val tasks = querySnapShot.toObjects(GroupTask::class.java)
                    _groupTask.value = tasks
                    _isLoading.value = false

                }
                .addOnFailureListener { e ->
                    _errorMessage.value = e.message
                    _isLoading.value = false
                }
        }
    }

    fun createGroupTask(
        groupId: String,
        title: String,
        userId: String,
        description: String,
        dueDate: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val task = hashMapOf(
            "groupTaskId" to UUID.randomUUID().toString(),
            "groupId" to groupId,
            "groupTaskTitle" to title,
            "groupTaskDescription" to description,
            "dueDate" to dueDate,
            "isCompleted" to false,
            "assignTo" to emptyList<String>(),
            "createdBy" to userId,
            "createdAt" to LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )


        db.collection("groupTasks")
            .add(task)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError() }
    }


}