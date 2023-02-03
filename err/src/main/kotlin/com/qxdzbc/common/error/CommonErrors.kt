package com.qxdzbc.common.error

object CommonErrors {
    private const val prefix = "Common Error "

    fun makeCommonReport(templateHeader:ErrorHeader, detail: String?): ErrorReport {
        return templateHeader.let { errHeader ->
            detail?.let {
                errHeader.setDescription(detail)
            } ?: templateHeader
        }.toErrorReport()
    }

    fun makeCommonExceptionErrorReport(templateHeader: ErrorHeader,detail:String?,exception: Throwable): ErrorReport {
        return SingleErrorReport(
            header = templateHeader.let { errHeader ->
                detail?.let {
                    errHeader.setDescription(detail)
                } ?: templateHeader
            },
            exception = exception
        )
    }

    /**
     */
    object TimeOut {
        val header = ErrorHeader("${prefix}1", "Timeout")
        data class Data(val detail: String)
        fun report(detail:String?): ErrorReport {
            return makeCommonReport(header,detail)
        }
    }

    /**
     * For reporting unknown exception
     */
    object Unknown {
        val header = ErrorHeader("${prefix}2", "Unknown error")

        data class Data(val additionalInfo: String, val exception: Exception?)
        fun report(detail: String?): ErrorReport {
            return makeCommonReport(TimeOut.header,detail)
        }
    }

    /**
     * this error indicates that an exception was caught
     */
    object ExceptionError {
        val header = ErrorHeader("${prefix}3", "Exception error")

        fun report(exception: Throwable): ErrorReport {
            return makeCommonExceptionErrorReport(header.appendDescription(":${exception}"),null,exception)
        }
        fun report(message:String?,exception: Throwable): ErrorReport {
            return makeCommonExceptionErrorReport(header,message,exception)
        }
    }

    object MultipleErrors {
        val header = ErrorHeader("$prefix 4", "Multiple errors")

        data class Data(val errorList: List<ErrorReport>)

        fun report(errorList: List<ErrorReport>): MultiErrorReport {
            return MultiErrorReport(
                header = header,
                singleErrorReportList =errorList
            )
        }
    }

    object NullObject{
        val header = ErrorHeader("${prefix} 5", "Null obj error")
        fun report(detail: String?=null): ErrorReport {
            if(detail!=null){
                return header.setDescription(detail).toErrorReport()
            }else{
                return header.toErrorReport()
            }
        }
    }
    object UnPlanned {
        val header = ErrorHeader("${prefix} 6", "Unplanned error")

        fun report(detail: String): ErrorReport {
            return header.setDescription(detail).toErrorReport()
        }
    }

}
