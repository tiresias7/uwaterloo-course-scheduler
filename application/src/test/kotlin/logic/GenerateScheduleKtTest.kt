package logic

import logic.preference.NoCollisionPreference
import logic.preference.NonPreferredTimePreference
import logic.preference.Preference
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.LocalTime

class GenerateScheduleKtTest {

    @Test
    fun getScheduleSingleCourse() {
        val hardCourses : List<String> = listOf("CS135")
        val softCourses : List<String> = listOf()
        val numOfCourses = 1
        val hardPreferences : List<Preference> = listOf(NoCollisionPreference(10000))
        val softPreferences : List<Preference> = listOf()
        val numOfSchedules = 1

        val result = getSchedule(hardCourses, softCourses, numOfCourses, hardPreferences, softPreferences, numOfSchedules)
        assertEquals(1, result.size)
        assertEquals(2, result[0].size)
        assertEquals(10000 * 100, hardPreferences[0].eval(result[0]))
    }

    @Test
    fun getScheduleMultipleCourses() {
        val hardCourses : List<String> = listOf("CS135", "CS136", "CS245", "CS246", "CS251")
        val softCourses : List<String> = listOf()
        val numOfCourses = 5
        val hardPreferences : List<Preference> = listOf(NoCollisionPreference(10000))
        val softPreferences : List<Preference> = listOf()
        val numOfSchedules = 1

        val result = getSchedule(hardCourses, softCourses, numOfCourses, hardPreferences, softPreferences, numOfSchedules)
        assertEquals(1, result.size)
        assertEquals(9, result[0].size)
        assertEquals(10000 * 100, hardPreferences[0].eval(result[0]))
    }

    @Test
    fun getScheduleAllSoftCourses() {
        val hardCourses : List<String> = listOf()
        val softCourses : List<String> = listOf("CS135", "CS136", "CS245", "CS246", "CS251", "CS350")
        val numOfCourses = 5
        val hardPreferences : List<Preference> = listOf(NoCollisionPreference(10000))
        val softPreferences : List<Preference> = listOf()
        val numOfSchedules = 1

        val result = getSchedule(hardCourses, softCourses, numOfCourses, hardPreferences, softPreferences, numOfSchedules)
        print(result)
        assertEquals(1, result.size)
        assertEquals(8, result[0].size)
        assertEquals(10000 * 100, hardPreferences[0].eval(result[0]))
    }

    @Test
    fun getScheduleHardSoftMix() {
        val hardCourses : List<String> = listOf("CS135", "CS136", "CS245")
        val softCourses : List<String> = listOf("CS246", "CS251", "CS350")
        val numOfCourses = 5
        val hardPreferences : List<Preference> = listOf(NoCollisionPreference(10000))
        val softPreferences : List<Preference> = listOf()
        val numOfSchedules = 1

        val result = getSchedule(hardCourses, softCourses, numOfCourses, hardPreferences, softPreferences, numOfSchedules)
        print(result)
        assertEquals(1, result.size)
        assertEquals(8, result[0].size)
        assertEquals(10000 * 100, hardPreferences[0].eval(result[0]))
    }

    @Test
    fun getScheduleSoftPreference() {
        val hardCourses : List<String> = listOf("CS135", "CS136", "CS245", "CS246", "CS251")
        val softCourses : List<String> = listOf()
        val numOfCourses = 5
        val hardPreferences : List<Preference> = listOf()
        val softPreferences : List<Preference> = listOf(NoCollisionPreference(1))
        val numOfSchedules = 1

        val result = getSchedule(hardCourses, softCourses, numOfCourses, hardPreferences, softPreferences, numOfSchedules)
        assertEquals(1, result.size)
        assertEquals(9, result[0].size)
        assertEquals(1 * 100, softPreferences[0].eval(result[0]))
    }

    @Test
    fun getScheduleSoftPreferenceNoSatisfy() {
        val hardCourses : List<String> = listOf()
        val softCourses : List<String> = listOf("CS135", "CS136", "CS245", "CS246", "CS251", "CS350")
        val numOfCourses = 5
        val hardPreferences : List<Preference> = listOf(NoCollisionPreference(10000))
        val softPreferences : List<Preference> = listOf(
            NonPreferredTimePreference(1,
            LocalTime.of(8, 0), LocalTime.of(9, 0))
        )
        val numOfSchedules = 1

        val result = getSchedule(hardCourses, softCourses, numOfCourses, hardPreferences, softPreferences, numOfSchedules)
        assertEquals(1, result.size)
        assertEquals(8, result[0].size)
        assertEquals(10000 * 100, hardPreferences[0].eval(result[0]))
        assertEquals(1 * 0, softPreferences[0].eval(result[0]))
    }
}