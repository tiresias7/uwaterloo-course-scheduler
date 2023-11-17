package logic.preference

import logic.Section
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

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
            if (section.days.intersect(days).isEmpty() ||
                section.startTime.isBefore(startTime) || section.endTime.isAfter(endTime)) {
                score -= 10
            }
        }
        return score
    }

    override fun toString(): String {
        var outStr = "Prefer time slots on "
        for (day in days){
            outStr += day.name.substring(0, 3)
            outStr += " "
        }
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        outStr += "from ${startTime.format(formatter)} to ${endTime.format(formatter)}"
        outStr += ". Weight: ${weight}"

        return outStr
    }
}