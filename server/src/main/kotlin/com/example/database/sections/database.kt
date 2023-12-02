package com.example.database.sections

import Section
import com.example.database.common.DBUtil
import java.io.File
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalTime
import java.sql.ResultSet
import java.time.DayOfWeek

fun createSectionsTableIfNotExists() {
    val createTableSQL = """
        CREATE TABLE IF NOT EXISTS sections (
            faculty VARCHAR(10),
            courseID VARCHAR(10),
            classNumber INT PRIMARY KEY UNIQUE,
            component VARCHAR(10),
            sectionNumber INT,
            campus VARCHAR(10),
            room VARCHAR(64),
            instructor VARCHAR(255),
            startTime TIME,
            endTime TIME,
            days VARCHAR(255),
            timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        )
    """.trimIndent()

    DBUtil.executeUpdate(createTableSQL)
}

fun insertSectionsIntoDatabase(sections: List<Section>, filename: String) {
    val faculty = filename.takeWhile { it.isLetter() }
    val courseID = filename.dropWhile { it.isLetter() }
    val insertSQL = """
        INSERT INTO sections
        (faculty, courseID, classNumber, component, sectionNumber, campus, room, instructor, startTime, endTime, days)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
            faculty = VALUES(faculty),
            courseID = VALUES(courseID),
            component = VALUES(component),
            sectionNumber = VALUES(sectionNumber),
            campus = VALUES(campus),
            room = VALUES(room),
            instructor = VALUES(instructor),
            startTime = VALUES(startTime),
            endTime = VALUES(endTime),
            days = VALUES(days),
            timestamp = CURRENT_TIMESTAMP
    """.trimIndent()

    sections.forEach { section ->
        DBUtil.executeUpdate(insertSQL, faculty, courseID, section.classNumber, section.component, section.sectionNumber,
            section.campus, section.room, section.instructor,
            java.sql.Time.valueOf(section.startTime.toJavaLocalTime()),
            java.sql.Time.valueOf(section.endTime.toJavaLocalTime()),
            section.days.joinToString(","))
    }
}


fun parseAndInsert(directoryPath: String) {
    val directory = File(directoryPath)
    if (directory.exists() && directory.isDirectory) {
        directory.listFiles()?.forEach { file ->
            if (file.isFile && file.name.endsWith(".html")) {
                try {
                    val sections = parser(file.absolutePath)
                    insertSectionsIntoDatabase(sections, file.nameWithoutExtension)
                } catch (e: Exception) {
                    print("Error with file ${file.nameWithoutExtension}\n")
                }
            }
        }
    }
}

fun parseSectionListResultSet(result: ResultSet): List<Section> {
    val sections = mutableListOf<Section>()

    while (result.next()) {
        // Here should be a warning where TBA / ONLINE classes will have no days, specially for TBA classes
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
    return sections
}

// To avoid multiple connections back and forth, query all faculty + courseId at once, and divide based on that.
fun querySectionsByFacultyId(faculty: String, courseID: String): List<List<Section>> {
    val querySQL = """
        SELECT *
        FROM sections
        WHERE faculty = ? AND courseID = ?
    """.trimIndent()

    val sections = DBUtil.executeQuery(querySQL, faculty, courseID) { result -> parseSectionListResultSet(result) }

    return sections.groupBy { it.component }.values.toList()
    .filter { it.isNotEmpty() && !it.first().component.contains("TST") }
}

fun queryCoursesByFaculty(faculty: String): List<String> {
    val querySQL = """
        SELECT DISTINCT courseID
        FROM sections
        WHERE faculty = ?
    """.trimIndent()
    val ids = mutableListOf<String>()

    DBUtil.executeQuery(querySQL, faculty) { result ->
        while (result.next()) {
            ids.add(result.getString("courseID"))
        }
    }
    return ids
}

fun queryDistinctFaculties(): List<String> {
    val querySQL = """
        SELECT DISTINCT faculty
        FROM sections
    """.trimIndent()
    val faculties = mutableListOf<String>()

    DBUtil.executeQuery(querySQL) { result ->
        while (result.next()) {
            faculties.add(result.getString("faculty"))
        }
    }
    return faculties
}

fun queryAllClasses(): List<String> {
    return queryDistinctFaculties().map { faculty ->
        queryCoursesByFaculty(faculty).map { courseId ->
            faculty + courseId
        }
    }.flatten().sorted()
}

// example usage, make modification in the future
fun main() {
    val directoryPath = "C:\\Users\\YZM\\Desktop\\chromedriver-win64\\course"

    // Create a table (if it doesn't exist)
    createSectionsTableIfNotExists()

    // Parse HTML files in the directory and insert data into the database
//    parseAndInsert(directoryPath)

    // Test out section queries
//    val section = querySectionsByFacultyId("MATH", "135")
//    section.forEach { list ->
//        println("newList")
//        list.forEach { sec ->
//            println(sec)
//        }
//    }

    // Test out courseId queries
//    val ids = queryCoursesByFaculty("CS")
//    ids.forEach { println(it) }

    // Test out faculty queries.
    val faculty = queryDistinctFaculties()
    faculty.forEach { println(it) }
}