package components.InputPreferences

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MaxHoursPerDayChip(
    addCallBack: (String, List<String>) -> Unit
) {
    val duration = remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }
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
                Text("Max Hours of Class Per Day: ")
                OutlinedTextField(
                    value = duration.value,
                    onValueChange = {
                        if (it == ""){
                            duration.value = it
                            isError.value = false
                        }
                        else if (it.all { char -> char.isDigit() }) {
                            if (it.toInt() <= 0 || it.toInt() > 24){
                                ;
                            }
                            else{
                                duration.value = it
                                isError.value = false
                            }
                        }
                    },
                    isError = isError.value,
                    modifier = Modifier.width(60.dp),
                )
            }
            FilledTonalButton(
                onClick = {
                    if (duration.value == ""){
                        isError.value = true
                    }
                    else{
                        addCallBack("MaxHoursPerDay", listOf(duration.value))
                    }
                }
            ){
                Text("Add")
            }
        }
    }
}