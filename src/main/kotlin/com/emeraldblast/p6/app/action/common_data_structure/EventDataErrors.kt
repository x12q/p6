package com.emeraldblast.p6.app.action.common_data_structure

import com.emeraldblast.p6.common.exception.error.ErrorHeader
import com.emeraldblast.p6.common.exception.error.ErrorReport

object EventDataErrors {
    val EVDTErr = "UI_EventDataErrors_"
    fun illegalDataStructure(clazz:Class<Any>):ErrorReport{
        return ErrorReport(
            header =  ErrorHeader(
                errorCode = "${EVDTErr}0",
                errorDescription = "Illegal event data structure: ${clazz.canonicalName}"
            )
        )
    }
}
