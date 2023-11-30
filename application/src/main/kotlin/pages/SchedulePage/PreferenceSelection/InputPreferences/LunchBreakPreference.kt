package pages.SchedulePage.PreferenceSelection.InputPreferences

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.SimpleTextField
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun LunchBreakPreferenceChip(
    addCallBack: (String, List<String>) -> Unit
) {
    val duration1 = remember { mutableStateOf("") }
    val duration2 = remember { mutableStateOf("") }
    val duration3 = remember { mutableStateOf("") }
    val duration4 = remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }
    val breakLength = remember { mutableStateOf("") }
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
                Text("Lunch break ")
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
                Text(" for ")
                SimpleTextField(
                    value = breakLength.value,
                    onValueChange = { newValue ->
                        if (newValue.matches(Regex("[0-9]*")) && newValue.length != 0 && newValue.length <= 3) {
                            breakLength.value = newValue
                            isError.value = false
                        } else {
                            isError.value = true
                        }
                    },
                    isError = isError,
                    modifier = Modifier.size(60.dp, 30.dp),
                )
                Text(" mins")
            }
            FilledTonalButton(
                onClick = {
                    if (duration1.value == "" || duration2.value == "" || duration3.value == "" || duration4.value == "" || breakLength.value == ""){
                        isError.value = true
                    }
                    else {
                        val d1 = duration1.value.padStart(2, '0')
                        val d2 = duration2.value.padStart(2, '0')
                        val d3 = duration3.value.padStart(2, '0')
                        val d4 = duration4.value.padStart(2, '0')
                        val formatter = DateTimeFormatter.ofPattern("HH:mm")
                        val startTime = LocalTime.parse("${d1}:${d2}", formatter)
                        val endTime = LocalTime.parse("${d3}:${d4}", formatter)
                        if (startTime < endTime){
                            addCallBack("LunchBreakPreference", listOf(startTime.format(formatter), endTime.format(formatter), breakLength.value))
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
