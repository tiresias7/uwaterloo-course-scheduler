import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch
import navcontroller.NavController
import navcontroller.NavigationHost
import navcontroller.composable
import navcontroller.rememberNavController
import screens.CourseSelectionPage
import screens.PreferenceSelectionPage
import screens.WelcomePage
import screens.SchedulePage
import style.AppTheme

fun main() = application {
    Window(
        title = "UW Course Scheduler",
        onCloseRequest = ::exitApplication
    ) {
        AppTheme { App() }
    }
}

@Composable
fun App(){
    val screens = Screen.values().toList()
    val navController by rememberNavController(Screen.WelcomePage.name)
    val currentScreen by remember {
        navController.currentScreen
    }
    CustomNavigationHost(navController = navController)
}

/**
 * Screens
 */
enum class Screen(
) {
    WelcomePage,
    SchedulePage,
    CourseSelectionPage,
    PreferenceSelectionPage
}


@Composable
fun CustomNavigationHost(
    navController: NavController
) {
    NavigationHost(navController) {
        composable(Screen.WelcomePage.name) {
            WelcomePage(navController)
        }

        composable(Screen.SchedulePage.name) {
            SchedulePage(navController)
        }

        composable(Screen.CourseSelectionPage.name) {
            CourseSelectionPage(navController)
        }

        composable(Screen.PreferenceSelectionPage.name) {
            PreferenceSelectionPage(navController)
        }
    }.build()
}