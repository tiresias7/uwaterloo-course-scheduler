package com.example.database.sections

import Section
import com.example.database.common.createDataSource
import java.io.File
import com.zaxxer.hikari.HikariDataSource
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalTime
import java.time.DayOfWeek

fun createSectionsTableIfNotExists(db: HikariDataSource) {
    db.connection.use { conn ->
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

        conn.createStatement().use { stmt ->
            stmt.execute(createTableSQL)
        }
    }
}

fun insertSectionsIntoDatabase(sections: List<Section>, filename: String, db: HikariDataSource) {
    db.connection.use { conn ->
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

        conn.prepareStatement(insertSQL).use { stmt ->
            for (section in sections) {
                stmt.setString(1, faculty) // Add faculty to the database
                stmt.setString(2, courseID) // Add course ID as a string
                stmt.setInt(3, section.classNumber)
                stmt.setString(4, section.component)
                stmt.setInt(5, section.sectionNumber)
                stmt.setString(6, section.campus)
                stmt.setString(7, section.room)
                stmt.setString(8, section.instructor)
                stmt.setTime(9, java.sql.Time.valueOf(section.startTime.toJavaLocalTime()))
                stmt.setTime(10, java.sql.Time.valueOf(section.endTime.toJavaLocalTime()))
                stmt.setString(11, section.days.joinToString(","))
                stmt.execute()
            }
        }
    }
}


fun parseAndInsert(directoryPath: String, db: HikariDataSource) {
    val directory = File(directoryPath)
    if (directory.exists() && directory.isDirectory) {
        directory.listFiles()?.forEach { file ->
            if (file.isFile && file.name.endsWith(".html")) {
                try {
                    val sections = parser(file.absolutePath)
                    insertSectionsIntoDatabase(sections, file.nameWithoutExtension, db)
                } catch (e: Exception) {
                    print("Error with file ${file.nameWithoutExtension}\n")
                }
            }
        }
    }
}

// To avoid multiple connections back and forth, query all faculty + courseId at once, and divide based on that.
fun querySectionsByFacultyId(faculty: String, courseID: String, db: HikariDataSource): List<List<Section>> {
    val querySQL = """
        SELECT *
        FROM sections
        WHERE faculty = ? AND courseID = ?
    """.trimIndent()
    val sections = mutableListOf<Section>()

    db.connection.use { conn ->
        conn.prepareStatement(querySQL).use { stmt ->
            stmt.setString(1, faculty)
            stmt.setString(2, courseID)

            stmt.executeQuery().use { result ->
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
            }
        }
    }
    // return return sections.groupBy { it.component }.values.toList()
    return sections.groupBy { it.component }.values.toList()
        .filter { it.isNotEmpty() && !it.first().component.contains("TST") }
}

fun queryCoursesByFaculty(faculty: String, db: HikariDataSource): List<String> {
    val querySQL = """
        SELECT DISTINCT courseID
        FROM sections
        WHERE faculty = ?
    """.trimIndent()
    val ids = mutableListOf<String>()

    db.connection.use { conn ->
        conn.prepareStatement(querySQL).use { stmt ->
            stmt.setString(1, faculty)

            stmt.executeQuery().use { result ->
                while (result.next()) {
                    ids.add(result.getString("courseID"))
                }
            }
        }
    }
    return ids
}

fun queryDistinctFaculties(db: HikariDataSource): List<String> {
    val querySQL = """
        SELECT DISTINCT faculty
        FROM sections
    """.trimIndent()
    val faculties = mutableListOf<String>()

    db.connection.use { conn ->
        conn.prepareStatement(querySQL).use { stmt ->
            stmt.executeQuery().use { result ->
                while (result.next()) {
                    faculties.add(result.getString("faculty"))
                }
            }
        }
    }
    return faculties
}

fun queryAllClasses(db: HikariDataSource): List<String> {
    return queryDistinctFaculties(db).map { faculty ->
        queryCoursesByFaculty(faculty, db).map { courseId ->
            faculty + courseId
        }
    }.flatten().sorted()
}

fun deleteRowsByHours(hours: Int, db: HikariDataSource) {
    db.connection.use { conn ->
        val deleteSQL = """
            DELETE FROM sections
            WHERE TIMESTAMPDIFF(HOUR, timestamp, NOW()) >= ${hours};
        """.trimIndent()

        conn.createStatement().use { stmt ->
            stmt.executeUpdate(deleteSQL)
        }
    }
}

// example usage, make modification in the future
fun main() {
    val directoryPath = "C:\\Users\\YZM\\Desktop\\chromedriver-win64\\course"

    // Connect to the MySQL database
    val database = createDataSource()

    // Create a table (if it doesn't exist)
    createSectionsTableIfNotExists(database)

    // Parse HTML files in the directory and insert data into the database
    parseAndInsert(directoryPath, database)

    // Test out section queries
    val section = querySectionsByFacultyId("MATH", "135", database)
    section.forEach { list ->
        println("newList")
        list.forEach { sec ->
            println(sec)
        }
    }

    // Test out courseId queries
//    val ids = queryCoursesByFaculty("CS", database)
//    ids.forEach { println(it) }

    // Test out faculty queries.
//    val faculty = queryDistinctFaculties(database)
//    faculty.forEach { println(it) }

    // Close the database connection
    database.close()
}