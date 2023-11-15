package pages.ProfilePage

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
import common.navDrawer
import pages.SchedulePage.ScheduleSection.schedule
import data.SectionUnit
import common.navcontroller.NavController
@Composable
fun profilePage(
    navController: NavController
) {
    navDrawer(navController, content = { profilePageContent(navController) })
}

@Composable
fun profilePageContent(
    navController: NavController
) {
    val interactionSource = remember { MutableInteractionSource() }
    var userFirstLastName = remember { mutableStateOf("Pilly Pishop")}
    var userName = remember { mutableStateOf("PPLegend")}
    var userEmail = remember { mutableStateOf("pplegend@pp.com")}
    var savedSections = remember { mutableStateListOf<SectionUnit>() }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 40.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {}
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(20.dp)
                .width(400.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(text = userFirstLastName.value, fontSize = 45.sp,
                modifier = Modifier.padding(top = 30.dp))
            Divider()
            Text("Username: " + userName.value, fontSize = 15.sp,
                modifier = Modifier.padding(top = 20.dp))
            Text("Email: " + userEmail.value, fontSize = 15.sp,
                modifier = Modifier.padding(top = 10.dp))
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            OutlinedButton(
                content = {Text("Log out")},
                onClick = {navController.navigate(Screen.LoginPage.name)},
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        schedule(savedSections)
    }
}