package logic.preference

import logic.Section
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
                section.startTime.isBefore(endTime) && section.endTime.isAfter(startTime)) {
                score -= 10
            }
        }
        return max(score, 0)
    }

    override fun toString(): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        var outStr = "Avoid "
        outStr += "${startTime.format(formatter)} to ${endTime.format(formatter)} on "
        outStr += convertfromDayOfWeek(days)
        //outStr += ". Weight: ${weight}"
        return outStr
    }
}