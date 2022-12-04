package com.qxdzbc.p6.ui.file

import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.ErrorReport
import java.nio.file.Path

object P6FileSaverErrors {
    val prefix = "P6FileSaverErrors_"

    val all = listOf(TargetPathPointToAnAlreadyOpenWb)
    fun coldInit(){
        val q = listOf(this)+all
        for( e in q){
            println("cold init ${e::class}")
        }
    }

    object TargetPathPointToAnAlreadyOpenWb{
        fun report(path:Path):ErrorReport{
            return ErrorHeader("${prefix}${all.indexOf(TargetPathPointToAnAlreadyOpenWb)}","workbook at path ${path.toAbsolutePath()} is already opened for editing, another workbook cannot overwrite it").toErrorReport()
        }
        fun report(detail:String?=null):ErrorReport{
            return ErrorHeader("${prefix}${all.indexOf(TargetPathPointToAnAlreadyOpenWb)}",detail?:"Can't save workbook because it is already opened").toErrorReport()
        }
    }
}
