import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import java.time.DayOfWeek

private const val MINUTESPERHOUR = 60

@Serializable
data class Section(
    val classNumber: Int = 0,
    val component: String = "",
    val sectionNumber: Int = 0,
    val campus: String = "",
    val room: String = "",
    val instructor: String = "",
    val startTime: LocalTime,
    val endTime: LocalTime,
    val days: Set<DayOfWeek> = emptySet(),
    val courseName: String = ""
) {
    fun toSectionUnit(): List<SectionUnit> {
        return this.days.map {
            SectionUnit(it.ordinal,
                startTime.hour + (startTime.minute.toFloat() / MINUTESPERHOUR),
                endTime.hour + (endTime.minute.toFloat() / MINUTESPERHOUR),
                courseName, instructor, room, component, sectionNumber, classNumber)
        }
    }
}

typealias Schedule = List<Section>

fun sectionListToUnits(sections: List<Section>): List<SectionUnit> {
    return sections.map { it.toSectionUnit() }.flatten()
}