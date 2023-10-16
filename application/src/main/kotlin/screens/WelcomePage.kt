package screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import navcontroller.NavController
import style.welcomeFontFamily


@Composable
fun WelcomePage(
    navController: NavController
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            welcomeMessage()
        }
        Spacer(modifier = Modifier.padding(vertical = 60.dp))
        ExtendedFloatingActionButton(
            onClick = {
                navController.navigate(Screen.SchedulePage.name)
            },
            icon = { Icon(Icons.Filled.Edit, "Begin Scheduling.") },
            text = { Text(text = "Begin Scheduling") },
        )
    }
}

@Composable
fun welcomeMessage() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Welcome to",
            style = TextStyle(
                fontSize = 80.sp,
                fontFamily = welcomeFontFamily
            )
        )
        Text("ω", fontSize = 90.sp)
        Text("UW Course Scheduler", fontSize = 30.sp)
    }
}