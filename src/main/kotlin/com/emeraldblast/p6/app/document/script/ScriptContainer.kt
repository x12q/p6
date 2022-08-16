package com.emeraldblast.p6.app.document.script

import com.emeraldblast.p6.app.common.Rse
import com.emeraldblast.p6.app.document.workbook.WorkbookKey
import com.emeraldblast.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result
interface ScriptContainer :Map<String,String>{
    fun getScript(name: String): String?
    fun getScriptRs(name: String): Rse<String>
    fun overwriteScriptRs(name:String, script: String): Result<ScriptContainer,ErrorReport>
    fun overwriteScript(name:String, script: String): ScriptContainer
    fun overwriteScript(scriptEntry: ScriptEntry): ScriptContainer
    fun addScriptRs(name:String, script:String):Result<ScriptContainer,ErrorReport>
    fun addScript(name:String, script:String):ScriptContainer
    fun overwriteAllScriptRs(scripts:List<SimpleScriptEntry>): Result<ScriptContainer,ErrorReport>
    fun overwriteAllScript(scripts:List<SimpleScriptEntry>): ScriptContainer
    fun containScript(scriptName:String):Boolean
    fun removeScript(name: String): ScriptContainer
    fun removeAllScript(): ScriptContainer
    val map:Map<String, String>
    val allScripts: Collection<SimpleScriptEntry>
    /**
     * give all script entry in this container a workbook key
     */
    fun allAsScriptEntry(wbKey: WorkbookKey?): Collection<ScriptEntry>
    fun renameScript(oldName: String, newName: String): ScriptContainer
}
