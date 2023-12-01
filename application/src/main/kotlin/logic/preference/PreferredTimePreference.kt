package logic.preference

import Section
import config.DebugConfig
import kotlinx.datetime.toJavaLocalTime
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.max

class PreferredTimePreference(
    override var weight: Int,
    private val startTime: LocalTime,
    private val endTime: LocalTime,
    private val days: Set<DayOfWeek> = EnumSet.allOf(DayOfWeek::class.java)
) : Preference() {
//    override var weight: Int = if (weight >= HARD_PREFERENCE_WEIGHT) {
//        throw IllegalArgumentException("PreferredTimePreference cannot be a hard preference.")
//    } else {
//        weight
//    }
    override val tag: String = "PreferredTime"

    override fun getScore(sections: List<Section>): Int {
        // We want to make it non-increasing
        // We want courses to be entirely within the preferred time
        var score = 100
        for (section in sections) {
            if (section.days.intersect(days).isNotEmpty() &&
                (section.startTime.toJavaLocalTime().isBefore(startTime) || section.endTime.toJavaLocalTime().isAfter(endTime))) {
                score -= 5
            }
        }
        return max(score, 0)
    }

    override fun toString(): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val timeStr = "${startTime.format(formatter)} to ${endTime.format(formatter)} on ${convertfromDayOfWeek(days)}"

        if (DebugConfig.PRINT_PREFERENCE_DEBUG_INFO) {
            return "$tag: $timeStr. Weight: $weight"
        }

        return "Prefer $timeStr"
    }
}