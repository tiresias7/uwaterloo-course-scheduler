package logic.preference

import java.time.LocalTime
import java.time.format.DateTimeFormatter

class PreferenceBuilder() {
    fun build(weight: Int, tag: String, inputList: List<String>): Preference?{
        if (tag == "MaxHoursPerDay"){
            return MaxHoursPerDayPreference(weight, inputList[0].toInt())
        }
        if (tag == "PreferredTime"){
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val startTime = LocalTime.parse(inputList[0], formatter)
            val finishTime = LocalTime.parse(inputList[1], formatter)
            return PreferredTimePreference(weight, startTime, finishTime)
        }
        if (tag == "NonPreferredTime"){
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val startTime = LocalTime.parse(inputList[0], formatter)
            val finishTime = LocalTime.parse(inputList[1], formatter)
            return NonPreferredTimePreference(weight, startTime, finishTime)
        }
        return null
    }
}