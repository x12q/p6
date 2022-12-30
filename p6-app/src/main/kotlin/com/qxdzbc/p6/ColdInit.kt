package com.qxdzbc.p6

import com.qxdzbc.p6.app.document.cell.CellErrors
import com.qxdzbc.p6.app.document.range.copy_paste.ClipboardReaderErrors
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnitErrors
import com.qxdzbc.p6.ui.file.P6FileSaverErrors

class ColdInit {
    val exunitErr = ExUnitErrors
    init{
        CellErrors.coldInit()
        ExUnitErrors.coldInit()
        P6FileSaverErrors.coldInit()
        ClipboardReaderErrors.coldInit()
    }
}
