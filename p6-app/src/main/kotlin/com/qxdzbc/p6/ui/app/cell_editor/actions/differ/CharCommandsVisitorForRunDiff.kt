package com.qxdzbc.p6.ui.app.cell_editor.actions.differ

import org.apache.commons.text.diff.CommandVisitor

class CharCommandsVisitorForRunDiff : CommandVisitor<Char> {
    var indexOfFirst = -1
    var indexOfSecond = -1

    private var _addition:String? = null
    var addFrom:Int?=null
    var addTo:Int?=null

    private var _hasDelete = false
    var deleteFrom:Int? = null
    var deleteTo:Int?=null

    val addition get()=_addition
    val hasDelete get()=_hasDelete

    /**
     * c from 1st
     */
    override fun visitKeepCommand(c: Char) {
        indexOfFirst+=1
        indexOfSecond+=1
    }

    /**
     * c from 2nd
     */
    override fun visitInsertCommand(c: Char) {
        indexOfSecond+=1
        if(_addition==null){
            _addition = ""
        }
        if(addFrom==null){
            addFrom = indexOfSecond
            addTo = indexOfSecond
        }else{
            addTo = indexOfSecond
        }

        _addition +=c
    }

    /**
     * c from 1st
     */
    override fun visitDeleteCommand(c: Char) {
        indexOfFirst+=1
        _hasDelete = true
        if(deleteFrom==null){
            deleteFrom = indexOfFirst
            deleteTo = indexOfFirst
        }else{
            deleteTo = indexOfFirst
        }
    }
}
