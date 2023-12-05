package logic

import cache.CourseCache
import logic.preference.Preference
import Schedule
import Section
import kotlinx.coroutines.*
import logic.schedulealgo.OptimizedScheduleAlgorithm
import kotlin.random.Random

fun getSchedule(
    hardCourses: List<String>,
    softCourses: List<String>,
    numOfCourses: Int,
    hardPreferences: List<Preference>,
    softPreferences: List<Preference>,
    numOfSchedules: Int = 1
): List<Schedule> {
    assert(hardCourses.size <= numOfCourses)
    assert(hardCourses.size + softCourses.size >= numOfCourses)

    val hardCourseAllSections : List<List<Section>> = hardCourses.map {
        CourseCache.getCourse(it)
    }.flatten()
    val softCourseSections : List<List<List<Section>>> = softCourses.map {
        CourseCache.getCourse(it)
    }

    val algo = OptimizedScheduleAlgorithm()

    // Must choose all hard courses
    // Choose soft courses until numOfCourses is reached
    val numOfSoftCourses = numOfCourses - hardCourses.size

    val scope = CoroutineScope(Dispatchers.Default)

    // First calculate a lower bound for the soft score
    val random = Random(System.currentTimeMillis())

    fun generateRandomSchedule(): Schedule {
        val shuffledSoftCoursesSections = softCourseSections.shuffled(random)
        val selectedSections = shuffledSoftCoursesSections.take(numOfSoftCourses).map { course ->
            course.map { component ->
                component[random.nextInt(component.size)]
            }
        }.flatten() + hardCourseAllSections.map { it[random.nextInt(it.size)] }

        return selectedSections
    }

    fun CoroutineScope.generateAndEvaluateRandomSchedules(
        numOfRandomTries: Int
    ): Deferred<Pair<Schedule, Int>?> = async {
        var bestScore = -1
        var bestSchedule: Schedule? = null
        fun violatesHardPreference(sections: List<Section>): Boolean {
            return hardPreferences.any { it.eval(sections) == 0 }
        }
        fun evalSoftPreference(sections: List<Section>): Int {
            return softPreferences.sumOf { it.eval(sections) }
        }

        repeat(numOfRandomTries) {
            val randomSchedule = generateRandomSchedule()
            if (!violatesHardPreference(randomSchedule)) {
                val score = evalSoftPreference(randomSchedule)
                if (score > bestScore) {
                    bestScore = score
                    bestSchedule = randomSchedule
                }
            }
        }

        bestSchedule?.let { Pair(it, bestScore) }
    }

    fun getBestSchedule(
        numOfCoroutines: Int,
        numOfRandomTries: Int
    ): Pair<Schedule, Int>? {
        val deferredResults = List(numOfCoroutines) {
            scope.generateAndEvaluateRandomSchedules(numOfRandomTries)
        }

        val bestSchedule = runBlocking {
            deferredResults.awaitAll()
                .filterNotNull()
                .maxByOrNull { it.second }
        }

        return bestSchedule
    }

    val bestSchedule = getBestSchedule(1000, 100)

    val allSchedules = mutableListOf<Pair<Schedule, Int>>(bestSchedule ?: Pair(emptyList(), -1))
    val softScoreLowerBound = bestSchedule?.second ?: -1

    fun chooseSoftCourses(currentSections: List<List<Section>>, index: Int = 0, softCourseChosen : Int = 0): List<Deferred<List<Pair<Schedule, Int>>>> {
        val deferredSchedules = mutableListOf<Deferred<List<Pair<Schedule, Int>>>>()

        if (softCourseChosen >= numOfSoftCourses) {
            deferredSchedules.add(scope.async {
                val possibleSchedules = algo.generateSchedules(hardCourseAllSections + currentSections, hardPreferences + softPreferences, numOfSchedules, softScoreLowerBound)
                possibleSchedules.map { schedule ->
                    val score = softPreferences.sumOf { it.eval(schedule) } + hardPreferences.sumOf { it.eval(schedule) }
                    Pair(schedule, score)
                }
            })
            return deferredSchedules
        }

        if (index < softCourseSections.size) {
            deferredSchedules.addAll(chooseSoftCourses(currentSections + softCourseSections[index], index + 1, softCourseChosen + 1))
            deferredSchedules.addAll(chooseSoftCourses(currentSections, index + 1, softCourseChosen))
        }

        return deferredSchedules
    }

    val deferredResults = chooseSoftCourses(emptyList())

    runBlocking {
        allSchedules.addAll(deferredResults.awaitAll().flatten())
    }

    return allSchedules.sortedByDescending { it.second }.map { it.first }.take(minOf(numOfSchedules, allSchedules.size))
}