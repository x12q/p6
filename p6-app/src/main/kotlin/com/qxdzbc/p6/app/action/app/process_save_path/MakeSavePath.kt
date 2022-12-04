package com.qxdzbc.p6.app.action.app.process_save_path

import com.qxdzbc.p6.ui.window.file_dialog.P6JFileFilters
import java.nio.file.Path
import javax.swing.filechooser.FileFilter

interface MakeSavePath {
    fun makeSavePath(originalPath: Path,fileFilter: FileFilter?):Path?
}
