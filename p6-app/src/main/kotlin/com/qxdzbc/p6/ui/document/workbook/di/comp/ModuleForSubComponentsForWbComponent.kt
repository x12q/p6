package com.qxdzbc.p6.ui.document.workbook.di.comp

import com.qxdzbc.p6.ui.document.worksheet.di.comp.WsComponent
import dagger.Module

/**
 * Aggregate all subcomponent for [WbComponent]
 */
@Module(
    subcomponents = [
        WsComponent::class
    ]
)
interface ModuleForSubComponentsForWbComponent