package pages.ProfilePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import friendSearchInputField
import org.jetbrains.skia.Color
import style.md_theme_light_inversePrimary
import style.md_theme_light_primaryContainer


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

@Composable
fun friendSection() {
    val friendRequestList = remember { mutableStateListOf<String>("x-ray", "yellow", "zebra") }
    val friendsList =
        remember {
            mutableStateListOf<String>(
                "queen",
                "room",
                "snake",
                "tea",
                "umbrella",
                "vehicle",
                "watermelon",
                "banana",
                "canana",
                "danana",
                "panana",
                "ananana"
            )
        }
    var ifFriendList by remember { mutableStateOf(true)}
    var ifMessages by remember { mutableStateOf(false)}
    var ifAddFriends by remember { mutableStateOf(false)}

    Column {
        Text(text = "My Friends", fontSize = 30.sp)
        Row(
            modifier = Modifier.padding(top = 10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (ifFriendList) {
                OutlinedButton(
                    content = { Text("Friend List") },
                    onClick = {
                        ifFriendList = true
                        ifMessages = false
                        ifAddFriends = false
                    },
                    modifier = Modifier.size(130.dp, 40.dp)
                )
            } else {
                TextButton(
                    content = { Text("Friend List") },
                    onClick = {
                        ifFriendList = true
                        ifMessages = false
                        ifAddFriends = false
                    },
                    modifier = Modifier.size(130.dp, 40.dp)
                )
            }
            if (ifMessages) {
                OutlinedButton(
                    content = { Text("Messages") },
                    onClick = {
                        ifFriendList = false
                        ifMessages = true
                        ifAddFriends = false
                    },
                    modifier = Modifier.size(130.dp, 40.dp)
                )
            } else {
                TextButton(
                    content = { Text("Messages") },
                    onClick = {
                        ifFriendList = false
                        ifMessages = true
                        ifAddFriends = false
                    },
                    modifier = Modifier.size(130.dp, 40.dp)
                )
            }
            if (ifAddFriends) {
                OutlinedButton(
                    content = { Text("Add Friends") },
                    onClick = {
                        ifFriendList = false
                        ifMessages = false
                        ifAddFriends = true
                    },
                    modifier = Modifier.size(140.dp, 40.dp)
                )
            } else {
                TextButton(
                    content = { Text("Add Friends") },
                    onClick = {
                        ifFriendList = false
                        ifMessages = false
                        ifAddFriends = true
                    },
                    modifier = Modifier.size(140.dp, 40.dp)
                )
            }
        }
        Box(
            modifier = Modifier.height(600.dp).fillMaxWidth().padding(top = 10.dp),
        ) {
            if (ifFriendList) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier,
                ) {
                    LazyColumn(
                        modifier = Modifier,
                    ) {
                        items(friendsList, { it }) { friend ->
                            ListItem(
                                modifier = Modifier
                                    .height(70.dp).fillMaxWidth(),
                                headlineContent = { Text(friend) },
                                tonalElevation = 30.dp,
                                colors = ListItemDefaults.colors(containerColor = md_theme_light_primaryContainer)
                            )
                            Divider(thickness = 1.dp, color = md_theme_light_inversePrimary)
                        }
                    }
                }
            } else if (ifMessages) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier,
                ) {
                    LazyColumn(
                        modifier = Modifier,
                    ) {
                        items(friendRequestList, { it }) { user ->
                            ListItem(
                                modifier = Modifier
                                    .height(70.dp).fillMaxWidth(),
                                headlineContent = { Text(user) },
                                trailingContent = {
                                    Row(

                                    ) {
                                        OutlinedButton(
                                            onClick = {},
                                            modifier = Modifier.padding(5.dp)
                                        ) {
                                            Text("Accept")
                                        }
                                        OutlinedButton(
                                            onClick = {},
                                            modifier = Modifier.padding(5.dp),
                                        ) {
                                            Text("Decline")
                                        }
                                    }
                                },
                                tonalElevation = 30.dp,
                                colors = ListItemDefaults.colors(containerColor = md_theme_light_primaryContainer)
                            )
                            Divider(thickness = 1.dp, color = md_theme_light_inversePrimary)
                        }
                    }
                }
            } else if (ifAddFriends) {
                friendSearchInputField(allUser, {})
            }
            /*{
            // display requests
            items(1) {
                Text(
                    "Pending Invite",
                    modifier = Modifier.background(color = md_theme_light_primaryContainer).fillMaxWidth()
                )
                Divider(thickness = 1.dp, color = md_theme_light_inversePrimary)
            }
            items(friendRequestList, { it }) { user ->
                ListItem(
                    modifier = Modifier
                        .height(70.dp).fillMaxWidth(),
                    headlineContent = { Text(user) },
                    trailingContent = {
                        Row(

                        ) {
                            FilledTonalButton(
                                onClick = {},
                                modifier = Modifier.padding(5.dp)
                            ) {
                                Text("Accept")
                            }
                            FilledTonalButton(
                                onClick = {},
                                modifier = Modifier.padding(5.dp)
                            ) {
                                Text("Decline")
                            }
                        }
                    },
                    tonalElevation = 30.dp,
                    colors = ListItemDefaults.colors(containerColor = md_theme_light_primaryContainer)
                )
                Divider(thickness = 1.dp, color = md_theme_light_inversePrimary)
            }


            // display friends
            items(1) {
                Text("Friends", modifier = Modifier.background(color = md_theme_light_primaryContainer).fillMaxWidth())
                Divider(thickness = 1.dp, color = md_theme_light_inversePrimary)
            }
            items(friendsList, { it }) { friend ->
                ListItem(
                    modifier = Modifier
                        .height(70.dp).fillMaxWidth(),
                    headlineContent = { Text(friend) },
                    tonalElevation = 30.dp,
                    colors = ListItemDefaults.colors(containerColor = md_theme_light_primaryContainer)
                )
                Divider(thickness = 1.dp, color = md_theme_light_inversePrimary)
            }
        }*/
        }
    }
}