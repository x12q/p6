package com.qxdzbc.p6.ui.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.qxdzbc.p6.err.ErrorContainer
import com.qxdzbc.p6.err.ErrorContainerImp
import com.qxdzbc.common.compose.Ms
import com.qxdzbc.common.compose.StateUtils.ms
import com.qxdzbc.p6.ui.app.cell_editor.state.CellEditorState
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.app.di.qualifiers.AppErrorContMs
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(P6AnvilScope::class)
data class AppStateImp @Inject constructor(
    @AppErrorContMs
    override val appErrorContainerMs: Ms<ErrorContainer> = ms(ErrorContainerImp()),
    override var stateCont: StateContainer,
    override var documentContainer: DocumentContainer,
    override val cellEditorStateMs: Ms<CellEditorState>,
    override var translatorContainer: TranslatorContainer,
) : AppState {

    override var cellEditorState: CellEditorState by cellEditorStateMs

    override var appErrorContainer: ErrorContainer by appErrorContainerMs

}
