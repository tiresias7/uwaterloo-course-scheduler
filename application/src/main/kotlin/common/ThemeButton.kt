package common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import style.MyColorTheme
import style.currentColorScheme

@Composable
fun ThemeButton(
    theme: MyColorTheme,
    isDark: MutableState<Boolean>
){
    var myTheme = theme
    var borderColor = Color.Black
    if (isDark.value) {
        myTheme = myTheme.toggleTheme()
        borderColor = Color.White
    }
    val isSelected = myTheme == currentColorScheme.value
    Box(
        modifier = Modifier
            .size(30.dp)
            .border(
                3.dp,
                color = if (isSelected) borderColor else Color.Transparent,
                shape = CircleShape
            )
            .padding(5.dp)
            .background(color = theme.cs.primary, shape = CircleShape)
            .clip(CircleShape)
            .clickable { currentColorScheme.value = myTheme }
    )
}