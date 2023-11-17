package pages.SchedulePage.PreferenceSelection

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import logic.preference.Preference
import org.burnoutcrew.reorderable.*
import pages.SchedulePage.PreferenceSelection.InputPreferences.NonPreferredTimeChip
import pages.SchedulePage.PreferenceSelection.InputPreferences.PreferredTimeChip
import pages.SchedulePage.PreferenceSelection.InputPreferences.MaxHoursPerDayChip
import style.md_theme_light_inversePrimary
import style.md_theme_light_primaryContainer

@Composable
fun preferenceSelectionSection(
    preferences: MutableList<Preference>,
    showCallBack: () -> Unit,
    changeCallBack: (from : Int , to : Int) -> Unit,
    deleteCallBack: (preference: Preference) -> Unit
) {
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        changeCallBack(from.index, to.index)
    })
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.width(180.dp).padding(end = 3.dp))
            Text("Preferences",fontSize = 14.sp,color = Color.Gray)
            Divider(modifier = Modifier.width(180.dp).padding(start = 3.dp))
        }
        ExtendedFloatingActionButton(
            onClick = { showCallBack() },
            icon = { Icon(Icons.Filled.Add, "Add Preferences") },
            text = { Text(text = "Add Preferences") },
            modifier = Modifier
                .size(width = 446.dp, height = 35.dp)
        )
        Text(
            text = "Drag and drop a selected preference to modify its weighting:",
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic
        )
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier.size(width = 446.dp, height = 450.dp).padding(bottom = 10.dp),
        ) {
            LazyColumn(
                state = state.listState,
                modifier = Modifier
                    .reorderable(state)
                    .detectReorder(state)
            ) {
                items(preferences, { it }) { preference ->
                    ReorderableItem(state, key = preference) { isDragging ->
                        val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                        ListItem(
                            modifier = Modifier
                                .shadow(elevation.value)
                                .size(width = 446.dp, height = 50.dp)
                                .clip(shape = RoundedCornerShape(0.dp)),
                            headlineContent = { Text(preference.toString()) },
                            trailingContent = { TextButton(
                                onClick = {deleteCallBack(preference)}
                            ){
                                Icon(Icons.Outlined.Close, "")
                            } },
                            tonalElevation = 30.dp,
                            colors = ListItemDefaults.colors(containerColor = md_theme_light_primaryContainer)
                        )
                        Divider(thickness = 1.dp, color = md_theme_light_inversePrimary)
                    }
                }
            }
        }
    }
}
@Composable
fun preferenceDialog(
    showAddPreference : MutableState<Boolean>,
    addCallBack: (String, List<String>) -> Unit
) {
    val preferenceDialogSection = listOf("Class Time Preference", "Course Preference", "Instructor Preference", "Friend Preference")
    if(showAddPreference.value) {
        Dialog(
            onDismissRequest = { showAddPreference.value = false },
            properties = DialogProperties(dismissOnClickOutside = true, usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .size(700.dp, 1000.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        . padding(20.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    for(sectionName in preferenceDialogSection) {
                        Text(sectionName,
                            fontSize = 30.sp,
                            modifier = Modifier
                                .padding(top = 20.dp)
                        )
                        if (sectionName == "Class Time Preference") {
                            Spacer(modifier = Modifier.height(5.dp))
                            MaxHoursPerDayChip(addCallBack)
                            Spacer(modifier = Modifier.height(5.dp))
                            PreferredTimeChip(addCallBack)
                            Spacer(modifier = Modifier.height(5.dp))
                            NonPreferredTimeChip(addCallBack)
                        }
                        else if (sectionName == "Course Preference") {
                            ////////FILLMEIN////////
                        }
                        else if (sectionName == "Instructor Preference") {
                            ////////FILLMEIN////////
                        }
                        else if (sectionName == "Friend Preference") {
                            ////////FILLMEIN////////
                        }
                    }
                    TextButton(
                        onClick = { showAddPreference.value = false },
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

