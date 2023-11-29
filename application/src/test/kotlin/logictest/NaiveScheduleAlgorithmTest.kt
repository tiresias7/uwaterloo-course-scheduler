package logictest

import database.common.createDataSource
import SelectedCourse
import logic.schedulealgo.NaiveScheduleAlgorithm
import logic.preference.NoCollisionPreference
import Section
import org.junit.jupiter.api.Test
import database.sections.querySectionsByFacultyId
import java.time.DayOfWeek
import java.time.LocalTime

class NaiveScheduleAlgorithmTest {
    @Test
    fun generateSchedules() {
        val preferences = listOf(
            NoCollisionPreference(10000),
        )
        val sectionLists = listOf(
            listOf(
                Section(startTime = LocalTime.of(8, 0), endTime = LocalTime.of(8, 50), days = setOf(DayOfWeek.MONDAY)),
                Section(startTime = LocalTime.of(9, 0), endTime = LocalTime.of(9, 50), days = setOf(DayOfWeek.MONDAY)),
            ),
            listOf(
                Section(startTime = LocalTime.of(8, 0), endTime = LocalTime.of(8, 50), days = setOf(DayOfWeek.MONDAY)),
                Section(startTime = LocalTime.of(9, 0), endTime = LocalTime.of(9, 50), days = setOf(DayOfWeek.MONDAY)),
            ),
        )

        val naiveScheduleAlgorithm = NaiveScheduleAlgorithm()

        val schedules = naiveScheduleAlgorithm.generateSchedules(sectionLists, preferences, 1)

        println(schedules)
    }

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
            createDataSource().use{ querySectionsByFacultyId(faculty, courseId, it) }
        }.flatten()
            .map { it.map { Section(it.classNumber, it.component, it.sectionNumber, it.campus, it.room, it.instructor, it.startTime, it.endTime,
                it.days, it.courseName) } }

        val naiveScheduleAlgorithm = NaiveScheduleAlgorithm()

        val startTime = System.currentTimeMillis()

        val schedules = naiveScheduleAlgorithm.generateSchedules(sectionLists, preferences, 1)

        val endTime = System.currentTimeMillis()

        println("Time taken: ${endTime - startTime}ms")

        println(schedules)
    }
}