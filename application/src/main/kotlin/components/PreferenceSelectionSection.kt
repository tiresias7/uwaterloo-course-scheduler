package components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import components.courseSelectionSection
import components.navDrawer
import components.scheduleSection
import data.SectionUnit
import kotlinx.serialization.json.*
import navcontroller.NavController

@Composable
fun preferenceSelectionSection(
    preferences : List<String>
) {
    ExtendedFloatingActionButton(
        onClick = {},
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
                FloatingActionButton(
                    onClick = {},
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(0.dp),
                ) {

                    Text(
                        text = preference,
                        textAlign = TextAlign.Start
                    )
                }
                Divider()
            }
        }
    }
}
