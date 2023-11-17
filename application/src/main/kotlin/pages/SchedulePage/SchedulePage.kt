package pages.SchedulePage

import pages.SchedulePage.CourseSelection.courseSelectionSection
import pages.SchedulePage.PreferenceSelection.preferenceDialog
import pages.SchedulePage.PreferenceSelection.preferenceSelectionSection
import pages.SchedulePage.ScheduleSection.schedule
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cache.CourseNameLoader
import common.*
import data.SectionUnit
import data.SelectedCourse
import database.common.createDataSource
import common.navcontroller.NavController
import database.sections.queryAllClasses
import logic.getSchedule
import logic.preference.NoCollisionPreference
import logic.preference.Preference
import logic.preference.PreferenceBuilder
import logic.sectionListToUnits

val allCourses = CourseNameLoader.getAllCourseNames()

@Composable
fun schedulePage(
    navController: NavController
) {
    navDrawer(navController, content = { schedulePageContent() })
}

@Composable
fun schedulePageContent(
) {
    val ifErrorDialog = remember { mutableStateOf(false)}
    val errorCauses = remember { mutableListOf<String>()}
    val interactionSource = remember { MutableInteractionSource() }
    val selectedCourses = remember { mutableStateListOf<SelectedCourse>() }  // state hoisting
    val requiredNumberOfCourses = remember { mutableStateOf(5) } // If no input then select 5 courses as default
    val returnedSections = remember { mutableStateListOf<SectionUnit>() }
    val showAddPreference = remember { mutableStateOf(false) }
    val selectedPreferences = remember { mutableStateListOf<Preference>() }
    val preferenceBuilder = PreferenceBuilder()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 40.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {}
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            courseSelectionWrapper(selectedCourses, requiredNumberOfCourses,
                addCallBack = { courseName: String ->
                    var alreadyAdded = false
                    for (course in selectedCourses) {
                        if (course.courseName == courseName) {
                            alreadyAdded = true
                        }
                    }
                    if (!alreadyAdded) {
                        selectedCourses.add(SelectedCourse(courseName, true))
                    }
                },
                toggleCallBack = { index: Int ->
                    val prev_required = selectedCourses[index].required
                    selectedCourses[index] = selectedCourses[index].copy(required = !prev_required)
                },
                deleteCallBack = { index: Int ->
                    selectedCourses.removeAt(index)
                })
            Spacer(modifier = Modifier.padding(10.dp))
            preferenceSelectionWrapper(selectedPreferences,
                showCallBack = {
                    showAddPreference.value = true
                },
                changeCallBack = { from: Int, to: Int ->
                    if (from < to){
                        for (p in selectedPreferences) {
                            if (p.weight > (from + 1) && p.weight <= (to + 1)) {
                                p.weight--
                            }
                        }
                        val removeElement = selectedPreferences.removeAt(from)
                        removeElement.weight = to + 1
                        selectedPreferences.add(to, removeElement)
                    }
                    else{
                        for (p in selectedPreferences) {
                            if (p.weight > to && p.weight <= from) {
                                p.weight++
                            }
                        }
                        val removeElement = selectedPreferences.removeAt(from)
                        removeElement.weight = to + 1
                        selectedPreferences.add(to, removeElement)
                    }
                },
                deleteCallBack = {
                    preference ->
                    var remove_flag = 0
                    for (p in selectedPreferences){
                        if (p === preference){
                            remove_flag = 1
                        }
                        if (remove_flag == 1){
                            p.weight--
                        }
                    }
                    selectedPreferences.remove(preference)
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.4f))
            schedule(returnedSections, modifier = Modifier.weight(5f))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.weight(0.6f)
            ) {
                OutlinedButton(
                    onClick = {
                        if (requiredNumberOfCourses.value < numOfHard(selectedCourses)) {
                            ifErrorDialog.value = true
                            errorCauses.add("hard > num")
                        }
                        else if(requiredNumberOfCourses.value > selectedCourses.size) {
                            ifErrorDialog.value = true
                            errorCauses.add("soft + hard < num")
                        }
                        else {
                            returnedSections.clear()
                            val tempSections = getScheduleService(
                                selectedCourses,
                                requiredNumberOfCourses,
                                selectedPreferences
                            )
                            if (tempSections.isNotEmpty()) {
                                returnedSections.addAll(tempSections)
                            }
                            else {
                                ifErrorDialog.value = true
                                errorCauses.add("no schedule returned")
                            }
                        }
                    },
                    modifier = Modifier
                        .size(width = 180.dp, height = 56.dp)
                        .padding(top = 10.dp)
                ) {
                    Text("Generate Schedule")
                }
                TextButton(
                    content = {Text("Save")},
                    onClick = {},
                    modifier = Modifier.padding(top = 10.dp)
                )
                /*TextButton(
                    content = {Text("Export")},
                    onClick = {}
                )*/
            }
        }
    }
    preferenceDialog(showAddPreference, addCallBack = { tag: String, inputList: List<String> ->
        preferenceBuilder.build(selectedPreferences.size+1, tag, inputList)?.let { selectedPreferences.add(it) }
    })
    errorDialog(ifErrorDialog, errorCauses)
}

@Composable
fun courseSelectionWrapper(
    selectedCourses: MutableList<SelectedCourse>,
    requiredNumberOfCourses: MutableState<Int>,
    addCallBack: (courseName: String) -> Unit,
    toggleCallBack: (index: Int) -> Unit,
    deleteCallBack: (index: Int) -> Unit,
) {
    //Course Selection Section
    courseSelectionSection(
        allCourses, selectedCourses, requiredNumberOfCourses,
        addCallBack,
        toggleCallBack,
        deleteCallBack
    )
}

@Composable
fun preferenceSelectionWrapper(
    selectedPreferences: MutableList<Preference>,
    showCallBack: () -> Unit,
    changeCallBack: (Int, Int) -> Unit,
    deleteCallBack: (preference: Preference) -> Unit
) {
    preferenceSelectionSection(selectedPreferences, showCallBack, changeCallBack, deleteCallBack)
}

fun getScheduleService(selectedCourses: MutableList<SelectedCourse>,
                       requiredNumberOfCourses: MutableState<Int>,
                       selectedPreferences: MutableList<Preference>): List<SectionUnit> {
    val hardCourses : MutableList<String> = mutableListOf()
    val softCourses : MutableList<String> = mutableListOf()
    val hardPreference : MutableList<Preference> = mutableListOf()
    val softPreference : MutableList<Preference> = mutableListOf()
    for(course in selectedCourses) {
        if (course.required) {
            hardCourses.add(course.courseName)
        }
        else {
            softCourses.add(course.courseName)
        }
    }
    hardPreference.add(NoCollisionPreference(10000))
    for(preference in selectedPreferences) {
        softPreference.add(preference)
    }
    println(hardCourses)
    println(softCourses)
    println(hardPreference)
    println(softPreference)
    println(requiredNumberOfCourses.value)
    val result : List<SectionUnit> = sectionListToUnits(getSchedule(hardCourses.toList(), softCourses.toList(), requiredNumberOfCourses.value,
        hardPreference.toList(), softPreference.toList())[0])
    println(result)
    return result
}

fun numOfHard(selectedCourses: MutableList<SelectedCourse>) : Int {
    var result = 0
    for (course in selectedCourses) {
        if (course.required) result++
    }
    return result
}

@Composable
fun errorDialog(ifErrorDialog : MutableState<Boolean>, errorCauses : MutableList<String>) {
    if (ifErrorDialog.value) {
        Dialog(
            onDismissRequest = { ifErrorDialog.value = false },
            properties = DialogProperties(dismissOnClickOutside = true, usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .size(600.dp, 400.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .size(600.dp, 400.dp)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text("Oops...", fontSize = 30.sp)
                    if (errorCauses.contains("hard > num")) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text("Following requirement(s) is not met:",
                                fontSize = 15.sp,
                                fontStyle = FontStyle.Italic,
                                modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 10.dp)
                            )
                            Text(
                                "Number of courses you want to take should not be less than the number of required courses",
                                fontSize = 15.sp,
                                modifier = Modifier.padding(start = 30.dp, end = 30.dp)
                            )
                        }
                    }
                    else if (errorCauses.contains("soft + hard < num")) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text("Following requirement(s) is not met:",
                                fontSize = 15.sp,
                                fontStyle = FontStyle.Italic,
                                modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 10.dp)
                            )
                            Text(
                                "Number of courses you want to take should not be greater than the number of selected courses",
                                fontSize = 15.sp,
                                modifier = Modifier.padding(start = 30.dp, end = 30.dp)
                            )
                        }
                    }
                    else if (errorCauses.contains("no schedule returned")) {
                        Text(
                            "It's impossible to generate a schedule without time conflict, including all courses you selected",
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 30.dp, end = 30.dp)
                        )
                    }
                    ExtendedFloatingActionButton(
                        content = {Text("Got it")},
                        onClick = {
                            ifErrorDialog.value = false
                            errorCauses.clear()
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}
