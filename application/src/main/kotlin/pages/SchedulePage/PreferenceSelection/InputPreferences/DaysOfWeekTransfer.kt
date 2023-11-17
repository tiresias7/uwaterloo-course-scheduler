package pages.SchedulePage.PreferenceSelection.InputPreferences

import androidx.compose.runtime.mutableStateOf

fun addedDays(newValue : String, daysSelected : MutableList<Int>) : String {
    val result = mutableStateOf("")
    if (newValue == "Add Monday" && !daysSelected.contains(1)) {
        daysSelected.add(1)
    }
    else if (newValue == "Add Tuesday" && !daysSelected.contains(2)) {
        daysSelected.add(2)
    }
    else if (newValue == "Add Wednesday" && !daysSelected.contains(3)) {
        daysSelected.add(3)
    }
    else if (newValue == "Add Thursday" && !daysSelected.contains(4)) {
        daysSelected.add(4)
    }
    else if (newValue == "Add Friday" && !daysSelected.contains(5)) {
        daysSelected.add(5)
    }
    else if (newValue == "Clear") {
        daysSelected.clear()
    }
    else if (newValue == "Add All Weekdays"){
        daysSelected.clear()
        daysSelected.addAll(listOf(1, 2, 3, 4, 5))
    }
    daysSelected.sort()
    for (days in daysSelected) {
        when(days) {
            1 -> result.value = result.value + 'M'
            2 -> result.value = result.value + 'T'
            3 -> result.value = result.value + 'W'
            4 -> result.value = result.value + "Th"
            5 -> result.value = result.value + 'F'
        }
    }
    return result.value
}