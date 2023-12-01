package logic.preference

import Section
import config.DebugConfig
import kotlinx.datetime.toJavaLocalTime
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.max

class NonPreferredTimePreference(
    override var weight: Int,
    private val startTime: LocalTime,
    private val endTime: LocalTime,
    private val days: Set<DayOfWeek> = EnumSet.allOf(DayOfWeek::class.java)
) : Preference() {
    override val tag: String = "NonPreferredTime"

    override fun getScore(sections: List<Section>): Int {
        // We want the course to have entirely no overlap with the non-preferred time
        var score = 100

        for (section in sections) {
            if (section.days.intersect(days).isNotEmpty() &&
                section.startTime.toJavaLocalTime().isBefore(endTime) && section.endTime.toJavaLocalTime().isAfter(startTime)) {
                score -= 10
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

        return "Avoid $timeStr"
    }
}