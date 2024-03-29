package pages.ProfilePage

import SectionUnit
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import common.LoadingFullScreen
import common.isLoading
import friendSearchInputField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import logic.ktorClient.*
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
fun friendSection(USER_ID : Int, fullHeight : Dp, fullWidth : Dp) {
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
    val friendName = remember { mutableStateOf("") }
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
                                                onClick = {
                                                    ifViewProfile.value = true;
                                                    friendID = friend.first
                                                    friendName.value = friend.second
                                                },
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
                                                    friendRequestList.value = fetchFriendRequests(USER_ID)
                                                    print(friendRequestList)
                                                }},
                                            modifier = Modifier.padding(5.dp)
                                        ) {
                                            Text("Accept")
                                        }
                                        OutlinedButton(
                                            onClick = { runBlocking {
                                                denyFriendRequest(USER_ID, user.first)
                                                friendRequestList.value = fetchFriendRequests(USER_ID)
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
    viewProfilePageDialog(friendID, ifViewProfile, friendName, fullHeight, fullWidth)
}

@Composable
fun viewProfilePageDialog(friendID : Int, ifViewProfile : MutableState<Boolean>, friendName : MutableState<String>, fullHeight : Dp, fullWidth : Dp) {
    if (ifViewProfile.value) {
        Dialog(
            onDismissRequest = { ifViewProfile.value = false },
            properties = DialogProperties(dismissOnClickOutside = true, usePlatformDefaultWidth = false),
        ) {
            Box(){
                val isInit = remember { mutableStateOf(false) }
                var friendSections = remember { mutableStateListOf<SectionUnit>() }
                var coroutineScope = rememberCoroutineScope()
                coroutineScope.launch {
                    if (!isInit.value){
                        isLoading.value = true
                        friendSections.clear()
                        friendSections.addAll(fromSectionToSectionUnit(fetchFriendProfile(USER_ID, friendID)).toMutableStateList())
                        isLoading.value = false
                        isInit.value = true
                    }
                }
                DisposableEffect(isLoading){
                    onDispose {
                        isLoading.value = false
                    }
                }
                Card(
                    modifier = Modifier.width(fullWidth * 6 / 10),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Transparent,
                    ),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        modifier = Modifier.size(fullWidth * 6 / 10, fullHeight * 8 / 10).padding(top = 20.dp, bottom = 20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val name: String = if (friendName.value.length > 10) {
                            friendName.value.take(10) + "..."
                        } else {
                            friendName.value
                        }
                        Text("$name's Schedule", fontSize = 35.sp, color = androidx.compose.ui.graphics.Color.White,)
                        schedule(friendSections, modifier = Modifier.padding(top = 20.dp, bottom = 20.dp))
                    }
                }
                if (isLoading.value) {
                    LoadingFullScreen("loading...", modifier = Modifier.matchParentSize())
                }
            }
        }
    }
}