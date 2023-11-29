package logictest

import SelectedCourse
import logic.preference.NoCollisionPreference
import logic.schedulealgo.OptimizedScheduleAlgorithm
import Section
import org.junit.jupiter.api.Test
import kotlinx.coroutines.runBlocking
import logic.ktorClient.querySectionsByFacultyId

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
            runBlocking{ querySectionsByFacultyId(faculty, courseId) }
        }.flatten()
            .map { it.map { sec -> Section(sec.classNumber, sec.component, sec.sectionNumber, sec.campus, sec.room, sec.instructor, sec.startTime, sec.endTime,
                sec.days, sec.courseName) } }

        val optimizedScheduleAlgorithm = OptimizedScheduleAlgorithm()

        val startTime = System.currentTimeMillis()

        val schedules = optimizedScheduleAlgorithm.generateSchedules(sectionLists, preferences, 1)

        val endTime = System.currentTimeMillis()

        println("Time taken: ${endTime - startTime}ms")

        println(schedules)

    }
}