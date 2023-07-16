package com.qxdzbc.p6.app.document.wb_container

import com.qxdzbc.common.Rse
import com.qxdzbc.common.ErrorUtils.getOrThrow
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory.Companion.createAndRefresh
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.absolute

/**
 * implementation of [WorkbookContainer] that stands on top of a [WorkbookStateContainer], and provides a more limited set of function to query and update workbook. Workbook state is update along workbook operations.
 */
data class WorkbookContainerImp @Inject constructor(
    private val wbStateCont: WorkbookStateContainer,
    private val wbStateFactory: WorkbookStateFactory,
) : AbsWorkbookContainer() {

    override val allWbs: List<Workbook> get() = wbStateCont.allWbStates.map { it.wb }

    override val allWbMs: List<Ms<Workbook>>
        get() = wbStateCont.allWbStates.map{it.wbMs}

    override fun getWbMsRs(wbKeySt: St<WorkbookKey>): Rse<Ms<Workbook>> {
        return wbStateCont.getWbStateRs(wbKeySt).map { it.wbMs }
    }

    override fun getWbMsRs(wbKey:WorkbookKey): Rse<Ms<Workbook>>{
        return this.wbStateCont.getWbStateRs(wbKey).map { it.wbMs }
    }

    override fun getWbMsRs(path: Path): Result<Ms<Workbook>, SingleErrorReport> {
        val rt:Ms<Workbook>? = this.wbStateCont.allWbStates.firstOrNull { it.wbKey.path?.absolute() == path.absolute() }?.wbMs
        if (rt != null) {
            return Ok(rt)
        } else {
            return Err(WorkbookContainerErrors.InvalidWorkbook.report(path))
        }
    }

    override fun addWbRs(wb: Workbook): Rse<Unit> {
        if (this.wbStateCont.containWbKey(wb.key)) {
            return WorkbookContainerErrors.WorkbookAlreadyExist.report2("Can't add workbook because workbook at ${wb.key} already exist").toErr()
        } else {
            val wbMs = ms(wb)
            val wbState: WorkbookState = wbStateFactory.createAndRefresh(wbMs)
            this.wbStateCont.addOrOverwriteWbState(wbState)
            return Ok(Unit)
        }
    }

    @Throws(Exception::class)
    override fun overwriteWb(wb: Workbook) {
        return this.overwriteWbRs(wb).getOrThrow()
    }

    override fun overwriteWbRs(wb: Workbook): Rse<Unit> {
        val wbStateMs: WorkbookState? = this.wbStateCont.getWbState(wb.key)
        if (wbStateMs != null) {
            val rs: Rse<Unit> = wbStateMs.overWriteWbRs(wb)
            return rs
        } else {
            return WorkbookContainerErrors.InvalidWorkbook.report("Workbook at ${wb.key} does not exist, therefore, can't not be overwritten")
                .toErr()
        }
    }

    override fun addOrOverWriteWbRs(wb: Workbook): Rse<Unit> {
        val addRs = this.addWbRs(wb)
        when (addRs) {
            is Ok -> return addRs
            is Err -> {
                val overWriteRs = this.overwriteWbRs(wb)
                return overWriteRs
            }
        }
    }
    @Deprecated("do not use this function. It is kept for reference purposes only. The action of forced overwriting a workbook is very destructive. Must be considered careful before use")
    @Throws(Exception::class)
    override fun addOrOverWriteWb(wb: Workbook) {
        return addOrOverWriteWbRs(wb).getOrThrow()
    }
    @Throws(Exception::class)
    override fun removeWb(wbKey: WorkbookKey) {
        return this.removeWbRs(wbKey).getOrThrow()
    }

    override fun removeWbRs(wbKey: WorkbookKey): Rse<Unit> {
        this.wbStateCont.removeWbState(wbKey)
        return Ok(Unit)
    }

    @Deprecated("don't use. It is dangerous to use this function because it creates inconsistency in the app state.")
    override fun removeAll() {
        this.wbStateCont.removeAll()
    }

    override fun containWb(wbKey: WorkbookKey): Boolean {
        return this.wbStateCont.containWbKey(wbKey)
    }
    @Throws(Exception::class)
    override fun replaceKey(oldKey: WorkbookKey, newKey: WorkbookKey) {
        return replaceKeyRs(oldKey, newKey).getOrThrow()
    }

    override fun replaceKeyRs(oldKey: WorkbookKey, newKey: WorkbookKey): Rse<Unit> {
        return this.wbStateCont.replaceKeyRs(oldKey,newKey)
    }
}
