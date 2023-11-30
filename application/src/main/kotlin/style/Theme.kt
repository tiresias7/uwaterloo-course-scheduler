package style

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle

private val LightColorSchemePurple = lightColorScheme(
    primary = md_theme_light_primarypurple,
    onPrimary = md_theme_light_onPrimarypurple,
    primaryContainer = md_theme_light_primaryContainerpurple,
    onPrimaryContainer = md_theme_light_onPrimaryContainerpurple,
    secondary = md_theme_light_secondarypurple,
    onSecondary = md_theme_light_onSecondarypurple,
    secondaryContainer = md_theme_light_secondaryContainerpurple,
    onSecondaryContainer = md_theme_light_onSecondaryContainerpurple,
    tertiary = md_theme_light_tertiarypurple,
    onTertiary = md_theme_light_onTertiarypurple,
    tertiaryContainer = md_theme_light_tertiaryContainerpurple,
    onTertiaryContainer = md_theme_light_onTertiaryContainerpurple,
    error = md_theme_light_errorpurple,
    onError = md_theme_light_onErrorpurple,
    errorContainer = md_theme_light_errorContainerpurple,
    onErrorContainer = md_theme_light_onErrorContainerpurple,
    outline = md_theme_light_outlinepurple,
    background = md_theme_light_backgroundpurple,
    onBackground = md_theme_light_onBackgroundpurple,
    surface = md_theme_light_surfacepurple,
    onSurface = md_theme_light_onSurfacepurple,
    surfaceVariant = md_theme_light_surfaceVariantpurple,
    onSurfaceVariant = md_theme_light_onSurfaceVariantpurple,
    inverseSurface = md_theme_light_inverseSurfacepurple,
    inverseOnSurface = md_theme_light_inverseOnSurfacepurple,
    inversePrimary = md_theme_light_inversePrimarypurple,
//    surfaceTint = md_theme_light_surfaceTintpurple,
//    outlineVariant = md_theme_light_outlineVariantpurple,
//    scrim = md_theme_light_scrimpurple,
)


private val DarkColorSchemePurple = darkColorScheme(
    primary = md_theme_dark_primarypurple,
    onPrimary = md_theme_dark_onPrimarypurple,
    primaryContainer = md_theme_dark_primaryContainerpurple,
    onPrimaryContainer = md_theme_dark_onPrimaryContainerpurple,
    secondary = md_theme_dark_secondarypurple,
    onSecondary = md_theme_dark_onSecondarypurple,
    secondaryContainer = md_theme_dark_secondaryContainerpurple,
    onSecondaryContainer = md_theme_dark_onSecondaryContainerpurple,
    tertiary = md_theme_dark_tertiarypurple,
    onTertiary = md_theme_dark_onTertiarypurple,
    tertiaryContainer = md_theme_dark_tertiaryContainerpurple,
    onTertiaryContainer = md_theme_dark_onTertiaryContainerpurple,
    error = md_theme_dark_errorpurple,
    onError = md_theme_dark_onErrorpurple,
    errorContainer = md_theme_dark_errorContainerpurple,
    onErrorContainer = md_theme_dark_onErrorContainerpurple,
    outline = md_theme_dark_outlinepurple,
    background = md_theme_dark_backgroundpurple,
    onBackground = md_theme_dark_onBackgroundpurple,
    surface = md_theme_dark_surfacepurple,
    onSurface = md_theme_dark_onSurfacepurple,
    surfaceVariant = md_theme_dark_surfaceVariantpurple,
    onSurfaceVariant = md_theme_dark_onSurfaceVariantpurple,
    inverseSurface = md_theme_dark_inverseSurfacepurple,
    inverseOnSurface = md_theme_dark_inverseOnSurfacepurple,
    inversePrimary = md_theme_dark_inversePrimarypurple,
//    surfaceTint = md_theme_dark_surfaceTintpurple,
//    outlineVariant = md_theme_dark_outlineVariantpurple,
//    scrim = md_theme_dark_scrimpurple,
)

private val LightColorSchemeGreen = lightColorScheme(
    primary = md_theme_light_primarygreen,
    onPrimary = md_theme_light_onPrimarygreen,
    primaryContainer = md_theme_light_primaryContainergreen,
    onPrimaryContainer = md_theme_light_onPrimaryContainergreen,
    secondary = md_theme_light_secondarygreen,
    onSecondary = md_theme_light_onSecondarygreen,
    secondaryContainer = md_theme_light_secondaryContainergreen,
    onSecondaryContainer = md_theme_light_onSecondaryContainergreen,
    tertiary = md_theme_light_tertiarygreen,
    onTertiary = md_theme_light_onTertiarygreen,
    tertiaryContainer = md_theme_light_tertiaryContainergreen,
    onTertiaryContainer = md_theme_light_onTertiaryContainergreen,
    error = md_theme_light_errorgreen,
    errorContainer = md_theme_light_errorContainergreen,
    onError = md_theme_light_onErrorgreen,
    onErrorContainer = md_theme_light_onErrorContainergreen,
    background = md_theme_light_backgroundgreen,
    onBackground = md_theme_light_onBackgroundgreen,
    outline = md_theme_light_outlinegreen,
    inverseOnSurface = md_theme_light_inverseOnSurfacegreen,
    inverseSurface = md_theme_light_inverseSurfacegreen,
    inversePrimary = md_theme_light_inversePrimarygreen,
    surfaceTint = md_theme_light_surfaceTintgreen,
    outlineVariant = md_theme_light_outlineVariantgreen,
    scrim = md_theme_light_scrimgreen,
    surface = md_theme_light_surfacegreen,
    onSurface = md_theme_light_onSurfacegreen,
    surfaceVariant = md_theme_light_surfaceVariantgreen,
    onSurfaceVariant = md_theme_light_onSurfaceVariantgreen,
)


private val DarkColorSchemeGreen = darkColorScheme(
    primary = md_theme_dark_primarygreen,
    onPrimary = md_theme_dark_onPrimarygreen,
    primaryContainer = md_theme_dark_primaryContainergreen,
    onPrimaryContainer = md_theme_dark_onPrimaryContainergreen,
    secondary = md_theme_dark_secondarygreen,
    onSecondary = md_theme_dark_onSecondarygreen,
    secondaryContainer = md_theme_dark_secondaryContainergreen,
    onSecondaryContainer = md_theme_dark_onSecondaryContainergreen,
    tertiary = md_theme_dark_tertiarygreen,
    onTertiary = md_theme_dark_onTertiarygreen,
    tertiaryContainer = md_theme_dark_tertiaryContainergreen,
    onTertiaryContainer = md_theme_dark_onTertiaryContainergreen,
    error = md_theme_dark_errorgreen,
    errorContainer = md_theme_dark_errorContainergreen,
    onError = md_theme_dark_onErrorgreen,
    onErrorContainer = md_theme_dark_onErrorContainergreen,
    background = md_theme_dark_backgroundgreen,
    onBackground = md_theme_dark_onBackgroundgreen,
    outline = md_theme_dark_outlinegreen,
    inverseOnSurface = md_theme_dark_inverseOnSurfacegreen,
    inverseSurface = md_theme_dark_inverseSurfacegreen,
    inversePrimary = md_theme_dark_inversePrimarygreen,
    surfaceTint = md_theme_dark_surfaceTintgreen,
    outlineVariant = md_theme_dark_outlineVariantgreen,
    scrim = md_theme_dark_scrimgreen,
    surface = md_theme_dark_surfacegreen,
    onSurface = md_theme_dark_onSurfacegreen,
    surfaceVariant = md_theme_dark_surfaceVariantgreen,
    onSurfaceVariant = md_theme_dark_onSurfaceVariantgreen,
)


val welcomeFontFamily = FontFamily(
    Font(
        resource = "Parisienne-Regular.ttf",
        weight = FontWeight.W400,
        style = FontStyle.Normal
    )
)

enum class MyColorTheme(val cs: ColorScheme) {
    GREEN(LightColorSchemeGreen),
    PURPLE(LightColorSchemePurple),
    GREEN_DARK(DarkColorSchemeGreen),
    PURPLE_DARK(DarkColorSchemePurple);

    fun toggleTheme(): MyColorTheme {
        return when (this) {
            GREEN -> GREEN_DARK
            PURPLE -> PURPLE_DARK
            GREEN_DARK -> GREEN
            PURPLE_DARK -> PURPLE
        }
    }
}

val isDark = mutableStateOf<Boolean>(false)
val currentColorScheme = mutableStateOf<MyColorTheme>(MyColorTheme.GREEN)

@Composable
fun AppTheme(
  content: @Composable() () -> Unit
) {
  val colorScheme = remember { currentColorScheme }

  MaterialTheme(
      colorScheme = colorScheme.value.cs,
      typography = Typography(),
      content = content
  )
}
