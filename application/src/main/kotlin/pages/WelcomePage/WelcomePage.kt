package pages.WelcomePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cache.CourseNameLoader
import common.navDrawer
import common.navcontroller.NavController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import style.currentColorScheme
import style.welcomeFontFamily


@Composable
fun welcomePage(
    navController: NavController
) {
    navDrawer(navController, content = { welcomePageContent(navController) })
}

@Composable
fun welcomePageContent(
    navController: NavController
){
    GlobalScope.launch {
        CourseNameLoader.cacheAllCourseNames()
    }
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(currentColorScheme.value.cs.primaryContainer, currentColorScheme.value.cs.background), // Gradient colors
        //startY = 0f, // Starting Y position of the gradient
        //endY = 500f // Ending Y position of the gradient
    )
    Row(){
        Column(
            modifier = Modifier.fillMaxSize().background(brush = gradientBrush),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
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
}

@Composable
fun welcomeMessage() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to",
            style = TextStyle(
                fontSize = 80.sp,
                fontFamily = welcomeFontFamily
            ),
            color = currentColorScheme.value.cs.onPrimaryContainer
        )
        Text("ω", fontSize = 90.sp)
        Text("UW Course Scheduler", fontSize = 30.sp)
    }
}
