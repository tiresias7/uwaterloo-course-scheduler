package pages.SchedulePage.ScheduleSection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import BASE_IMAGE_WIDTH
import BLOCK_WIDTH
import SectionUnit
import kotlinx.coroutines.launch
import style.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sectionBlock(section: SectionUnit, baseWidth: Dp) {
    val interactionSource = remember { MutableInteractionSource() }
    val tooltipState = remember { RichTooltipState() }
    val scope = rememberCoroutineScope()
    val isHover by interactionSource.collectIsHoveredAsState()
    if (isHover) {
        ElevatedCard(
            modifier = Modifier
                .offset(
                    x = baseWidth * section.getXOffset() / BASE_IMAGE_WIDTH - BLOCK_WIDTH.dp + section.getWidth().dp,
                    y = baseWidth * section.getYOffset() / BASE_IMAGE_WIDTH + section.getHeight().dp
                )
                .heightIn(0.dp, 400.dp).widthIn(0.dp, 150.dp)
                .hoverable(interactionSource)
                .zIndex(1f),
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
            ){
                SelectionContainer { Text(section.courseName + "\n" + section.profName + "\n" + section.location) }
            }
        }
    }
    else {
        scope.launch { tooltipState.dismiss() }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = currentColorScheme.value.cs.primaryContainer),
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier
            .size(
                width = baseWidth * section.getWidth() / BASE_IMAGE_WIDTH,
                height = baseWidth * section.getHeight() / BASE_IMAGE_WIDTH
            )
            .offset(
                x = baseWidth * section.getXOffset() / BASE_IMAGE_WIDTH,
                y = baseWidth * section.getYOffset() / BASE_IMAGE_WIDTH
            )
            .hoverable(interactionSource)
    ) {
        SelectionContainer {
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