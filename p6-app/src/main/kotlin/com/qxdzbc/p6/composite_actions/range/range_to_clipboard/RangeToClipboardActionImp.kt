package com.qxdzbc.p6.composite_actions.range.range_to_clipboard

import com.github.michaelbull.result.*
import com.qxdzbc.p6.composite_actions.range.RangeId
import com.qxdzbc.p6.common.err.ErrorReportWithNavInfo.Companion.withNav
import com.qxdzbc.p6.common.utils.RseNav
import com.qxdzbc.p6.document_data_layer.range.copy_paste.RangeCopier
import com.qxdzbc.p6.di.P6AnvilScope
import com.qxdzbc.p6.ui.app.error_router.ErrorRouter
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class RangeToClipboardActionImp @Inject constructor(
    val sc:StateContainer,
    private val errorRouter: ErrorRouter,
    private val copier: RangeCopier,
    val docCont: DocumentContainer,
) : RangeToClipboardAction {

    override fun rangeToClipboard(request: RangeToClipboardRequest): RseNav<RangeToClipboardResponse2> {
        val res = copyRangeToClipboard2(request)
        return applyRes(res)
    }

    fun copyRangeToClipboard2(request: RangeToClipboardRequest): RseNav<RangeToClipboardResponse2> {
        val rt = docCont
            .getRangeRs(request.rangeId)
            .flatMap { range ->
                val copyRs = this.copier.copyRange(range)
                copyRs
            }.map {
                RangeToClipboardResponse2(
                    range = it,
                    windowId = request.windowId
                )
            }.mapError {
                it.withNav(request)
            }
        return rt
    }


    fun applyRes(res: RseNav<RangeToClipboardResponse2>): RseNav<RangeToClipboardResponse2> {
        res.onFailure {
            errorRouter.publish(it)
        }.onSuccess {
            apply(it.range.rangeId,it.windowId)
        }
        return res
    }

    fun apply(rangeId: RangeId, windowId: String?) {
        // find the correct cursor state ms
        val cursorStateMs = sc
            .getWbState(rangeId.wbKey)
            ?.getWsState(rangeId.wsName)
            ?.cursorStateMs
        if(cursorStateMs!=null){
            cursorStateMs.value =cursorStateMs.value.setClipboardRange(
                rangeId.rangeAddress
            )
        }
    }
}
