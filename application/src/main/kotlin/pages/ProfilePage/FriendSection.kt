package pages.ProfilePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import friendSearchInputField
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

    Column {
        Text(text = "My Friends", fontSize = 30.sp)
        Divider()
        friendSearchInputField(allUser, {})

        LazyColumn(
            modifier = Modifier.height(600.dp).fillMaxWidth().padding(bottom = 10.dp),
        ) {
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
        }
    }
}