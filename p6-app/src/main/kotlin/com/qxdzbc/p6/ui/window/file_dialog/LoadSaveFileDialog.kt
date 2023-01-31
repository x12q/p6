package com.qxdzbc.p6.ui.window.file_dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.singleWindowApplication
import com.qxdzbc.p6.app.action.app.process_save_path.MakeSavePath
import com.qxdzbc.p6.app.action.app.process_save_path.MakeSavePathImp
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

@Composable
fun FrameWindowScope.LoadSaveFileDialog(
    title: String? = null,
    isLoad: Boolean = true,
    defaultFileFilter: FileFilter? = P6JFileFilters.p6,
    launchScope: CoroutineScope,
    makeSavePath: MakeSavePath,
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

        /*
         x: result code indicates the button that users click after the dialog is displayed
         JFileChooser.APPROVE_OPTION: click Ok button
         JFileChooser.CANCEL_OPTION: click Cancel button
         */
        val resultCode = if (isLoad) {
            fileChooser.showOpenDialog(window)
        } else {
            fileChooser.showSaveDialog(window)
        }

        when (resultCode) {
            JFileChooser.APPROVE_OPTION -> {
                val path = fileChooser.selectedFile?.toPath()
                if (path != null) {
                    val truePath = makeSavePath.makeSavePath(path, fileChooser.fileFilter)
                    truePath?.also {
                        if (!isLoad) {
                            if (Files.exists(truePath)) {
                                val fileName = FilenameUtils.getName(truePath.toString())
                                val i = JOptionPane.showConfirmDialog(
                                    null,
                                    "File ${fileName} already exists. Do you want to overwrite it?",
                                    "Overwrite file?",
                                    JOptionPane.YES_NO_OPTION
                                )
                                if (i == JOptionPane.YES_OPTION) {
                                    onResult(truePath)
                                }

                            } else {
                                onResult(truePath)
                            }
                        } else {
                            if (Files.exists(truePath)) {
                                onResult(truePath)
                            }
                        }
                    }
                } else {
                    onCancel()
                }
            }

            JFileChooser.CANCEL_OPTION -> {
                onCancel()
            }

            else -> {
                onCancel()
            }
        }
    }
}

fun main() {
    singleWindowApplication {
        LoadSaveFileDialog(title = " custom title", isLoad = true, defaultFileFilter = null, onResult = {
            println(it)
        }, onCancel = {}, makeSavePath = MakeSavePathImp(), launchScope = GlobalScope)
    }
}
