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
import data.SectionUnit
import data.SelectedCourse
import kotlinx.serialization.json.*
import navcontroller.NavController


val sections_string = """
    [
  {
    "day": 0,
    "startTime": 9,
    "finishTime": 11,
    "courseName": "CS341",
    "profName": "banana",
    "location": "HH"
  },
  {
    "day": 0,
    "startTime": 15,
    "finishTime": 16.5,
    "courseName": "CS342",
    "profName": "canana",
    "location": "FF"
  },
  {
    "day": 0,
    "startTime": 18,
    "finishTime": 20,
    "courseName": "CS343",
    "profName": "danana",
    "location": "DD"
  },
  {
    "day": 0,
    "startTime": 20,
    "finishTime": 22,
    "courseName": "CS343",
    "profName": "danana",
    "location": "DD"
  },
  {
    "day": 1,
    "startTime": 14.5,
    "finishTime": 15.8,
    "courseName": "CS343",
    "profName": "danana",
    "location": "DD"
  },
  {
    "day": 2,
    "startTime": 14.5,
    "finishTime": 15.8,
    "courseName": "CS343",
    "profName": "danana",
    "location": "DD"
  },
  {
    "day": 3,
    "startTime": 14.5,
    "finishTime": 15.8,
    "courseName": "CS343",
    "profName": "danana",
    "location": "DD"
  },
  {
    "day": 4,
    "startTime": 14.5,
    "finishTime": 15.8,
    "courseName": "CS343",
    "profName": "danana",
    "location": "DD"
  }
]
""".trimIndent()

val sections = Json.decodeFromString<List<SectionUnit>>(sections_string)

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
        selectSection(clicked)
        scheduleSection(clicked, sections)
    }
}

@Composable
fun selectSection(
    clicked: MutableState<Boolean>
) {
    // Need Data: Course Name Strings/All Preferences
    val allCourses = listOf(
        "CS350", "CS341", "CS346", "ECON371", "CO487",
        "CS136", "CS135", "CS246", "ECON101", "MATH239",
        "CS245", "CS251", "CS240", "ECON102", "GEOG101",
        "PHY101", "HLTH101", "EARTH123", "CLAS101", "SPCOM223"
    ).sorted()
    val preferences: List<String> = listOf(
        "Time for classes", "Time for breaks",
        "Time of conflicts", "Location", "Instructor",
    )

    var selectedCourses = remember { mutableStateListOf<SelectedCourse>() }  // state hoisting

    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        //Course Selection Section
        courseSelectionSection(
            allCourses, selectedCourses,
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
            }
        )

        //Preference Selection Section
        preferenceSelectionSection(preferences)

        //Generate Button
        OutlinedButton(
            onClick = { clicked.value = true },
            modifier = Modifier
                .size(width = 180.dp, height = 56.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Generate Schedule")
        }
    }
}

