package com.example.taskassistant.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class Group(
    val groupId: String = "",
    val groupName: String = "",
    val groupDescription: String = "",
    val createdBy: String = "",
    val createdAt: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
    val updatedAt: String = ""
)