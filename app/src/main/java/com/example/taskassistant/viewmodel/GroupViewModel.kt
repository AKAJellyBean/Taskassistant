package com.example.taskassistant.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskassistant.data.Group
import com.example.taskassistant.data.GroupMember
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class GroupViewModel: ViewModel() {

    var groupName by mutableStateOf("")
    var groupDescription by mutableStateOf("")

    private val db = Firebase.firestore

    fun createGroup(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val groupId = UUID.randomUUID().toString()
        val group = Group(
            groupId = groupId,
            groupName = groupName,
            groupDescription = groupDescription,
            createdBy = userId,

        )

        db.collection("groups")
            .document(group.groupId)
            .set(group)
            .addOnSuccessListener {
                val memberId = UUID.randomUUID().toString()

                val groupMember = GroupMember(
                    groupMemberId = memberId,
                    userId = userId,
                    roleId = "leader",
                    groupId = groupId,
                    joinedAt = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                )

                db.collection("groupMembers")
                    .document(memberId)
                    .set(groupMember)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onFailure(it) }
            }
            .addOnFailureListener { onFailure(it) }
    }
}