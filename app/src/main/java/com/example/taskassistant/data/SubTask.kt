package com.example.taskassistant.data

data class SubTask (
    val subTaskId: String = "",
    val taskGroupId: String = "",
    val subTaskTitle: String = "",
    val dueDate: String = "",
    val priority: String = "",
    var isCompleted: Boolean = false
)