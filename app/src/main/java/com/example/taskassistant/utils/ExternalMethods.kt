package com.example.taskassistant.utils

import android.content.Context
import android.media.MediaPlayer
import com.example.taskassistant.R
import java.security.MessageDigest
import android.media.RingtoneManager
import android.media.Ringtone
import android.net.Uri


fun hashPassword(password: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}

fun verifyPassword(inputPassword: String, storedHashedPassword: String): Boolean {
    val inputHashed = hashPassword(inputPassword)
    return inputHashed == storedHashedPassword

}

fun playNotificationSound(context: Context) {
    try {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone: Ringtone = RingtoneManager.getRingtone(context, notification)
        ringtone.play()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}



