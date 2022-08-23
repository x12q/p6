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
import javax.inject.Inject

class PasteRangeRMImp @Inject constructor(
    @AppStateMs private val appStateMs: Ms<AppState>,
    private val paster: RangePaster
) : PasteRangeRM {

    private var appState by appStateMs

    override fun pasteRange(request: PasteRangeRequest): PasteRangeResponse? {
        val wsState = appState.getWbState(request.wbWs.wbKey)?.getWsState(request.wbWs.wsName)
        if(wsState!=null){
            val target = RangeId(
                rangeAddress = RangeAddress(request.anchorCell),
                wbKey = request.wbWs.wbKey,
                wsName = request.wbWs.wsName
            )
            val pasteRs = paster.paste(target)
            val rt = pasteRs.mapBoth(
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

    override fun pasteRange2(request: PasteRangeRequest2): PasteRangeResponse? {
        val wsState = appState.getWbState(request.wbKey)?.getWsState(request.wsName)
        if(wsState!=null){
            val target = RangeId(
                rangeAddress = request.rangeId.rangeAddress,
                wbKey = request.wbKey,
                wsName = request.wsName
            )
            val pasteRs = paster.paste(target)
            val rt = pasteRs.mapBoth(
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
