package com.emeraldblast.p6.app.code

import com.emeraldblast.p6.app.coderunner.CodeRunner
import com.emeraldblast.p6.app.document.cell.address.CellAddress
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

object BackEndCommanders {
    fun std(codeRunner: CodeRunner):PythonCommander{
        return PythonCommanderImp(codeRunner)
    }

    fun dummy():PythonCommander{
        return object :PythonCommander{
            override suspend fun initBackEnd(): Result<String, ErrorReport> {
                val str = "init backend"
                println(str)
                return Ok(str)
            }
        }
    }
}
