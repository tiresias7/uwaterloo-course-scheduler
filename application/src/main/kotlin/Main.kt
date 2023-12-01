import pages.ProfilePage.profilePage
import pages.SchedulePage.schedulePage
import pages.WelcomePage.welcomePage
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import common.navcontroller.NavController
import common.navcontroller.NavigationHost
import common.navcontroller.composable
import common.navcontroller.rememberNavController
import pages.LoginPage.loginPage
import style.AppTheme
import style.MyColorTheme
import style.currentColorScheme
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

fun main() = application {
    val savedPreferences = readFromFile()
    val windowState = savedPreferences.second ?: rememberWindowState(placement = WindowPlacement.Fullscreen)
    if (savedPreferences.first != null){
        currentColorScheme.value = savedPreferences.first!!
    }

    Window(
        state = windowState,
        title = "UW Course Scheduler",
        onCloseRequest = {
            onCloseSave(windowState, currentColorScheme.value)
            exitApplication()
        }
    ) {
        AppTheme { App() }
    }
}

fun readFromFile(): Pair<MyColorTheme?, WindowState?> {
    val properties = Properties()
    var cs: MyColorTheme?

    try {
        FileInputStream("local.properties").use { inputStream ->
            properties.load(inputStream)
            try {
                cs = MyColorTheme.valueOf(properties.getProperty("theme") ?: "null")
            } catch (e: IllegalArgumentException) {
                cs = null
            }

            val x = properties.getProperty("window.x")?.toFloatOrNull() ?: return Pair(cs, null)
            val y = properties.getProperty("window.y")?.toFloatOrNull() ?: return Pair(cs, null)
            val width = properties.getProperty("window.width")?.toFloatOrNull() ?: return Pair(cs, null)
            val height = properties.getProperty("window.height")?.toFloatOrNull() ?: return Pair(cs, null)

            return Pair(
                cs, WindowState(
                    position = WindowPosition(x.dp, y.dp),
                    size = DpSize(width.dp, height.dp)
                )
            )
        }
    } catch (e: Exception) {
        // Handle file not found or invalid properties
        return Pair(null, null)
    }
}

fun onCloseSave(
    windowState: WindowState,
    cs: MyColorTheme
) {
    val properties = Properties()
    val position = windowState.position
    val size = windowState.size

    properties.setProperty("window.x", position.x.value.toString())
    properties.setProperty("window.y", position.y.value.toString())
    properties.setProperty("window.width", size.width.value.toString())
    properties.setProperty("window.height", size.height.value.toString())
    properties.setProperty("theme", cs.name)

    try {
        FileOutputStream("local.properties").use { outputStream ->
            properties.store(outputStream, "Saved preference")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun App() {
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

    }.build()
}
