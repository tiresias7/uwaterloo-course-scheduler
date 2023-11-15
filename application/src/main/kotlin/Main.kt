import pages.FriendPage.friendPage
import pages.ProfilePage.profilePage
import pages.SchedulePage.schedulePage
import pages.WelcomePage.welcomePage
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import common.navcontroller.NavController
import common.navcontroller.NavigationHost
import common.navcontroller.composable
import common.navcontroller.rememberNavController
import pages.LoginPage.loginPage
import style.AppTheme

fun main() = application {
    val windowState = rememberWindowState(placement = WindowPlacement.Fullscreen)
    Window(
        state = windowState,
        title = "UW Course Scheduler",
        onCloseRequest = ::exitApplication
    ) {
        AppTheme { App() }
    }
}

@Composable
fun App(){
    val navController by rememberNavController(Screen.LoginPage.name)
    CustomNavigationHost(navController = navController)
}

/**
 * Screens
 */
enum class Screen(
) {
    WelcomePage,
    SchedulePage,
    LoginPage,
    ProfilePage,
    FriendPage
}


@Composable
fun CustomNavigationHost(
    navController: NavController
) {
    NavigationHost(navController) {

        composable(Screen.WelcomePage.name) {
            welcomePage(navController)
        }

        composable(Screen.SchedulePage.name) {
            schedulePage(navController)
        }

        composable(Screen.LoginPage.name) {
            loginPage(navController)
        }

        composable(Screen.ProfilePage.name) {
            profilePage(navController)
        }

        composable(Screen.FriendPage.name) {
            friendPage(navController)
        }

    }.build()
}
