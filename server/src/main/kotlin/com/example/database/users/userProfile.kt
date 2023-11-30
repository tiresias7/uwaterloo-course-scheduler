package com.example.database.users

import Section
import com.zaxxer.hikari.HikariDataSource
import com.example.database.common.createDataSource
import kotlinx.datetime.toKotlinLocalTime
import java.time.DayOfWeek

fun createUserProfileTableIfNotExists(db: HikariDataSource) {
    val createSQL = """
        CREATE TABLE IF NOT EXISTS user_profiles (
            user INT,
            profile_number INT,
            section INT,
            FOREIGN KEY (user) REFERENCES users(id),
            FOREIGN KEY (section) REFERENCES sections(classNumber)
        )
    """.trimIndent()

    db.connection.use { conn ->
        conn.createStatement().use { stmt ->
            stmt.execute(createSQL)
        }
    }
}

// Return true for successful reset, false for invalid SectionUnit length
fun resetUserProfileByUID(id: Int, profileNumber: Int, profile: List<Int>, db: HikariDataSource) {
    dropExistingUserProfileByUID(id, profileNumber, db)
    profile.forEach { insertNewSectionByUIDClassNumber(id, profileNumber, it, db) }
}

//private fun resetUserProfileWithSerializationByUID(id: Int, profile: List<String>, db: HikariDataSource) {
//    dropExistingUserProfileByUID(id, db)
//    profile.forEach { insertNewSectionUnitWithSerializationByUID(id, it, db) }
//}

private fun insertNewSectionByUIDClassNumber(id: Int, profileNumber: Int, classNumber: Int, db: HikariDataSource) {
    val insertSQL = """
        INSERT IGNORE INTO user_profiles 
        (user, profile_number, section)  
        VALUES (?, ?, ?)
    """.trimIndent()

    db.connection.use { conn ->
        conn.prepareStatement(insertSQL).use { stmt ->
            stmt.setInt(1, id)
            stmt.setInt(2, profileNumber)
            stmt.setInt(3, classNumber)
            stmt.execute()
        }
    }
}

private fun dropExistingUserProfileByUID(id: Int, profileNumber: Int, db: HikariDataSource) {
    val deleteSQL = """
        DELETE FROM user_profiles
        WHERE user = $id AND profile_number = $profileNumber
    """.trimIndent()

    db.connection.use { conn ->
        conn.createStatement().use { stmt ->
            stmt.execute(deleteSQL)
        }
    }
}

fun queryExisingUserProfileByUID(id: Int, profileNumber: Int, db: HikariDataSource): MutableList<Section> {
    val querySQL = """
       SELECT *
       FROM user_profiles
       JOIN courses.sections s on s.classNumber = user_profiles.section
       WHERE user = $id AND profile_number = $profileNumber
    """.trimIndent()

    val sections = mutableListOf<Section>()
    db.connection.use { conn ->
        conn.prepareStatement(querySQL).use { stmt ->
            stmt.executeQuery().use { result ->
                while (result.next()) {
                    val days: Set<DayOfWeek> = if (result.getString("days") == "") emptySet()
                    else result.getString("days").split(",").map { DayOfWeek.valueOf(it) }.toSet()
                    sections.add(
                        Section(
                            result.getInt("classNumber"),
                            result.getString("component"),
                            result.getInt("sectionNumber"),
                            result.getString("campus"),
                            result.getString("room"),
                            result.getString("instructor"),
                            result.getTime("startTime").toLocalTime().toKotlinLocalTime(),
                            result.getTime("endTime").toLocalTime().toKotlinLocalTime(),
                            days,
                            result.getString("faculty") + result.getString("courseID")
                        )
                    )
                }
            }
        }
    }
    return sections
}

fun main() {
    createDataSource().use {
        createUserProfileTableIfNotExists(it)
//       insertNewSectionByUIDClassNumber(1, 1, 2785, it)
//        insertNewSectionByUIDClassNumber(1, 1, 2786, it)
//
//        insertNewSectionByUIDClassNumber(1, 2, 2789, it)
       queryExisingUserProfileByUID(1, 1, it).forEach { temp -> println(temp) }
    }
}