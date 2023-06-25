package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import org.apache.commons.text.diff.CommandVisitor

class CharCommandsVisitor : CommandVisitor<Char> {
    private var _addition:String? = null
//    private var addFrom:Int?=null
//    private var addTo:Int?=null

    private var _hasDelete = false
//    private var deleteFrom:Int? = null
//    private var deleteTo:Int?=null

    val addition get()=_addition
    val hasDelete get()=_hasDelete

    override fun visitKeepCommand(c: Char) {
    }

    override fun visitInsertCommand(c: Char) {
        if(_addition==null){
            _addition = ""

        }
        _addition +=c
    }

    override fun visitDeleteCommand(c: Char) {
        _hasDelete = true
        _addition = null
    }
}
