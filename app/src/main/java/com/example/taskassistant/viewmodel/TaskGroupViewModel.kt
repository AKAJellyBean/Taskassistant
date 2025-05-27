package com.example.taskassistant.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.taskassistant.data.TaskGroup
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.ArrayList
import java.util.UUID

class TaskGroupViewModel: ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var dueDate by mutableStateOf("")
    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate: StateFlow<Long?> = _selectedDate

    val taskGroupList: SnapshotStateList<TaskGroup> = mutableStateListOf()

    private val db = Firebase.firestore

    fun updateSelectedDate(date: Long?) {
        _selectedDate.value = date
    }

    fun createTaskGroup(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        val taskGroup = TaskGroup(
            taskGroupId = UUID.randomUUID().toString(),
            userId = userId,
            taskGroupTitle = title,
            taskGroupDescription = description,
            dueDate = dueDate,
            isCompleted = false
        )

        db.collection("taskGroups")
            .document(taskGroup.taskGroupId)
            .set(taskGroup)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }

    }

    fun getUserTaskList(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("taskGroups")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                taskGroupList.clear()  // Clear previous data to avoid duplicates

                for (document in querySnapshot.documents) {
                    val taskGroup = document.toObject(TaskGroup::class.java)
                    if (taskGroup != null) {
                        taskGroupList.add(taskGroup)
                    }
                }

                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun loadTaskGroupDetails(taskGroupId: String) {
        db.collection("taskGroups")
            .document(taskGroupId)
            .get()
            .addOnSuccessListener { document ->
                document?.toObject(TaskGroup::class.java)?.let { taskGroup ->
                    title = taskGroup.taskGroupTitle
                    description = taskGroup.taskGroupDescription
                    dueDate = taskGroup.dueDate
                }

            }
    }

    fun updateTaskGroup(
        taskGroupId: String,
        updatedTaskGroupTitle: String,
        updatedTaskGroupDescription: String,
        updatedDueDate: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val updatedTaskGroup = mapOf(
            "taskGroupTitle" to updatedTaskGroupTitle,
            "taskGroupDescription" to updatedTaskGroupDescription,
            "dueDate" to updatedDueDate
        )

        db.collection("taskGroups")
            .document(taskGroupId)
            .update(updatedTaskGroup)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun updateTaskGroupStatus(
        taskGroupId: String,
        updatedCompleted: Boolean,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val updatedStatus = mapOf(
            "completed" to updatedCompleted
        )

        db.collection("taskGroups")
            .document(taskGroupId)
            .update(updatedStatus)
            .addOnSuccessListener {
                val index = taskGroupList.indexOfFirst { it.taskGroupId == taskGroupId }
                if (index != -1) {
                    taskGroupList[index] = taskGroupList[index].copy(isCompleted = updatedCompleted)
                }
                onSuccess()
            }
            .addOnFailureListener { onFailure(it) }
    }

    fun deleteTaskGroup(
        taskGroupId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("taskGroups")
            .document(taskGroupId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }



}