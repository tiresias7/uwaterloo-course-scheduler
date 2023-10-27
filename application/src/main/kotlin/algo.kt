import kotlin.Comparator
import java.util.PriorityQueue
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

fun algorithm(sectionsList: List<Set<Section>>, preferences: List<Preference>, totalSchedules: Int): List<List<Section>>{
    val cartesianSections: Sequence<List<Section>> =  sectionsList.fold(emptySequence()) {
            acc, set -> acc.flatMap { list -> set.map { element -> list + element } }
    }

    val customComparator = Comparator<Pair<List<Section>, Int>> { o1, o2 -> o1.second - o2.second }
    val topTenSections = PriorityQueue(customComparator)
    cartesianSections.forEach { sections ->
        val totalRating = preferences.sumOf { it(sections) }
        topTenSections.add(Pair(sections, totalRating))
        if (topTenSections.size > totalSchedules) {
            topTenSections.poll()
        }
    }
    return topTenSections.map { it.first }
        .sortedBy { sections -> sections.maxByOrNull { it.endTime }?.endTime }
        .take(totalSchedules)
}