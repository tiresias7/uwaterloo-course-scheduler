import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalFocusManager
import data.SelectedCourse


val list = mutableStateOf(listOf<String>())
val value = mutableStateOf(TextFieldValue())
val dropDownExpanded = mutableStateOf(false)

@Composable
fun courseSearchInputField(
    all: List<String>,
    addCallBack: (courseName: String) -> Unit
) {
    var label by remember { mutableStateOf("Add Courses") }
    val ifFocused = mutableStateOf(true)
    var placeholder by remember { mutableStateOf("Eg. CS346") }
    val focusManager = LocalFocusManager.current
    Box() {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.size(width = 446.dp, height = 60.dp)
                    .onFocusChanged { focusState ->
                        ifFocused.value = focusState.isFocused
                        if (!ifFocused.value) {
                            value.value = TextFieldValue("")
                            placeholder = "Eg. CS346"
                        } else {
                            label = "Add Courses"
                            dropDownExpanded.value = true
                            list.value = all
                        }
                    },
                singleLine = true,
                value = value.value,
                onValueChange = { newValue: TextFieldValue ->
                    dropDownExpanded.value = true
                    value.value = newValue
                    list.value = searchFilter(newValue.text, all)
                },
                label = { Text(label) },
                placeholder = { Text(placeholder) },
                leadingIcon = {
                    Icon(Icons.Outlined.Search, "Share to friends")
                }
            )
        }
        DropdownMenu(
            expanded = dropDownExpanded.value,
            properties = PopupProperties(),
            onDismissRequest = { dropDownExpanded.value = false },
            modifier = Modifier.width(446.dp).heightIn(max = 500.dp)
        ) {
            if (list.value.isNotEmpty()) {
                list.value.forEach { text: String ->
                    DropdownMenuItem(
                        modifier = Modifier.size(446.dp, 35.dp),
                        onClick = {
                            dropDownExpanded.value = false
                            value.value = TextFieldValue("")
                            focusManager.clearFocus()
                            label = text + " Added ✓"
                            addCallBack(text)
                        }
                    ) {
                        Text(text = text)
                    }
                    Divider()
                }
            } else {
                DropdownMenuItem(
                    modifier = Modifier.size(446.dp, 35.dp),
                    onClick = {}
                ) {
                    Text(text = "No Course Found")
                }
            }
        }
    }
}

fun searchFilter(inputValue : String, allCourses : List<String>) : List<String> {
    val startWith = allCourses.filter{
        it.startsWith(inputValue, ignoreCase = true) ||
                inputValue.startsWith(it, ignoreCase = true)
    }.sorted()
    println(startWith)
    val containWith = allCourses.filter {
        !startWith.contains(it) &&
                (inputValue.replace("\\s".toRegex(), "").contains(it, ignoreCase = true) ||
                        it.contains(inputValue.replace("\\s".toRegex(), ""), ignoreCase = true))
    }.sorted()
    return startWith + containWith
}