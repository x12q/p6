package com.emeraldblast.p6.ui.app

data class ActiveWindowPointerImp(
    override val windowId:String?
) : ActiveWindowPointer {

    override fun pointTo(windowId: String?): ActiveWindowPointer {
        return this.copy(windowId = windowId)
    }

    override fun nullify(): ActiveWindowPointer {
        return this.pointTo(null)
    }
}
