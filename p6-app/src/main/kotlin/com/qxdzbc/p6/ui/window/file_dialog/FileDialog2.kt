package com.qxdzbc.p6.ui.window.file_dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.singleWindowApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import org.apache.commons.io.FilenameUtils
import java.nio.file.Path
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter

@Composable
fun FrameWindowScope.FileDialog2(
    title: String? = null,
    isLoad: Boolean = true,
    defaultFileFilter: FileFilter? = P6JFileFilters.p6,
    launchScope: CoroutineScope = GlobalScope,
    onResult: (result: Path?) -> Unit
) {
    DisposableEffect(Unit) {
        val job = launchScope.launch(Dispatchers.Swing) {
            val fileChooser = run {
                JFileChooser().apply {
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
            }
            val resultCode = if (isLoad) {
                fileChooser.showOpenDialog(null)
            } else {
                fileChooser.showSaveDialog(null)
            }
            if (resultCode == JFileChooser.APPROVE_OPTION) {
                val path = fileChooser.selectedFile?.toPath()
                if(path!=null){
                    when (val filter = fileChooser.fileFilter) {
                        is P6JFileFilters -> {
                            val extensionRs = kotlin.runCatching {
                                val extension = path?.let { FilenameUtils.getExtension(it.toString()) }
                                extension
                            }
                            if(extensionRs.isSuccess){
                                if(extensionRs.getOrNull() == filter.extension){
                                    onResult(path)
                                }else{
                                    val newPath = Path.of(path.toString()+".${filter.extension}")
                                    onResult(newPath)
                                }
                            }
                            else{
                                // do nothing
                            }
                        }
                        else -> {
                            // do nothing
                        }
                    }
                }
            }
        }
        onDispose {
            job.cancel()
        }
    }
}


fun main() {
    singleWindowApplication {
        FileDialog2(title = " custom title", isLoad = true, defaultFileFilter = null) {
            println(it)
        }
    }
}
