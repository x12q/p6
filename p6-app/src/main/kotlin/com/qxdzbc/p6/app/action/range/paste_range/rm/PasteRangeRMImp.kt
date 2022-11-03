package com.qxdzbc.p6.app.action.range.paste_range.rm

import androidx.compose.runtime.getValue
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeRequest2
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeResponse
import com.qxdzbc.p6.app.document.range.copy_paste.RangePaster
import com.github.michaelbull.result.mapBoth
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope

import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class PasteRangeRMImp @Inject constructor(
    private val stateContSt:St<@JvmSuppressWildcards StateContainer>,
    private val paster: RangePaster
) : PasteRangeRM {

    val stateCont by stateContSt

    override fun pasteRange(request: PasteRangeRequest2): PasteRangeResponse? {
        val wsState = stateCont.getWbState(request.wbKey)?.getWsState(request.wsName)
        if(wsState!=null){
            val target = RangeIdImp(
                rangeAddress = request.rangeId.rangeAddress,
                wbKeySt = wsState.wbKeySt,
                wsNameSt =wsState.wsNameSt
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
