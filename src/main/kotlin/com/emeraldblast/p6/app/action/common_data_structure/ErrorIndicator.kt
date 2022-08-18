package com.emeraldblast.p6.app.action.common_data_structure

import com.emeraldblast.p6.app.common.utils.Rs
import com.emeraldblast.p6.app.common.proto.toModel
import com.emeraldblast.p6.app.common.proto.toProto
import com.emeraldblast.p6.app.communication.res_req_template.IsError
import com.emeraldblast.p6.app.communication.res_req_template.IsLegal
import com.emeraldblast.p6.app.communication.res_req_template.WithErrorReport
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.proto.CommonProtos
import com.emeraldblast.p6.proto.CommonProtos.ErrorIndicatorProto
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok

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
