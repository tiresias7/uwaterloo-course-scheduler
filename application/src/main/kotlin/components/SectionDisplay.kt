package components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import data.BASE_IMAGE_WIDTH
import data.BLOCK_WIDTH
import data.SectionUnit
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sectionBlock(section: SectionUnit, baseWidth: Dp) {
    val interactionSource = remember { MutableInteractionSource() }
    val tooltipState = remember { RichTooltipState() }
    val scope = rememberCoroutineScope()
    val isHover by interactionSource.collectIsHoveredAsState()
    if (isHover) {
        scope.launch { tooltipState.show() }
    } else {
        scope.launch { tooltipState.dismiss() }
    }

    RichTooltipBox(
        title = {
            Box(){
                Text(section.courseName)
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(section.profName)
                Text(section.location)
            }
        },
        modifier = Modifier
            .offset(
                x = baseWidth * section.getXOffset() / BASE_IMAGE_WIDTH - BLOCK_WIDTH.dp,
                y = baseWidth * section.getYOffset() / BASE_IMAGE_WIDTH
            )
            .height(100.dp),
        tooltipState = tooltipState,
        action = {},
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            ),
            border = BorderStroke(0.1.dp, Color.Gray),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier
                .size(
                    width = baseWidth * section.getWidth() / BASE_IMAGE_WIDTH,
                    height = baseWidth * section.getHeight() / BASE_IMAGE_WIDTH
                )
                .offset(
                    x = baseWidth * section.getXOffset() / BASE_IMAGE_WIDTH,
                    y = baseWidth * section.getYOffset() / BASE_IMAGE_WIDTH
                )
                .tooltipAnchor()
                .hoverable(interactionSource)
        ) {
            Text(
                text = (section.courseName + " "
                        + section.component + " "
                        + section.sectionNum),
                modifier = Modifier
                    .padding(5.dp),
                textAlign = TextAlign.Start,
            )
        }
    }

}