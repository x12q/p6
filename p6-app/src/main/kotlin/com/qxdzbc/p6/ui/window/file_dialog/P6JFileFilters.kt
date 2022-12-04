package com.qxdzbc.p6.ui.window.file_dialog

import java.io.File
import javax.swing.filechooser.FileFilter

class P6JFileFilters (override val extension: String, private val desc:String) : FileFilter(),WithFileExtension {
    companion object {
        val csv = P6JFileFilters("csv","*.csv")
        val p6 = P6JFileFilters("p6","*.p6")
        val all = listOf(csv, p6)
    }
    override fun accept(f: File?): Boolean {
        if (f?.isDirectory == true) {
            return true
        } else {
            return f?.name?.lowercase()?.endsWith(extension) == true
        }
    }

    override fun getDescription(): String {
        return desc
    }
}
