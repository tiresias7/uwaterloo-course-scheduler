package ktor

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(val email: String, val password: String)

@Serializable
data class SignUpRequest(val name: String, val passwordHashed: String, val email: String)

@Serializable
data class SignResponse(val uid: Int, val cookie: String)