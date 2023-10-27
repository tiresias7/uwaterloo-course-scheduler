import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.time.LocalTime
import java.util.Locale
import java.util.EnumSet
import java.time.format.DateTimeFormatter


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
    val days: Set<Day>
)

fun main() {
    parser("C:\\Users\\YZM\\Desktop\\courses\\")
}


fun parseDayInfo(input: String): Triple<LocalTime, LocalTime, Set<Day>> {
    if (input.isEmpty()) {
        return Triple(LocalTime.parse("00:00"), LocalTime.parse("00:00"), emptySet())
    }

    val regex = Regex("([A-Za-z]+)\\s+(\\d{1,2}:\\d{2}(?:AM|PM)?)\\s+-\\s+(\\d{1,2}:\\d{2}(?:AM|PM)?)")
    val matchResult = regex.find(input)
    if (matchResult != null) {
        val (daysStr, startTimeStr, endTimeStr) = matchResult.destructured
        val days = daysStr.chunked(2).mapNotNull { dayAbbreviations[it] }.toSet()
        val startTime = LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("h:mma", Locale.US))
        val endTime = LocalTime.parse(endTimeStr, DateTimeFormatter.ofPattern("h:mma", Locale.US))
        return Triple(startTime, endTime, EnumSet.copyOf(days))
    } else {
        throw IllegalArgumentException("Invalid input format: $input")
    }
}

fun parseSectionTime(input: String): Pair<String, Int> {
    val pattern = Regex("(\\d+)-(\\w+)")
    val matchResult = pattern.find(input)
    val (sectionNumber, sectionType) = matchResult!!.destructured
    return Pair(sectionType, sectionNumber.toInt())
}

fun parseDistinctLocation(input: String) {
    if (input.lowercase().contains("online") or input.contains("TBA")) return
    return

}
fun parser(filepath: String): ArrayList<Section> {
    val file = File(filepath)
    val document: Document = Jsoup.parse(file, "UTF-8")

    // Select the field element
    val fieldElement = document.select("field[id=win0divPAGECONTAINER]")
    // Parse the CDATA content as HTML
    val htmlDoc = Jsoup.parseBodyFragment(fieldElement.text())
    // Select the td elements within the table
    val tdElements = htmlDoc.select("table.PSPAGECONTAINER").select("td")

    val classInfoList = ArrayList<Section>()

    val classNbrElements: List<String> = tdElements.select("span[id^=MTG_CLASS_NBR\$]").map { it.text() }
    val classSectionElements: List<String> = tdElements.select("span[id^=MTG_CLASSNAME\$]").map { it.text() }
    val campusElements: List<String> = tdElements.select("span[id^=UW_DERIVED_CLSR_CAMPUS\$]").map { it.text() }
    val daytimeElements: List<String> = tdElements.select("span[id^=MTG_DAYTIME\$]").map { it.text() }
    val roomElements: List<String> = tdElements.select("span[id^=MTG_ROOM\$]").map { it.html() }
    val instructorElements: List<String> = tdElements.select("span[id^=MTG_INSTR\$]").map { it.html() }
    val meetingDatesElements: List<String> = tdElements.select("span[id^=MTG_TOPIC\$]").map { it.text() }

    // Verify that the same number of elements were found for each category
    if (listOf(classNbrElements.size, classSectionElements.size, campusElements.size, daytimeElements.size,
            roomElements.size, instructorElements.size, meetingDatesElements.size).distinct().size == 1) {
        for (i in classNbrElements.indices) {
            val classNbr = classNbrElements[i].toInt()
            val sectionComponent = parseSectionTime(classSectionElements[i])
            val campus = campusElements[i]
            val dayTimeInfo = parseDayInfo(daytimeElements[i])
            val room = roomElements[i].split("<br>").first()
            val instructor = instructorElements[i].split("<br>").asSequence().map { it.split(",").map { it.trim() } }
                .flatten().filter { it.isNotBlank() }.distinct().joinToString(", ")

            // Create a ClassInfo object and add it to the list
            val classInfo = Section(classNbr, sectionComponent.first, sectionComponent.second, campus,
                room, instructor, dayTimeInfo.first, dayTimeInfo.second, dayTimeInfo.third)
            classInfoList.add(classInfo)
        }
    } else {
        println("Error: Inconsistent data. $filepath")
    }

    // Print the list of class information
   return classInfoList
}
