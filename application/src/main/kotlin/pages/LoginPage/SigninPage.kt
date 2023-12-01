package pages.LoginPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.input.key.*
import androidx.compose.ui.zIndex
import cache.CourseNameLoader
import common.LoadingFullScreen
import common.SimpleTextField
import common.isLoading
import common.navcontroller.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import logic.ktorClient.signInExistingUsersByEmail
import logic.ktorClient.signUpNewUsers
import request.SignStatus

var USER_ID = 0
var USER_NAME = ""
var USER_EMAIL = ""

@Composable
fun loginPage(
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    var email by remember { mutableStateOf(TextFieldValue()) }
    var emailLabel by remember { mutableStateOf("Enter Email") }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var passwordLabel by remember { mutableStateOf("Enter Password") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val ifShowSignup = remember { mutableStateOf(false) }
    //val isLoading = remember { mutableStateOf(false) }
    Box(
    ) {
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
                    Text(
                        "Sign in",
                        fontSize = 40.sp,
                    )
                    Text(
                        "Your personal UWaterloo course scheduler",
                        fontSize = 15.sp,
                    )
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    OutlinedTextField(
                        singleLine = true,
                        value = email,
                        onValueChange = { newValue: TextFieldValue ->
                            email = newValue
                            emailLabel = "Enter Email"
                            isError = false
                        },
                        label = { Text(emailLabel, fontStyle = FontStyle.Italic) },
                        leadingIcon = {
                            Icon(Icons.Outlined.Person, "Email")
                        },
                        isError = isError,
                        modifier = Modifier.width(400.dp)
                    )
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    OutlinedTextField(
                        singleLine = true,
                        value = password,
                        onValueChange = { newValue: TextFieldValue ->
                            password = newValue
                            passwordLabel = "Enter Password"
                            isError = false
                        },
                        label = { Text(passwordLabel, fontStyle = FontStyle.Italic) },
                        leadingIcon = {
                            Icon(Icons.Outlined.Lock, "Password")
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                modifier = Modifier.width(50.dp).padding(end = 10.dp),
                                colors = IconButtonDefaults.iconButtonColors()
                            ) {
                                if (passwordVisible) {
                                    Text("Hide", fontSize = 14.sp)
                                } else {
                                    Text("Show", fontSize = 14.sp)
                                }
                            }
                        },
                        isError = isError,
                        modifier = Modifier.width(400.dp).onKeyEvent { event ->
                            if (event.key == Key.Enter && event.type == KeyEventType.KeyUp) {
                                if (email.text == "" || password.text == "") {
                                    isError = true
                                    if (email.text == "") {
                                        emailLabel = "Please enter email"
                                    }
                                    if (password.text == "") {
                                        passwordLabel = "Please enter password"
                                    }
                                } else {
                                    isLoading.value = true
                                    coroutineScope.launch(Dispatchers.IO) {
                                        val response = signInExistingUsersByEmail(email.text, password.text)
                                        val status = response.first
                                        when (status) {
                                            SignStatus.SIGN_IN_INVALID -> {
                                                isLoading.value = false
                                                isError = true
                                                emailLabel = "Email Not Found"
                                                email = TextFieldValue("")
                                                password = TextFieldValue("")
                                            }
                                            SignStatus.SIGN_IN_FAILED -> {
                                                isLoading.value = false
                                                isError = true
                                                passwordLabel = "Incorrect Password"
                                                password = TextFieldValue("")
                                            }
                                            else -> {
                                                USER_EMAIL = email.text
                                                USER_ID = response.second.first
                                                USER_NAME = response.second.second
                                                println(USER_EMAIL)
                                                println(USER_ID)
                                                println(USER_NAME)
//                                                email = TextFieldValue("")
//                                                password = TextFieldValue("")
                                                isError = false
                                                isLoading.value = false
                                                navController.navigate(Screen.WelcomePage.name)
                                            }
                                        }
                                    }
                                }
                            }
                            false
                        },
                    )
                    TextButton(
                        content = { Text("Forgot password?", fontSize = 15.sp) },
                        onClick = {}
                    )
                    Spacer(modifier = Modifier.padding(vertical = 15.dp))
                    Button(
                        onClick = {
                            if (email.text == "" || password.text == "") {
                                isError = true
                                if (email.text == "") {
                                    emailLabel = "Please enter email"
                                }
                                if (password.text == "") {
                                    passwordLabel = "Please enter password"
                                }
                            } else {
                                isLoading.value = true
                                coroutineScope.launch(Dispatchers.IO) {
                                    val response = signInExistingUsersByEmail(email.text, password.text)
                                    val status = response.first
                                    isLoading.value = false
                                    when (status) {
                                        SignStatus.SIGN_IN_INVALID -> {
                                            isError = true
                                            emailLabel = "Email Not Found"
                                            email = TextFieldValue("")
                                            password = TextFieldValue("")
                                        }
                                        SignStatus.SIGN_IN_FAILED -> {
                                            isError = true
                                            passwordLabel = "Incorrect Password"
                                            password = TextFieldValue("")
                                        }
                                        else -> {
                                            USER_EMAIL = email.text
                                            USER_ID = response.second.first
                                            USER_NAME = response.second.second
//                                            email = TextFieldValue("")
//                                            password = TextFieldValue("")
                                            isError = false
                                            navController.navigate(Screen.WelcomePage.name)
                                        }
                                    }
                                }
                            }
                        },
                        content = { Text(text = "Sign in") },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(400.dp, 50.dp)
                    )
                    TextButton(
                        content = { Text("New to ω? Join now", fontSize = 15.sp) },
                        onClick = {
                            email = TextFieldValue("")
                            password = TextFieldValue("")
                            isError = false
                            emailLabel = "Enter Email"
                            passwordLabel = "Enter Password"
                            ifShowSignup.value = true
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
        if (isLoading.value) {
            LoadingFullScreen("Signing in", modifier = Modifier.matchParentSize())
        }
    }

    signupDialog(ifShowSignup)
}

@Composable
fun signupDialog(
    ifShowSignup: MutableState<Boolean>
) {
    val coroutineScope = rememberCoroutineScope()
    var userName by remember { mutableStateOf(TextFieldValue()) }
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password1 by remember { mutableStateOf(TextFieldValue()) }
    var password2 by remember { mutableStateOf(TextFieldValue()) }
    var iconPassword by remember { mutableStateOf(Icons.Outlined.Lock) }
    var ifEmailAlreadyExists by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val ifNameInvalid = remember { mutableStateOf(false) }
    val ifEmailInvalid = remember { mutableStateOf(false) }
    val ifPasswordInvalid = remember { mutableStateOf(false) }
    val ifNotSame = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    //var secretPassword by remember { mutableStateOf(TextFieldValue()) }
    if (ifShowSignup.value) {
        Dialog(
            onDismissRequest = { ifShowSignup.value = false },
            properties = DialogProperties(dismissOnClickOutside = true, usePlatformDefaultWidth = false)
        ) {
            Box(){
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .size(width = 500.dp, height = 650.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Sign up",
                                fontSize = 40.sp,
                            )
                            TextButton(
                                content = { Text("Quit") },
                                onClick = {
                                    ifShowSignup.value = false
                                },
                                modifier = Modifier.size(60.dp, 50.dp)
                            )
                        }
                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
                        Text("Create Username (1-20 characters)", fontSize = 12.sp)
                        SimpleTextField(
                            singleLine = true,
                            value = userName.text,
                            onValueChange = { newValue: String ->
                                userName = TextFieldValue(newValue)
                                ifNameInvalid.value = !(newValue.matches(Regex("[a-zA-Z0-9]*")) && newValue.length <= 20 &&
                                        newValue.isNotEmpty())
                            },
                            leadingIcon = {
                                Icon(Icons.Outlined.Person, "Username")
                            },
                            isError = ifNameInvalid,
                            modifier = Modifier.size(500.dp, 50.dp)
                        )
                        Text(
                            "Please use numbers and letters only, no whitespace",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        Text("Enter your Email", fontSize = 12.sp)
                        SimpleTextField(
                            singleLine = true,
                            value = email.text,
                            onValueChange = { newValue: String ->
                                email = TextFieldValue(newValue)
                                ifEmailAlreadyExists = false
                                ifEmailInvalid.value =
                                    !(newValue.matches(Regex("[a-zA-Z0-9@.]*")) && newValue.length <= 50 &&
                                            newValue.isNotEmpty())
                            },
                            leadingIcon = {
                                Icon(Icons.Outlined.Email, "Email")
                            },
                            isError = ifEmailInvalid,
                            modifier = Modifier.size(500.dp, 50.dp)
                        )
                        if (ifEmailAlreadyExists) {
                            Text("Email Already Exists", fontSize = 12.sp, color = Color.Red)
                            Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        } else {
                            Spacer(modifier = Modifier.padding(vertical = 10.dp))
                        }
                        Text("Create Password (5-20 characters)", fontSize = 12.sp)
                        SimpleTextField(
                            singleLine = true,
                            value = password1.text,
                            onValueChange = { newValue: String ->
                                password1 = TextFieldValue(newValue)
                                ifPasswordInvalid.value =
                                    !(newValue.matches(Regex("[a-zA-Z0-9]*")) && newValue.length <= 20 &&
                                            newValue.length >= 5)
                            },
                            leadingIcon = {
                                Icon(Icons.Outlined.Lock, "Password")
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                IconButton(
                                    onClick = { passwordVisible = !passwordVisible },
                                    modifier = Modifier.width(50.dp).padding(end = 10.dp),
                                    colors = IconButtonDefaults.iconButtonColors()
                                ) {
                                    if (passwordVisible) {
                                        Text("Hide", fontSize = 14.sp)
                                    } else {
                                        Text("Show", fontSize = 14.sp)
                                    }
                                }
                            },
                            isError = ifPasswordInvalid,
                            modifier = Modifier.size(500.dp, 50.dp)
                        )
                        Text(
                            "Please use numbers and letters only, no whitespace",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        Text("Confirm Password", fontSize = 12.sp)
                        SimpleTextField(
                            singleLine = true,
                            //value = password2.text.map{'*'}.joinToString(separator = ""),
                            value = password2.text,
                            onValueChange = { newValue: String ->
                                password2 = TextFieldValue(newValue)
                                if (password2.text == password1.text && !ifPasswordInvalid.value) {
                                    ifNotSame.value = false
                                    iconPassword = Icons.Outlined.Check
                                } else {
                                    ifNotSame.value = true
                                    iconPassword = Icons.Outlined.Close
                                }
                                if (password2.text == "") {
                                    iconPassword = Icons.Outlined.Lock
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                IconButton(
                                    onClick = { passwordVisible = !passwordVisible },
                                    modifier = Modifier.width(50.dp).padding(end = 10.dp),
                                    colors = IconButtonDefaults.iconButtonColors()
                                ) {
                                    if (passwordVisible) {
                                        Text("Hide", fontSize = 14.sp)
                                    } else {
                                        Text("Show", fontSize = 14.sp)
                                    }
                                }
                            },
                            leadingIcon = {
                                Icon(iconPassword, "Password")
                            },
                            modifier = Modifier.size(500.dp, 50.dp),
                            isError = ifNotSame
                        )
                        Spacer(modifier = Modifier.padding(vertical = 20.dp))
                        Button(
                            onClick = {
                                if (userName.text == "") ifNameInvalid.value = true
                                if (email.text == "") ifEmailInvalid.value = true
                                if (password1.text == "") {
                                    ifPasswordInvalid.value = true
                                    ifNotSame.value = true
                                }
                                if (!ifNotSame.value && userName.text != "" && email.text != "" && password1.text != "") {
                                    isLoading.value = true
                                    coroutineScope.launch(Dispatchers.IO) {
                                        val status = signUpNewUsers(userName.text, password1.text, email.text).first
                                        isLoading.value = false
                                        if (status == SignStatus.SIGN_UP_FAILED) {
                                            ifEmailAlreadyExists = true
                                        } else {
                                            userName = TextFieldValue("")
                                            email = TextFieldValue("")
                                            password1 = TextFieldValue("")
                                            password2 = TextFieldValue("")
                                            ifShowSignup.value = false
                                        }
                                    }
                                }
                            },
                            content = { Text(text = "Sign up") },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(400.dp, 50.dp)
                        )
                    }
                }
                if (isLoading.value) {
                    LoadingFullScreen("Signing in", modifier = Modifier.matchParentSize())
                }
            }
        }
    }
}
