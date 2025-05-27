package com.example.taskassistant.data

data class TaskGroup(
    val taskGroupId: String = "",
    val userId: String = "",
    val taskGroupTitle: String = "",
    val taskGroupDescription: String = "",
    val dueDate: String = "",
    val isCompleted: Boolean = false
)