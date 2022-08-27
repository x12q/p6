package com.qxdzbc.p6.app.action.range.paste_range.rm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.app.action.range.RangeId
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeRequest
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeRequest2
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeResponse
import com.qxdzbc.p6.di.state.app_state.AppStateMs
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.copy_paste.RangePaster
import com.qxdzbc.p6.ui.app.state.AppState
import com.qxdzbc.common.compose.Ms
import com.github.michaelbull.result.mapBoth
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.document.range.address.RangeAddresses
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.state.app_state.StateContainerSt
import com.qxdzbc.p6.ui.app.state.StateContainer
import javax.inject.Inject

class PasteRangeRMImp @Inject constructor(
    @StateContainerSt private val stateContSt:St<@JvmSuppressWildcards StateContainer>,
    private val paster: RangePaster
) : PasteRangeRM {

    val stateCont by stateContSt

    override fun pasteRange(request: PasteRangeRequest2): PasteRangeResponse? {
        val wsState = stateCont.getWbState(request.wbKey)?.getWsState(request.wsName)
        if(wsState!=null){
            val target = RangeId(
                rangeAddress = request.rangeId.rangeAddress,
                wbKey = request.wbKey,
                wsName = request.wsName
            )
            val pasteRs:Rse<Workbook> = paster.paste(target)
            val rt:PasteRangeResponse = pasteRs.mapBoth(
                success = { wb ->
                    PasteRangeResponse(
                        WorkbookUpdateCommonResponse(
                            wbKey = request.wbKey,
                            newWorkbook = wb,
                            windowId = request.windowId
                        )
                    )
                },
                failure = { er ->
                    PasteRangeResponse(
                        WorkbookUpdateCommonResponse(
                            wbKey = request.wbKey,
                            errorReport = er,
                            windowId = request.windowId
                        )
                    )
                }
            )
            return rt
        }
        return null
    }
}
