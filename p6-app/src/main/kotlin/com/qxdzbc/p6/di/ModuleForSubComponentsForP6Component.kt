package com.qxdzbc.p6.di

import com.qxdzbc.p6.ui.window.di.comp.WindowComponent
import dagger.Module

/**
 * A module for tying all sub-components to [P6Component]
 */
@Module(
    subcomponents = [
        WindowComponent::class,
    ]
)
interface ModuleForSubComponentsForP6Component