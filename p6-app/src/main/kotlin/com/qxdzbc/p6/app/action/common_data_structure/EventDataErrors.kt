package com.qxdzbc.p6.app.action.common_data_structure

import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport

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
