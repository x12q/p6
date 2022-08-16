package com.emeraldblast.p6.app.oddity

import com.emeraldblast.p6.common.exception.error.CommonErrors
import com.emeraldblast.p6.common.exception.error.ErrorHeader
import com.emeraldblast.p6.common.exception.error.ErrorReport
import javax.inject.Inject

fun ErrorReport.oddMsg():String{
    return this.toString()
}

data class OddityContainerImp constructor (override val oddList: List<OddMsg> = emptyList()) : OddityContainer {
    override fun add(msg:OddMsg):OddityContainerImp{
           return this.copy(oddList=this.oddList + msg)
    }

    override fun addErrorReport(errorReport: ErrorReport?): OddityContainer {
        return this.add(OddMsg.Error(errorReport ?: ErrorReport(CommonErrors.Unknown.header)))
    }

    override fun addFatalErrorReport(errorReport: ErrorReport?): OddityContainer {
        val od = OddMsg.FatalError(errorReport?:CommonErrors.Unknown.header.toErrorReport())
        return this.add(od)
    }

    override fun remove(oddMsg: OddMsg): OddityContainer {
        return this.copy(this.oddList - oddMsg)
    }

    override fun containErrorReportOfType(errorHeader: ErrorHeader): Boolean {
        for(oddMsg in this.oddList){
            if(oddMsg.errorReport.isType(errorHeader)){
                return true
            }
        }
        return false
    }

    override fun containErrorReportOfType(errorReport: ErrorReport): Boolean {
        for(oddMsg in this.oddList){
            if(oddMsg.errorReport.isType(errorReport)){
                return true
            }
        }
        return false
    }

    override fun containErrorReport(errorReport: ErrorReport): Boolean {
        for(oddMsg in this.oddList){
            if(oddMsg.errorReport.identicalTo(errorReport)){
                return true
            }
        }
        return false
    }

    override val size: Int = this.oddList.size
}
