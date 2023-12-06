package pages.SchedulePage.ScheduleSection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import SectionUnit
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import style.currentColorScheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun schedule(
    sections: List<SectionUnit>,
    modifier: Modifier = Modifier
) {
    var img_width by remember { mutableStateOf(0.dp) }
    var img_height by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current.density
    Box(
        modifier = modifier,
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(containerColor = currentColorScheme.value.cs.background),
            shape = RoundedCornerShape((0.dp)),
            modifier = Modifier.onPointerEvent(PointerEventType.Move) {
                val position = it.changes.first().position;
                println(position)
            }
        ) {
            Box(
                contentAlignment = Alignment.TopStart,
            ) {
                Image(
                    modifier = Modifier.onSizeChanged {
                        img_width =
                            (it.width / density).dp     // layoutSize returns the value same as mouse offset
                        img_height = (it.height / density).dp
                    },
                    painter = painterResource("schedule_bg.svg"),
                    contentDescription = "schedule background"
                )
                Box() {
                    for (section in sections) {
                        sectionBlock(section, img_width)
                    }
                }
            }
        }
    }

}