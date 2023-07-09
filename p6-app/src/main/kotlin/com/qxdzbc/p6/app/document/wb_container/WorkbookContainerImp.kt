package com.qxdzbc.p6.app.document.wb_container

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.Rse
import com.qxdzbc.common.ErrorUtils.getOrThrow
import com.qxdzbc.common.ResultUtils.toOk
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

data class WorkbookContainerImp @Inject constructor(
    private val wbStateContMs: WorkbookStateContainer,
    private val wbStateFactory: WorkbookStateFactory,
) : AbsWorkbookContainer() {

    private var wbStateCont: WorkbookStateContainer = wbStateContMs

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

    override fun addWbRs(wb: Workbook): Rse<WorkbookContainer> {
        if (this.wbStateCont.containWbKey(wb.key)) {
            return WorkbookContainerErrors.WorkbookAlreadyExist.report2("Can't add workbook because workbook at ${wb.key} already exist").toErr()
        } else {
            val wbMs = ms(wb)
            val wbState: WorkbookState = wbStateFactory.createAndRefresh(wbMs)
            this.wbStateCont = this.wbStateCont.addOrOverwriteWbState(wbState)
            return Ok(this)
        }
    }
    @kotlin.jvm.Throws(Exception::class)
    override fun overwriteWB(wb: Workbook): WorkbookContainer {
        return this.overwriteWBRs(wb).getOrThrow()
    }

    override fun overwriteWBRs(wb: Workbook): Rse<WorkbookContainer> {
        val wbStateMs: WorkbookState? = this.wbStateCont.getWbState(wb.key)
        if (wbStateMs != null) {
            val rs: Rse<Unit> = wbStateMs.overWriteWbRs(wb)
            return rs.map { this }
        } else {
            return WorkbookContainerErrors.InvalidWorkbook.report("Workbook at ${wb.key} does not exist, therefore, can't not be overwritten")
                .toErr()
        }
    }

    override fun addOrOverWriteWbRs(wb: Workbook): Rse<WorkbookContainer> {
        val addRs = this.addWbRs(wb)
        when (addRs) {
            is Ok -> return addRs
            is Err -> {
                val overWriteRs = this.overwriteWBRs(wb)
                return overWriteRs
            }
        }
    }
    @kotlin.jvm.Throws(Exception::class)
    override fun addOrOverWriteWb(wb: Workbook): WorkbookContainer {
        return addOrOverWriteWbRs(wb).getOrThrow()
    }
    @kotlin.jvm.Throws(Exception::class)
    override fun removeWb(wbKey: WorkbookKey): WorkbookContainer {
        return this.removeWbRs(wbKey).getOrThrow()
    }

    override fun removeWbRs(wbKey: WorkbookKey): Rse<WorkbookContainer> {
        this.wbStateCont = this.wbStateCont.removeWbState(wbKey)
        return Ok(this)
    }

    override fun removeAll(): WorkbookContainer {
        this.wbStateCont = this.wbStateCont.removeAll()
        return this
    }

    override fun containWb(wbKey: WorkbookKey): Boolean {
        return this.wbStateCont.containWbKey(wbKey)
    }
    @kotlin.jvm.Throws(Exception::class)
    override fun replaceKey(oldKey: WorkbookKey, newKey: WorkbookKey): WorkbookContainer {
        return replaceKeyRs(oldKey, newKey).getOrThrow()
    }

    override fun replaceKeyRs(oldKey: WorkbookKey, newKey: WorkbookKey): Rse<WorkbookContainer> {
        this.wbStateCont = this.wbStateCont.replaceKey(oldKey, newKey)
        return this.toOk()
    }
}
