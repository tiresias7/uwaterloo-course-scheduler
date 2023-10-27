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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
        modifier = Modifier.size(width = 446.dp, height = 400.dp),
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
                        headlineContent = {Text(preference)}
                    )
                    Divider()
                }
            }
        }
    }
}
