package com.qxdzbc.p6.app.code

import com.qxdzbc.p6.app.app_context.InitErrors
import com.qxdzbc.p6.app.code.script_builder.AllScriptBuilderImp
import com.qxdzbc.p6.app.coderunner.CodeRunner
import com.qxdzbc.p6.app.common.utils.Utils
import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import javax.inject.Inject

class PythonCommanderImp @Inject constructor(private val runner: CodeRunner) : PythonCommander {

    override suspend fun initBackEnd(): Result<String, ErrorReport> {
        val initScript = Utils.readResource("/script/initScript.py")
        if(initScript!=null){
            val rt = runner.run(initScript)
            return rt
        }else{
            return Err(InitErrors.invalidInitScript())
        }
    }
}
