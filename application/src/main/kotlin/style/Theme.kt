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

private val LightColorSchemeBlue = lightColorScheme(
    primary = md_theme_light_primaryblue,
    onPrimary = md_theme_light_onPrimaryblue,
    primaryContainer = md_theme_light_primaryContainerblue,
    onPrimaryContainer = md_theme_light_onPrimaryContainerblue,
    secondary = md_theme_light_secondaryblue,
    onSecondary = md_theme_light_onSecondaryblue,
    secondaryContainer = md_theme_light_secondaryContainerblue,
    onSecondaryContainer = md_theme_light_onSecondaryContainerblue,
    tertiary = md_theme_light_tertiaryblue,
    onTertiary = md_theme_light_onTertiaryblue,
    tertiaryContainer = md_theme_light_tertiaryContainerblue,
    onTertiaryContainer = md_theme_light_onTertiaryContainerblue,
    error = md_theme_light_errorblue,
    errorContainer = md_theme_light_errorContainerblue,
    onError = md_theme_light_onErrorblue,
    onErrorContainer = md_theme_light_onErrorContainerblue,
    background = md_theme_light_backgroundblue,
    onBackground = md_theme_light_onBackgroundblue,
    outline = md_theme_light_outlineblue,
    inverseOnSurface = md_theme_light_inverseOnSurfaceblue,
    inverseSurface = md_theme_light_inverseSurfaceblue,
    inversePrimary = md_theme_light_inversePrimaryblue,
    surfaceTint = md_theme_light_surfaceTintblue,
    outlineVariant = md_theme_light_outlineVariantblue,
    scrim = md_theme_light_scrimblue,
    surface = md_theme_light_surfaceblue,
    onSurface = md_theme_light_onSurfaceblue,
    surfaceVariant = md_theme_light_surfaceVariantblue,
    onSurfaceVariant = md_theme_light_onSurfaceVariantblue,
)


private val DarkColorSchemeBlue = darkColorScheme(
    primary = md_theme_dark_primaryblue,
    onPrimary = md_theme_dark_onPrimaryblue,
    primaryContainer = md_theme_dark_primaryContainerblue,
    onPrimaryContainer = md_theme_dark_onPrimaryContainerblue,
    secondary = md_theme_dark_secondaryblue,
    onSecondary = md_theme_dark_onSecondaryblue,
    secondaryContainer = md_theme_dark_secondaryContainerblue,
    onSecondaryContainer = md_theme_dark_onSecondaryContainerblue,
    tertiary = md_theme_dark_tertiaryblue,
    onTertiary = md_theme_dark_onTertiaryblue,
    tertiaryContainer = md_theme_dark_tertiaryContainerblue,
    onTertiaryContainer = md_theme_dark_onTertiaryContainerblue,
    error = md_theme_dark_errorblue,
    errorContainer = md_theme_dark_errorContainerblue,
    onError = md_theme_dark_onErrorblue,
    onErrorContainer = md_theme_dark_onErrorContainerblue,
    background = md_theme_dark_backgroundblue,
    onBackground = md_theme_dark_onBackgroundblue,
    outline = md_theme_dark_outlineblue,
    inverseOnSurface = md_theme_dark_inverseOnSurfaceblue,
    inverseSurface = md_theme_dark_inverseSurfaceblue,
    inversePrimary = md_theme_dark_inversePrimaryblue,
    surfaceTint = md_theme_dark_surfaceTintblue,
    outlineVariant = md_theme_dark_outlineVariantblue,
    scrim = md_theme_dark_scrimblue,
    surface = md_theme_dark_surfaceblue,
    onSurface = md_theme_dark_onSurfaceblue,
    surfaceVariant = md_theme_dark_surfaceVariantblue,
    onSurfaceVariant = md_theme_dark_onSurfaceVariantblue,
)

private val LightColorSchemeOrange = lightColorScheme(
    primary = md_theme_light_primaryorange,
    onPrimary = md_theme_light_onPrimaryorange,
    primaryContainer = md_theme_light_primaryContainerorange,
    onPrimaryContainer = md_theme_light_onPrimaryContainerorange,
    secondary = md_theme_light_secondaryorange,
    onSecondary = md_theme_light_onSecondaryorange,
    secondaryContainer = md_theme_light_secondaryContainerorange,
    onSecondaryContainer = md_theme_light_onSecondaryContainerorange,
    tertiary = md_theme_light_tertiaryorange,
    onTertiary = md_theme_light_onTertiaryorange,
    tertiaryContainer = md_theme_light_tertiaryContainerorange,
    onTertiaryContainer = md_theme_light_onTertiaryContainerorange,
    error = md_theme_light_errororange,
    errorContainer = md_theme_light_errorContainerorange,
    onError = md_theme_light_onErrororange,
    onErrorContainer = md_theme_light_onErrorContainerorange,
    background = md_theme_light_backgroundorange,
    onBackground = md_theme_light_onBackgroundorange,
    outline = md_theme_light_outlineorange,
    inverseOnSurface = md_theme_light_inverseOnSurfaceorange,
    inverseSurface = md_theme_light_inverseSurfaceorange,
    inversePrimary = md_theme_light_inversePrimaryorange,
    surfaceTint = md_theme_light_surfaceTintorange,
    outlineVariant = md_theme_light_outlineVariantorange,
    scrim = md_theme_light_scrimorange,
    surface = md_theme_light_surfaceorange,
    onSurface = md_theme_light_onSurfaceorange,
    surfaceVariant = md_theme_light_surfaceVariantorange,
    onSurfaceVariant = md_theme_light_onSurfaceVariantorange,
)


private val DarkColorSchemeOrange = darkColorScheme(
    primary = md_theme_dark_primaryorange,
    onPrimary = md_theme_dark_onPrimaryorange,
    primaryContainer = md_theme_dark_primaryContainerorange,
    onPrimaryContainer = md_theme_dark_onPrimaryContainerorange,
    secondary = md_theme_dark_secondaryorange,
    onSecondary = md_theme_dark_onSecondaryorange,
    secondaryContainer = md_theme_dark_secondaryContainerorange,
    onSecondaryContainer = md_theme_dark_onSecondaryContainerorange,
    tertiary = md_theme_dark_tertiaryorange,
    onTertiary = md_theme_dark_onTertiaryorange,
    tertiaryContainer = md_theme_dark_tertiaryContainerorange,
    onTertiaryContainer = md_theme_dark_onTertiaryContainerorange,
    error = md_theme_dark_errororange,
    errorContainer = md_theme_dark_errorContainerorange,
    onError = md_theme_dark_onErrororange,
    onErrorContainer = md_theme_dark_onErrorContainerorange,
    background = md_theme_dark_backgroundorange,
    onBackground = md_theme_dark_onBackgroundorange,
    outline = md_theme_dark_outlineorange,
    inverseOnSurface = md_theme_dark_inverseOnSurfaceorange,
    inverseSurface = md_theme_dark_inverseSurfaceorange,
    inversePrimary = md_theme_dark_inversePrimaryorange,
    surfaceTint = md_theme_dark_surfaceTintorange,
    outlineVariant = md_theme_dark_outlineVariantorange,
    scrim = md_theme_dark_scrimorange,
    surface = md_theme_dark_surfaceorange,
    onSurface = md_theme_dark_onSurfaceorange,
    surfaceVariant = md_theme_dark_surfaceVariantorange,
    onSurfaceVariant = md_theme_dark_onSurfaceVariantorange,
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
    BLUE(LightColorSchemeBlue),
    ORANGE(LightColorSchemeOrange),
    GREEN_DARK(DarkColorSchemeGreen),
    PURPLE_DARK(DarkColorSchemePurple),
    BLUE_DARK(DarkColorSchemeBlue),
    ORANGE_DARK(DarkColorSchemeOrange);

    fun toggleTheme(): MyColorTheme {
        return when (this) {
            GREEN -> GREEN_DARK
            PURPLE -> PURPLE_DARK
            BLUE -> BLUE_DARK
            ORANGE -> ORANGE_DARK
            GREEN_DARK -> GREEN
            PURPLE_DARK -> PURPLE
            BLUE_DARK -> BLUE
            ORANGE_DARK -> ORANGE
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
