package com.qxdzbc.common

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
)
annotation class P6ExperimentalApi(
    val message:String = "This API is experimental, use with care."
)