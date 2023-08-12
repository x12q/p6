package com.qxdzbc.p6.document_data_layer.cell.address

class InvalidLabelException(val label:String) : RuntimeException("label $label is invalid") {
}
