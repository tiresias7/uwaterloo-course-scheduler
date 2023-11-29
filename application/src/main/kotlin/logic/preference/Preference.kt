package logic.preference

import Section

abstract class Preference {
    companion object {
        const val HARD_PREFERENCE_WEIGHT = 10000

//        fun create(tag: String, weight: Int): Preference {
//            return when (tag) {
//                "NoCollision" -> NoCollisionPreference(weight)
//                else -> throw IllegalArgumentException("Unknown preference type")
//            }
//        }
    }

    abstract var weight: Int      // Weight of this preference, higher is more important
                                  // Hard preferences should have a weight of 10000
                                  // Soft preferences should smaller weights, starting from 1
    abstract val tag: String      // Category of this preference

    // Method to determine if a preference is hard or soft.
    fun isHard(): Boolean = weight == HARD_PREFERENCE_WEIGHT

    // Evaluate a given schedule of sections.
    // Should return an integer score ranging from 0 to 100.
    // Should be monotonic, i.e. if A is a subset of B, then eval(A) >= eval(B).
    protected abstract fun getScore(sections: List<Section>): Int

    fun eval(sections: List<Section>, weighted: Boolean = true): Int {
        val evalResult = getScore(sections)
        if (isHard() && evalResult != 100) {
            return 0
        }
        return if (weighted) evalResult * weight else evalResult
    }
}
