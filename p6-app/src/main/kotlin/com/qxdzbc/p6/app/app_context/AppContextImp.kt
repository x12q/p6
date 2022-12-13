package com.qxdzbc.p6.app.app_context

import com.qxdzbc.p6.di.Username
import javax.inject.Inject


class AppContextImp @Inject constructor(
    @Username
    override val username: String,
) : AppContext
