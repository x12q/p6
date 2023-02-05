package com.qxdzbc.p6.app.document.workbook

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.github.michaelbull.result.map
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainer
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerErrors
import com.qxdzbc.p6.app.document.worksheet.WsNameGenerator
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * A wb factory that can automatically generate names for new workbooks
 */
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class AutoNameWbFactory @Inject constructor(
    val wbContMs: Ms<WorkbookContainer>,
    val wsNameGenerator: WsNameGenerator,
) : WorkbookFactory {
    private var wbCont by wbContMs

    companion object {
        private val namePattern = Pattern.compile("Book[1-9][0-9]*")
    }

    override fun createWbRs(wbName: String?): Rse<Workbook> {
        // extract indices from workbook name that conforms with the pattern "Book<num>".
        // Eg:{ "Book1", "Book2" }-> {1,2}

        val newWbName = wbName?: run{
            val wbIndices = wbContMs.value.allWbs
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
        if(wbCont.containWb(newWbKey)){
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
