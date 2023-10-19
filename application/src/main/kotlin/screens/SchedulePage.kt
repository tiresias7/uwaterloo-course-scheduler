package screens

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.navDrawer
import navcontroller.NavController
import screens.schedulePage
import screens.welcomePage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp


@Composable
fun schedulePage(
    navController: NavController
) {
    navDrawer(navController, content = { schedulePageContent(navController) })
}

@Composable
fun schedulePageContent(
    navController: NavController
) {
    var clicked =  remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 40.dp)
            .padding(top = 10.dp)
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        selectSection(clicked)
        scheduleSection(clicked)
    }
}
@Composable
fun selectSection(
    clicked: MutableState<Boolean>
) {
    val preferences : List<String> = listOf("Time for classes", "Time for breaks", "Time of conflicts", "Location" , "Instructor", )
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        ExtendedFloatingActionButton(
            onClick = {},
            icon = { Icon(Icons.Filled.Add, "Add Courses") },
            text = { Text(text = "Add Courses") },
            modifier = Modifier
                .size(width = 180.dp, height = 56.dp)
        )
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .size(width = 446.dp, height = 238.dp)
        ) {
            Text(
                text = "Selected Courses",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
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
            modifier = Modifier.size(width = 446.dp, height = 238.dp),
            shape = RoundedCornerShape(0.dp)
        ) {
            LazyColumn(
                //modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
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
        OutlinedButton(
            onClick = { clicked.value = true},
            modifier = Modifier
                .size(width = 180.dp, height = 56.dp)
                .align(CenterHorizontally)
        ) {
            Text("Generate Schedule")
        }
    }
}

@Composable
fun scheduleSection(
    clicked: MutableState<Boolean>
) {
    var img_width by remember { mutableStateOf(0.dp) }
    var img_height by remember { mutableStateOf(0.dp) }
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.End
    ) {
        Card(
            modifier = Modifier.onSizeChanged{
                img_width = it.width.dp;
                img_height = it.height.dp;
            },
            border = BorderStroke(0.1.dp,Color.Black),
            colors = CardDefaults.cardColors(containerColor = White),
            shape = RoundedCornerShape((0.dp))
        ) {
            Box(
                contentAlignment = TopStart
            ) {
                Image(
                    painter = painterResource("schedule_bg.svg"),
                    contentDescription = "schedule background"
                )
                if (clicked.value){
                    Box(){
                        ElevatedCard(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            ),
                            modifier = Modifier
                                .size(width = img_width / 7, height = img_width / 30 * 4).offset(x = img_width / 6.3.toFloat() * 3.2.toFloat(), y = img_height / 2.25.toFloat()),
                        ) {
                            Text(
                                text = "CS346",
                                modifier = Modifier
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                            )
                        }
                        ElevatedCard(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            ),
                            modifier = Modifier
                                .size(width = img_width / 7, height = img_width / 30 * 4).offset(x = img_width / 6.3.toFloat() * 5.4.toFloat(), y = img_height / 2.25.toFloat()),
                        ) {
                            Text(
                                text = "CS346",
                                modifier = Modifier
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                            )
                        }
                        ElevatedCard(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            ),
                            modifier = Modifier
                                .size(width = img_width / 7, height = img_width / 30 * 4).offset(x = img_width / 6.3.toFloat() * 2.1.toFloat(), y = img_height / 5.6.toFloat()),
                        ) {
                            Text(
                                text = "CS350",
                                modifier = Modifier
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                            )
                        }
                        ElevatedCard(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            ),
                            modifier = Modifier
                                .size(width = img_width / 7, height = img_width / 30 * 4).offset(x = img_width / 6.3.toFloat() * 4.3.toFloat(), y = img_height / 5.6.toFloat()),
                        ) {
                            Text(
                                text = "CS350",
                                modifier = Modifier
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }


            }
        }
        Row() {
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .size(width = 40.dp, height = 40.dp)
            ) {
                Icon(
                    Icons.Outlined.Star,
                    "Save to my schedule"
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .size(width = 40.dp, height = 40.dp)
            ) {
                Icon(
                    Icons.Outlined.Person,
                    "Share to friends"
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .size(width = 40.dp, height = 40.dp)
            ) {
                Icon(
                    Icons.Outlined.Share,
                    "Export"
                )
            }
        }
    }
}
