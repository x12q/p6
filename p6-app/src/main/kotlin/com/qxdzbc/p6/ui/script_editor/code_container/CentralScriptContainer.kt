package com.qxdzbc.p6.ui.script_editor.code_container

import com.qxdzbc.p6.app.common.utils.Rse
import com.qxdzbc.p6.app.document.script.ScriptEntry
import com.qxdzbc.p6.app.document.script.ScriptEntryKey
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.p6.common.exception.error.ErrorReport
import com.github.michaelbull.result.Result

/**
 * a central point to access scripts of both app and wbs
 */
interface CentralScriptContainer {

    /**
     * Replace a key with another key.
     * Update the respective entry with the new key too.
     * Preserve the order of the entries.
     * @return [ErrorReport] if unable to replace the [oldKey] with the [newKey]. Otherwise return a new container
     */
    fun replaceScriptKeyRs(oldKey: ScriptEntryKey, newKey: ScriptEntryKey): Result<CentralScriptContainer, ErrorReport>
    fun replaceWbKey(oldWbkey:WorkbookKey,newWbKey:WorkbookKey):CentralScriptContainer

    fun contains(scriptKey: ScriptEntryKey): Boolean

    fun removeScriptOfWb(workbookKey: WorkbookKey): CentralScriptContainer
    fun removeScript(scriptKey: ScriptEntryKey): CentralScriptContainer

    fun getAppScript(scripKey: ScriptEntryKey): ScriptEntry?
    fun getScript(scriptEntryKey: ScriptEntryKey?): ScriptEntry?
    fun getScripts(wbKey: WorkbookKey?): Collection<ScriptEntry>

    fun addOrOverwriteScript(scriptEntry: ScriptEntry): CentralScriptContainer
    fun addOrOverwriteScriptRs(scriptEntry: ScriptEntry): Result<CentralScriptContainer,ErrorReport>
    fun addOrOverwriteMultiScripts(scriptEntryList: List<ScriptEntry>):CentralScriptContainer
    fun addOrOverwriteMultiScriptsRs(scriptEntryList: List<ScriptEntry>):Result<CentralScriptContainer,ErrorReport>

    /**
     * Add a script entry with error reporting
     */
    fun addScriptRs(scriptEntry: ScriptEntry):Result<CentralScriptContainer,ErrorReport>
    /**
     * Add a script entry, throw an exception if unable to find a place for such script entry
     */
    fun addScript(scriptEntry: ScriptEntry):CentralScriptContainer
    /**
     * Add multiple script entry, throw an exception if unable to find a place for such script entry
     */
    fun addMultiScripts(scriptEntries: Collection<ScriptEntry>):CentralScriptContainer
    fun addMultiScriptsForce(scriptEntries: Collection<ScriptEntry>):CentralScriptContainer

    fun addScriptContFor(wbKey: WorkbookKey):CentralScriptContainer
    fun removeScriptContFor(wbKey: WorkbookKey):CentralScriptContainer

    val allAppScripts: Collection<ScriptEntry>
    val allWbScripts: List<ScriptEntry>
    val allScripts: List<ScriptEntry>
    val allWbKey: List<WorkbookKey>

    /**
     * add a script entry forcefully, guarantee that the returning container contain the input script entry
     */
    fun addScriptForce(scriptEntry: ScriptEntry): CentralScriptContainer
    fun addScriptsRs(scriptEntries: Collection<ScriptEntry>): Rse<CentralScriptContainer>
}
