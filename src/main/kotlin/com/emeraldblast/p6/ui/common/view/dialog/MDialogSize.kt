package com.emeraldblast.p6.ui.common.view.dialog

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class MDialogSize(
    val minWidth:Dp,
    val maxWidth:Dp,
    val minHeight:Dp,
    val maxHeight:Dp

) {
    constructor(
        minWidth:Int,
        maxWidth:Int,
        minHeight:Int,
        maxHeight:Int
    ):this(minWidth.dp,maxWidth.dp,minHeight.dp,maxHeight.dp)
}
