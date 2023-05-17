package com.qxdzbc.p6

import com.qxdzbc.p6.app.document.cell.CellErrors
import com.qxdzbc.p6.app.document.range.copy_paste.ClipboardReaderErrors
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnitErrors
import com.qxdzbc.p6.ui.file.P6FileSaverErrors

/**
 * Run all the cold init. The purpose of cold init is to load classes into memory, so that later they can be found by the runtime.
 * This is required, so that when the app's stack is overflowed by running formulas, NoClassDef exceptions are not rised.
 */
class ColdInit {
    val exunitErr = ExUnitErrors
    init{
        CellErrors.coldInit()
        ExUnitErrors.coldInit()
        P6FileSaverErrors.coldInit()
        ClipboardReaderErrors.coldInit()
    }
}
