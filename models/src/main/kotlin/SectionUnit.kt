import kotlinx.serialization.Serializable

// Hardcoded values
val BASE_IMAGE_WIDTH = 830
val MON_CENTER_X: Float = 193F
val FRI_CENTER_X: Float = 770F
val TIME_LINE_START_Y: Float = 35F
val TIME_LINE_END_Y: Float = 766F
val INTERVAL_NUM: Int = 14   // the number of 1-hour intervals between start and finish
val DAY_PADDING: Float = 5F    // the padding distance between two adjacent block
// Hardcoded values end
val BLOCK_WIDTH: Float = (FRI_CENTER_X - MON_CENTER_X) / 4 - DAY_PADDING

@Serializable
class SectionUnit(
    val day: Int,
    val startTime: Float,
    val finishTime: Float,
    val courseName: String,
    val profName: String,
    val location: String,
    val component: String = "LEC",  // eg. LEC, TUT
    val sectionNum: Int = 1,    // eg. 1, 2
    val classNumber: Int,
){
    fun getWidth(): Float{
        return BLOCK_WIDTH
    }

    fun getHeight(): Float{
        return (TIME_LINE_END_Y - TIME_LINE_START_Y) / INTERVAL_NUM * (finishTime - startTime)
    }

    fun getXOffset(): Float{
        return (FRI_CENTER_X - MON_CENTER_X) / 4 * day + MON_CENTER_X - (BLOCK_WIDTH / 2)
    }

    fun getYOffset(): Float{
        return (startTime - 8) * (TIME_LINE_END_Y - TIME_LINE_START_Y) / INTERVAL_NUM + TIME_LINE_START_Y
    }
}
