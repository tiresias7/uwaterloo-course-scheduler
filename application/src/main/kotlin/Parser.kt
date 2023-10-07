import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File


data class ClassInfo(
    val classNbr: String,
    val classSection: String,
    val campus: String,
    val daytime: String,
    val room: String,
    val instructor: String,
    val meetingDates: String
)

fun main() {
    val filepath =  "C:\\Users\\YZM\\Documents\\WeChat Files\\wxid_z00j2i3si3a712\\FileStorage\\File\\2023-10\\test.txt"
    val file = File(filepath)
    val document: Document = Jsoup.parse(file, "UTF-8")

    // Select the field element
    val fieldElement = document.select("field[id=win0divPAGECONTAINER]")

    // Parse the CDATA content as HTML
    val htmlDoc = Jsoup.parseBodyFragment(fieldElement.text())

    // Select the td elements within the table
    val tdElements = htmlDoc.select("table.PSPAGECONTAINER").select("td")

    val classInfoList = ArrayList<ClassInfo>()

    val classNbrElements: List<String> = tdElements.select("span[id^=MTG_CLASS_NBR\$]").map { it.text() }
    val classSectionElements: List<String> = tdElements.select("span[id^=MTG_CLASSNAME\$]").map { it.text() }
    val campusElements: List<String> = tdElements.select("span[id^=UW_DERIVED_CLSR_CAMPUS\$]").map { it.text() }
    val daytimeElements: List<String> = tdElements.select("span[id^=MTG_DAYTIME\$]").map { it.text() }
    val roomElements: List<String> = tdElements.select("span[id^=MTG_ROOM\$]").map { it.text() }
    val instructorElements: List<String> = tdElements.select("span[id^=MTG_INSTR\$]").map { it.text() }
    val meetingDatesElements: List<String> = tdElements.select("span[id^=MTG_TOPIC\$]").map { it.text() }

//    println(
//        "Class Nbr Size: ${classNbrElements.size}, " +
//                "Class Section Size: ${classSectionElements.size}, " +
//                "Campus Size: ${campusElements.size}, " +
//                "Daytime Size: ${daytimeElements.size}, " +
//                "Room Size: ${roomElements.size}, " +
//                "Instructor Size: ${instructorElements.size}, " +
//                "Meeting Dates Size: ${meetingDatesElements.size}"
//    )


    // Verify that the same number of elements were found for each category
    if (listOf(classNbrElements.size, classSectionElements.size, campusElements.size, daytimeElements.size,
            roomElements.size, instructorElements.size, meetingDatesElements.size).distinct().size == 1) {
        for (i in classNbrElements.indices) {
            val classNbr = classNbrElements[i]
            val classSection = classSectionElements[i]
            val campus = campusElements[i]
            val daytime = daytimeElements[i]
            val room = roomElements[i]
            val instructor = instructorElements[i]
            val meetingDates = meetingDatesElements[i]

            // Create a ClassInfo object and add it to the list
            val classInfo = ClassInfo(classNbr, classSection, campus, daytime, room, instructor, meetingDates)
            classInfoList.add(classInfo)
        }
    } else {
        println("Error: Inconsistent data. The number of elements for each category does not match.")
    }

    // Print the list of class information
    classInfoList.forEach { classInfo ->
        println(classInfo)
    }
    // Extract elements using CSS selectors
}
