package screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import components.courseSelectionSection
import components.navDrawer
import components.preferenceSelectionSection
import components.scheduleSection
import createDataSource
import data.SectionUnit
import data.SelectedCourse
import navcontroller.NavController
import queryAllClasses
import testAlgo

val allCourses = queryAllClasses(createDataSource())

@Composable
fun schedulePage(
    navController: NavController
) {
    navDrawer(navController, content = { schedulePageContent(navController) })
}

@Composable
fun schedulePageContent(
    navController: NavController
) {
    val clicked = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    var selectedCourses = remember { mutableStateListOf<SelectedCourse>() }  // state hoisting
    var returnedSections = remember { mutableStateListOf<SectionUnit>() }
    var showAddPreference = remember { mutableStateOf(false) }

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
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            courseSelectionWrapper(clicked, selectedCourses,
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
            preferenceSelectionWrapper(clicked, showCallBack = {
                showAddPreference.value = true
            })

            //Generate Button
            OutlinedButton(
                onClick = {
                    clicked.value = true;
                    returnedSections.clear()
                    returnedSections.addAll(testAlgo(selectedCourses).toMutableStateList())
                },
                modifier = Modifier
                    .size(width = 180.dp, height = 56.dp)
                    .align(CenterHorizontally)
            ) {
                Text("Generate Schedule")
            }
        }
        scheduleSection(clicked, returnedSections)
    }

    if (showAddPreference.value) {
        Dialog(
            onDismissRequest = { showAddPreference.value = false },
            properties = DialogProperties(dismissOnClickOutside = true)
        ) {
            Card(
                modifier = Modifier
                    .size(600.dp, 800.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = CenterHorizontally,
                ) {
                    Text("Add preferences")
                    TextButton(
                        onClick = { showAddPreference.value = false },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
fun courseSelectionWrapper(
    clicked: MutableState<Boolean>,
    selectedCourses: MutableList<SelectedCourse>,
    addCallBack: (courseName: String) -> Unit,
    toggleCallBack: (index: Int) -> Unit,
    deleteCallBack: (index: Int) -> Unit,
) {
    //Course Selection Section
    courseSelectionSection(
        allCourses, selectedCourses,
        addCallBack,
        toggleCallBack,
        deleteCallBack
    )
}

@Composable
fun preferenceSelectionWrapper(
    clicked: MutableState<Boolean>,
    showCallBack: () -> Unit
) {
    val preferences = remember {
        mutableStateListOf(
            "Time for classes", "Time for breaks",
            "Time of conflicts", "Location", "Instructor",
        )
    }
    //var addedPreferences = remember { mutableStateListOf<Preference>() }  // state hoisting
    preferenceSelectionSection(preferences, showCallBack,
        changeCallBack = { from: Int, to: Int ->
            val removeElement = preferences.removeAt(from)
            preferences.add(to, removeElement)
        })
}

