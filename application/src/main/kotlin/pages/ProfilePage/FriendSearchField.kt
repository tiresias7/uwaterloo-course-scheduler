import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import kotlinx.coroutines.runBlocking
import logic.ktorClient.sendFriendRequest
import request.RequestStatus


@Composable
fun friendSearchInputField(
    all: MutableList<Pair<Int, String>>,
    addCallBack: (courseName: String) -> Unit,
    myID : Int,
) {
    val userListShown = remember { mutableListOf<String>() }
    val userNameList = remember { mutableListOf<String>() }
    val userIDDict = remember { mutableMapOf<String, Int>() }
    val inputValue = remember { mutableStateOf(TextFieldValue()) }
    val dropDownExpanded = remember { mutableStateOf(false) }
    var label by remember { mutableStateOf("Enter UID") }
    val ifFocused = mutableStateOf(true)
    val focusManager = LocalFocusManager.current

    for (user in all) {
        userNameList.add(user.second)
        userIDDict[user.second] = user.first
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                modifier = Modifier.size(300.dp, 60.dp)
                    .onFocusChanged { focusState ->
                        ifFocused.value = focusState.isFocused
                        if (!ifFocused.value) {
                            //inputValue.value = TextFieldValue("")
                        } else {
                            label = "Enter UID"
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
            ExtendedFloatingActionButton(
                content = { Text("Add") },
                onClick = {
                    if (inputValue.value.text != "") {
                        val status = runBlocking {
                            sendFriendRequest(myID, inputValue.value.text.toInt())
                        }
                        if (status != RequestStatus.FRIEND_REQUEST_SUCCESS) {
                        }
                    }
                },
                modifier = Modifier.size(80.dp, 50.dp).padding(start = 10.dp)
            )
        }
    }
}