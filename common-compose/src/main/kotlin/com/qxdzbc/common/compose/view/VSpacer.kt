package com.qxdzbc.common.compose.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * Vertical spacer that stretches vertically
 */
@Composable
fun VSpacer(height: Dp, modifier: Modifier =Modifier){
    Spacer(
        Modifier.height(height).then(modifier)
    )
}