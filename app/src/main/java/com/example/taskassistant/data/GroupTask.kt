package com.example.taskassistant.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class GroupTask(
    val groupTaskId: String = "",
    val createdBy: String = "",
    val groupId: String = "",
    val groupTaskTitle: String = "",
    val groupTaskDescription: String = "",
    val dueDate: String = "",
    val isCompleted: Boolean = false,
    val assignTo: List<String> = emptyList(),
    val createdAt: String =  LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
)
