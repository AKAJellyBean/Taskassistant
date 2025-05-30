package com.example.taskassistant.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskassistant.data.GroupFile
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class GroupFileViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance().reference

    private val _files = MutableStateFlow<List<GroupFile>>(emptyList())
    val files: StateFlow<List<GroupFile>> = _files

    fun fetchGroupFiles(groupId: String) {
        db.collection("groupFiles")
            .whereEqualTo("groupId", groupId)
            .get()
            .addOnSuccessListener { result ->
                _files.value = result.toObjects(GroupFile::class.java)
            }
    }

    fun uploadFile(
        groupId: String,
        fileUri: Uri,
        fileName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val fileId = UUID.randomUUID().toString()
        val ref = storage.child("groupFiles/$fileId-$fileName")

        println("Uploading file: $fileName to $fileId")

        ref.putFile(fileUri)
            .addOnSuccessListener {
                println("Upload succeeded")
                ref.downloadUrl.addOnSuccessListener { uri ->
                    println("Got download URL: $uri")
                    val fileData = GroupFile(
                        fileId = fileId,
                        fileName = fileName,
                        fileUrl = uri.toString(),
                        groupId = groupId,
                        uploadedAt = Date().toString()
                    )

                    db.collection("groupFiles")
                        .document(fileId)
                        .set(fileData)
                        .addOnSuccessListener {
                            println("Document added")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            println("Firestore error: ${e.message}")
                            onError(e.message ?: "Firestore Error")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Upload failed: ${e.message}")
                onError(e.message ?: "Upload Failed")
            }
    }

}
