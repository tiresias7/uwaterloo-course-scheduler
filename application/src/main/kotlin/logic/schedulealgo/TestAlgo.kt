package logic.schedulealgo

import SectionUnit
import SelectedCourse
import database.common.createDataSource
import database.sections.querySectionsByFacultyId
import logic.preference.NoCollisionPreference
import sectionListToUnits

fun testAlgo(selectSections: List<SelectedCourse>): List<SectionUnit> {
    val allCourseSections = selectSections.map { it ->
        val faculty = it.courseName.takeWhile { it.isLetter() }
        val courseId = it.courseName.dropWhile { it.isLetter() }
        createDataSource().use{ querySectionsByFacultyId(faculty, courseId, it) }
    }.flatten()
    val algo = NaiveScheduleAlgorithm()
    val topOneSection = algo.generateSchedules(allCourseSections, listOf(NoCollisionPreference(10000)), 1).first()
    return sectionListToUnits(topOneSection)
}

fun main() {
    testAlgo(listOf(
        SelectedCourse("CS135", true),
        SelectedCourse("CS136", true),
        SelectedCourse("CS245", true),
        SelectedCourse("CS246", true),
        SelectedCourse("CS251", true),
        SelectedCourse("CS350", true)
    )).forEach {
        println(it.courseName + " " + it.startTime + " " + it.finishTime + "  Day: " + it.day)
    }
}
