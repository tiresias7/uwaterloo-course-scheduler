package screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.navDrawer
import navcontroller.NavController
import screens.schedulePage
import screens.welcomePage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White


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
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.End
    ) {
        Card(
            modifier = Modifier.size(width = 700.dp, height = 700.dp),
            border = BorderStroke(1.dp,Color.Black),
            //colors = CardDefaults.cardColors(containerColor = White)
        ) {
        }
        Row() {
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .size(width = 40.dp, height = 40.dp)
            ) {
                Icon(
                    Icons.Filled.Star,
                    "Add Preferences"
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .size(width = 40.dp, height = 40.dp)
            ) {
                Icon(
                    Icons.Filled.Person,
                    "Add Preferences"
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .size(width = 40.dp, height = 40.dp)
            ) {
                Icon(
                    Icons.Filled.Share,
                    "Add Preferences"
                )
            }
        }
    }
}
