package com.qxdzbc.p6.app.oddity

import com.qxdzbc.common.error.CommonErrors
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport

fun ErrorReport.oddMsg():String{
    return this.toString()
}

data class ErrorContainerImp constructor (override val errList: List<ErrMsg> = emptyList()) : ErrorContainer {
    override fun add(msg:ErrMsg):ErrorContainerImp{
           return this.copy(errList=this.errList + msg)
    }

    override fun addErrorReport(errorReport: ErrorReport?): ErrorContainer {
        return this.add(ErrMsg.Error(errorReport ?: ErrorReport(CommonErrors.Unknown.header)))
    }

    override fun addFatalErrorReport(errorReport: ErrorReport?): ErrorContainer {
        val od = ErrMsg.FatalError(errorReport?:CommonErrors.Unknown.header.toErrorReport())
        return this.add(od)
    }

    override fun remove(errMsg: ErrMsg): ErrorContainer {
        return this.copy(this.errList - errMsg)
    }

    override fun containErrorReportOfType(errorHeader: ErrorHeader): Boolean {
        for(oddMsg in this.errList){
            if(oddMsg.errorReport.isType(errorHeader)){
                return true
            }
        }
        return false
    }

    override fun containErrorReportOfType(errorReport: ErrorReport): Boolean {
        for(oddMsg in this.errList){
            if(oddMsg.errorReport.isType(errorReport)){
                return true
            }
        }
        return false
    }

    override fun containErrorReport(errorReport: ErrorReport): Boolean {
        for(oddMsg in this.errList){
            if(oddMsg.errorReport.identicalTo(errorReport)){
                return true
            }
        }
        return false
    }

    override val size: Int = this.errList.size
}
