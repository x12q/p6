package com.qxdzbc.p6.composite_actions.app.process_save_path

import java.nio.file.Path
import javax.swing.filechooser.FileFilter

interface MakeSavePath {
    fun makeSavePath(originalPath: Path,fileFilter: FileFilter?):Path?
}
