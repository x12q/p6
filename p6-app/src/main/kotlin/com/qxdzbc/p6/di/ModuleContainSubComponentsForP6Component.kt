package com.qxdzbc.p6.di

import com.qxdzbc.p6.ui.workbook.di.comp.WbComponent
import com.qxdzbc.p6.ui.worksheet.di.WsComponent
import com.qxdzbc.p6.ui.window.di.comp.WindowComponent
import dagger.Module

/**
 * A module for tying all sub components to [P6Component], so that they can access all the objects inside [P6Component].
 * Read [DIPatternDoc.md] for detail about this DI structure.
 */
@Module(
    subcomponents = [
        WindowComponent::class,
        WbComponent::class,
        WsComponent::class,
    ]
)
interface ModuleContainSubComponentsForP6Component