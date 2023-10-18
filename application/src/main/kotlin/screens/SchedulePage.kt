package screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
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
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 40.dp)
            .padding(top = 10.dp)
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        generateSection()
        scheduleSection()
    }
}
@Composable
fun generateSection() {
    Column(
        modifier = Modifier
            .fillMaxHeight(),
            //.padding(start = 80.dp),
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
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .size(width = 446.dp, height = 238.dp)
        ) {
            Text(
                text = "Selected preferences",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
        OutlinedButton(
            onClick = {},
            modifier = Modifier
                .size(width = 180.dp, height = 56.dp)
                .align(CenterHorizontally)
        ) {
            Text("Generate Schedule")
        }
    }
}

@Composable
fun scheduleSection() {
    var img_width by remember { mutableStateOf(0.dp) }
    var img_height by remember { mutableStateOf(0.dp) }
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.End
    ) {
        Card(
            modifier = Modifier.requiredSize(width = 800.dp, height = 800.dp),
            border = BorderStroke(1.dp,Color.Black),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Box(
                contentAlignment = TopStart
            ) {
                Image(
                    modifier = Modifier.fillMaxSize().onSizeChanged{
                        img_width = it.width.dp;
                        img_height = it.height.dp;
                    },
                    painter = painterResource("schedule_bg.svg"),
                    contentDescription = "schedule background"
                )
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .size(width = img_width / 7, height = img_width / 30 * 4).offset(x = img_width.div(6.3.toFloat()), y = img_height / 8),
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
                        .size(width = img_width / 7, height = img_width / 30 * 4).offset(x = img_width / 6.3.toFloat() * 2.1.toFloat(), y = img_height / 8),
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
