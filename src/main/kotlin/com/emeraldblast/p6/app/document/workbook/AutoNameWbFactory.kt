package com.emeraldblast.p6.app.document.workbook

import com.emeraldblast.p6.di.state.app_state.WbContainerMs
import com.emeraldblast.p6.app.document.wb_container.WorkbookContainer
import com.emeraldblast.p6.app.document.worksheet.WsNameGenerator
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.MsUtils.toMs
import java.util.regex.Pattern
import javax.inject.Inject
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map

/**
 * A wb factory that can automatically generate names for new workbooks
 */
class AutoNameWbFactory @Inject constructor(
    @WbContainerMs
    val wbContMs:Ms<WorkbookContainer>,
    val wsNameGenerator: WsNameGenerator,
) : WorkbookFactory {

    companion object {
        private val namePattern = Pattern.compile("Book[1-9][0-9]*")
    }

    override fun createWbRs(): Result<Workbook, ErrorReport> {
        // extract indices from workbook name that follow the patter.
        // Eg:{ "Book1", "Book2" }-> {1,2}
        val wbIndices = wbContMs.value.wbList
            .map { it.key.name }
            .filter {
                namePattern.matcher(it).matches()
            }.map {
                it.substring("Book".length).toInt()
            }
        val nextWbIndex = (wbIndices.maxOrNull() ?: 0)+1
        val sheetNameRs = wsNameGenerator.nextName()
        val rt = sheetNameRs.map {
            val wb = WorkbookImp(
                keyMs = WorkbookKey("Book${nextWbIndex}").toMs(),
            )
            val wb2 = wb.createNewWs(it)
            wb2
        }
        return rt
    }
}
