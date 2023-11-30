package common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
) {
    val isDark = remember { isDark }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Box(
                            contentAlignment = Alignment.CenterStart
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
                                Icon(
                                    Icons.Outlined.ArrowBack,
                                    contentDescription = "",
                                    tint = currentColorScheme.value.cs.primary
                                )
                            }

                        }
                        NavigationDrawerItem(
                            modifier = Modifier.padding(10.dp, 0.dp),
                            icon = { Icon(Icons.Outlined.Add, "") },
                            label = { Text(text = "Scheduler") },
                            selected = navController.currentScreen.value == Screen.SchedulePage.name,
                            onClick = {
                                navController.navigate(Screen.SchedulePage.name)
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
                    }
                    Column {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text("Theme")
                            Spacer(Modifier.width(5.dp))
                            // Theme button here
                            ThemeButton(MyColorTheme.GREEN, isDark)
                            Spacer(Modifier.width(5.dp))
                            ThemeButton(MyColorTheme.PURPLE, isDark)
                            Spacer(Modifier.width(5.dp))
                            ThemeButton(MyColorTheme.BLUE, isDark)
                            Spacer(Modifier.width(5.dp))
                            ThemeButton(MyColorTheme.ORANGE, isDark)
                        }
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Dark Mode")
                            Spacer(Modifier.width(5.dp))
                            Switch(
                                checked = isDark.value,
                                onCheckedChange = {
                                    isDark.value = it
                                    currentColorScheme.value = currentColorScheme.value.toggleTheme()
                                },
                                thumbContent =
                                if (isDark.value) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(SwitchDefaults.IconSize),
                                        )
                                    }
                                } else {
                                    null
                                }
                            )
                        }
                    }
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier.fillMaxHeight().size(width = 50.dp, height = 0.dp)
                        .background(color = currentColorScheme.value.cs.surfaceVariant),
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
                        Icon(
                            Icons.Outlined.Menu,
                            contentDescription = "",
                            tint = currentColorScheme.value.cs.primary
                        )
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