package logictest

import logic.preference.Preference
import Section
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class PreferenceTest {

    // A minimal concrete subclass of Preference for testing purposes
    private class TestPreference : Preference() {
        override var weight: Int = 2
        override val tag: String = "Test"

        override fun getScore(sections: List<Section>): Int = 42
    }

    @Test
    fun getWeight() {
        val pref = TestPreference()
        assertEquals(2, pref.weight)
    }

    @Test
    fun getTag() {
        val pref = TestPreference()
        assertEquals("Test", pref.tag)
    }

    @Test
    fun isHard() {
        val pref = TestPreference()
        assertFalse(pref.isHard())
    }

    @Test
    fun eval() {
        val pref = TestPreference()
        val sections = listOf<Section>()
        assertEquals(84, pref.eval(sections))
        assertEquals(42, pref.eval(sections, false))
    }
}