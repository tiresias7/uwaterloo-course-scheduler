import data.SectionUnit
import java.time.LocalTime

const val MINUTESPERHOUR = 60
data class ClassInfo(
    val classNbr: String,
    val classSection: String,
    val campus: String,
    val daytime: String,
    val room: String,
    val instructor: String,
    val meetingDates: String
)

enum class Day {
    MON, TUE, WED, THU, FRI, SAT, SUN
}

val dayAbbreviations = mapOf(
    "Mo" to Day.MON, "Tu" to Day.TUE, "We" to Day.WED, "Th" to Day.THU,
    "Fr" to Day.FRI, "Sa" to Day.SAT, "Su" to Day.SUN
)

enum class CourseComponent {
    LEC, TUT, LAB
}

data class Section(
    val classNumber: Int,
    val component: String,
    val sectionNumber: Int,
    val campus: String,
    val room: String,
    val instructor: String,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val days: Set<Day>,
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