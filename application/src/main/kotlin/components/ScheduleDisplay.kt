package components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import data.SectionUnit

@Composable
fun scheduleSection(
    sections: MutableList<SectionUnit>
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        schedule(sections)
        Row(
            modifier = Modifier.requiredHeight(60.dp)
        ) {
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .size(width = 40.dp, height = 40.dp)
            ) {
                Icon(
                    Icons.Outlined.Star,
                    "Save to my schedule"
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .size(width = 40.dp, height = 40.dp)
            ) {
                Icon(
                    Icons.Outlined.Person,
                    "Share to friends"
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .size(width = 40.dp, height = 40.dp)
            ) {
                Icon(
                    Icons.Outlined.Share,
                    "Export"
                )
            }
        }
    }
}

@Composable
fun schedule(
    sections: MutableList<SectionUnit>,
    modifier: Modifier = Modifier
) {
    var img_width by remember { mutableStateOf(0.dp) }
    var img_height by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current.density
    Card(
        modifier = modifier,
        border = BorderStroke(0.1.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape((0.dp)),
//            modifier = Modifier.onPointerEvent(PointerEventType.Move) {
//                val position = it.changes.first().position;
//                println(position)
//            }
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