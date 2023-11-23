package logic.preference

import logic.Section
import java.time.DayOfWeek
import java.util.*

class DayOffPreference(
    override var weight: Int,
) : Preference() {
    override val tag: String = "DayOff"

    override fun getScore(sections: List<Section>): Int {
        val dayOff = EnumMap<DayOfWeek, Boolean>(DayOfWeek::class.java).apply {
            DayOfWeek.entries.forEach { this[it] = true }
        }

        sections.forEach { section ->
            section.days.forEach { day ->
                dayOff[day] = false
            }
        }

        // Consider only weekdays
        val workDays = setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)
        val hasDayOff = workDays.any { dayOff[it] == true }

        return if (hasDayOff) 100 else 0
    }

    override fun toString(): String {
        return "Prefer at least one weekday off. Weight: $weight"
    }
}