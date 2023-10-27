import data.SectionUnit
import data.SelectedCourse
import kotlin.Comparator
import java.util.PriorityQueue

const val ACCEPTLEVEL = 95;
abstract class Preference {
    abstract operator fun invoke(sections: List<Section>): Int
}

class ExamplePreference : Preference() {
    override fun invoke(sections: List<Section>): Int {
        return 75
    }
}

class BasicConflictPreference : Preference() {
    override fun invoke(sections: List<Section>): Int {
        val timeInfo = sections.flatMap { section -> section.days.map {
                day -> Triple(day, section.startTime, section.endTime) }  }
        // Iterate through the list of tuples and create a new list of tuples (LocalTime, Int, Day)
        val timeMarks = timeInfo.flatMap { (day, start, end) ->
            listOf(Triple(start, 1, day), Triple(end, -1, day))
        }
        // Sort the list of tuples based on the first element (LocalTime)
        val sortedTimeMarks = timeMarks.sortedBy { it.first }
        val intervalMaps = MutableList(Day.entries.size) { 0 }
        sortedTimeMarks.forEach {
            intervalMaps[it.third.ordinal] += it.second
            if (intervalMaps[it.third.ordinal] >= 2) return 0
        }
        return 100
    }
}

fun <T> List<List<T>>.cartesianProduct(): List<List<T>> {
    if (isEmpty()) return listOf(emptyList())
    val result = mutableListOf<List<T>>()
    fun generateCombinations(index: Int, currentList: List<T>) {
        if (index == size) {
            result.add(currentList)
            return
        }
        for (element in this[index]) {
            generateCombinations(index + 1, currentList + element)
        }
    }

    generateCombinations(0, emptyList())
    return result
}

fun algorithm(sectionsList: List<List<Section>>, preferences: List<Preference>, totalSchedules: Int): List<List<Section>>{
    val customComparator = Comparator<Pair<List<Section>, Int>> { o1, o2 -> o1.second - o2.second }
    val topTenSections = PriorityQueue(customComparator)
    for (sections in sectionsList.cartesianProduct()) {
        val totalRating = preferences.sumOf { it(sections) }
        topTenSections.add(Pair(sections, totalRating))
        if (topTenSections.size > totalSchedules && topTenSections.poll().second / (preferences.size) >= ACCEPTLEVEL) {
            break;
        }
    }
    return topTenSections.map { it.first }
        .sortedBy { sections -> sections.maxByOrNull { it.endTime }?.endTime }
        .take(totalSchedules)
}

fun testAlgo(selectSections: List<SelectedCourse>): List<SectionUnit> {
    val allCourseSections = selectSections.map { it ->
        val faculty = it.courseName.takeWhile { it.isLetter() }
        val courseId = it.courseName.dropWhile { it.isLetter() }
        createDataSource().use{ querySectionsByFacultyId(faculty, courseId, it) }
    }.flatten()
    val topOneSection = algorithm(allCourseSections, listOf(BasicConflictPreference()), 1).first()
    return sectionListToUnits(topOneSection)
}

//fun main() {
//    testAlgo(listOf(
//        SelectedCourse("CS240", true),
//        SelectedCourse("CS135", true),
//        SelectedCourse("CS245", true),
//        SelectedCourse("CS251", true),
//        SelectedCourse("CS341", true),
//        SelectedCourse("CS346", true)
//    )).forEach {
//        println(it.courseName + " " + it.startTime + " " + it.finishTime + "  Day: " + it.day)
//    }
//}
