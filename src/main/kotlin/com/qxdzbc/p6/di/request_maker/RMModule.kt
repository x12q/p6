package com.qxdzbc.p6.di.request_maker

import com.qxdzbc.p6.app.action.request_maker.BaseRM
import com.qxdzbc.p6.app.action.request_maker.BaseRMImp
import com.qxdzbc.p6.app.action.request_maker.TemplateRM
import com.qxdzbc.p6.app.action.request_maker.TemplateRMImp
import com.qxdzbc.p6.di.P6Singleton
import dagger.Binds

@dagger.Module(
    includes = [
        AppRMModule::class,
        CellRMModule::class,
        WorkbookRMModule::class,
        WorksheetRMModule::class,
        WindowRMModule::class,
        RangeRMModule::class,
        ScriptRMModule::class,
    ]
)
interface RMModule {

    @Binds
    @P6Singleton
    fun RequestMakerForTemplate(i: TemplateRMImp): TemplateRM

    @Binds
    @P6Singleton
    fun BaseRequestMaker(i: BaseRMImp): BaseRM
}
