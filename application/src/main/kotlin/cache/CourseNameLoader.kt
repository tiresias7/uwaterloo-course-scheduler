package cache

import kotlinx.coroutines.runBlocking
import logic.ktorClient.queryAllClasses
import java.util.concurrent.CompletableFuture

object CourseNameLoader {
    private var allCourseNames: MutableList<String> = mutableListOf()

    suspend fun cacheAllCourseNames() {
        allCourseNames.clear()
        allCourseNames.addAll(queryAllClasses())
    }

    fun getAllCourseNames(): List<String> {
        return allCourseNames
    }
}
