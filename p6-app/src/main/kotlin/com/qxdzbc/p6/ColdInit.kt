package com.qxdzbc.p6

import com.qxdzbc.p6.app.document.cell.CellErrors
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnitErrors

class ColdInit {
    val exunitErr = ExUnitErrors
    init{
        CellErrors.coldInit()
        ExUnitErrors.coldInit()
    }
}
