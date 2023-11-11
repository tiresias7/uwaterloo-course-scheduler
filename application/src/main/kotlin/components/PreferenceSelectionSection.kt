package components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import data.SelectedCourse
import org.burnoutcrew.reorderable.*

@Composable
fun preferenceSelectionSection(
    preferences: MutableList<String>,
    showCallBack: () -> Unit,
    changeCallBack: (from : Int , to : Int) -> Unit,
) {
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        changeCallBack(from.index, to.index)
    })
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ExtendedFloatingActionButton(
            onClick = { showCallBack() },
            icon = { Icon(Icons.Filled.Add, "Add Preferences") },
            text = { Text(text = "Add Preferences") },
            modifier = Modifier
                .size(width = 200.dp, height = 56.dp)
        )
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier.size(width = 446.dp, height = 450.dp),
            shape = RoundedCornerShape(0.dp)
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
                                .size(width = 446.dp, height = 50.dp),
                            headlineContent = { Text(preference) }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}
@Composable
fun preferenceDialog(
    showAddPreference : MutableState<Boolean>
) {
    val preferenceDialogSection = listOf("Time", "Courses", "Instructors", "Friends")
    if(showAddPreference.value) {
        Dialog(
            onDismissRequest = { showAddPreference.value = false },
            properties = DialogProperties(dismissOnClickOutside = true)
        ) {
            Card(
                modifier = Modifier
                    .size(600.dp, 800.dp)
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
                        Divider(thickness = 2.dp)
                        if (sectionName == "Time") {
                            ElevatedCard(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 6.dp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                            ) {
                                Text("Prefer classes after 10am",
                                        modifier = Modifier
                                        .padding(start = 20.dp, top = 5.dp)
                                )
                            }
                        }
                        else if (sectionName == "Courses") {
                            ElevatedCard(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 6.dp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                            ) {
                                Text("Prefer classes after 10am",
                                    modifier = Modifier
                                        .padding(start = 20.dp, top = 5.dp)
                                )
                            }
                        }
                        else if (sectionName == "Instructors") {
                            ElevatedCard(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 6.dp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                            ) {
                                Text("Prefer classes after 10am",
                                    modifier = Modifier
                                        .padding(start = 20.dp, top = 5.dp)
                                )
                            }
                        }
                        else if (sectionName == "Friends") {
                            ElevatedCard(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 6.dp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                            ) {
                                Text("Prefer classes after 10am",
                                    modifier = Modifier
                                        .padding(start = 20.dp, top = 5.dp)
                                )
                            }
                        }
                    }
                    TextButton(
                        onClick = { showAddPreference.value = false },
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

