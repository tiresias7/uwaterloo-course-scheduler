package pages.LoginPage

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.navcontroller.NavController

@Composable
fun loginPage(
    navController: NavController
){
    var userName by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 20.dp, start = 50.dp),
        ) {
            Text(
                "ω",
                fontSize = 50.sp,
            )
        }
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .size(width = 400.dp, height = 480.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize()
            ) {
                Text("Sign in",
                    fontSize = 40.sp,
                )
                Text("Your personal UWaterloo course scheduler",
                    fontSize = 15.sp,
                )
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                OutlinedTextField(
                    singleLine = true,
                    value = userName,
                    onValueChange = { newValue: TextFieldValue ->
                        userName = newValue
                    },
                    label = { Text("Enter Username", fontStyle = FontStyle.Italic) },
                    leadingIcon = {
                        Icon(Icons.Outlined.Person, "Search Course")
                    },
                    modifier = Modifier.width(400.dp)
                )
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                OutlinedTextField(
                    singleLine = true,
                    value = password,
                    onValueChange = { newValue: TextFieldValue ->
                        password = newValue
                    },
                    label = { Text("Enter Password", fontStyle = FontStyle.Italic) },
                    leadingIcon = {
                        Icon(Icons.Outlined.Lock, "Search Course")
                    },
                    modifier = Modifier.width(400.dp)
                )
                TextButton(
                    content = {Text("Forgot password?", fontSize = 15.sp)},
                    onClick = {}
                )
                Spacer(modifier = Modifier.padding(vertical = 15.dp))
                Button(
                    onClick = { navController.navigate(Screen.WelcomePage.name) },
                    content = { Text(text = "Sign in") },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(400.dp, 50.dp)
                )
                TextButton(
                    content = {Text("New to ω? Join now", fontSize = 15.sp)},
                    onClick = {
                        userName = TextFieldValue("")
                        password = TextFieldValue("")
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(200.dp, 50.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("UWCourseScheduler@2023")
            Text("    Maybe something here")
            Text("    Maybe something here")
            Text("    Maybe something here")
            Text("    Maybe something here")
        }
    }
}