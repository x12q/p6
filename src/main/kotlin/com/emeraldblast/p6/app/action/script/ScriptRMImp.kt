package com.emeraldblast.p6.app.action.script

import com.emeraldblast.p6.app.action.script.new_script.rm.NewScriptRM

import javax.inject.Inject

class ScriptRMImp @Inject constructor(

    private val ns: NewScriptRM,
) : ScriptRM, NewScriptRM by ns
