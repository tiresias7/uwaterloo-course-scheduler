package com.example.database.users

import Section
import com.example.database.common.DBUtil
import com.example.database.sections.parseSectionListResultSet

fun createUserProfileTableIfNotExists() {
    val createSQL = """
        CREATE TABLE IF NOT EXISTS user_profiles (
            user INT,
            profile_number INT,
            section INT,
            FOREIGN KEY (user) REFERENCES users(id),
            FOREIGN KEY (section) REFERENCES sections(classNumber)
        )
    """.trimIndent()

    DBUtil.executeUpdate(createSQL)
}

// Return true for successful reset, false for invalid SectionUnit length
fun resetUserProfileByUID(id: Int, profileNumber: Int, profile: List<Int>) {
    dropExistingUserProfileByUID(id, profileNumber)
    profile.forEach { insertNewSectionByUIDClassNumber(id, profileNumber, it) }
}

//private fun resetUserProfileWithSerializationByUID(id: Int, profile: List<String>, db: HikariDataSource) {
//    dropExistingUserProfileByUID(id, db)
//    profile.forEach { insertNewSectionUnitWithSerializationByUID(id, it, db) }
//}

private fun insertNewSectionByUIDClassNumber(id: Int, profileNumber: Int, classNumber: Int) {
    val insertSQL = """
        INSERT IGNORE INTO user_profiles 
        (user, profile_number, section)  
        VALUES (?, ?, ?)
    """.trimIndent()

    DBUtil.executeUpdate(insertSQL, id, profileNumber, classNumber)
}

private fun dropExistingUserProfileByUID(id: Int, profileNumber: Int) {
    val deleteSQL = """
        DELETE FROM user_profiles
        WHERE user = ? AND profile_number = ?
    """.trimIndent()

    DBUtil.executeUpdate(deleteSQL, id, profileNumber)
}

fun queryExisingUserProfileByUID(id: Int, profileNumber: Int): MutableList<Section> {
    val querySQL = """
       SELECT *
       FROM user_profiles
       JOIN courses.sections s on s.classNumber = user_profiles.section
       WHERE user = ? AND profile_number = ?
    """.trimIndent()

    return DBUtil.executeQuery(querySQL, id, profileNumber) { result ->
        parseSectionListResultSet(result).toMutableList()
    }
}

fun main() {
    insertNewSectionByUIDClassNumber(1, 1, 2785)
    insertNewSectionByUIDClassNumber(1, 1, 2786)

    insertNewSectionByUIDClassNumber(1, 2, 2789)
    queryExisingUserProfileByUID(1, 1).forEach { temp -> println(temp) }
}