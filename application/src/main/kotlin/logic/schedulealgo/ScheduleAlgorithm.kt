package logic.schedulealgo

import Section
import logic.preference.Preference

abstract class ScheduleAlgorithm {

    /**
     * Generates a list of top-scoring schedules based on the provided preferences.
     *
     * @param sectionLists List of Sections lists to choose from.
     * @param preferences List of Preferences to score the schedules.
     * @param totalSchedules The number of top schedules to return.
     * @return List of top-scoring schedule combinations.
     */
    abstract fun generateSchedules(
        sectionLists: List<List<Section>>,
        preferences: List<Preference>,
        totalSchedules: Int
    ): List<List<Section>>
}

