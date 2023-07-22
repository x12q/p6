package com.qxdzbc.p6.ui.app.action

import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ContributesBinding(P6AnvilScope::class)
class AppActionTableImp @Inject constructor() : AppActionTable
