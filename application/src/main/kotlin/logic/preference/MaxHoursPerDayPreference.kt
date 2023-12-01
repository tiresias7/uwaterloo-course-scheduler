package logic.preference

import Section
import config.DebugConfig
import kotlinx.datetime.toJavaLocalTime
import java.time.DayOfWeek
import java.time.Duration
import java.util.*
import kotlin.math.max

class MaxHoursPerDayPreference(
    override var weight: Int,
    private val maxHoursPerDay: Int
) : Preference() {
    override val tag: String = "MaxHoursPerDay"

    override fun getScore(sections: List<Section>): Int {
        val hoursPerDay = EnumMap<DayOfWeek, Long>(DayOfWeek::class.java)

        // Calculate total hours per day
        sections.forEach { section ->
            section.days.forEach { day ->
                hoursPerDay[day] = (hoursPerDay[day] ?: 0) +
                        Duration.between(section.startTime.toJavaLocalTime(), section.endTime.toJavaLocalTime()).toMinutes()
            }
        }

        // Any extra hours should be penalized
        val totalExtraHours = hoursPerDay.values.sumOf { max(0, it - maxHoursPerDay * 60) }

        return max(100 - totalExtraHours.toInt() * 10, 0)
    }

    override fun toString(): String {
        if (DebugConfig.PRINT_PREFERENCE_DEBUG_INFO) {
            return "$tag: $maxHoursPerDay hours. Weight: $weight"
        }
        return "Maximum hours of classes per day: $maxHoursPerDay"
    }
}
