package com.example.taskassistant.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskassistant.data.GroupMember
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class GroupMembersViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _members = MutableStateFlow<List<GroupMember>>(emptyList())
    val members: StateFlow<List<GroupMember>> get() = _members

    private val _membersWithUsernames = MutableStateFlow<List<Pair<GroupMember, String>>>(emptyList())
    val membersWithUsernames: StateFlow<List<Pair<GroupMember, String>>> get() = _membersWithUsernames

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    var searchQuery by mutableStateOf("")
    var searchResults by mutableStateOf<List<Pair<String, String>>>(emptyList()) // userId to username
    var isSearching by mutableStateOf(false)
    var searchError by mutableStateOf<String?>(null)
    var showAddUserDialog by mutableStateOf(false)

    fun fetchGroupMembersWithUsernames(groupId: String) {
        _isLoading.value = true
        _errorMessage.value = null

        db.collection("groupMembers")
            .whereEqualTo("groupId", groupId)
            .get()
            .addOnSuccessListener { snapshot ->
                val membersList = snapshot.toObjects(GroupMember::class.java)
                _members.value = membersList

                // Fetch usernames for members asynchronously
                viewModelScope.launch {
                    val combined = fetchUsernamesForMembers(membersList)
                    _membersWithUsernames.value = combined
                    _isLoading.value = false
                }
            }
            .addOnFailureListener { e ->
                _errorMessage.value = e.message
                _isLoading.value = false
            }
    }

    private suspend fun fetchUsernamesForMembers(
        groupMembers: List<GroupMember>
    ): List<Pair<GroupMember, String>> {
        val results = mutableListOf<Pair<GroupMember, String>>()

        for (member in groupMembers) {
            try {
                val userDoc = db.collection("users").document(member.userId).get().await()
                val firstName = userDoc.getString("firstName") ?: ""
                val lastName = userDoc.getString("lastName") ?: ""
                val fullName = when {
                    firstName.isNotBlank() && lastName.isNotBlank() -> "$firstName $lastName"
                    firstName.isNotBlank() -> firstName
                    lastName.isNotBlank() -> lastName
                    else -> "Unknown User"
                }
                results.add(member to fullName)
            } catch (e: Exception) {
                results.add(member to "Unknown User")
            }
        }
        return results
    }

    fun searchUsersByUsername(query: String) {
        isSearching = true
        searchError = null

        viewModelScope.launch {
            try {
                val db = FirebaseFirestore.getInstance()
                db.collection("logins")
                    .orderBy("username")
                    .startAt(query)
                    .endAt("$query\uf8ff") // FireStore search prefix trick
                    .get()
                    .addOnSuccessListener { result ->
                        val users = result.documents.mapNotNull { doc ->
                            val userId = doc.getString("userId")
                            val username = doc.getString("username")
                            if (userId != null && username != null) userId to username else null
                        }
                        searchResults = users
                    }
                    .addOnFailureListener { e ->
                        searchError = "Search failed: ${e.message}"
                    }
                    .addOnCompleteListener {
                        isSearching = false
                    }
            } catch (e: Exception) {
                searchError = "Unexpected error: ${e.message}"
                isSearching = false
            }
        }
    }


    fun addUserToGroup(groupId: String, userId: String) {
        viewModelScope.launch {
            try {
                val db = FirebaseFirestore.getInstance()
                val groupMemberId = UUID.randomUUID().toString()
                val joinedAt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                val newMember = hashMapOf(
                    "groupMemberId" to groupMemberId,
                    "groupId" to groupId,
                    "userId" to userId,
                    "roleId" to "member", // default role
                    "joinedAt" to joinedAt
                )

                db.collection("groupMembers")
                    .document(groupMemberId)
                    .set(newMember)
                    .addOnSuccessListener {
                        println("User added successfully")
                        // Optional: refresh group members list
                        fetchGroupMembersWithUsernames(groupId)
                    }
                    .addOnFailureListener { e ->
                        println("Error adding user: ${e.message}")
                    }

            } catch (e: Exception) {
                println("Exception adding user: ${e.message}")
            }
        }
    }


}
