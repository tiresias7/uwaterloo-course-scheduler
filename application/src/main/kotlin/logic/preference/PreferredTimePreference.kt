package logic.preference

import logic.Section
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*

class PreferredTimePreference(
    weight: Int,
    private val startTime: LocalTime,
    private val endTime: LocalTime,
    private val days: Set<DayOfWeek> = EnumSet.allOf(DayOfWeek::class.java)
) : Preference() {
    override val weight: Int = if (weight >= HARD_PREFERENCE_WEIGHT) {
        throw IllegalArgumentException("PreferredTimePreference cannot be a hard preference.")
    } else {
        weight
    }
    override val tag: String = "PreferredTime"

    override fun getScore(sections: List<Section>): Int {
        // We want to make it non-increasing
        // We want courses to be entirely within the preferred time
        var score = 100
        for (section in sections) {
            if (section.days.intersect(days).isEmpty() ||
                section.startTime.isBefore(startTime) || section.endTime.isAfter(endTime)) {
                score -= 10
            }
        }
        return score
    }

}