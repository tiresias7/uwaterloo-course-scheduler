package logic.ktorClient

import Section
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ktor.FacultyIDQueryRequest

suspend fun querySectionsByFacultyId(faculty: String, courseID: String): List<List<Section>> {
    return httpClient.get("$baseUrl/section/faculty/id") {
        contentType(ContentType.Application.Json)
        setBody(FacultyIDQueryRequest(faculty, courseID))
    }.body()
}

suspend fun queryAllClasses(): List<String> {
    return httpClient.get("$baseUrl/section/courses").body()
}

suspend fun main() {
    println("...........")
    querySectionsByFacultyId("CS", "135").forEach {
        println(it)
    }
    println("...........")
    println(queryAllClasses())
}