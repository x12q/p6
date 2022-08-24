package com.qxdzbc.p6.app.action.common_data_structure

import com.qxdzbc.p6.app.common.proto.ProtoUtils.toModel
import com.qxdzbc.p6.app.common.proto.ProtoUtils.toProto
import com.qxdzbc.p6.app.communication.res_req_template.IsError
import com.qxdzbc.p6.app.communication.res_req_template.IsLegal
import com.qxdzbc.p6.app.communication.res_req_template.WithErrorReport
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.proto.CommonProtos
import com.qxdzbc.p6.proto.CommonProtos.ErrorIndicatorProto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.qxdzbc.common.Rs

data class ErrorIndicator(
    override val errorReport: ErrorReport?
) : WithErrorReport, IsError, IsLegal {
    override val isError: Boolean get()=errorReport!=null
    companion object {
        fun fromProto(proto: CommonProtos.ErrorIndicatorProto): ErrorIndicator {
            return ErrorIndicator(
                errorReport = if (proto.hasErrorReport()) proto.errorReport.toModel() else null
            )
        }

        val noError = ErrorIndicator( null)

        /**
         * create an error indicator from an error report
         */
        fun error(errorReport: ErrorReport): ErrorIndicator {
            return ErrorIndicator(
                errorReport
            )
        }

        /**
         * convert an error report into an error indicator
         */
        fun ErrorReport.toErrIndicator(): ErrorIndicator {
            return ErrorIndicator(this)
        }

        fun Rs<Unit, ErrorReport>.toErrIndicator(): ErrorIndicator {
            when(this){
                is Ok->return noError
                is Err -> return error(this.error)
            }
        }
    }

    fun toProto(): ErrorIndicatorProto {
        return ErrorIndicatorProto.newBuilder()
            .setIsError(this.isError)
            .apply {
                val er = this@ErrorIndicator.errorReport
                if (er != null) {
                    setErrorReport(er.toProto())
                }
            }
            .build()
    }

    override fun isLegal(): Boolean {
        if(this.isError && this.errorReport!=null){
            return true
        }
        if(this.isOk && this.errorReport==null){
            return true
        }
        return false
    }
}

fun ErrorIndicatorProto.toModel(): ErrorIndicator {
    return ErrorIndicator.fromProto(this)
}
