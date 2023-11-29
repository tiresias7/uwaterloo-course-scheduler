package logic.preference

import Section
import java.time.DayOfWeek
import java.time.Duration
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.max

class LunchBreakPreference(
    override var weight: Int,
    private val startTime: LocalTime,
    private val endTime: LocalTime,
    private val lunchBreakLength: Int, // in minutes
) : Preference() {
    override val tag: String = "LunchBreak"

    override fun getScore(sections: List<Section>): Int {
        var score = 100

        DayOfWeek.entries.forEach { day ->
            val intervals = sections.filter { day in it.days }
                .map { it.startTime to it.endTime }
                .sortedBy { it.first }

            if (!hasSufficientBreak(intervals, startTime, endTime, lunchBreakLength)) {
                score -= 20 // Adjust score reduction as needed
            }
        }

        return max(score, 0)
    }

    private fun hasSufficientBreak(intervals: List<Pair<LocalTime, LocalTime>>, start: LocalTime, end: LocalTime, breakLength: Int): Boolean {
        var currentStart = start
        for ((sectionStart, sectionEnd) in intervals) {
            if (sectionStart > currentStart && Duration.between(currentStart.toJavaLocalTime(), sectionStart.toJavaLocalTime()).toMinutes() >= breakLength) {
                currentStart = sectionEnd
            } else if (sectionStart <= currentStart) {
                currentStart = maxOf(currentStart, sectionEnd)
            }
        }
        return Duration.between(currentStart.toJavaLocalTime(), end.toJavaLocalTime()).toMinutes() >= breakLength
    }

    override fun toString(): String {
        var outStr = "Lunch break "
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        outStr += "${startTime.toJavaLocalTime().format(formatter)} to ${endTime.toJavaLocalTime().format(formatter)} for $lunchBreakLength mins"
        //outStr += ". Weight: $weight"

        return outStr
    }
}