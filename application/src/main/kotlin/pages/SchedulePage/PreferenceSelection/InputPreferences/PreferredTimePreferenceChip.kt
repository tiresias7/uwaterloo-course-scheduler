package pages.SchedulePage.PreferenceSelection.InputPreferences

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import common.SimpleTextField
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun PreferredTimeChip(
    addCallBack: (String, List<String>) -> Unit
) {
    val duration1 = remember { mutableStateOf("") }
    val duration2 = remember { mutableStateOf("") }
    val duration3 = remember { mutableStateOf("") }
    val duration4 = remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }
    val dropDownExpanded = remember {mutableStateOf(false)}
    var days by remember { mutableStateOf("") }
    var daysSelected = remember { mutableListOf<Int>()}
    val weekDays = listOf("Add All Weekdays", "Add Monday", "Add Tuesday", "Add Wednesday", "Add Thursday", "Add Friday", "Clear")
    val focusManager = LocalFocusManager.current
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ){
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Prefer time slot ")
                SimpleTextField(
                    value = duration1.value,
                    onValueChange = {
                        checkHour(it, duration1, isError)
                    },
                    isError = isError,
                    modifier = Modifier.size(50.dp, 30.dp),
                )
                Text(" : ")
                SimpleTextField(
                    value = duration2.value,
                    onValueChange = {
                        checkMinute(it, duration2, isError)
                    },
                    isError = isError,
                    modifier = Modifier.size(50.dp, 30.dp),
                )
                Text(" to ")
                SimpleTextField(
                    value = duration3.value,
                    onValueChange = {
                        checkHour(it, duration3, isError)
                    },
                    isError = isError,
                    modifier = Modifier.size(50.dp, 30.dp),
                )
                Text(" : ")
                SimpleTextField(
                    value = duration4.value,
                    onValueChange = {
                        checkMinute(it, duration4, isError)
                    },
                    isError = isError,
                    modifier = Modifier.size(50.dp, 30.dp),
                )
                Text(" on ")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SimpleTextField(
                        modifier = Modifier.size(100.dp, 30.dp)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    dropDownExpanded.value = true
                                } else {
                                    dropDownExpanded.value = false
                                }
                            },
                        readOnly = true,
                        value = days,
                        onValueChange = {},
                        isError = isError
                    )
                    DropdownMenu(
                        expanded = dropDownExpanded.value,
                        properties = PopupProperties(),
                        onDismissRequest = { dropDownExpanded.value = false },
                        modifier = Modifier.size(200.dp, 270.dp)
                    ) {
                        weekDays.forEach { text: String ->
                            androidx.compose.material.DropdownMenuItem(
                                modifier = Modifier.height(35.dp),
                                onClick = {
                                    dropDownExpanded.value = false
                                    days = addedDays(text, daysSelected)
                                    isError.value = false
                                    focusManager.clearFocus()
                                }
                            ) {
                                Text(text = text)
                            }
                            Divider()
                        }
                    }
                }
            }
            FilledTonalButton(
                onClick = {
                    if (duration1.value == "" || duration2.value == "" || duration3.value == "" || duration4.value == ""){
                        isError.value = true
                    }
                    else {
                        val d1 = duration1.value.padStart(2, '0')
                        val d2 = duration2.value.padStart(2, '0')
                        val d3 = duration3.value.padStart(2, '0')
                        val d4 = duration4.value.padStart(2, '0')
                        val dayOfWeek = days
                        val formatter = DateTimeFormatter.ofPattern("HH:mm")
                        val startTime = LocalTime.parse("${d1}:${d2}", formatter)
                        val endTime = LocalTime.parse("${d3}:${d4}", formatter)
                        if (startTime < endTime && !days.isEmpty()){
                            addCallBack("PreferredTime", listOf(startTime.format(formatter), endTime.format(formatter), dayOfWeek))
                        }
                        else{
                            isError.value = true
                        }
                    }
                }
            ){
                Text("Add")
            }
        }
    }
}

fun checkHour(str: String, targetStr: MutableState<String>, error: MutableState<Boolean>){
    if (str == ""){
        targetStr.value = str
        error.value = false
    }
    else if (str.all { char -> char.isDigit() }) {
        if (str.toInt() < 0 || str.toInt() > 24 || str.length > 2){
            ;
        }
        else{
            targetStr.value = str
            error.value = false
        }
    }
}

fun checkMinute(str: String, targetStr: MutableState<String>, error: MutableState<Boolean>){
    if (str == ""){
        targetStr.value = str
        error.value = false
    }
    else if (str.all { char -> char.isDigit() }) {
        if (str.toInt() < 0 || str.toInt() > 59 || str.length > 2){
            ;
        }
        else{
            targetStr.value = str
            error.value = false
        }
    }
}