package logic.ktorClient

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ktor.SignInRequest
import ktor.SignResponse
import ktor.SignUpRequest
import request.SignStatus
import utility.hashPassword

suspend fun signUpNewUsers(name: String, password: String, email: String): Triple<SignStatus, Int, String> {
    val respond = httpClient.post("$baseUrl/user/sign-up") {
        contentType(ContentType.Application.Json)
        setBody(SignUpRequest(name, hashPassword(password), email))
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

// The password hashing over internet might need to be resolved
suspend fun signInExistingUsersByEmail(email: String, password: String): Triple<SignStatus, Int, String> {
    val respond = httpClient.post("$baseUrl/user/sign-in") {
        contentType(ContentType.Application.Json)
        setBody(SignInRequest(email, password))
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

suspend fun main() {
    println(signInExistingUsersByEmail("aaa@gmail.com", "password"))
    println(signInExistingUsersByEmail("aaa@gmail.com", "passd"))
    println(signInExistingUsersByEmail("pp@gmail.com", "password"))
    println("....................")
    println(signUpNewUsers("hello", "password","aaa@gmail.com"))
    println(signUpNewUsers("xiaoye", "password","pp@good.com"))
    println(signInExistingUsersByEmail("pp@good.com", "password"))
}
