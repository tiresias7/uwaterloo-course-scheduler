package logictest

import SelectedCourse
import logic.schedulealgo.NaiveScheduleAlgorithm
import logic.preference.NoCollisionPreference
import Section
import org.junit.jupiter.api.Test
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.toKotlinLocalTime
import logic.ktorClient.querySectionsByFacultyId
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
                Section(startTime = LocalTime.of(8, 0).toKotlinLocalTime(), endTime = LocalTime.of(8, 50).toKotlinLocalTime(), days = setOf(DayOfWeek.MONDAY)),
                Section(startTime = LocalTime.of(9, 0).toKotlinLocalTime(), endTime = LocalTime.of(9, 50).toKotlinLocalTime(), days = setOf(DayOfWeek.MONDAY)),
            ),
            listOf(
                Section(startTime = LocalTime.of(8, 0).toKotlinLocalTime(), endTime = LocalTime.of(8, 50).toKotlinLocalTime(), days = setOf(DayOfWeek.MONDAY)),
                Section(startTime = LocalTime.of(9, 0).toKotlinLocalTime(), endTime = LocalTime.of(9, 50).toKotlinLocalTime(), days = setOf(DayOfWeek.MONDAY)),
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
            runBlocking {
                querySectionsByFacultyId(faculty, courseId)
            }
        }.flatten()
            .map { it.map { sec -> Section(sec.classNumber, sec.component, sec.sectionNumber, sec.campus, sec.room, sec.instructor, sec.startTime, sec.endTime,
                sec.days, sec.courseName) } }

        val naiveScheduleAlgorithm = NaiveScheduleAlgorithm()

        val startTime = System.currentTimeMillis()

        val schedules = naiveScheduleAlgorithm.generateSchedules(sectionLists, preferences, 1)

        val endTime = System.currentTimeMillis()

        println("Time taken: ${endTime - startTime}ms")

        println(schedules)
    }
}