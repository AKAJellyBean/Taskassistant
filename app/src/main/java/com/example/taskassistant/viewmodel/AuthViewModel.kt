package com.example.taskassistant.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskassistant.data.User
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskassistant.user.interfaces.auth.DatePickerModal
import com.example.taskassistant.utils.hashPassword
import com.example.taskassistant.utils.verifyPassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AuthViewModel: ViewModel() {
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var dateOfBirth by mutableStateOf("")
    var email by mutableStateOf("")
    var mobileNumber by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate: StateFlow<Long?> = _selectedDate

    private val firestore = FirebaseFirestore.getInstance()
    private val db = Firebase.firestore

    private val _actualUsername: String? = null
    private val _actualPassword: String? = null


    private fun convertToFirestoreTimestamp(dateString: String): Timestamp? {
        return try {
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            val date = sdf.parse(dateString)
            if (date != null) Timestamp(date) else null
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Date parse error: ${e.message}")
            null
        }
    }

    fun updateSelectedDate(date: Long?) {
        _selectedDate.value = date
    }

    fun getUser(): User {
        return User(
            firstName = firstName,
            lastName = lastName,
            dateOfBirth = dateOfBirth,
            email = email,
            mobileNumber = mobileNumber
        )
    }

    // Registering New User
    fun registerUser(
        onSuccess: (userId: String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = UUID.randomUUID().toString()
        val userMap = mapOf(
            "userId" to userId,
            "firstName" to firstName,
            "lastName" to lastName,
            "dob" to dateOfBirth,
            "email" to email,
            "mobileNumber" to mobileNumber,


        )

        Firebase.firestore.collection("users")
            .document(userId)
            .set(userMap)
            .addOnSuccessListener { onSuccess(userId) }
            .addOnFailureListener { onFailure(it) }


    }



    // Create Login Credentials
    fun createCredentials(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val loginId = UUID.randomUUID().toString()
        val hashedPassword = hashPassword(password)
        val loginMap = mapOf(
            "loginId" to loginId,
            "userId" to userId,
            "username" to username,
            "password" to hashedPassword
        )

        Firebase.firestore.collection("logins")
            .document(loginId)
            .set(loginMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // Login User
    fun userLogin(
        onSuccess: (userId: String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val enteredUsername = username
        val enteredPassword = password


        db.collection("logins")
            .whereEqualTo("username", enteredUsername)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()
                    val actualPassword = document.getString("password") ?: ""

                    if (verifyPassword(enteredPassword, actualPassword)) {
                        val userId = document.getString("userId") ?: ""
                        onSuccess(userId)
                    } else {


                        onFailure(Exception("Invalid Username or Password"))
                    }

                } else {
                    onFailure(Exception("Invalid Username or Password"))
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Track User Login
    fun trackUserLog(userId: String) {
        val logID = UUID.randomUUID().toString()
        val logMap = mapOf(
            "logID" to logID,
            "userId" to userId,
            "lastLogin" to FieldValue.serverTimestamp()
        )

        Firebase.firestore.collection("logs")
            .document(logID)
            .set(logMap)
    }


}