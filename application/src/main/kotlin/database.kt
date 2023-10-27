import java.io.File
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

fun createDataSource(url: String, dbUser:String, dbPass: String): HikariDataSource {
    val config = HikariConfig()
    config.jdbcUrl = url
    config.username = dbUser
    config.password = dbPass
    return HikariDataSource(config)
}

fun createTableIfNotExists(db: HikariDataSource) {
    db.connection.use { conn ->
        val createTableSQL = """
            CREATE TABLE IF NOT EXISTS sections (
                faculty VARCHAR(10),
                courseID VARCHAR(10),
                classNumber INT,
                component VARCHAR(10),
                sectionNumber INT,
                campus VARCHAR(10),
                room VARCHAR(255),
                instructor VARCHAR(255),
                startTime TIME,
                endTime TIME,
                days VARCHAR(255),
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                PRIMARY KEY (faculty, courseID, classNumber, component, sectionNumber)
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
                stmt.setTime(9, java.sql.Time.valueOf(section.startTime))
                stmt.setTime(10, java.sql.Time.valueOf(section.endTime))
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
                    sections.add(
                        Section(
                            result.getInt("classNumber"),
                            result.getString("component"),
                            result.getInt("sectionNumber"),
                            result.getString("campus"),
                            result.getString("room"),
                            result.getString("instructor"),
                            result.getTime("startTime").toLocalTime(),
                            result.getTime("endTime").toLocalTime(),
                            result.getString("days").split(",").map { Day.valueOf(it) }.toSet(),
                            result.getString("faculty") + result.getString("courseID")
                        )
                    )
                }
            }
        }
    }
   return sections.groupBy { it.component }.values.toList()
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
    val directoryPath = "C:\\Users\\YZM\\Desktop\\courses"
    val databaseUrl = "jdbc:mysql://34.130.134.71:3306/courses"
    val databaseUser = "root"
    val databasePassword = "PPLegend"

    // Connect to the MySQL database
    val database = createDataSource(databaseUrl, databaseUser, databasePassword)

    // Create a table (if it doesn't exist)
    createTableIfNotExists(database)

    // Parse HTML files in the directory and insert data into the database
//    parseAndInsert(directoryPath, database)

    // Test out old data cleanup
//    deleteRowsByHours(0, database)


    // Test out section queries
    val section = querySectionsByFacultyId("CS", "137", database)
    section.forEach { list ->
        println("newlist")
        list.forEach{
            sec -> println(sec)
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