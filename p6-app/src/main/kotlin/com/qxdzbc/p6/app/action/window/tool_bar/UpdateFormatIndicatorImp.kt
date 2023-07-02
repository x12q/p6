package com.qxdzbc.p6.app.action.window.tool_bar

import androidx.compose.runtime.getValue
import com.qxdzbc.common.compose.St
import com.qxdzbc.p6.app.action.common_data_structure.WbWsSt
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.state.StateContainer
import com.qxdzbc.p6.ui.window.tool_bar.text_size_selector.state.TextSizeSelectorState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class UpdateFormatIndicatorImp @Inject constructor(
    val stateContainerSt:StateContainer,
): UpdateFormatIndicator {

    private val sc  = stateContainerSt

    override fun updateFormatIndicator(wbWsSt:WbWsSt){
        sc.getCursorState(wbWsSt)?.also {cursorState->
            val cursorId = cursorState.id
            sc.getCellFormatTable(wbWsSt)?.also { cft->
                sc.getTextSizeSelectorStateMs(cursorId.wbKey)?.also { textSizeSelectorStateMs ->
                    textSizeSelectorStateMs.value = textSizeSelectorStateMs.value.setHeaderText(
                        cft.textSizeTable.getFormatValue(cursorState.mainCell)?.toString() ?: TextSizeSelectorState.defaultHeader
                    )
                }

                sc.getTextColorSelectorStateMs(cursorId.wbKey)?.also { textColorSelectorStateMs ->
                    textColorSelectorStateMs.value = textColorSelectorStateMs.value.setCurrentColor(
                        cft.textColorTable.getFormatValue(cursorState.mainCell)
                    )
                }

                sc.getCellBackgroundColorSelectorStateMs(cursorId.wbKey)?.also {cellBackgroundColorSelectorStateMs->
                    cellBackgroundColorSelectorStateMs.value = cellBackgroundColorSelectorStateMs.value.setCurrentColor(
                        cft.cellBackgroundColorTable.getFormatValue(cursorState.mainCell)
                    )
                }
            }
        }
    }
}
