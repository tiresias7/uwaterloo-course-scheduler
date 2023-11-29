package logictest

import logic.preference.NoCollisionPreference
import Section
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.DayOfWeek
import java.time.LocalTime

class NoCollisionPreferenceTest {

    @Test
    fun eval() {
        val pref = NoCollisionPreference(10000)
        val sections = listOf(
            Section(
                startTime = LocalTime.of(9, 0),
                endTime = LocalTime.of(10, 50),
                days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            ),
            Section(
                startTime = LocalTime.of(9, 0),
                endTime = LocalTime.of(10, 50),
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
                startTime = LocalTime.of(9, 0),
                endTime = LocalTime.of(10, 50),
                days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            ),
            Section(
                startTime = LocalTime.of(9, 0),
                endTime = LocalTime.of(10, 50),
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
                startTime = LocalTime.of(9, 0),
                endTime = LocalTime.of(10, 50),
                days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            ),
            Section(
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(11, 50),
                days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)
            ),
            Section(
                startTime = LocalTime.of(11, 0),
                endTime = LocalTime.of(12, 50),
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
                startTime = LocalTime.of(11, 30),
                endTime = LocalTime.of(12, 50),
                days = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            ),
            Section(
                startTime = LocalTime.of(13, 30),
                endTime = LocalTime.of(14, 20),
                days = setOf(DayOfWeek.FRIDAY)
            ),
            Section(
                startTime = LocalTime.of(11, 30),
                endTime = LocalTime.of(12, 50),
                days = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            ),
            Section(
                startTime = LocalTime.of(14, 30),
                endTime = LocalTime.of(15, 50),
                days = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            ),
            Section(
                startTime = LocalTime.of(11, 30),
                endTime = LocalTime.of(12, 20),
                days = setOf(DayOfWeek.FRIDAY)
            ),
            Section(
                startTime = LocalTime.of(8, 30),
                endTime = LocalTime.of(9, 20),
                days = setOf(DayOfWeek.WEDNESDAY)
            ),
            Section(
                startTime = LocalTime.of(11, 30),
                endTime = LocalTime.of(12, 50),
                days = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            ),
            Section(
                startTime = LocalTime.of(13, 0),
                endTime = LocalTime.of(14, 20),
                days = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            ),
            Section(
                startTime = LocalTime.of(15, 30),
                endTime = LocalTime.of(16, 20),
                days = setOf(DayOfWeek.WEDNESDAY)
            ),
            Section(
                startTime = LocalTime.of(11, 30),
                endTime = LocalTime.of(12, 50),
                days = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            )
        )
        assertEquals(0, pref.eval(sections))
    }
}