package com.qxdzbc.p6.ui.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.di.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
data class ActiveWindowPointerImp(
    val windowIdMs:Ms<String?>
) : ActiveWindowPointer {

    @Inject
    constructor():this(mutableStateOf(null))

    override val windowId:String? by windowIdMs

    override fun pointTo(windowId: String?) {
        windowIdMs.value = windowId
    }

    override fun nullify() {
        this.pointTo(null)
    }
}
