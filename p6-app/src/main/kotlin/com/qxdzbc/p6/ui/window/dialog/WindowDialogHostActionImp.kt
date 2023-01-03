package com.qxdzbc.p6.ui.window.dialog

import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.window.dialog.ask_to_save_dialog.AskSaveDialogAction
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class WindowDialogHostActionImp @Inject constructor(
    override val askSaveDialogAction: AskSaveDialogAction
) : WindowDialogHostAction
