package com.emeraldblast.p6.app.document.wb_container

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.emeraldblast.p6.app.common.Rse
import com.emeraldblast.p6.app.common.utils.ErrorUtils.getOrThrow
import com.emeraldblast.p6.app.common.utils.ResultUtils.toOk
import com.emeraldblast.p6.app.document.workbook.Workbook
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.emeraldblast.p6.di.state.app_state.WbStateContMs
import com.emeraldblast.p6.ui.common.compose.Ms
import com.emeraldblast.p6.ui.common.compose.StateUtils.toMs
import com.emeraldblast.p6.ui.common.compose.ms
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookState
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookStateFactory
import com.emeraldblast.p6.ui.document.workbook.state.WorkbookStateFactory.Companion.createRefresh
import com.emeraldblast.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.absolute

data class WorkbookContainerImp2 @Inject constructor(
    @WbStateContMs
    private val wbStateContMs: Ms<WorkbookStateContainer>,
    private val wbStateFactory: WorkbookStateFactory,
) : BaseWorkbookContainer() {
    private var wbStateCont: WorkbookStateContainer by wbStateContMs
    override val wbList: List<Workbook> get() = wbStateCont.allStates.map { it.wb!! }
    override fun getWbMs(wbKey: WorkbookKey): Ms<Workbook>? {
        return this.wbStateCont.getWbState(wbKey)?.wbMs
    }

    private val map get() = wbList.associateBy { it.key }

    override fun getWbRs(wbKey: WorkbookKey): Result<Workbook, ErrorReport> {
        val wb = this.map[wbKey]
        return wb?.let { Ok(wb) } ?: Err(WorkbookContainerErrors.InvalidWorkbook.report(wbKey))
    }

    override fun getWbRs(path: Path): Result<Workbook, ErrorReport> {
        for (wb in this.wbList) {
            if (wb.key.path?.absolute() == path.absolute()) {
                return Ok(wb)
            }
        }
        return Err(WorkbookContainerErrors.InvalidWorkbook.report(path))
    }


    override fun addWbRs(wb: Workbook): Result<WorkbookContainer, ErrorReport> {
        if (wb.key in map.keys) {
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
        val wbStateMs = this.wbStateCont.getWbStateMs(wb.key)
        if (wbStateMs != null) {
            val rs = wbStateMs.value.overWriteWbRs(wb)
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
        val addRs=this.addWbRs(wb)
        when(addRs) {
            is Ok-> return addRs
            is Err -> {
                val overWriteRs=this.overwriteWBRs(wb)
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
        this.wbStateCont = this.wbStateCont.replaceKey(oldKey,newKey)
        return this.toOk()
    }
}
