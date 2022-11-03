package com.qxdzbc.p6.app.document.workbook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerErrors
import com.qxdzbc.p6.app.document.worksheet.WsNameGenerator
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * A wb factory that can automatically generate names for new workbooks
 */
class AutoNameWbFactory @Inject constructor(
    val wbContMs: Ms<WorkbookContainer>,
    val wsNameGenerator: WsNameGenerator,
) : WorkbookFactory {
    private var wbCont by wbContMs

    companion object {
        private val namePattern = Pattern.compile("Book[1-9][0-9]*")
    }

    override fun createWbRs(wbName: String?): Result<Workbook, ErrorReport> {
        // extract indices from workbook name that follow the patter.
        // Eg:{ "Book1", "Book2" }-> {1,2}

        val newWbName = wbName?: run{
            val wbIndices = wbContMs.value.wbList
                .map { it.key.name }
                .filter {
                    namePattern.matcher(it).matches()
                }.map {
                    it.substring("Book".length).toInt()
                }
            val nextWbIndex = (wbIndices.maxOrNull() ?: 0) + 1
            "Book${nextWbIndex}"
        }
        val newWbKey = WorkbookKey(newWbName)
        if(wbCont.hasWb(newWbKey)){
            return WorkbookContainerErrors.WorkbookAlreadyExist.report(newWbKey).toErr()
        }else{
            val sheetNameRs = wsNameGenerator.nextName()
            val rt = sheetNameRs.map {
                val wb = WorkbookImp(
                    keyMs = WorkbookKey(newWbName).toMs(),
                )
                val wb2 = wb.createNewWs(it)
                wb2
            }
            return rt
        }
    }
}
