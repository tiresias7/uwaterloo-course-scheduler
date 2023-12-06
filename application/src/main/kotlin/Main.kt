import pages.ProfilePage.profilePage
import pages.SchedulePage.schedulePage
import pages.WelcomePage.welcomePage
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
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
import style.isDark
import java.awt.Dimension
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

val homedir = System.getProperty("user.home")
val outputDir = Paths.get(homedir, "UWCourseScheduler")
val outputFile = Paths.get(outputDir.toString(), "local.properties").toString()


fun main() = application {
    val savedPreferences = readFromFile()
    val windowState = savedPreferences.second ?: rememberWindowState(placement = WindowPlacement.Fullscreen)
    if (savedPreferences.first != null){
        currentColorScheme.value = savedPreferences.first!!
        isDark.value = savedPreferences.first!!.name.contains("dark", ignoreCase = true)
    }

    Window(
        state = windowState,
        title = "UW Course Scheduler",
        onCloseRequest = {
            onCloseSave(windowState, currentColorScheme.value)
            exitApplication()
        },
    ) {
        //val density = LocalDensity.current.density
        window.minimumSize = Dimension((1152).toInt(), (786).toInt())

        AppTheme { App() }
    }
}

fun readFromFile(): Pair<MyColorTheme?, WindowState?> {
    val properties = Properties()
    var cs: MyColorTheme?

    try {
        FileInputStream(outputFile).use { inputStream ->
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

//    properties.setProperty("window.x", position.x.value.toString())
//    properties.setProperty("window.y", position.y.value.toString())
//    properties.setProperty("window.width", size.width.value.toString())
//    properties.setProperty("window.height", size.height.value.toString())
    properties.setProperty("theme", cs.name)

    try {
        Files.createDirectories(outputDir)
        FileOutputStream(outputFile).use { outputStream ->
            properties.store(outputStream, "Saved preference")
        }
        print(outputFile)
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
