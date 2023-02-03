package com.qxdzbc.p6.app.document.worksheet

import com.qxdzbc.p6.app.document.cell.address.CellAddress
import com.qxdzbc.p6.app.document.range.address.RangeAddress
import com.qxdzbc.common.error.ErrorHeader
import com.qxdzbc.common.error.SingleErrorReport
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.rpc.cell.msg.CellIdDM
import com.qxdzbc.p6.ui.document.worksheet.state.RangeConstraint

object WorksheetErrors {
    val UI_WSErr = "UI_WorksheetErrors_"
    object InvalidCell {
        val header = ErrorHeader(
            errorCode = "${UI_WSErr}0",
            errorDescription = "Cell address is pointing to a non-existing cell"
        )
        fun report(cellAddress: CellAddress): ErrorReport {
            return header
                .setDescription("Cell address ${cellAddress.label} is invalid")
                .toErrorReport()
        }

        fun report(detail:String?): ErrorReport {
            return header
                .let {
                    detail?.let {
                        header.setDescription(it)
                    }?: header
                }
                .toErrorReport()
        }
    }
    fun InvalidCell(cellAddress: CellAddress): SingleErrorReport {
        return SingleErrorReport(
            header = ErrorHeader(
                errorCode = "${UI_WSErr}0",
                errorDescription = "Cell address ${cellAddress.label} is invalid"
            )
        )
    }

    val CantCopyOnFragmentedSelection = SingleErrorReport(
        header = ErrorHeader(
            errorCode = "${UI_WSErr}1",
            errorDescription = "Can't perform copy over fragmented selection"
        )
    )

    object InvalidRange {
        val header = ErrorHeader(
            errorCode = "${UI_WSErr}2",
            errorDescription = "range is not in worksheet's constraint"
        )

        fun report(rangeAddress: RangeAddress, constraint:RangeConstraint):ErrorReport{
            return header.setDescription("range ${rangeAddress} is not within worksheet constraint of ${constraint} ").toErrorReport()
        }
    }

    object WSNotBelongToAWB{
        val header = ErrorHeader(
            errorCode = "${UI_WSErr}3",
            errorDescription = "worksheet does not belong to a workbook"
        )
        fun report(wsName:String):ErrorReport{
            return header.setDescription("Worksheet ${wsName} does not belong to any workbook. It must belong to a workbook so that performing calculation on it to be possible.").toErrorReport()
        }
    }

    object UnableToUpdateCell{
        val header = ErrorHeader(
            errorCode = "${UI_WSErr}4",
            errorDescription = "unable to update cell"
        )
        fun report(cellId:CellIdDM):ErrorReport{
            return header.setDescription("Unable to update cell at ${cellId}").toErrorReport()
        }
    }
}
