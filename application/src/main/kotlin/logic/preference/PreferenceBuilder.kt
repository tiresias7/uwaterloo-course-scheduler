package logic.preference

import androidx.compose.runtime.mutableStateOf
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class PreferenceBuilder() {
    fun build(weight: Int, tag: String, inputList: List<String>): Preference?{
        if (tag == "MaxHoursPerDay"){
            return MaxHoursPerDayPreference(weight, inputList[0].toInt())
        }
        if (tag == "PreferredTime"){
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val startTime = LocalTime.parse(inputList[0], formatter)
            val finishTime = LocalTime.parse(inputList[1], formatter)
            val days = convertToDayOfWeek(inputList[2])
            return PreferredTimePreference(weight, startTime, finishTime, days)
        }
        if (tag == "NonPreferredTime"){
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val startTime = LocalTime.parse(inputList[0], formatter)
            val finishTime = LocalTime.parse(inputList[1], formatter)
            val days = convertToDayOfWeek(inputList[2])
            return NonPreferredTimePreference(weight, startTime, finishTime, days)
        }
        return null
    }
}

fun convertToDayOfWeek(days : String) : Set<DayOfWeek> {
    val length = days.length - 1
    val result = mutableSetOf<DayOfWeek>()
    for (i in 0..length) {
        if (days[i] == 'M') {
            result.add(DayOfWeek.MONDAY)
        }
        else if (days[i] == 'T') {
            if (i + 1 <= length && days[i + 1] == 'h') {
                result.add(DayOfWeek.THURSDAY)
            }
            else {
                result.add(DayOfWeek.TUESDAY)
            }
        }
        else if (days[i] == 'F') {
            result.add(DayOfWeek.FRIDAY)
        }
        else if (days[i] == 'W') {
            result.add(DayOfWeek.WEDNESDAY)
        }
        else if (days[i] == 'A') {
            return EnumSet.allOf(DayOfWeek::class.java)
        }
    }
    return result.toSet()
}

fun convertfromDayOfWeek(days : Set<DayOfWeek>) : String {
    val length = days.size - 1
    val result = mutableStateOf("")
    for (day in days) {
        if (day == DayOfWeek.MONDAY) {
            result.value += "M/"
        }
        else if (day == DayOfWeek.TUESDAY) {
            result.value += "T/"
        }
        else if (day == DayOfWeek.WEDNESDAY) {
            result.value += "W/"
        }
        else if (day == DayOfWeek.THURSDAY) {
            result.value += "Th/"
        }
        else if (day == DayOfWeek.FRIDAY) {
            result.value += "F/"
        }
    }
    return result.value.removeSuffix("/")
}