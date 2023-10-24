package components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import courseSearchInputField
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.outlined.Done
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.sp
import data.SelectedCourse
import style.*
import javax.swing.text.Style


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun courseSelectionSection(
    allCourses: List<String>,
    selectedCourses: MutableList<SelectedCourse>,
    addCallBack: (courseName: String) -> Unit,
    toggleCallBack: (index: Int) -> Unit,
    deleteCallBack: (index: Int) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        courseSearchInputField(allCourses, addCallBack)
        Text(
            text = "Click on a selected course to modify its priority:",
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic
        )
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .size(width = 446.dp, height = 238.dp)
        ) {
            courseGrid(selectedCourses, toggleCallBack, deleteCallBack)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun courseGrid(
    selectedCourses: MutableList<SelectedCourse>,
    toggleCallBack: (index: Int) -> Unit,
    deleteCallBack: (index: Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(selectedCourses.size) { index ->
            courseChip(index, selectedCourses[index], toggleCallBack, deleteCallBack)
        }
    }
}
