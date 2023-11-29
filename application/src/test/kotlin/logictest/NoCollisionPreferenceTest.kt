package logictest

import logic.preference.NoCollisionPreference
import Section
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.DayOfWeek
import kotlinx.datetime.LocalTime

class NoCollisionPreferenceTest {

    @Test
    fun eval() {
        val pref = NoCollisionPreference(10000)
        val sections = listOf(
            Section(
                startTime = LocalTime(9, 0),
                endTime = LocalTime(10, 50),
                days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            ),
            Section(
                startTime = LocalTime(9, 0),
                endTime = LocalTime(10, 50),
                days = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            ),
        )
        assertEquals(1000000, pref.eval(sections))
    }

    @Test
    fun eval2() {
        val pref = NoCollisionPreference(1)
        val sections = listOf(
            Section(
                startTime = LocalTime(9, 0),
                endTime = LocalTime(10, 50),
                days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            ),
            Section(
                startTime = LocalTime(9, 0),
                endTime = LocalTime(10, 50),
                days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            ),
        )
        assertEquals(0, pref.eval(sections))
    }

    @Test
    fun eval3() {
        val pref = NoCollisionPreference(1)
        val sections = listOf(
            Section(
                startTime = LocalTime(9, 0),
                endTime = LocalTime(10, 50),
                days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            ),
            Section(
                startTime = LocalTime(10, 0),
                endTime = LocalTime(11, 50),
                days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            ),
            Section(
                startTime = LocalTime(11, 0),
                endTime = LocalTime(12, 50),
                days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            ),
        )
        assertEquals(0, pref.eval(sections))
    }

    @Test
    fun eval4() {
        val pref = NoCollisionPreference(1)
        val sections = listOf(
            Section(
                startTime = LocalTime(11, 30),
                endTime = LocalTime(12, 50),
                days = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            ),
            Section(
                startTime = LocalTime(13, 30),
                endTime = LocalTime(14, 20),
                days = setOf(DayOfWeek.FRIDAY)
            ),
            Section(
                startTime = LocalTime(11, 30),
                endTime = LocalTime(12, 50),
                days = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            ),
            Section(
                startTime = LocalTime(14, 30),
                endTime = LocalTime(15, 50),
                days = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            ),
            Section(
                startTime = LocalTime(11, 30),
                endTime = LocalTime(12, 20),
                days = setOf(DayOfWeek.FRIDAY)
            ),
            Section(
                startTime = LocalTime(8, 30),
                endTime = LocalTime(9, 20),
                days = setOf(DayOfWeek.WEDNESDAY)
            ),
            Section(
                startTime = LocalTime(11, 30),
                endTime = LocalTime(12, 50),
                days = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            ),
            Section(
                startTime = LocalTime(13, 0),
                endTime = LocalTime(14, 20),
                days = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            ),
            Section(
                startTime = LocalTime(15, 30),
                endTime = LocalTime(16, 20),
                days = setOf(DayOfWeek.WEDNESDAY)
            ),
            Section(
                startTime = LocalTime(11, 30),
                endTime = LocalTime(12, 50),
                days = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            )
        )
        assertEquals(0, pref.eval(sections))
    }
}