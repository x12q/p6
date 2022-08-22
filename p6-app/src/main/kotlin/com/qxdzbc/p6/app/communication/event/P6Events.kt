package com.qxdzbc.p6.app.communication.event

import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetRequest
import com.qxdzbc.p6.app.action.workbook.set_active_ws.SetActiveWorksheetResponse
import com.qxdzbc.p6.app.action.common_data_structure.WorkbookUpdateCommonResponse
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookRequest
import com.qxdzbc.p6.app.action.app.close_wb.CloseWorkbookResponse
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookRequest
import com.qxdzbc.p6.app.action.app.load_wb.LoadWorkbookResponse
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookRequest
import com.qxdzbc.p6.app.action.app.create_new_wb.CreateNewWorkbookResponse
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookRequest
import com.qxdzbc.p6.app.action.app.save_wb.SaveWorkbookResponse
import com.qxdzbc.p6.app.action.cell.cell_multi_update.CellMultiUpdateRequest
import com.qxdzbc.p6.app.action.cell.cell_multi_update.CellMultiUpdateResponse
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateRequest
import com.qxdzbc.p6.app.action.cell.cell_update.CellUpdateResponse
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeRequest
import com.qxdzbc.p6.app.action.range.paste_range.PasteRangeResponse
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardRequest
import com.qxdzbc.p6.app.action.range.range_to_clipboard.RangeToClipboardResponse
import com.qxdzbc.p6.app.action.script.new_script.NewScriptRequest
import com.qxdzbc.p6.app.action.script.new_script.NewScriptResponse
import com.qxdzbc.p6.app.action.script.script_change.ScriptChangeRequest
import com.qxdzbc.p6.app.action.script.script_change.ScriptChangeResponse
import com.qxdzbc.p6.app.action.workbook.new_worksheet.CreateNewWorksheetRequest
import com.qxdzbc.p6.app.action.workbook.new_worksheet.CreateNewWorksheetResponse
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.DeleteWorksheetRequest
import com.qxdzbc.p6.app.action.workbook.delete_worksheet.DeleteWorksheetResponse
import com.qxdzbc.p6.app.action.worksheet.delete_cell.DeleteCellRequest
import com.qxdzbc.p6.app.action.worksheet.delete_cell.DeleteCellResponse
import com.qxdzbc.p6.app.action.worksheet.update_multi_cell.DeleteMultiRequest
import com.qxdzbc.p6.app.action.worksheet.update_multi_cell.DeleteMultiResponse
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetRequest
import com.qxdzbc.p6.app.action.worksheet.rename_ws.RenameWorksheetResponse
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.qxdzbc.p6.message.api.connection.service.zmq_services.msg.P6Event
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok

object P6Events {

    val unknown = P6Event("__0__", "Unknown event")
    val allP6EventDefs = run {
        val l = this::class.nestedClasses.mapNotNull {
            val oi = it.objectInstance
            if (oi != null && oi is WithEventDefList) {
                val z = oi.defList
                z
            } else {
                null
            }
        }.flatten()
        l
    }

    object Script:WithEventDefList{
        val SCRE = "SCRIPT_EVENT_"
        object ScriptChange:P6EventMetaDef{
            override val event: P6Event = P6Event("${SCRE}0","Script change")
            override val Request: Any? = ScriptChangeRequest::class
            override val Response: Any = ScriptChangeResponse::class
        }
        object NewScript:P6EventMetaDef{
            override val event: P6Event = P6Event("${SCRE}1","new script")
            override val Request: Any? = NewScriptRequest::class
            override val Response: Any = NewScriptResponse::class
        }
    }

    object Range : WithEventDefList {
        val RE = "RANGE_EVENT_"

        object RangeToClipboard : P6EventMetaDef {
            override val event = P6Event("${RE}1", "Range to clipboard")
            override val Request: Any = RangeToClipboardRequest::class
            override val Response: Any = RangeToClipboardResponse::class
        }

        object PasteRange : P6EventMetaDef {
            override val event: P6Event = P6Event("${RE}2", "Range to clipboard")
            override val Request: Any = PasteRangeRequest::class
            override val Response: Any = PasteRangeResponse::class
        }

        override val defList: List<P6EventMetaDef> = extractP6EventDef(this)
    }

    object Cell : WithEventDefList {
        private val CE = "CELL_EVENT"

        object Update : P6EventMetaDef {
            override val event = P6Event("${CE}1", "cell update")
            override val Request: Any = CellUpdateRequest::class
            override val Response: Any = CellUpdateResponse::class
        }

        object CellMultiUpdate : P6EventMetaDef {
            override val event = P6Event("${CE}2", "cell multi update")
            override val Request: Any = CellMultiUpdateRequest::class
            override val Response: Any = CellMultiUpdateResponse::class
        }

        override val defList: List<P6EventMetaDef> = extractP6EventDef(this)
    }

    object Worksheet : WithEventDefList {
        private val WSE = "WORKSHEET_EVENT"

        object Rename : P6EventMetaDef {
            override val event = P6Event("${WSE}2", "rename worksheet")
            override val Request: Any = RenameWorksheetRequest::class
            override val Response: Any = RenameWorksheetResponse::class
        }

        object DeleteCell : P6EventMetaDef {
            override val event = P6Event("${WSE}3", "Delete cell")
            override val Request: Any = DeleteCellRequest::class
            override val Response: Any = DeleteCellResponse::class

        }

        object DeleteMulti : P6EventMetaDef {
            override val event = P6Event("${WSE}4", "Delete multi")
            override val Request: Any = DeleteMultiRequest::class
            override val Response: Any = DeleteMultiResponse::class
        }

        override val defList: List<P6EventMetaDef> = extractP6EventDef(this)
    }

    object Workbook : WithEventDefList {
        private val WBE = "WORKBOOK_EVENT"
        override val defList = extractP6EventDef(this)

        object Update:P6EventMetaDef{
            override val event = P6Event("${WBE}0", "Workbook update event")
            override val Request: Any? = null
            override val Response: Any = WorkbookUpdateCommonResponse::class
        }

        object CreateNewWorksheet : P6EventMetaDef {
            override val event = P6Event("${WBE}3", "create new worksheet")
            override val Request: Any = CreateNewWorksheetRequest::class
            override val Response: Any = CreateNewWorksheetResponse::class

        }

        object DeleteWorksheet : P6EventMetaDef {
            override val event = P6Event("${WBE}2", "remove worksheet")
            override val Request: Any = DeleteWorksheetRequest::class
            override val Response: Any = DeleteWorksheetResponse::class
        }
    }

    object App : WithEventDefList {
        private val APPE = "APP_EVENT_" // app event

        object SetActiveWorksheet : P6EventMetaDef {
            override val event = P6Event("${APPE}0", "Set active worksheet")
            override val Request = SetActiveWorksheetRequest::class
            override val Response = SetActiveWorksheetResponse::class
        }

        object SaveWorkbook : P6EventMetaDef {
            override val event = P6Event("${APPE}1", "save workbook")
            override val Request = SaveWorkbookRequest::class
            override val Response = SaveWorkbookResponse::class
        }

        object LoadWorkbook : P6EventMetaDef {
            override val event = P6Event("${APPE}2", "load workbook")
            override val Request = LoadWorkbookRequest::class
            override val Response = LoadWorkbookResponse::class
        }

        object CreateNewWorkbook : P6EventMetaDef {
            override val event = P6Event("${APPE}3", "create new workbook")
            override val Request = CreateNewWorkbookRequest::class
            override val Response = CreateNewWorkbookResponse::class
        }

        object CloseWorkbook : P6EventMetaDef {
            override val event = P6Event("${APPE}4", "close workbook")
            override val Request = CloseWorkbookRequest::class
            override val Response = CloseWorkbookResponse::class
        }

        override val defList: List<P6EventMetaDef> = extractP6EventDef(this)
    }

    fun checkEvent(inputEvent: P6Event, targetEvent: P6Event, handlerName: String) {
        if (inputEvent != targetEvent) {
            throw RuntimeException("Misaligned event. $handlerName is only for ${targetEvent.code} - ${targetEvent.name} ")
        }
    }

    fun checkEventRs(actual: P6Event, expect: P6Event): com.github.michaelbull.result.Result<Unit, ErrorReport> {
        if (actual != expect) {
            return Err(P6EventErrors.wrongEventError(expect, actual))
        } else {
            return Ok(Unit)
        }
    }
}

fun P6Event.isUnknown(event: P6Event): Boolean {
    return event == P6Events.unknown
}

interface WithEventDefList {
    val defList: List<P6EventMetaDef> get()=extractP6EventDef(this)
}

private fun extractP6EventDef(o: Any): List<P6EventMetaDef> {
    return o::class.nestedClasses
        .mapNotNull {
            if (it.objectInstance is P6EventMetaDef) {
                val z = it.objectInstance as P6EventMetaDef
                z
            } else {
                null
            }
        }
}
