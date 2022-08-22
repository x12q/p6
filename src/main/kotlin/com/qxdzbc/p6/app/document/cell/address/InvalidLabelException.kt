package com.qxdzbc.p6.app.document.cell.address

class InvalidLabelException(val label:String) : RuntimeException("label $label is invalid") {
}
