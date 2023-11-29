package pages.SchedulePage.CourseSelection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import SelectedCourse
import style.md_theme_light_inversePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun courseChip(
    index: Int,
    course: SelectedCourse,
    toggleCallBack: (index: Int) -> Unit,
    deleteCallBack: (index: Int) -> Unit
) {
    val chipColor = if (course.required) {
        InputChipDefaults.inputChipColors(selectedContainerColor = md_theme_light_inversePrimary)
    } else {
        InputChipDefaults.inputChipColors()
    }
    val dropDownExpanded = remember { mutableStateOf(false) }
    Box() {
        InputChip(
            onClick = {
                dropDownExpanded.value = true
            },
            colors = chipColor,
            label = {
                var leadingSpace = ""
                if(course.courseName.length >= 8) {
                    Text(course.courseName)
                }
                else if(course.courseName.length == 7) {
                    Text(" " + course.courseName)
                }
                else {
                    val numOfLeading = 9 - course.courseName.length
                    for(i in 1..numOfLeading) {
                        leadingSpace = "$leadingSpace "
                    }
                    Text(leadingSpace + course.courseName)
                }
            },
            selected = true,
            modifier = Modifier.padding(4.dp).size(width = 100.dp, 40.dp),
        )
        DropdownMenu(
            expanded = dropDownExpanded.value,
            onDismissRequest = { dropDownExpanded.value = false },
            offset = DpOffset(10.dp, -(10.dp))
        ) {
            DropdownMenuItem(
                onClick = {
                    dropDownExpanded.value = false
                    toggleCallBack(index)
                }
            ) {
                Text(text = if (course.required) "Set to Optional" else "Set to Required")
            }
            DropdownMenuItem(
                onClick = {
                    dropDownExpanded.value = false
                    deleteCallBack(index)
                }
            ) {
                Text(text = "Remove this course")
            }
        }
    }
}