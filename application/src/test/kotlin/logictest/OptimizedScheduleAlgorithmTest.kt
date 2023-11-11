package logictest

import data.SelectedCourse
import logic.preference.NoCollisionPreference
import logic.schedulealgo.OptimizedScheduleAlgorithm
import logic.Section
import org.junit.jupiter.api.Test

import database.sections.querySectionsByFacultyId

class OptimizedScheduleAlgorithmTest {

    @Test
    fun generateSchedules2() {
        val preferences = listOf(
            NoCollisionPreference(10000),
        )
        val sectionLists = listOf(
            SelectedCourse("CS135", true),
            SelectedCourse("CS136", true),
            SelectedCourse("CS245", true),
            SelectedCourse("CS246", true),
            SelectedCourse("CS251", true),
            SelectedCourse("CS350", true)
        ).map { it ->
            val faculty = it.courseName.takeWhile { it.isLetter() }
            val courseId = it.courseName.dropWhile { it.isLetter() }
            database.common.createDataSource().use{ querySectionsByFacultyId(faculty, courseId, it) }
        }.flatten()
            .map { it.map { Section(it.classNumber, it.component, it.sectionNumber, it.campus, it.room, it.instructor, it.startTime, it.endTime,
                it.days, it.courseName) } }

        val optimizedScheduleAlgorithm = OptimizedScheduleAlgorithm()

        val startTime = System.currentTimeMillis()

        val schedules = optimizedScheduleAlgorithm.generateSchedules(sectionLists, preferences, 1)

        val endTime = System.currentTimeMillis()

        println("Time taken: ${endTime - startTime}ms")

        println(schedules)

    }
}