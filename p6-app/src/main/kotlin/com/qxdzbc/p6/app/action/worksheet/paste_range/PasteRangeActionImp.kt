package com.qxdzbc.p6.app.action.worksheet.paste_range

import androidx.compose.runtime.getValue
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.onSuccess
import com.qxdzbc.common.Rse
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.applier.WorkbookUpdateCommonApplier
import com.qxdzbc.p6.app.action.cell.update_cell_format.UpdateCellFormatAction
import com.qxdzbc.p6.app.action.common_data_structure.WbWs
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.app.action.range.RangeIdImp
import com.qxdzbc.p6.app.command.BaseCommand
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.p6.app.document.range.copy_paste.RangePaster
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.DocumentContainer
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class PasteRangeActionImp @Inject constructor(
    private val stateContSt: St<@JvmSuppressWildcards StateContainer>,
    private val paster: RangePaster,
    private val wbUpdateApplier: WorkbookUpdateCommonApplier,
    val docContMs: Ms<DocumentContainer>,
    val updateCellFormatAction: UpdateCellFormatAction,
) : PasteRangeAction {

    private val sc by stateContSt
    private val dc by docContMs

    override fun pasteRange(wbwsSt: WbWsSt, rangeAddress: RangeAddress, undo: Boolean): Rse<Unit> {
        val wbkSt = wbwsSt.wbKeySt
        val wsNameSt = wbwsSt.wsNameSt
        val windowId = sc.getWindowStateMsByWbKey(wbwsSt.wbKey)?.value?.id
        val wsState = sc.getWsState(wbwsSt)

        if (wsState != null) {
            val target = RangeIdImp(
                rangeAddress = rangeAddress,
                wbKeySt = wsState.wbKeySt,
                wsNameSt = wsState.wsNameSt
            )
            val pasteResponse = paster.paste2(target)
            val pasteRs: Rse<Workbook> = pasteResponse.rs

            pasteRs.onSuccess {
                val wbKey = wbwsSt.wbKey
                val updateWbRequest = WorkbookUpdateCommonResponse(
                    wbKey = wbKey,
                    newWorkbook = it,
                    windowId = windowId
                )
                val command = object : BaseCommand() {
                    val reverseRes = WorkbookUpdateCommonResponse(
                        errorReport = null,
                        wbKey = wbKey,
                        newWorkbook = wbKey.let { dc.getWb(it) },
                        windowId = windowId
                    )
                    val _updateRequest = updateWbRequest
                    val sourceRangeId = pasteResponse.sourceRangeId
                    val targetRangeId = target

                    val sourceFormat = sourceRangeId
                        ?.let { sc.getCellFormatTable(it) }
                        ?.getFormatConfigForRange(sourceRangeId.rangeAddress)
                        ?.shift(sourceRangeId.rangeAddress.topLeft,targetRangeId.rangeAddress.topLeft)
                    val targetFormat = sc.getCellFormatTable(targetRangeId)
                        ?.getFormatConfigForRange(targetRangeId.rangeAddress)
                    override fun run() {
                        wbUpdateApplier.apply(_updateRequest)
                        targetFormat?.also {
                            updateCellFormatAction.clearFormat(targetRangeId, targetFormat, false)
                        }
                        sourceFormat?.also {
                            updateCellFormatAction.applyFormatConfig(targetRangeId, sourceFormat, false)
                        }
                    }

                    override fun undo() {
                        wbUpdateApplier.apply(reverseRes)
                        sourceFormat?.also {
                            updateCellFormatAction.clearFormat(targetRangeId, sourceFormat, false)
                        }
                        targetFormat?.also {
                            updateCellFormatAction.applyFormatConfig(targetRangeId, targetFormat, false)
                        }
                    }
                }

                if (undo) {
                    val commandStackMs = sc.getCommandStackMs(wbwsSt)
                    if (commandStackMs != null) {
                        commandStackMs.value = commandStackMs.value.add(command)
                    }
                }
                command.run()
            }
        }

        return Ok(Unit)
    }

    override fun pasteRange(wbws: WbWs, rangeAddress: RangeAddress, undo: Boolean): Rse<Unit> {
        val rt = sc.getWbWsStRs(wbws).flatMap {
            this.pasteRange(it, rangeAddress, undo)
        }
        return rt
    }
}
