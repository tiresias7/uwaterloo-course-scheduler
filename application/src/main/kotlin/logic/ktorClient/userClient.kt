package logic.ktorClient

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import ktor.ProfileUpdateRequest
import ktor.SignInRequest
import ktor.SignResponse
import ktor.SignUpRequest
import request.SignStatus
import utility.hashPassword

suspend fun signUpNewUsers(name: String, password: String, email: String): Triple<SignStatus, Pair<Int, String>, String> {
    val respond = httpClient.post("$baseUrl/user/sign-up") {
        contentType(ContentType.Application.Json)
        setBody(SignUpRequest(name, hashPassword(password), email))
    }
    val statusCode = respond.status
    return when (statusCode) {
        HttpStatusCode.Conflict -> Triple(SignStatus.SIGN_UP_FAILED, Pair(0, ""), "")
        else -> {
            val message: SignResponse = respond.body()
            Triple(SignStatus.SIGN_UP_CREATE, message.uidNamePair, message.cookie)
        }
    }
}

// The password hashing over internet might need to be resolved
suspend fun signInExistingUsersByEmail(email: String, password: String): Triple<SignStatus, Pair<Int, String>, String> {
    val respond = httpClient.put("$baseUrl/user/sign-in") {
        contentType(ContentType.Application.Json)
        setBody(SignInRequest(email, password))
    }
    val statusCode = respond.status
    return when (statusCode) {
        HttpStatusCode.NotFound -> Triple(SignStatus.SIGN_IN_INVALID, Pair(0, ""), "")
        HttpStatusCode.Unauthorized -> Triple(SignStatus.SIGN_IN_FAILED, Pair(0, ""), "")
        else -> {
            val message: SignResponse = respond.body()
            Triple(SignStatus.SIGN_IN_SUCCESS, message.uidNamePair, message.cookie)
        }
    }
}

suspend fun updateUserProfile(id: Int, profileNumber: Int, profile: List<Int>) {
    httpClient.put("$baseUrl/friend/profile") {
        contentType(ContentType.Application.Json)
        setBody(ProfileUpdateRequest(id, profileNumber, profile))
    }
}

suspend fun main() {
    updateUserProfile(2, 1, intArrayOf(2787, 2786).toList())
//    println(signInExistingUsersByEmail("aaa@gmail.com", "password"))
//    println(signInExistingUsersByEmail("aaa@gmail.com", "passd"))
//    println(signInExistingUsersByEmail("pp@gmail.com", "password"))
//    println("....................")
//    println(signUpNewUsers("hello", "password","aaa@gmail.com"))
//    println(signUpNewUsers("xiaoye", "password","pp@good.com"))
//    println(signInExistingUsersByEmail("pp@good.com", "password"))
}
