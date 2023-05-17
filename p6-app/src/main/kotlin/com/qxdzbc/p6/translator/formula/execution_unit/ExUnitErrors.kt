package com.qxdzbc.p6.translator.formula.execution_unit

import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.cell.CellErrors

object ExUnitErrors {
    val prefix = "ExUnitErrors "

    private var _l:List<Any>? = null

    val all:List<Any> get(){
        if(_l==null){
            _l=listOf(IncompatibleType,IllegalReturnType,InvalidFunction)
        }
        return _l!!
    }

    fun coldInit(){
        val q = listOf(this)+ CellErrors.all
        for (e in q){
            println("Cold init ${e::class}")
        }
    }
    object IncompatibleType {
        val header = ErrorHeader("${prefix}0", "incompatible type")
        fun report(objs: List<Any>): ErrorReport {
            val typeNameList = objs.map { it::class.simpleName }
            return header.setDescription("These types cannot go together: ${typeNameList}").toErrorReport()
        }

        fun report(detail: String? = null): ErrorReport {
            return (detail?.let { header.setDescription(it) } ?: header).toErrorReport()
        }
    }

    object IllegalReturnType {
        val header = ErrorHeader("${prefix}1", "Illegal return type")
        fun report(o: Any): ErrorReport {
            return header.setDescription("return type ${o::class.simpleName} is illegal. A worksheet function can only return Result<String,ErrorReport>, Result<Number,ErrorReport>, Result<Boolean,ErrorReport>")
                .toErrorReport()
        }

        fun report(detail: String?=null): ErrorReport {
            return header.setDescription("return type is illegal. A worksheet function can only return Result<String,ErrorReport>, Result<Number,ErrorReport>, Result<Boolean,ErrorReport>")
                .toErrorReport()
        }
    }
    object InvalidFunction {
        val header = ErrorHeader("${prefix}2", "Illegal return type")
        fun report(functionName: String): ErrorReport {
            return header.setDescription("function $functionName is invalid").toErrorReport()
        }
    }
}
