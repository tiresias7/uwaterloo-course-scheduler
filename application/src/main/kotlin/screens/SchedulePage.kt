package screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import components.courseSelectionSection
import components.navDrawer
import components.preferenceSelectionSection
import components.scheduleSection
import createDataSource
import data.SectionUnit
import data.SelectedCourse
import kotlinx.serialization.json.*
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
        selectSection(clicked, selectedCourses,
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
            },
            generateCallBack = {
                returnedSections.clear()
                returnedSections.addAll(testAlgo(selectedCourses).toMutableStateList())
            })
        scheduleSection(clicked, returnedSections)
    }
}

@Composable
fun selectSection(
    clicked: MutableState<Boolean>,
    selectedCourses: MutableList<SelectedCourse>,
    addCallBack: (courseName: String) -> Unit,
    toggleCallBack: (index: Int) -> Unit,
    deleteCallBack: (index: Int) -> Unit,
    generateCallBack: () -> Unit
) {
    // Need Data: Course Name Strings/All Preference
    val preferences: List<String> = listOf(
        "Time for classes", "Time for breaks",
        "Time of conflicts", "Location", "Instructor",
    )

    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        //Course Selection Section
        courseSelectionSection(
            allCourses, selectedCourses,
            addCallBack,
            toggleCallBack,
            deleteCallBack
        )

        //Preference Selection Section
        preferenceSelectionSection(preferences)

        //Generate Button
        OutlinedButton(
            onClick = {
                clicked.value = true;
                generateCallBack()
            },
            modifier = Modifier
                .size(width = 180.dp, height = 56.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Generate Schedule")
        }
    }
}

