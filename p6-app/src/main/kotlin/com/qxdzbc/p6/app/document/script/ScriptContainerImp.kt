package com.qxdzbc.p6.app.document.script

import com.qxdzbc.common.Rse
import com.qxdzbc.common.ErrorUtils.getOrThrow
import com.qxdzbc.common.ResultUtils.toOk
import com.qxdzbc.p6.app.document.workbook.WorkbookKey
import com.qxdzbc.common.error.ErrorReport
import com.qxdzbc.p6.ui.script_editor.code_container.ScriptContainerErrors
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.qxdzbc.common.CollectionUtils.replaceKey

data class ScriptContainerImp(override val map: Map<String, String> = emptyMap()) : ScriptContainer,
    Map<String, String> by map {

    companion object {
        fun fromScriptEntries(entries: List<ScriptEntry>): ScriptContainerImp {
            return ScriptContainerImp(
                entries.associateBy(
                    keySelector = { it.name },
                    valueTransform = { it.script }
                )
            )
        }

        fun fromSimpleScriptEntries(entries: List<SimpleScriptEntry>): ScriptContainerImp {
            return ScriptContainerImp(
                entries.associateBy(
                    keySelector = { it.name },
                    valueTransform = { it.script }
                )
            )
        }
    }

    override fun getScript(name: String): String? {
        return this.map[name]
    }

    override fun getScriptRs(name: String): Rse<String> {
        return this.map[name]?.let {
            Ok(it)
        }?: ScriptContainerErrors.ScriptNotExist.report("Script named \"${name}\" does not exist.").toErr()
    }

    override fun overwriteScriptRs(name: String, script: String): Result<ScriptContainer, ErrorReport> {
        // TODO this is a placeholder for future error checking: such as checking script name
        return Ok(this.copy(map = map + (name to script)))
    }
    @kotlin.jvm.Throws(Exception::class)
    override fun overwriteScript(name: String, script: String): ScriptContainer {
        return this.overwriteScriptRs(name, script).getOrThrow()
    }

    override fun overwriteScript(scriptEntry: ScriptEntry): ScriptContainer {
        return this.overwriteScript(scriptEntry.name, scriptEntry.script)
    }

    override fun addScriptRs(name: String, script: String): Result<ScriptContainer, ErrorReport> {
        if (this.map.containsKey(name)) {
            return ScriptContainerErrors.CantAddScriptBecauseItAlreadyExist.report(name).toErr()
        } else {
            return this.overwriteScript(name, script).toOk()
        }
    }
    @kotlin.jvm.Throws(Exception::class)
    override fun addScript(name: String, script: String): ScriptContainer {
        return addScriptRs(name, script).getOrThrow()
    }

    override fun overwriteAllScriptRs(scripts: List<SimpleScriptEntry>): Result<ScriptContainer, ErrorReport> {
        var c: ScriptContainer = this
        for (script in scripts) {
            val rs = c.overwriteScriptRs(script.name, script.script)
            when (rs) {
                is Err -> return rs
                is Ok -> c = rs.value
            }
        }
        return Ok(c)
    }
    @kotlin.jvm.Throws(Exception::class)
    override fun overwriteAllScript(scripts: List<SimpleScriptEntry>): ScriptContainer {
        return this.overwriteAllScriptRs(scripts).getOrThrow()
    }

    override fun containScript(scriptName: String): Boolean {
        return this.map.containsKey(scriptName)
    }


    override fun removeScript(name: String): ScriptContainer {
        return this.copy(map = map - name)
    }

    override fun removeAllScript(): ScriptContainer {
        return this.copy(map = emptyMap())
    }

    override val allScripts: Collection<SimpleScriptEntry>
        get() = this.map.map {
            SimpleScriptEntry(name = it.key, script = it.value)
        }

    override fun allAsScriptEntry(wbKey: WorkbookKey?): Collection<ScriptEntry> {
        return this.map.map {
            ScriptEntry(
                key = ScriptEntryKey(
                    name = it.key,
                    wbKey = wbKey
                ),
                script = it.value
            )
        }
    }

    override fun renameScript(oldName: String, newName: String): ScriptContainer {
        return this.copy(map = this.map.replaceKey(oldName, newName))
    }

}
