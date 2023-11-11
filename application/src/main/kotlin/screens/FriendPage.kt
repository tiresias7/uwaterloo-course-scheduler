package screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.navDrawer
import components.preferenceDialog
import components.schedule
import components.scheduleSection
import data.SectionUnit
import data.SelectedCourse
import logic.schedulealgo.testAlgo
import navcontroller.NavController
@Composable
fun friendPage(
    navController: NavController
) {
    navDrawer(navController, content = { friendPageContent(navController) })
}

@Composable
fun friendPageContent(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(60.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Text("My Friend Lists", fontSize = 40.sp)
        Divider()
    }
}