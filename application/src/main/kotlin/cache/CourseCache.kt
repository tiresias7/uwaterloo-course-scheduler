package cache

import database.common.createDataSource
import database.sections.querySectionsByFacultyId
import kotlinx.coroutines.*
import logic.Section

typealias CourseData = List<List<Section>>

object CourseCache {
    private val courseCache = mutableMapOf<String, CourseData>()
    private val scope = CoroutineScope(Dispatchers.IO)

    fun cacheCourse(courseId: String) {
        scope.launch {
            val courseData = fetchCourseFromDatabase(courseId)
            courseCache[courseId] = courseData
        }
    }

    fun getCourse(courseId: String): CourseData = runBlocking {
        courseCache[courseId] ?: fetchCourseFromDatabase(courseId).also {
            courseCache[courseId] = it
        }
    }

    private suspend fun fetchCourseFromDatabase(courseName: String): CourseData {
        val faculty = courseName.takeWhile { it.isLetter() }
        val courseId = courseName.dropWhile { it.isLetter() }

        return withContext(Dispatchers.IO) {
            querySectionsByFacultyId(faculty, courseId, createDataSource())
        }
    }
}