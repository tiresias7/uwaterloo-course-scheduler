package logic

import data.SectionUnit
import java.time.LocalTime
import java.time.DayOfWeek

private const val MINUTESPERHOUR = 60

data class Section(
    val classNumber: Int = 0,
    val component: String = "",
    val sectionNumber: Int = 0,
    val campus: String = "",
    val room: String = "",
    val instructor: String = "",
    val startTime: LocalTime = LocalTime.MIN,
    val endTime: LocalTime = LocalTime.MAX,
    val days: Set<DayOfWeek> = emptySet(),
    val courseName: String = ""
) {
    fun toSectionUnit(): List<SectionUnit> {
        return this.days.map {
            SectionUnit(it.ordinal,
                startTime.hour + (startTime.minute.toFloat() / MINUTESPERHOUR),
                endTime.hour + (endTime.minute.toFloat() / MINUTESPERHOUR),
                courseName, room, instructor)
        }
    }
}

fun sectionListToUnits(sections: List<Section>): List<SectionUnit> {
    return sections.map { it.toSectionUnit() }.flatten()
}