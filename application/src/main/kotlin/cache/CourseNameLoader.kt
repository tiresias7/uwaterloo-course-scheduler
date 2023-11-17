package cache

import database.common.createDataSource
import database.sections.queryAllClasses
import java.util.concurrent.CompletableFuture

object CourseNameLoader {
    private val allCourseNames: CompletableFuture<List<String>> = CompletableFuture.supplyAsync {
        queryAllClasses(createDataSource())
    }

    fun cacheAllCourseNames() {
        allCourseNames.complete(queryAllClasses(createDataSource()))
    }

    fun getAllCourseNames(): List<String> {
        return allCourseNames.get() // blocks until futureCourses is done
    }
}
