package common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import common.navcontroller.NavController
import style.*

@Composable
fun navDrawer(
    navController: NavController,
    content: @Composable () -> Unit
){
    // [START android_compose_layout_material_modal_drawer_programmatic]
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    contentAlignment = Alignment.CenterStart
                ){
                    TextButton(
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "", tint =  md_theme_light_primary)
                    }

                }
                NavigationDrawerItem(
                    modifier = Modifier.padding(10.dp, 0.dp),
                    icon = { Icon(Icons.Outlined.Add, "") },
                    label = { Text(text = "Scheduler") },
                    selected = navController.currentScreen.value == Screen.WelcomePage.name,
                    onClick = {
                        navController.navigate(Screen.WelcomePage.name)
                        scope.launch {
                            drawerState.apply { close() }
                        }
                    }
                )
                NavigationDrawerItem(
                    modifier = Modifier.padding(10.dp, 0.dp),
                    icon = { Icon(Icons.Outlined.Home, "") },
                    label = { Text(text = "My Profile") },
                    //badge = { Text(text = "0") },
                    selected = navController.currentScreen.value == Screen.ProfilePage.name,
                    onClick = {
                        scope.launch {
                            drawerState.apply { close() }
                        }
                        navController.navigate(Screen.ProfilePage.name)
                    }
                )
                NavigationDrawerItem(
                    modifier = Modifier.padding(10.dp, 0.dp),
                    icon = { Icon(Icons.Outlined.Person, "") },
                    label = { Text(text = "My Friends") },
                    //badge = { Text(text = "0") },
                    selected = navController.currentScreen.value == Screen.FriendPage.name,
                    onClick = {
                        scope.launch {
                            drawerState.apply { close() }
                        }
                        navController.navigate(Screen.FriendPage.name)
                    }
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier.fillMaxHeight().size(width = 50.dp, height = 0.dp).background(color = md_theme_light_surfaceVariant),
                ){
                    TextButton(
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Outlined.AccountCircle, contentDescription = "", tint =  md_theme_light_primary)
                    }
                }
            }
        ) { contentPadding ->
            // Screen content
            content()
            // [START_EXCLUDE silent]
            Box(modifier = Modifier.padding(contentPadding)) { /* ... */ }
            // [END_EXCLUDE]
        }
    }
}