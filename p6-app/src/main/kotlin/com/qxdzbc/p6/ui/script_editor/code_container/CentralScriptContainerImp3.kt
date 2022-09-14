package com.qxdzbc.p6.ui.script_editor.code_container

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.common.Rs
import com.qxdzbc.common.Rse
import com.qxdzbc.common.ErrorUtils.getOrThrow
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.p6.app.document.script.*
import com.qxdzbc.p6.app.document.wb_container.WorkbookContainerErrors
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.document.workbook.state.cont.WorkbookStateContainer
import com.github.michaelbull.result.*


data class CentralScriptContainerImp3(
    val appScriptContainerMs: Ms<ScriptContainer> = ms(ScriptContainerImp()),
    val wbStateContMs: Ms<WorkbookStateContainer>
) : CentralScriptContainer {

    var appScriptContainer: ScriptContainer by appScriptContainerMs
    private var wbStateCont by wbStateContMs
    override fun removeScriptOfWb(workbookKey: WorkbookKey): CentralScriptContainer {
        wbStateCont.getWbState(workbookKey)?.let { wss ->
            wss.scriptCont = wss.scriptCont.removeAllScript()
        }
        return this
    }

    private fun getScriptContMsRs(wbKey: WorkbookKey?): Rse<Ms<ScriptContainer>> {
        if (wbKey == null) {
            return Ok(this.appScriptContainerMs)
        } else {
            return this.wbStateCont.getWbStateRs(wbKey).map { it.scriptContMs }
        }
    }

    private fun getScriptContMs(wbKey: WorkbookKey?): Ms<ScriptContainer>? {
        return this.getScriptContMsRs(wbKey).component1()
    }

    /**
     * This effectively move the script from one place to another
     */
    override fun replaceScriptKeyRs(
        oldKey: ScriptEntryKey,
        newKey: ScriptEntryKey
    ): Result<CentralScriptContainer, ErrorReport> {
        val wbk1 = oldKey.wbKey
        val wbk2 = newKey.wbKey
        val n1 = oldKey.name
        val n2 = newKey.name
        if (wbk1 == wbk2) {
            val rt: Result<CentralScriptContainer, ErrorReport> = this.getScriptContMsRs(wbk1).map { cont1 ->
                cont1.value = cont1.value.renameScript(n1, n2)
                this
            }
            return rt
        } else {
            val rt= this.getScriptContMsRs(wbk1).flatMap { cont1 ->
                this.getScriptContMsRs(wbk2).flatMap { cont2 ->
                    cont1.value.getScriptRs(n1).flatMap {script->
                        cont1.value = cont1.value.removeScript(n1)
                        cont2.value = cont2.value.overwriteScript(n2,script)
                        Ok(this)
                    }
                }
            }
            return rt
        }
    }



    override fun replaceWbKey(oldWbkey: WorkbookKey, newWbKey: WorkbookKey): CentralScriptContainer {
        this.wbStateCont = this.wbStateCont.replaceKey(oldWbkey,newWbKey)
        return this
    }

    override fun contains(scriptKey: ScriptEntryKey): Boolean {
        return this.getScript(scriptKey) != null
    }

    override fun removeScript(scriptKey: ScriptEntryKey): CentralScriptContainer {
        val wbKey = scriptKey.wbKey
        if (wbKey != null) {
            this.wbStateCont.getWbState(wbKey)?.let {
                it.scriptCont = it.scriptCont.removeScript(scriptKey.name)
            }
        } else {
            appScriptContainer = appScriptContainer.removeScript(scriptKey.name)
        }
        return this
    }

    fun getWbScript(wbKey: WorkbookKey, name: String): ScriptEntry? {
        return this.getScriptContMs(wbKey)?.value?.getScript(name)?.let {
            ScriptEntry(
                key = ScriptEntryKey(name = name, wbKey = wbKey),
                script = it
            )
        }
    }

    fun getWbScript(scripKey: ScriptEntryKey): ScriptEntry? {
        if (scripKey.wbKey != null) {
            return getWbScript(scripKey.wbKey, scripKey.name)
        } else {
            return null
        }
    }

    fun getScriptsOfWb(wbKey: WorkbookKey): Collection<ScriptEntry> {
        return this.wbStateCont.getWbState(wbKey)?.scriptCont?.allScripts?.map {
            ScriptEntry(
                key = ScriptEntryKey(
                    name = it.name,
                    wbKey = wbKey
                ),
                script = it.script
            )
        } ?: emptyList()
    }

    fun addWbScript(scriptEntry: ScriptEntry): CentralScriptContainerImp3 {
        val wbKey = scriptEntry.key.wbKey
        if (wbKey == null) {
            return this
        } else {
            this.wbStateCont.getWbState(wbKey)?.let { wss ->
                val scMs = wss.scriptContMs
                scMs.value = scMs.value.addScript(
                    scriptEntry.name, scriptEntry.script
                )
            }
            return this
        }
    }

    fun addWbScript(workbookKey: WorkbookKey, scriptList: List<ScriptEntry>): CentralScriptContainerImp3 {
        val wss = this.wbStateCont.getWbState(workbookKey)
        if (wss!=null) {
            val scMs = wss.scriptContMs
            var i = scMs.value
            for (e in scriptList) {
                i = i.addScript(e.name, e.script)
            }
            scMs.value = i
        }
        return this
    }

    fun addWbScriptRs(
        workbookKey: WorkbookKey,
        scriptEntry: SimpleScriptEntry
    ): Result<CentralScriptContainer, ErrorReport> {
        return this.getWbScriptContMsRs(workbookKey).flatMap { scMs ->
            scMs.value = scMs.value.addScript(scriptEntry.name, scriptEntry.script)
            Ok(this)
        }
    }
    @kotlin.jvm.Throws(Exception::class)
    override fun addOrOverwriteScript(scriptEntry: ScriptEntry): CentralScriptContainer {
        return this.addOrOverwriteScriptRs(scriptEntry).getOrThrow()
    }

    override fun addOrOverwriteScriptRs(scriptEntry: ScriptEntry): Rs<CentralScriptContainer, ErrorReport> {
        if (scriptEntry.key.wbKey == null) {
            val rs = appScriptContainer.overwriteScriptRs(scriptEntry.name, scriptEntry.script)
            when (rs) {
                is Ok -> {
                    appScriptContainer = rs.value
                    return Ok(this)
                }
                is Err -> return rs
            }
        } else {
            return this.addOrOverwriteWbScriptRs(scriptEntry.key.wbKey, scriptEntry.toSimpleEntry())
        }
    }


    fun getWbScriptContMsRs(wbKey: WorkbookKey): Rse<Ms<ScriptContainer>> {
        val wss = this.wbStateCont.getWbState(wbKey)
        if (wss!=null) {
            return wss.scriptContMs.toOk()
        } else {
            return WorkbookContainerErrors.InvalidWorkbook.report("can't get script container for workbook at ${wbKey} because that workbook does not exist.")
                .toErr()
        }
    }

    fun addOrOverwriteWbScriptRs(
        workbookKey: WorkbookKey,
        scriptEntry: SimpleScriptEntry
    ): Rs<CentralScriptContainer, ErrorReport> {

        val se = scriptEntry
        val rs: Result<Ms<ScriptContainer>, ErrorReport> = this.getWbScriptContMsRs(workbookKey)

        val rt: Rs<CentralScriptContainer, ErrorReport> = rs.flatMap { scMs ->
            scMs.value.overwriteScriptRs(se.name, se.script)
                .flatMap {
                    scMs.value = it
                    Ok(scMs)
                }
        }.map {
            this
        }
        return rt
    }
    @kotlin.jvm.Throws(Exception::class)
    override fun addOrOverwriteMultiScripts(scriptEntryList: List<ScriptEntry>): CentralScriptContainer {
        return this.addOrOverwriteMultiScriptsRs(scriptEntryList).getOrThrow()
    }

    override fun addOrOverwriteMultiScriptsRs(scriptEntryList: List<ScriptEntry>): Result<CentralScriptContainer, ErrorReport> {
        var c: CentralScriptContainer = this
        for (se in scriptEntryList) {
            val rs = c.addOrOverwriteScriptRs(se)
            when (rs) {
                is Ok -> {
                    c = rs.value
                }
                is Err -> {
                    return rs
                }
            }
        }
        return Ok(c)
    }

    override fun addScriptRs(scriptEntry: ScriptEntry): Result<CentralScriptContainer, ErrorReport> {
        val se = scriptEntry
        val seKey = se.key
        if (seKey.wbKey == null) {
            val rs = this.appScriptContainer.addScriptRs(seKey.name, se.script)
            val rt = rs.map {
                this.appScriptContainer = it
                this
            }
            return rt
        } else {
            return this.addWbScriptRs(seKey.wbKey, scriptEntry.toSimpleEntry())
        }
    }
    @kotlin.jvm.Throws(Exception::class)
    override fun addScript(scriptEntry: ScriptEntry): CentralScriptContainer {
        return this.addScriptRs(scriptEntry).getOrThrow()
    }

    override fun addScriptForce(scriptEntry: ScriptEntry): CentralScriptContainer {
        val rs = this.addScriptRs(scriptEntry)
        val wbk = scriptEntry.wbKey
        if(wbk==null){
            this.appScriptContainer = this.appScriptContainer.overwriteScript(scriptEntry)
        }else{
            this.wbStateCont.getWbState(wbk)?.let {wss->
                val scMs = wss.scriptContMs
                scMs.value = scMs.value.overwriteScript(scriptEntry)
            }
        }
        return this
    }

    override fun addScriptsRs(scriptEntries: Collection<ScriptEntry>): Rse<CentralScriptContainer> {
        var v: CentralScriptContainer = this
        var cantAddList = emptyList<ScriptEntry>()
        for (s in scriptEntries) {
            val wbk = s.wbKey
            val canAdd = if (wbk != null) {
                this.wbStateCont.containWbKey(wbk)
            } else {
                true
            }
            if (!canAdd) {
                cantAddList = cantAddList + s
            }
        }

        if (cantAddList.isNotEmpty()) {
            return CentralScriptContainerErrors.CantAddScript.report(
                "can't add the following script to script container:\n"
                        + cantAddList.joinToString(System.lineSeparator()) { it.repStr() }
            ).toErr()
        } else {
            for (s in scriptEntries) {
                v = v.addScriptForce(s)
            }
        }

        return v.toOk()
    }
    @kotlin.jvm.Throws(Exception::class)
    override fun addMultiScripts(scriptEntries: Collection<ScriptEntry>): CentralScriptContainer {
        return this.addScriptsRs(scriptEntries).getOrThrow()
    }

    override fun addMultiScriptsForce(scriptEntries: Collection<ScriptEntry>): CentralScriptContainer {
        var v: CentralScriptContainer = this
        for (s in scriptEntries) {
            v = v.addScriptForce(s)
        }
        return v
    }

    override fun addScriptContFor(wbKey: WorkbookKey): CentralScriptContainer {
        // do nothing
        return this
    }

    override fun removeScriptContFor(wbKey: WorkbookKey): CentralScriptContainer {
        // do nothing
        return this
    }

    override fun getAppScript(scripKey: ScriptEntryKey): ScriptEntry? {
        if (scripKey.wbKey == null) {
            val script = this.appScriptContainer.getScript(scripKey.name)
            if (script != null) {
                return ScriptEntry(
                    key = scripKey,
                    script = script
                )
            } else {
                return null
            }
        } else {
            return null
        }
    }

    override fun getScript(scriptEntryKey: ScriptEntryKey?): ScriptEntry? {
        if (scriptEntryKey != null) {
            if (scriptEntryKey.wbKey == null) {
                return this.getAppScript(scriptEntryKey)
            } else {
                return this.getWbScript(scriptEntryKey)
            }
        } else {
            return null
        }
    }

    override fun getScripts(wbKey: WorkbookKey?): Collection<ScriptEntry> {
        if (wbKey == null) {
            return this.allAppScripts
        } else {
            return this.getScriptsOfWb(wbKey)
        }
    }

    override val allAppScripts: Collection<ScriptEntry>
        get() {
            return this.appScriptContainer.allAsScriptEntry(null)
        }

    override val allWbScripts: List<ScriptEntry>
        get() = this.wbStateCont.allStates
            .flatMap { wss -> wss.scriptCont.allAsScriptEntry(wss.wbKey) }

    override val allScripts: List<ScriptEntry>
        get() = this.allWbScripts + allAppScripts

    override val allWbKey: List<WorkbookKey>
        get() = this.wbStateCont.allStates
            .map { wss -> wss.wbKey }
}
