package pages.ProfilePage

import Section
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import common.navDrawer
import pages.SchedulePage.ScheduleSection.schedule
import SectionUnit
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import common.LoadingFullScreen
import common.isLoading
import common.navcontroller.NavController
import kotlinx.coroutines.launch
import logic.ktorClient.fetchFriendProfile
import pages.LoginPage.USER_EMAIL
import pages.LoginPage.USER_ID
import pages.LoginPage.USER_NAME

@Composable
fun profilePage(
    navController: NavController
) {
    navDrawer(navController, content = { profilePageContent(navController) })
}

@Composable
fun profilePageContent(
    navController: NavController
) {
    val interactionSource = remember { MutableInteractionSource() }
    var savedSections = remember { mutableStateListOf<SectionUnit>() }
    val isInit = remember { mutableStateOf(false) }
    val localDensity = LocalDensity.current
    var fullHeight by remember { mutableStateOf(0.dp) }
    var fullWidth by remember { mutableStateOf(0.dp) }
    var coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        if (!isInit.value){
            isLoading.value = true
            savedSections.clear()
            savedSections.addAll(fromSectionToSectionUnit(fetchFriendProfile(USER_ID, USER_ID)).toMutableStateList())
            isLoading.value = false
            isInit.value = true
        }
    }
    DisposableEffect(isLoading){
        onDispose {
            isLoading.value = false
        }
    }
    Box(){
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(start = 40.dp)
                .onGloballyPositioned { coordinates ->
                    fullHeight = with(localDensity) { coordinates.size.height.toDp() }
                    fullWidth = with(localDensity) { coordinates.size.width.toDp() }
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().requiredHeight(120.dp)
                    .padding(start = 90.dp, top = 50.dp)
                    .weight(0.4f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Row(
                        modifier = Modifier.padding(start = 10.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val name : String
                        val email : String
                        if (USER_NAME.length > 10) { name = USER_NAME.take(10) + "..."}
                        else { name = USER_NAME }
                        if (USER_EMAIL.length > 30) { email = USER_NAME.take(30) + "..."}
                        else { email = USER_EMAIL }
                        Text(
                            text = "My profile", fontSize = 45.sp,
                            modifier = Modifier.padding(end = 50.dp)
                        )
                        Text(
                            "UID: " + USER_ID, fontSize = 15.sp,
                            modifier = Modifier.padding(end = 20.dp)
                        )
                        Text(
                            "Email: " + email, fontSize = 15.sp,
                            modifier = Modifier.padding(end = 50.dp)
                        )
                    }
                    OutlinedButton(
                        content = { Text("Log out") },
                        onClick = { navController.navigate(Screen.LoginPage.name) },
                        modifier = Modifier.size(100.dp, 40.dp).align(Alignment.CenterVertically)
                    )
                }
                Divider(modifier = Modifier.padding(end = 100.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(5f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {}
                    ),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier
                        .width(400.dp)
                        .padding(bottom = 10.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    ///////// friend ///////////////
                    friendSection(USER_ID, fullHeight, fullWidth)
                    ///////////////////////////////
                }
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.weight(0.4f))
                    schedule(savedSections, modifier = Modifier.weight(5f))
                    Row(
                        modifier = Modifier.weight(0.6f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top,
                    ) {
                        /*Text(
                            text = USER_NAME + "'s Schedule", fontSize = 35.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )*/
                    }

                }
            }
        }
        if (isLoading.value) {
            LoadingFullScreen("loading...", modifier = Modifier.matchParentSize())
        }
    }
}

fun fromSectionToSectionUnit(sections : List<Section>) : MutableList<SectionUnit> {
    var sectionUnits = mutableListOf<SectionUnit>()
    for (section in sections) {
        sectionUnits.addAll(section.toSectionUnit())
    }
    return sectionUnits
}