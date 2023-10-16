package screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import navcontroller.NavController
import style.md_theme_light_primary
import style.md_theme_light_surfaceTint
import style.welcomeFontFamily
import style.md_theme_dark_onPrimaryContainer
import screens.schedulePage
import screens.welcomePage

@Composable
fun welcomePage(
    navController: NavController
) {
    // [START android_compose_layout_material_modal_drawer_programmatic]
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Outlined.AccountCircle, contentDescription = "", tint = md_theme_light_primary)
                    }

                }
                NavigationDrawerItem(
                    label = { Text(text = "Home Page") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                )
                NavigationDrawerItem(
                    label = { Text(text = "Schedule Page") },
                    selected = false,
                    onClick = { navController.navigate(Screen.SchedulePage.name) }
                )
                NavigationDrawerItem(
                    label = { Text(text = "My Schedule") },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                FloatingActionButton(
                    modifier = Modifier
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(0.dp),
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                ){
                    Column (
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = 20.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Icon(

                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "",
                            tint = md_theme_light_primary
                        )
                    }
                }
            }
        ) { contentPadding ->
            // Screen content
            WelcomePageContent(navController)
            // [START_EXCLUDE silent]
            // Box(modifier = Modifier.padding(contentPadding)) { /* ... */ }
            // [END_EXCLUDE]
        }
    }
    // [END android_compose_layout_material_modal_drawer_programmatic]
}


@Composable
fun WelcomePageContent(
    navController: NavController
){
    Row(){
        Column(
            modifier = Modifier.fillMaxSize(),
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
            )
        )
        Text("ω", fontSize = 90.sp)
        Text("UW Course Scheduler", fontSize = 30.sp)
    }
}
