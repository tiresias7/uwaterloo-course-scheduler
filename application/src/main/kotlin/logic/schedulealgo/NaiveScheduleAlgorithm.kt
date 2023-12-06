package logic.schedulealgo

import Section
import logic.preference.Preference
import java.util.*

class NaiveScheduleAlgorithm : ScheduleAlgorithm() {
    override fun generateSchedules(
        sectionLists: List<List<Section>>,
        preferences: List<Preference>,
        totalSchedules: Int,
        softScoreLowerBound: Int
    ): List<List<Section>> {
        // A priority queue to keep the lowest score at the top (min-heap based on score).
        val topSchedules = PriorityQueue<Pair<List<Section>, Int>>(compareBy { it.second })

        fun cartesianProductSequence(lists: List<List<Section>>): Sequence<List<Section>> =
            lists.fold(sequenceOf(listOf())) { acc, set ->
                acc.flatMap { list -> set.asSequence().map { element -> list + element } }
            }

        cartesianProductSequence(sectionLists).forEach { combination ->
            val score = preferences.sumOf { preference ->
                preference.eval(combination)
            }

            topSchedules.add(Pair(combination, score))
            if (topSchedules.size > totalSchedules) {
                // Remove the schedule with the lowest score
                topSchedules.poll()
            }
        }

        // Since the priority queue has the lowest score at the top, reverse it to get the highest scores first.
        return topSchedules.toList().sortedByDescending { it.second }.map { it.first }
    }

}
