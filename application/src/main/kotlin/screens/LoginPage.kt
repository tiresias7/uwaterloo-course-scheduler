package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.navDrawer
import navcontroller.NavController
import style.welcomeFontFamily

val userName = mutableStateOf(TextFieldValue())
val password = mutableStateOf(TextFieldValue())
@Composable
fun loginPage(
    navController: NavController
) {
    navDrawer(navController, content = { loginPageContent(navController) })
}

@Composable
fun loginPageContent(
    navController: NavController
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedTextField(
            singleLine = true,
            value = userName.value,
            onValueChange = { newValue: TextFieldValue ->
                userName.value = newValue
            },
            label = { Text("Enter Username", fontStyle = FontStyle.Italic) },
            leadingIcon = {
                Icon(Icons.Outlined.Person, "Search Course")
            }
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        OutlinedTextField(
            singleLine = true,
            value = password.value,
            onValueChange = { newValue: TextFieldValue ->
                password.value = newValue
            },
            label = { Text("Enter Password", fontStyle = FontStyle.Italic) },
            leadingIcon = {
                Icon(Icons.Outlined.Lock, "Search Course")
            }
        )
        Spacer(modifier = Modifier.padding(vertical = 20.dp))
        ExtendedFloatingActionButton(
            onClick = {
                navController.navigate(Screen.WelcomePage.name)
            },
            icon = { Icon(Icons.Filled.Edit, "log in") },
            text = { Text(text = "Log in") },
        )
    }
}