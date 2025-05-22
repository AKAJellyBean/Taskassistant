package com.example.taskassistant.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class HomeViewModel: ViewModel() {
    val welcomeMessage by mutableStateOf("Welcome to Task Assistant!")

}