import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle


@Composable
fun friendSearchInputField(
    all: List<String>,
    addCallBack: (courseName: String) -> Unit
) {
    val userList = remember { mutableStateOf(listOf<String>()) }
    val inputValue = remember { mutableStateOf(TextFieldValue()) }
    val dropDownExpanded = remember { mutableStateOf(false) }
    var label by remember { mutableStateOf("Search Username or UID to add friends") }
    val ifFocused = mutableStateOf(true)
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier.height(650.dp).fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                modifier = Modifier.height(60.dp).fillMaxWidth()
                    .onFocusChanged { focusState ->
                        ifFocused.value = focusState.isFocused
                        if (!ifFocused.value) {
                            inputValue.value = TextFieldValue("")
                        } else {
                            label = "Search Username or UID to add friends"
                            dropDownExpanded.value = true
                            userList.value = all
                        }
                    },
                singleLine = true,
                value = inputValue.value,
                onValueChange = { newValue: TextFieldValue ->
                    dropDownExpanded.value = true
                    inputValue.value = newValue
                    userList.value = searchFilter(newValue.text, all)
                },
                label = { Text(label, fontStyle = FontStyle.Italic) },
                leadingIcon = {
                    Icon(Icons.Outlined.Search, "Search User")
                }
            )
        }
        DropdownMenu(
            expanded = dropDownExpanded.value,
            properties = PopupProperties(),
            onDismissRequest = { dropDownExpanded.value = false },
            modifier = Modifier.width(446.dp).heightIn(max = 500.dp)
        ) {
            if (userList.value.isNotEmpty()) {
                userList.value.take(50).forEach { text: String ->
                    DropdownMenuItem(
                        modifier = Modifier.height(45.dp).fillMaxWidth(),
                        onClick = {
                            dropDownExpanded.value = false
                            inputValue.value = TextFieldValue("")
                            focusManager.clearFocus()
                            label = text + " Added ✓"
                            //addCallBack(text)
                        }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp)
                        ) {
                            Text(text = text)
                            FilledTonalButton(
                                onClick = {},
                                modifier = Modifier.padding(5.dp)
                            ){
                                Text("Invite")
                            }
                        }
                    }
                }
            } else {
                DropdownMenuItem(
                    modifier = Modifier.size(446.dp, 35.dp),
                    onClick = {}
                ) {
                    Text(text = "No Matching User")
                }
            }
        }
    }
}