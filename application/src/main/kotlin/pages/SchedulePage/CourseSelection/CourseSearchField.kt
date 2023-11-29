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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import cache.CourseCache


val list = mutableStateOf(listOf<String>())
val value = mutableStateOf(TextFieldValue())
val dropDownExpanded = mutableStateOf(false)

@Composable
fun courseSearchInputField(
    all: List<String>,
    addCallBack: (courseName: String) -> Unit
) {
    var label by remember { mutableStateOf("Add as many courses as you want") }
    val ifFocused = mutableStateOf(true)
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
                        } else {
                            label = "Add more courses"
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
                label = { Text(label, fontStyle = FontStyle.Italic) },
                placeholder = { Text("Eg. CS346") },
                leadingIcon = {
                    Icon(Icons.Outlined.Search, "Search Course")
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
                list.value.take(50).forEach { text: String ->
                    DropdownMenuItem(
                        modifier = Modifier.size(446.dp, 35.dp),
                        onClick = {
                            dropDownExpanded.value = false
                            value.value = TextFieldValue("")
                            focusManager.clearFocus()
                            label = text + " Added ✓"
                            CourseCache.cacheCourse(text)
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
                    Text(text = "No Matching Course")
                }
            }
        }
    }
}

fun searchFilter(inputValue : String, all : List<String>) : List<String> {
    val startWith = all.filter{
        it.startsWith(inputValue, ignoreCase = true) ||
                inputValue.startsWith(it, ignoreCase = true)
    }.sorted()
    val containWith = all.filter {
        !startWith.contains(it) &&
                (inputValue.replace("\\s".toRegex(), "").contains(it, ignoreCase = true) ||
                        it.contains(inputValue.replace("\\s".toRegex(), ""), ignoreCase = true))
    }.sorted()
    return startWith + containWith
}