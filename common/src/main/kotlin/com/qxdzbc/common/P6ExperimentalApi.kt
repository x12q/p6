package com.qxdzbc.common


/**
 * For marking classes and functions as experimental.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
)
annotation class P6ExperimentalApi(
    val message:String = "This API is experimental, use with care."
)