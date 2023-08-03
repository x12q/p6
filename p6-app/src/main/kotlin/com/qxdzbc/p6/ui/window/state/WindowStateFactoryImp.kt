package com.qxdzbc.p6.ui.window.state

import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.ui.window.di.comp.WindowComponent
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton


@Singleton
@ContributesBinding(scope=P6AnvilScope::class)
class WindowStateFactoryImp @Inject constructor(
    val windowCompBuilderProvider:Provider<WindowComponent.Builder>
) : WindowStateFactory {
    override fun createDefault(id: String): WindowState {
        return windowCompBuilderProvider.get().setId(id).build().windowState()
    }
}