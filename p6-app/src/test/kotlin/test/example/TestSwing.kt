package test.example

import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qxdzbc.p6.ui.common.compose.P6TestApp
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.BorderLayout
import java.awt.Component
import javax.swing.JButton
import javax.swing.JPanel

fun main(){
    P6TestApp{
        SwingPanel(
            background = Color.White,
            modifier = Modifier.size(1000.dp, 500.dp),
            factory = {
                val jpanel = JPanel(BorderLayout())
                val textArea = RSyntaxTextArea(20, 60)
                textArea.syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_PYTHON
                textArea.isCodeFoldingEnabled = true
                val scrollPane = RTextScrollPane(textArea)
                jpanel.add(scrollPane)
                jpanel
            }
        )
    }
}

@Composable
fun MButton(text: String = "", action: (() -> Unit)? = null) {
    Button(
        modifier = Modifier.size(270.dp, 30.dp),
        onClick = { action?.invoke() }
    ) {
        Text(text)
    }
}

fun actionButton(
    text: String,
    action: () -> Unit
): JButton {
    val button = JButton(text)
    button.alignmentX = Component.CENTER_ALIGNMENT
    button.addActionListener { action() }

    return button
}
