package com.qxdzbc.p6.ui.window.file_dialog

import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.singleWindowApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import org.apache.commons.io.FilenameUtils
import java.nio.file.Files
import java.nio.file.Path
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileFilter

fun FrameWindowScope.OpenFileDialog(
    title: String? = null,
    isLoad: Boolean = true,
    defaultFileFilter: FileFilter? = P6JFileFilters.p6,
    launchScope: CoroutineScope = GlobalScope,
    onResult: (result: Path?) -> Unit,
    onCancel: () -> Unit,
) {
    launchScope.launch(Dispatchers.Swing) {
        val fileChooser = JFileChooser().apply {
            for (filter in P6JFileFilters.all) {
                addChoosableFileFilter(filter)
            }
            defaultFileFilter?.also {
                fileFilter = defaultFileFilter
            }
            title?.also {
                dialogTitle = title
            }
        }

        val resultCode = if (isLoad) {
            fileChooser.showOpenDialog(window)
        } else {
            fileChooser.showSaveDialog(window)
        }

        if (resultCode == JFileChooser.APPROVE_OPTION) {
            val path = fileChooser.selectedFile?.toPath()
            if (path != null) {
                val filter = fileChooser.fileFilter
                if (filter is P6JFileFilters) {
                    val extensionRs = kotlin.runCatching {
                        FilenameUtils.getExtension(path.toString())
                    }
                    if (extensionRs.isSuccess) {
                        val truePath = if (extensionRs.getOrNull() == filter.extension) {
                            path
                        } else {
                            val newPath = Path.of(path.toString() + ".${filter.extension}")
                            newPath
                        }
                        if(Files.exists(truePath)){
                            val fileName=FilenameUtils.getName(truePath.toString())
                            val i = JOptionPane.showConfirmDialog(null, "File ${fileName} already exists. Do you want to overwrite it?", "Overwrite file?", JOptionPane.YES_NO_OPTION)
                            if (i == JOptionPane.YES_OPTION) {
                                onResult(truePath)
                            }
                        }else{
                            onResult(truePath)
                        }
                    }
                }
            } else {
                onCancel()
            }
        }
    }
}

fun main() {
    singleWindowApplication {
        OpenFileDialog(title = " custom title", isLoad = true, defaultFileFilter = null, onResult = {
            println(it)
        }, onCancel = {})
    }
}
