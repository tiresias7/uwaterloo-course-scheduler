package logic

import database.course.createDataSource
import database.course.querySectionsByFacultyId
import logic.preference.Preference
import logic.schedulealgo.OptimizedScheduleAlgorithm
import java.util.*

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

    val hardCourseAllSections : List<List<Section>> = hardCourses.map { it ->
        val faculty = it.takeWhile { it.isLetter() }
        val courseId = it.dropWhile { it.isLetter() }
        createDataSource().use{ querySectionsByFacultyId(faculty, courseId, it) }
    }.flatten()
    val softCourseSections : List<List<List<Section>>> = softCourses.map { it ->
        val faculty = it.takeWhile { it.isLetter() }
        val courseId = it.dropWhile { it.isLetter() }
        createDataSource().use{ querySectionsByFacultyId(faculty, courseId, it) }
    }
    println(softCourseSections)

    val algo = OptimizedScheduleAlgorithm()
    val schedules = PriorityQueue<Pair<Schedule, Int>>(compareBy { it.second })

    // Must choose all hard courses
    // Choose soft courses until numOfCourses is reached
    val numOfSoftCourses = numOfCourses - hardCourses.size

    fun chooseSoftCourses(currentSections: List<List<Section>>, index: Int = 0, softCourseChosen : Int = 0) {
        if (softCourseChosen >= numOfSoftCourses) {
            val possibleSchedules = algo.generateSchedules(hardCourseAllSections + currentSections, hardPreferences + softPreferences, numOfSchedules)
            for (possibleSchedule in possibleSchedules) {
                val score = softPreferences.sumOf { it.eval(possibleSchedule) } + hardPreferences.sumOf { it.eval(possibleSchedule) }
                schedules.add(Pair(possibleSchedule, score))
                if (schedules.size > numOfSchedules) {
                    schedules.poll()
                }
            }
            return
        }

        if (index >= softCourseSections.size) {
            return
        }

        chooseSoftCourses(currentSections + softCourseSections[index], index + 1, softCourseChosen + 1)
        chooseSoftCourses(currentSections, index + 1, softCourseChosen)
    }

    chooseSoftCourses(listOf())

    return schedules.toList().sortedByDescending { it.second }.map { it.first }
}