package logic.ktorClient

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ktor.SignResponse
import request.SignStatus
import utility.hashPassword

suspend fun signUpNewUsers(name: String, password: String, email: String): Triple<SignStatus, Int, String> {
    val respond = httpClient.post("$baseUrl/user/sign-up") {
        parameter("name", name)
        parameter("passwordHashed", hashPassword(password))
        parameter("email", email)
    }
    val statusCode = respond.status
    return when (statusCode) {
        HttpStatusCode.Conflict -> Triple(SignStatus.SIGN_UP_FAILED, 0, "")
        else -> {
            val message: SignResponse = respond.body()
            Triple(SignStatus.SIGN_UP_CREATE, message.uid, message.cookie)
        }
    }
}

suspend fun signInExistingUsersByEmail(email: String, password: String): Triple<SignStatus, Int, String> {
    val respond = httpClient.post("$baseUrl/user/sign-in") {
        parameter("passwordHashed", hashPassword(password))
        parameter("email", email)
    }
    val statusCode = respond.status
    return when (statusCode) {
        HttpStatusCode.NotFound -> Triple(SignStatus.SIGN_IN_INVALID, 0, "")
        HttpStatusCode.Unauthorized -> Triple(SignStatus.SIGN_IN_FAILED, 0, "")
        else -> {
            val message: SignResponse = respond.body()
            Triple(SignStatus.SIGN_IN_SUCCESS, message.uid, message.cookie)
        }
    }
}
