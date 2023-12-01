package pages.ProfilePage

import SectionUnit
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import friendSearchInputField
import kotlinx.coroutines.runBlocking
import logic.ktorClient.*
import org.jetbrains.skia.Color
import pages.LoginPage.USER_ID
import pages.LoginPage.USER_NAME
import pages.SchedulePage.CourseSelection.dropDownExpanded
import pages.SchedulePage.ScheduleSection.schedule
import style.currentColorScheme

/*
var allUser = listOf(
    "apple",
    "beta",
    "cookie",
    "dean",
    "elephant",
    "fire",
    "goose",
    "horse",
    "italy",
    "jiggle",
    "kitty",
    "lemma",
    "moose",
    "nonono",
    "opera",
    "pig"
)
*/

var allUser : MutableList<Pair<Int, String>> = mutableListOf()

@Composable
fun friendSection( USER_ID : Int, savedSections : MutableList<SectionUnit>) {
    val friendsList = remember { mutableStateOf(runBlocking {
        fetchFriendList(USER_ID)
    }) }
    //val friendRequestList = remember { mutableListOf<Pair<Int, String>>() }
    val friendRequestList = remember { mutableStateOf(runBlocking {
        fetchFriendRequests(USER_ID)
    }) }
    var ifFriendList by remember { mutableStateOf(true)}
    var ifMessages by remember { mutableStateOf(false)}
    val localDensity = LocalDensity.current
    var column1HeightDp by remember { mutableStateOf(0.dp) }
    val ifViewProfile = remember { mutableStateOf(false)}
    var friendID by remember { mutableStateOf(0)}
    val interactionSource = remember { MutableInteractionSource() }
    Column (
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(top = 50.dp)
        .onGloballyPositioned { coordinates ->
            column1HeightDp = with(localDensity) { coordinates.size.height.toDp() }
        }
    )
    {
        Text(text = "Friends", fontSize = 30.sp)
        Row(
            modifier = Modifier.padding(top = 10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (ifFriendList) {
                OutlinedButton(
                    content = { Text("View and Add") },
                    onClick = {
                        friendsList.value = runBlocking {
                            fetchFriendList(USER_ID)
                        }
                        ifFriendList = true
                        ifMessages = false
                    },
                    modifier = Modifier.size(180.dp, 40.dp)
                )
            } else {
                TextButton(
                    content = { Text("View and Add") },
                    onClick = {
                        friendsList.value = runBlocking {
                            fetchFriendList(USER_ID)
                        }
                        ifFriendList = true
                        ifMessages = false
                    },
                    modifier = Modifier.size(180.dp, 40.dp)
                )
            }
            if (ifMessages) {
                OutlinedButton(
                    content = { Text("Friend Requests") },
                    onClick = {
                        friendRequestList.value = runBlocking {
                            fetchFriendRequests(USER_ID)
                        }
                        ifFriendList = false
                        ifMessages = true
                    },
                    modifier = Modifier.size(180.dp, 40.dp).padding(start = 5.dp)
                )
            } else {
                TextButton(
                    content = { Text("Friend Requests") },
                    onClick = {
                        friendRequestList.value = runBlocking {
                            fetchFriendRequests(USER_ID)
                        }
                        ifFriendList = false
                        ifMessages = true
                    },
                    modifier = Modifier.size(180.dp, 40.dp).padding(start = 5.dp)
                )
            }
        }
        Box(
            modifier = Modifier.height(column1HeightDp * 88 / 100).fillMaxWidth().padding(top = 10.dp),
        ) {
            if (ifFriendList) {
                Column(
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        "Add Friends:",
                        modifier = Modifier,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic
                    )
                    friendSearchInputField(allUser, {}, USER_ID)
                    Text(
                        "My Friend List:",
                        modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic
                    )
                    if (friendsList.value.isEmpty()) {
                        Text("You currently have no friend in ω",
                            fontStyle = FontStyle.Italic)
                    }
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                    ) {
                        LazyColumn(
                            modifier = Modifier,
                        ) {
                            items(friendsList.value, { it }) { friend ->
                                ListItem(
                                    modifier = Modifier
                                        .height(50.dp).fillMaxWidth(),
                                    headlineContent = { Text(friend.second) },
                                    trailingContent = {
                                        Row() {
                                            TextButton(
                                                onClick = { runBlocking {
                                                    fetchFriendProfile(USER_ID, friend.first)
                                                    ifViewProfile.value = true;
                                                    friendID = friend.first
                                                }},
                                                modifier = Modifier.padding(end = 4.dp)
                                            ) {
                                                Text("View Schedule")
                                            }
                                            TextButton(
                                                onClick = { runBlocking {
                                                    deleteFriendRelation(USER_ID, friend.first)
                                                    friendsList.value = fetchFriendList(USER_ID)
                                                } },
                                                modifier = Modifier
                                            ) {
                                                Text("Remove")
                                            }
                                        }
                                    },
                                    tonalElevation = 30.dp,
                                    colors = ListItemDefaults.colors(containerColor = currentColorScheme.value.cs.primaryContainer)
                                )
                                Divider(thickness = 1.dp, color = currentColorScheme.value.cs.inversePrimary)
                            }
                        }
                    }
                }
            } else if (ifMessages) {
                if (friendRequestList.value.isEmpty()) {
                    Text("You currently have no invitation",
                            fontStyle = FontStyle.Italic)
                }
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier,
                ) {
                    LazyColumn(
                        modifier = Modifier,
                    ) {
                        items(friendRequestList.value, { it }) { user ->
                            ListItem(
                                modifier = Modifier
                                    .height(70.dp).fillMaxWidth(),
                                headlineContent = { Text(user.second) },
                                trailingContent = {
                                    Row(

                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                runBlocking {
                                                    approveFriendRequest(USER_ID, user.first)
                                                    //friendRequestList.clear()
                                                    //friendRequestList.addAll(fetchFriendRequests(USER_ID))
                                                    friendRequestList.value = fetchFriendRequests(USER_ID)
                                                }},
                                            modifier = Modifier.padding(5.dp)
                                        ) {
                                            Text("Accept")
                                        }
                                        OutlinedButton(
                                            onClick = { runBlocking {
                                                denyFriendRequest(USER_ID, user.first)
                                            }},
                                            modifier = Modifier.padding(5.dp),
                                        ) {
                                            Text("Decline")
                                        }
                                    }
                                },
                                tonalElevation = 30.dp,
                                colors = ListItemDefaults.colors(containerColor = currentColorScheme.value.cs.primaryContainer)
                            )
                            Divider(thickness = 1.dp, color = currentColorScheme.value.cs.inversePrimary)
                        }
                    }
                }
            }
        }
    }
    viewProfilePageDialog(friendID, ifViewProfile, savedSections)
}

@Composable
fun viewProfilePageDialog(friendID : Int, ifViewProfile : MutableState<Boolean>, savedSections: MutableList<SectionUnit>) {
    if (ifViewProfile.value) {
        Dialog(
            onDismissRequest = { ifViewProfile.value = false },
            properties = DialogProperties(dismissOnClickOutside = true, usePlatformDefaultWidth = false),
        ){
            var friendSections : MutableList<SectionUnit>
            runBlocking { friendSections = fromSectionToSectionUnit(fetchFriendProfile(USER_ID, friendID)) }
            Column(
                modifier = Modifier.fillMaxHeight().width(1000.dp).padding(top = 20.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val name: String = if (USER_NAME.length > 10) {
                    USER_NAME.take(10) + "..."
                } else {
                    USER_NAME
                }
                Text("$name 's Schedule", fontSize = 35.sp, color = androidx.compose.ui.graphics.Color.White,)
                schedule(friendSections, modifier = Modifier.padding(top = 20.dp, bottom = 20.dp))
            }
        }
    }
}