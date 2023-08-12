package com.qxdzbc.p6.di

import com.qxdzbc.p6.ui.workbook.di.comp.WbComponent
import com.qxdzbc.p6.ui.worksheet.di.comp.WsComponent
import com.qxdzbc.p6.ui.window.di.comp.WindowComponent
import dagger.Module

/**
 * A module for tying all sub-components to [P6Component]
 */
@Module(
    subcomponents = [
        WindowComponent::class,
        WbComponent::class,
        WsComponent::class,
    ]
)
interface ModuleForSubComponentsForP6Component