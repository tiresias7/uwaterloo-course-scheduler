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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import data.BASE_IMAGE_WIDTH
import data.SectionUnit

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun scheduleSection(
    clicked: MutableState<Boolean>,
    sections: MutableList<SectionUnit>
) {
    var img_width by remember { mutableStateOf(0.dp) }
    var img_height by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current.density

    Column(
        horizontalAlignment = Alignment.End
    ) {
        Card(
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
                        Card(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 0.dp
                            ),
                            border = BorderStroke(0.1.dp, Color.Gray),
                            shape = RoundedCornerShape(0.dp),
                            modifier = Modifier
                                .size(
                                    width = img_width * section.getWidth() / BASE_IMAGE_WIDTH,
                                    height = img_width * section.getHeight() / BASE_IMAGE_WIDTH
                                )
                                .offset(
                                    x = img_width * section.getXOffset() / BASE_IMAGE_WIDTH,
                                    y = img_width * section.getYOffset() / BASE_IMAGE_WIDTH
                                ),
                        ) {
                            Text(
                                text = (section.courseName + "\n"
                                        + section.location + "\n"
                                        + section.profName),
                                modifier = Modifier
                                    .padding(5.dp),
                                textAlign = TextAlign.Start,
                            )
                        }
                    }
                }
            }
        }
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