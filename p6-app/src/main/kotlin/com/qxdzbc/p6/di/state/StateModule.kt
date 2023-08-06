package com.qxdzbc.p6.di.state

import com.qxdzbc.p6.di.state.app_state.AppStateModule

import dagger.Module

@Module(
    includes = [
        AppStateModule::class,
    ]
)
interface StateModule

