package components

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
import androidx.compose.ui.unit.dp

@Composable
fun preferenceSelectionSection(
    preferences: List<String>,
    showCallBack: () -> Unit
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
        modifier = Modifier.size(width = 446.dp, height = 400.dp),
        shape = RoundedCornerShape(0.dp)
    ) {
        LazyColumn() {
            items(preferences) { preference ->
                ListItem(
                    headlineContent = { Text(preference) },

                    )
                Divider()
            }
        }
    }
}
