package com.emeraldblast.p6.app.action.cell

import com.emeraldblast.p6.app.action.cell.cell_update.rm.CellUpdateRM

import javax.inject.Inject

class CellRMImp @Inject constructor(

    private val cu: CellUpdateRM
) : CellRM, CellUpdateRM by cu
