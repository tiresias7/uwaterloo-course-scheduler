package net.codebot.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class Note(
    val id: Int,
    val title: String? = null,
    val content: String? = null
)

fun MutableList<Note>.findNextID(): Int {
    return (this.maxOfOrNull { it.id } ?: 0) + 1
}

fun MutableList<Note>.add(title: String? = null, content: String? = null) {
    this.add(Note(id = findNextID(), title = title, content = content))
}

fun MutableList<Note>.save(filename: String) {
    val jsonList = Json.encodeToString(this.toList())
    File(filename).writeText(jsonList)
}

fun MutableList<Note>.restore(filename: String) {
    try {
        val jsonList = File(filename).readText()
        this.clear()
        this.addAll(Json.decodeFromString(jsonList))
    } catch (_: Exception) {
    }
}