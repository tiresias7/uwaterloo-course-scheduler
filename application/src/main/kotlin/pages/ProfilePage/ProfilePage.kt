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
import pages.LoginPage.USER_EMAIL
import pages.LoginPage.USER_ID
import pages.LoginPage.USER_NAME

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
            Text(text = USER_NAME, fontSize = 45.sp,
                modifier = Modifier.padding(top = 30.dp))
            Divider()
            Text("UID: " + USER_ID, fontSize = 15.sp,
                modifier = Modifier.padding(top = 20.dp))
            Text("Email: " + USER_EMAIL, fontSize = 15.sp,
                modifier = Modifier.padding(top = 10.dp))
            Spacer(modifier = Modifier.padding(vertical = 20.dp))

            ///////// friend ///////////////
            friendSection()
            ///////////////////////////////
            OutlinedButton(
                content = {Text("Log out")},
                onClick = {navController.navigate(Screen.LoginPage.name)},
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(0.4f))
            schedule(savedSections, modifier = Modifier.weight(5f))
            Row(
                modifier = Modifier.weight(0.6f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top,
            ) {
                Text(text = USER_NAME + "'s Schedule", fontSize = 35.sp,
                    modifier = Modifier.padding(top = 10.dp))
            }

        }

    }
}