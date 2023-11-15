package pages.SchedulePage.CourseSelection

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import courseSearchInputField
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import common.SimpleTextField
import data.SelectedCourse


@Composable
fun courseSelectionSection(
    allCourses: List<String>,
    selectedCourses: MutableList<SelectedCourse>,
    requiredNumberOfCourses: MutableState<Int>,
    addCallBack: (courseName: String) -> Unit,
    toggleCallBack: (index: Int) -> Unit,
    deleteCallBack: (index: Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.width(195.dp).padding(end = 3.dp))
            Text("Courses",fontSize = 14.sp,color = Color.Gray)
            Divider(modifier = Modifier.width(195.dp).padding(start = 3.dp))
        }
        courseSearchInputField(allCourses, addCallBack)
        Text(
            text = "Click on a selected course to modify its priority:",
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic
        )
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .size(width = 446.dp, height = 119.dp)
                .padding(5.dp),
        ) {
            courseGrid(selectedCourses, toggleCallBack, deleteCallBack)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text ="Maximum number of courses you want to take:  ",
                fontSize = 15.sp,
            )
            numberOfCoursesSelectionField(requiredNumberOfCourses)
        }
    }
}

@Composable
fun courseGrid(
    selectedCourses: MutableList<SelectedCourse>,
    toggleCallBack: (index: Int) -> Unit,
    deleteCallBack: (index: Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(selectedCourses.size) { index ->
            courseChip(index, selectedCourses[index], toggleCallBack, deleteCallBack)
        }
    }
}

val dropDownExpanded = mutableStateOf(false)

@Composable
fun numberOfCoursesSelectionField(
    requiredNumberOfCourses: MutableState<Int>
) {
    val all = listOf("1", "2", "3", "4", "5", "6", "7")
    var label by remember { mutableStateOf("5") }
    val focusManager = LocalFocusManager.current
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        SimpleTextField(
            modifier = Modifier.size(40.dp, 25.dp)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        dropDownExpanded.value = true
                    } else {
                        dropDownExpanded.value = false
                    }
                },
            shape = RoundedCornerShape(5.dp),
            readOnly = true,
            value = label,
            onValueChange = {}
        )
        DropdownMenu(
            expanded = dropDownExpanded.value,
            properties = PopupProperties(),
            onDismissRequest = { dropDownExpanded.value = false },
            modifier = Modifier.size(40.dp, 250.dp)
        ) {
            all.forEach { text: String ->
                DropdownMenuItem(
                    modifier = Modifier.height(35.dp),
                    onClick = {
                        dropDownExpanded.value = false
                        requiredNumberOfCourses.value = text.toInt()
                        focusManager.clearFocus()
                        label = text
                    }
                ) {
                    Text(text = text)
                }
                Divider()
            }
        }
    }
}