package database.users

import SectionUnit
import com.zaxxer.hikari.HikariDataSource
import database.common.createDataSource
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun createUserProfileTableIfNotExists(db: HikariDataSource) {
    val createSQL = """
        CREATE TABLE IF NOT EXISTS user_profiles (
            user INT,
            section varchar(255),
            FOREIGN KEY (user) REFERENCES users(id)
        )
    """.trimIndent()

    db.connection.use { conn ->
        conn.createStatement().use { stmt ->
            stmt.execute(createSQL)
        }
    }
}

// Return true for successful reset, false for invalid SectionUnit length
fun resetUserProfileByUID(id: Int, profile: List<SectionUnit>, db: HikariDataSource): Boolean {
    val profileString = profile.map {
        val temp = Json.encodeToString(it)
        if (temp.length <= 255) temp else return false
    }
    resetUserProfileWithSerializationByUID(id, profileString, db)
    return true
}

private fun resetUserProfileWithSerializationByUID(id: Int, profile: List<String>, db: HikariDataSource) {
    dropExistingUserProfileByUID(id, db)
    profile.forEach { insertNewSectionUnitWithSerializationByUID(id, it, db) }
}

private fun insertNewSectionUnitWithSerializationByUID(id: Int, s: String, db: HikariDataSource) {
    val insertSQL = """
        INSERT IGNORE INTO user_profiles 
        (user, section)  
        VALUES (?, ?)
    """.trimIndent()

    db.connection.use { conn ->
        conn.prepareStatement(insertSQL).use { stmt ->
            stmt.setInt(1, id)
            stmt.setString(2, s)
            stmt.execute()
        }
    }
}

private fun dropExistingUserProfileByUID(id: Int, db: HikariDataSource) {
    val deleteSQL = """
        DELETE FROM user_profiles
        WHERE user = $id
    """.trimIndent()

    db.connection.use { conn ->
        conn.createStatement().use { stmt ->
            stmt.execute(deleteSQL)
        }
    }
}

fun queryExisingUserProfileByUID(id: Int, db: HikariDataSource): MutableList<SectionUnit> {
    val querySQL = """
       SELECT *
       FROM user_profiles
       WHERE user = $id
    """.trimIndent()

    val ret = mutableListOf<SectionUnit>()
    db.connection.use { conn ->
        conn.prepareStatement(querySQL).use { stmt ->
            stmt.executeQuery().use { result ->
                while (result.next()) {
                    ret.add(Json.decodeFromString(result.getString("section")))
                }
            }
        }
    }
    return ret
}

fun main() {
    createDataSource().use {
        createUserProfileTableIfNotExists(it)
        val s = mutableListOf<SectionUnit>()
        s.add(SectionUnit(1, 14.00f, 15.00f, "cs231", "good", "mc123"))
        resetUserProfileByUID(1, s, it)
        queryExisingUserProfileByUID(1, it).forEach { temp -> println(Json.encodeToString(temp)) }
    }
}