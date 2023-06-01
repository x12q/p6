package com.qxdzbc.common.compose

object AnimationUtils {

    fun normalizeNumber(number: Float, min: Float, max: Float): Float {
        return (number - min) / (max - min)
    }

    enum class TimeDirection {
        FORWARD, // forward means time running from 0->1
        BACKWARD; // backward means time running from 1->0
    }


    /**
     * Animation between [lowerThreshold] and [upperThreshold]
     */
    fun calculateAnimationProgressWithTimeThreshold(
        timeDirection: TimeDirection,
        time: Float,
        lowerThreshold: Float,
        upperThreshold: Float,
    ): Float {
        require(time in 0.0..1.0) {
            "Time must be between 0.0 and 1.0"
        }
        require(lowerThreshold in 0.0..1.0) {
            "lowerThreshold must be between 0.0 and 1.0"
        }
        require(upperThreshold in 0.0..1.0) {
            "upperThreshold must be between 0.0 and 1.0"
        }

        if (time < lowerThreshold) {
            return 0f
        } else if (time > upperThreshold) {
            return 1f
        } else {
            return when (timeDirection) {
                TimeDirection.FORWARD -> normalizeNumber(time, lowerThreshold, upperThreshold)
                TimeDirection.BACKWARD -> normalizeNumber(-time, -lowerThreshold, -upperThreshold)
            }
        }
    }

    /**
     * Compute animation progress by chronological starting point on a timescale of 0 and 1. Starting points = the point when the animation starts.
     *
     * [lowerStartingPoint]: starting point for when time moves forward, must be between 0-1, otherwise the app will crash
     *
     * [upperStartingPoint]: starting point for when time moves backward, must be between 0-1, otherwise the app will crash
     */
    fun calculateAnimationProgressByTimeStartingPoint(
        timeDirection: TimeDirection,
        time: Float,
        lowerStartingPoint: Float,
        upperStartingPoint: Float,
    ): Float {
        require(time in 0.0..1.0) {
            "Time must be between 0.0 and 1.0"
        }
        require(lowerStartingPoint in 0.0..1.0) {
            "lowerStartingPoint must be between 0.0 and 1.0"
        }
        require(upperStartingPoint in 0.0..1.0) {
            "upperStartingPoint must be between 0.0 and 1.0"
        }
        return when (timeDirection) {
            TimeDirection.FORWARD -> calculateAnimationProgressWithTimeThreshold(
                timeDirection,
                time,
                upperStartingPoint,
                1f
            )

            TimeDirection.BACKWARD -> calculateAnimationProgressWithTimeThreshold(
                timeDirection,
                time,
                0f,
                lowerStartingPoint
            )
        }
    }
}