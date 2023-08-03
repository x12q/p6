package com.qxdzbc.p6.ui.document.workbook.di

import com.qxdzbc.common.compose.Ms
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.P6Component
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import javax.inject.Singleton


/**
 * A factory that can create [WorkbookState] from [Ms] of [Workbook].
 * The resulting [WorkbookState] has everything its needs and all its children object wired up to the correct state.
 * This factory is provided as a [Singleton] by [P6Component.wbStateFactory].
 */
interface WbStateFactory {

    /**
     * create a new [WorkbookState] for [wbMs], the returned [WorkbookState] contains the component that backs it.
     */
    fun makeWbState(wbMs: Ms<Workbook>, windowId:String?=null): WorkbookState

}

