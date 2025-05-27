package com.example.taskassistant.viewmodel

import androidx.compose.foundation.MutatePriority
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.taskassistant.data.SubTask
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class SubTaskViewModel: ViewModel() {
    var title by mutableStateOf("")
    var dueDate by mutableStateOf("")
    var priority by mutableStateOf("")
    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate: StateFlow<Long?> = _selectedDate

    private val _selectedPriority = MutableStateFlow<String?>(null)
    var selectedPriority: StateFlow<String?> = _selectedPriority

    val subTaskList: SnapshotStateList<SubTask> = mutableStateListOf()
    private val db = Firebase.firestore

    fun updateSelectedDate(date: Long?) {
        _selectedDate.value = date
    }

    fun updateSelectedPriority(priority: String?) {
        _selectedPriority.value = priority
    }

    fun createSubTask(
        taskGroupId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val priorityValue = selectedPriority.value ?: ""
        val subTask = SubTask(
            subTaskId = UUID.randomUUID().toString(),
            taskGroupId = taskGroupId,
            subTaskTitle = title,
            dueDate = dueDate,
            priority = priorityValue,
            isCompleted = false,
        )

        db.collection("subTasks")
            .document(subTask.subTaskId)
            .set(subTask)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getUserSubTaskList(
        taskGroupId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("subTasks")
            .whereEqualTo("taskGroupId", taskGroupId)
            .get()
            .addOnSuccessListener { querySnapShot ->
                subTaskList.clear() // clear previous data for avoid duplication

                for (document in querySnapShot.documents) {
                    val subTask = document.toObject(SubTask::class.java)
                    if (subTask != null) {
                        subTaskList.add(subTask)
                    }
                }
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun loadSubTaskDetails(subTaskId: String) {
        db.collection("subTasks")
            .document(subTaskId)
            .get()
            .addOnSuccessListener { document ->
                document?.toObject(SubTask::class.java)?.let { subTask ->
                    title = subTask.subTaskTitle
                    dueDate = subTask.dueDate
                    _selectedPriority.value = subTask.priority
                    _selectedDate.value = null // convert dueDate to timestamp if needed
                }
            }
    }


    fun updateSubTask(
        subTaskId: String,
        updatedTitle: String,
        updatedDueDate: String,
        updatedPriority: String,
        updatedIsCompleted: Boolean,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val updatedTask = mapOf(
            "subTaskTitle" to updatedTitle,
            "dueDate" to updatedDueDate,
            "priority" to updatedPriority,
            "isCompleted" to updatedIsCompleted
        )

        db.collection("subTasks")
            .document(subTaskId)
            .update(updatedTask)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun updateSubTaskStatus(
        subTaskId: String,
        updatedIsCompleted: Boolean,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val updatedStatus = mapOf(
            "isCompleted" to updatedIsCompleted
        )

        db.collection("subTasks")
            .document(subTaskId)
            .update(updatedStatus)
            .addOnSuccessListener {
                val index = subTaskList.indexOfFirst { it.subTaskId == subTaskId }
                if (index != -1 ) {
                    subTaskList[index] = subTaskList[index].copy(isCompleted = updatedIsCompleted)
                }
                onSuccess()
            }
            .addOnFailureListener { onFailure(it) }
    }

    fun deleteSubTask(
        subTaskId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("subTasks")
            .document(subTaskId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}