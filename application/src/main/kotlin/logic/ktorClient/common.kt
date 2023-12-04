package logic.ktorClient

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
        addDefaultResponseValidation()
    }
}

// Shared base URL
const val baseUrl = "https://ktor-course-select-pbz7aoqn4a-pd.a.run.app"
