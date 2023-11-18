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
import logic.friends.sendFriendRequest
import pages.LoginPage.USER_ID
import java.util.Dictionary


@Composable
fun friendSearchInputField(
    all: MutableList<Pair<Int, String>>,
    addCallBack: (courseName: String) -> Unit
) {
    val userListShown = remember { mutableListOf<String>() }
    val userNameList = remember { mutableListOf<String>() }
    val userIDDict = remember { mutableMapOf<String, Int>() }
    val inputValue = remember { mutableStateOf(TextFieldValue()) }
    val dropDownExpanded = remember { mutableStateOf(false) }
    var label by remember { mutableStateOf("Search Username or UID to add friends") }
    val ifFocused = mutableStateOf(true)
    val focusManager = LocalFocusManager.current

    for (user in all) {
        userNameList.add(user.second)
        userIDDict[user.second] = user.first
    }

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
                            userListShown.clear()
                            userListShown.addAll(userNameList)
                        }
                    },
                singleLine = true,
                value = inputValue.value,
                onValueChange = { newValue: TextFieldValue ->
                    dropDownExpanded.value = true
                    inputValue.value = newValue
                    userListShown.clear()
                    userListShown.addAll(searchFilter(newValue.text, userNameList))
                },
                label = { Text(label, fontStyle = FontStyle.Italic) },
                leadingIcon = {
                    Icon(Icons.Outlined.Search, "Search User")
                }
            )
            DropdownMenu(
                expanded = dropDownExpanded.value,
                properties = PopupProperties(),
                onDismissRequest = { dropDownExpanded.value = false },
                modifier = Modifier.width(446.dp).heightIn(max = 500.dp)
            ) {
                if (userListShown.isNotEmpty()) {
                    userListShown.take(50).forEach { text: String ->
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
                                    onClick = {sendFriendRequest(USER_ID, userIDDict[text]!!)},
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
}