package logic.preference

import Section
import java.time.DayOfWeek
import java.time.LocalTime

class NoCollisionPreference(override var weight: Int) : Preference() {
    override val tag: String = "NoCollision"

    override fun getScore(sections: List<Section>): Int {
        val events = mutableListOf<Event>()

        // Create an event for the start and end of each section for each day it occurs.
        for (section in sections) {
            for (day in section.days) {
                events.add(Event(day, section.startTime, 1))  // +1 for start of a section
                events.add(Event(day, section.endTime, -1))   // -1 for end of a section
            }
        }

        // Sort the events first by day, then by time, then by type (end before start if times are equal).
        events.sortWith(compareBy<Event> { it.day }.thenBy { it.time }.thenBy { it.type })

        var currentOverlap = 0

        // Check each day for overlaps
        // check for online course, return No Collision
        if (events.isEmpty()){
            return 100
        }

        var currentDay = events.first().day
        for (event in events) {
            // Reset the counter if it's a new day
            if (event.day != currentDay) {
                currentDay = event.day
                currentOverlap = 0
            }

            currentOverlap += event.type
            if (currentOverlap > 1) {
                return 0
            }
        }

        return 100
    }

    data class Event(val day: DayOfWeek, val time: LocalTime, val type: Int) // +1 for start, -1 for end
}