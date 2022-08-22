package com.qxdzbc.p6.ui.window.file_dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.AwtWindow
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.singleWindowApplication
import java.awt.FileDialog
import java.io.File
import java.nio.file.Path

@Composable
fun FrameWindowScope.FileDialog(
    title: String,
    isLoad: Boolean,
    onResult: (result: Path?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(window, title, if (isLoad) LOAD else SAVE) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    if (file != null) {
                        onResult(File(directory).resolve(file).toPath())
                    } else {
                        onResult(null)
                    }
                }
            }
        }
    },
    dispose = FileDialog::dispose
)

fun main(){
    singleWindowApplication {
        FileDialog("zxc",false){
            println(it)
        }
    }
}
