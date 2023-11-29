package com.example.logic

import java.security.MessageDigest
import java.util.*

private val secretKey = "pp_secret_key" // Change this to actual key
private val digest = MessageDigest.getInstance("SHA-512") // algorithm
fun generateUserCookie(uid: Int): String {
    val timestamp = System.currentTimeMillis()
    val data = "$uid:$timestamp:$secretKey"
    val hash = Base64.getEncoder().encodeToString(digest.digest(data.toByteArray()))
    return "$uid:$timestamp:$hash"
}

fun verifyUserCookie(cookie: String): Boolean {
    val parts = cookie.split(":")
    if (parts.size != 3) return false

    val uid = parts[0].toIntOrNull() ?: return false
    val timestamp = parts[1].toLongOrNull() ?: return false
    val hash = parts[2]

    // Validate the timestamp
    val currentTime = System.currentTimeMillis()
    if (currentTime - timestamp > 3600 * 1000) {
        return false // Cookie expired (1 hr)
    }

    // Verify the hash
    val data = "$uid:$timestamp:$secretKey"
    val computedHash = Base64.getEncoder().encodeToString(digest.digest(data.toByteArray()))

    return hash == computedHash
}