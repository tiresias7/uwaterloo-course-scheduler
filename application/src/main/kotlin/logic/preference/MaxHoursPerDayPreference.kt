package logic.preference

import logic.Section
import java.time.DayOfWeek
import java.time.Duration
import java.util.*
import kotlin.math.min

class MaxHoursPerDayPreference(
    override var weight: Int,
    private val maxHoursPerDay: Int
) : Preference() {
    override val tag: String = "MaxHoursPerDay"

    override fun getScore(sections: List<Section>): Int {
        val hoursPerDay = EnumMap<DayOfWeek, Long>(DayOfWeek::class.java)

        // Initialize the map with zeros
        DayOfWeek.entries.forEach { day ->
            hoursPerDay[day] = 0L
        }

        // Tally hours per day
        sections.forEach { section ->
            section.days.forEach { day ->
                hoursPerDay[day] = (hoursPerDay[day] ?: 0) +
                        Duration.between(section.startTime, section.endTime).toMinutes()
            }
        }

        val totalViolations = min(hoursPerDay.values.count { it > maxHoursPerDay * 60 }, 5)

        return (100 - totalViolations * 20)
    }

    override fun toString(): String {
        return "Maximum hours of classes per day: $maxHoursPerDay"
    }
}
