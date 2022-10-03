package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import org.apache.commons.text.diff.CommandVisitor

class CharCommandsVisitor : CommandVisitor<Char> {
    private var _addition = ""
    private var _hasAdditionOnly = true
    val addition get()=_addition
    val hasAdditionOnly get() = _hasAdditionOnly
    override fun visitKeepCommand(c: Char) {
    }

    override fun visitInsertCommand(c: Char) {
        _addition +=c
    }

    override fun visitDeleteCommand(c: Char) {
        _hasAdditionOnly = false
    }
}
