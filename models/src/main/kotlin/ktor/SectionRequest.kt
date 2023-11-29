package ktor

import kotlinx.serialization.Serializable

@Serializable
data class FacultyIDQueryRequest(val faculty: String, val courseID: String)