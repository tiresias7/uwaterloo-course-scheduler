package ktor

import kotlinx.serialization.Serializable

@Serializable
data class FacultyIDQueryRequest(val faculty: String, val courseID: String)

@Serializable
data class ProfileUpdateRequest(val id: Int, val profileNumber: Int, val profile: List<Int>)