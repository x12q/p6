package com.qxdzbc.p6.app.document.wb_container

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.Rse
import com.qxdzbc.common.ErrorUtils.getOrThrow
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.p6.app.document.workbook.Workbook
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.di.state.app_state.WbStateContMs
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.St
import com.qxdzbc.common.compose.StateUtils.toMs
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookState
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory
import com.qxdzbc.p6.ui.document.workbook.state.WorkbookStateFactory.Companion.createRefresh
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.absolute

data class WorkbookContainerImp @Inject constructor(
    @WbStateContMs
    private val wbStateContMs: Ms<WorkbookStateContainer>,
    private val wbStateFactory: WorkbookStateFactory,
) : AbsWorkbookContainer() {

    private var wbStateCont: WorkbookStateContainer by wbStateContMs
    override val wbList: List<Workbook> get() = wbStateCont.allStates.map { it.wb }

    override fun getWbMsRs(wbKeySt: St<WorkbookKey>): Result<Ms<Workbook>, ErrorReport> {
        return wbStateCont.getWbStateRs(wbKeySt).map { it.wbMs }
    }

    override fun getWbMsRs(wbKey:WorkbookKey): Result<Ms<Workbook>,ErrorReport>{
        return this.wbStateCont.getWbStateRs(wbKey).map { it.wbMs }
    }

    override fun getWbMsRs(path: Path): Result<Ms<Workbook>, ErrorReport> {
        val rt:Ms<Workbook>? = this.wbStateCont.allStates.firstOrNull { it.wbKey.path?.absolute() == path.absolute() }?.wbMs
        if (rt != null) {
            return Ok(rt)
        } else {
            return Err(WorkbookContainerErrors.InvalidWorkbook.report(path))
        }
    }

    override fun addWbRs(wb: Workbook): Result<WorkbookContainer, ErrorReport> {
        if (this.wbStateCont.containWbKey(wb.key)) {
            return WorkbookContainerErrors.WorkbookAlreadyExist.report(wb.key).toErr()
        } else {
            val wbMs = ms(wb)
            val wbState: WorkbookState = wbStateFactory.createRefresh(wbMs)
            this.wbStateCont = this.wbStateCont.addWbState(wbState.toMs())
            return Ok(this)
        }
    }

    override fun overwriteWB(wb: Workbook): WorkbookContainer {
        return this.overwriteWBRs(wb).getOrThrow()
    }

    override fun overwriteWBRs(wb: Workbook): Rse<WorkbookContainer> {
        val wbStateMs: Ms<WorkbookState>? = this.wbStateCont.getWbStateMs(wb.key)
        if (wbStateMs != null) {
            val rs: Rse<WorkbookState> = wbStateMs.value.overWriteWbRs(wb)
            when (rs) {
                is Ok -> {
                    wbStateMs.value = rs.value
                    return Ok(this)
                }
                is Err -> return rs
            }
        } else {
            return WorkbookContainerErrors.InvalidWorkbook.report("Workbook at ${wb.key} does not exist, therefore, can't not be replaced")
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

    override fun addOrOverWriteWb(wb: Workbook): WorkbookContainer {
        return addOrOverWriteWbRs(wb).getOrThrow()
    }

    override fun removeWb(wbKey: WorkbookKey): WorkbookContainer {
        return this.removeWbRs(wbKey).getOrThrow()
    }

    override fun removeWbRs(wbKey: WorkbookKey): Rse<WorkbookContainer> {
        //TODO add rs
        this.wbStateCont = this.wbStateCont.removeWbState(wbKey)
        return Ok(this)
    }

    override fun removeAll(): WorkbookContainer {
        this.wbStateCont = this.wbStateCont.removeAll()
        return this
    }

    override fun hasWb(wbKey: WorkbookKey): Boolean {
        return this.wbStateCont.containWbKey(wbKey)
    }

    override fun replaceKey(oldKey: WorkbookKey, newKey: WorkbookKey): WorkbookContainer {
        return replaceKeyRs(oldKey, newKey).getOrThrow()
    }

    override fun replaceKeyRs(oldKey: WorkbookKey, newKey: WorkbookKey): Rse<WorkbookContainer> {
        this.wbStateCont = this.wbStateCont.replaceKey(oldKey, newKey)
        return this.toOk()
    }
}
