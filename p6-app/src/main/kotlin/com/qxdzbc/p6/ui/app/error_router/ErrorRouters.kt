package com.qxdzbc.p6.ui.app.error_router

import com.github.michaelbull.result.onFailure
import com.qxdzbc.common.Rs
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.workbook.WorkbookKey

object ErrorRouters {
     fun <T> Rs<T,ErrorReport>.publishErrIfNeed(errorRouter: ErrorRouter, windowId:String?=null, wbKey: WorkbookKey?=null):Rs<T,ErrorReport>{
        this.onFailure {
            errorRouter.publishToWindow(it,windowId,wbKey)
        }
         return this
    }

    fun <T> Rs<T,ErrorReport>.publishErrIfNeedSt(errorRouter: ErrorRouter, windowId:String?=null, wbKeySt:St<WorkbookKey>? = null):Rs<T,ErrorReport>{
        this.onFailure {
            errorRouter.publishToWindow(it,windowId,wbKeySt)
        }
        return this
    }

    fun <T> Rs<T, ErrorReport>.publishErrToWindowIfNeed(errorRouter: ErrorRouter, windowId:String?=null):Rs<T,ErrorReport>{
        this.onFailure {
            errorRouter.publishToWindow(it,windowId)
        }
        return this
    }

    fun <T> Rs<T,ErrorReport>.publishErrToWindowIfNeed(errorRouter: ErrorRouter, wbKey:WorkbookKey?):Rs<T,ErrorReport>{
        this.onFailure {
            errorRouter.publishToWindow(it,wbKey)
        }
        return this
    }
}
