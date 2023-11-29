//package database.deprecated
//
//import database.sections.parser
//import org.jetbrains.exposed.sql.*
//import org.jetbrains.exposed.sql.javatime.*
//import org.jetbrains.exposed.sql.transactions.transaction
//import java.io.File
//import Section
//import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
//import java.time.LocalDateTime
//import java.time.DayOfWeek
//
//object SectionsTable : Table() {
//    val faculty = varchar("faculty", 10)
//    val courseID = varchar("courseID", 10)
//    val classNumber = integer("classNumber")
//    val component = varchar("component", 10)
//    val sectionNumber = integer("sectionNumber")
//    val campus = varchar("campus", 10)
//    val room = varchar("room", 20)
//    val instructor = varchar("instructor", 255)
//    val startTime = time("startTime")
//    val endTime = time("endTime")
//    val days = varchar("days", 255)
//    val timestamp = datetime("timestamp").clientDefault{ LocalDateTime.now() }
//
//    override val primaryKey = PrimaryKey(faculty, courseID, classNumber, component, sectionNumber, name = "PK_Sections")
//}
//
//fun connectToDB(url: String = "jdbc:mysql://34.130.134.71:3306/courses", dbUser:String = "root", dbPass: String = "PPLegend") {
//    Database.connect(url, driver = "com.mysql.cj.jdbc.Driver", user = dbUser, password = dbPass)
//}
//
//fun createTableIfNotExists() {
//    transaction {
//        SchemaUtils.createMissingTablesAndColumns(SectionsTable)
//    }
//}
//
//fun insertSectionsIntoDatabase(sections: List<Section>, filename: String) {
//    transaction {
//        val facultyName = filename.takeWhile { c -> c.isLetter() }
//        val courseIDName = filename.dropWhile { c -> c.isLetter() }
//        for (section in sections) {
//            val foundRow = SectionsTable.select {
//                (SectionsTable.faculty eq facultyName) and
//                        (SectionsTable.courseID eq courseIDName) and
//                        (SectionsTable.classNumber eq section.classNumber) and
//                        (SectionsTable.component eq section.component) and
//                        (SectionsTable.sectionNumber eq section.sectionNumber)
//            }.firstOrNull()
//
//            if (foundRow != null) {
//                SectionsTable.update({ SectionsTable.classNumber eq section.classNumber }) {
//                    it[campus] = section.campus
//                    it[room] = section.room
//                    it[instructor] = section.instructor
//                    it[startTime] = section.startTime
//                    it[endTime] = section.endTime
//                    it[days] = section.days.joinToString(",")
//                    it[timestamp] = LocalDateTime.now()
//                }
//            } else {
//                SectionsTable.insert {
//                    it[faculty] = facultyName
//                    it[courseID] = courseIDName
//                    it[classNumber] = section.classNumber
//                    it[component] = section.component
//                    it[sectionNumber] = section.sectionNumber
//                    it[campus] = section.campus
//                    it[room] = section.room
//                    it[instructor] = section.instructor
//                    it[startTime] = section.startTime
//                    it[endTime] = section.endTime
//                    it[days] = section.days.joinToString(",")
//                    it[timestamp] = LocalDateTime.now()
//                }
//            }
//        }
//    }
//}
//
//fun parseAndInsert(directoryPath: String) {
//    val directory = File(directoryPath)
//    if (directory.exists() && directory.isDirectory) {
//        directory.listFiles()?.forEach { file ->
//            if (file.isFile && file.name.endsWith(".html")) {
//                try {
//                    val sections = parser(file.absolutePath)
//                    insertSectionsIntoDatabase(sections, file.nameWithoutExtension)
//                } catch (e: Exception) {
//                    print("Error with file ${file.nameWithoutExtension}\n")
//                }
//            }
//        }
//    }
//}
//
//
//// To avoid multiple connections back and forth, query all faculty + courseId at once, and divide based on that.
//fun querySectionsByFacultyId(faculty: String, courseID: String): List<List<Section>> {
//    val sections = mutableListOf<Section>()
//
//    transaction {
//        SectionsTable.select {
//            SectionsTable.faculty eq faculty and (SectionsTable.courseID eq courseID)
//        }.forEach {
//            val days: Set<DayOfWeek> = if (it[SectionsTable.days] == "") emptySet()
//                else it[SectionsTable.days].split(",").map { DayOfWeek.valueOf(it) }.toSet()
//
//            sections.add(
//                Section(
//                    it[SectionsTable.classNumber],
//                    it[SectionsTable.component],
//                    it[SectionsTable.sectionNumber],
//                    it[SectionsTable.campus],
//                    it[SectionsTable.room],
//                    it[SectionsTable.instructor],
//                    it[SectionsTable.startTime],
//                    it[SectionsTable.endTime],
//                    days,
//                    it[SectionsTable.faculty] + it[SectionsTable.courseID]
//                )
//            )
//        }
//    }
//    return sections.groupBy { it.component }.values.toList().filter { it.isNotEmpty() && !it.first().component.contains("TST") }
//}
//
//
//fun queryCoursesByFaculty(faculty: String): List<String> {
//    val ids = mutableListOf<String>()
//
//    transaction {
//        SectionsTable.slice(SectionsTable.courseID).select {
//            SectionsTable.faculty eq faculty
//        }.forEach {
//            ids.add(it[SectionsTable.courseID])
//        }
//    }
//    return ids
//}
//
//fun queryDistinctFaculties(): List<String> {
//    val faculties = mutableListOf<String>()
//
//    transaction {
//        SectionsTable.slice(SectionsTable.faculty).selectAll().forEach {
//            faculties.add(it[SectionsTable.faculty])
//        }
//    }
//    return faculties
//}
//
//fun queryAllClasses(): List<String> {
//    return queryDistinctFaculties().map { faculty ->
//        queryCoursesByFaculty(faculty).map { courseId ->
//            faculty + courseId
//        }
//    }.flatten().sorted()
//}
//
//
//fun deleteRowsByHours(hours: Int) {
//    transaction {
//        SectionsTable.deleteWhere {
//            timestamp lessEq LocalDateTime.now().minusHours(hours.toLong())
//        }
//    }
//}
//
//
//fun main() {
//    val directoryPath = "C:\\Users\\YZM\\Desktop\\courses"
//
//    // Connect to the MySQL database
//    connectToDB()
//
//    // Create a table (if it doesn't exist)
//    createTableIfNotExists()
//
//    // Parse HTML files in the directory and insert data into the database
//    parseAndInsert(directoryPath)
//
//    // Test out old data cleanup
////    deleteRowsByHours(0)
//
//
//    // Test out section queries
//    val section = querySectionsByFacultyId("CS", "350")
//    section.forEach { list ->
//        println("newline")
//        list.forEach{
//                sec -> println(sec)
//        }
//    }
//
//    // Test out courseId queries
////    val ids = queryCoursesByFaculty("CS", database)
////    ids.forEach { println(it) }
//
//    // Test out faculty queries.
////    val faculty = queryDistinctFaculties(database)
////    faculty.forEach { println(it) }
//
//    // Close the database connection
//}