package logic.preference

import logic.Section
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*

class NonPreferredTimePreference(
    override val weight: Int,
    private val startTime: LocalTime,
    private val endTime: LocalTime,
    private val days: Set<DayOfWeek> = EnumSet.allOf(DayOfWeek::class.java)
) : Preference() {
    override val tag: String = "NonPreferredTime"

    override fun getScore(sections: List<Section>): Int {
        // We want the course to have entirely no overlap with the non-preferred time
        for (section in sections) {
            if (section.days.intersect(days).isNotEmpty() &&
                section.startTime.isBefore(endTime) && section.endTime.isAfter(startTime)) {
                return 0
            }
        }
        return 100
    }

}