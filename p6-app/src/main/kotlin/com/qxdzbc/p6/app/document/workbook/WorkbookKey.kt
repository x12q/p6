package com.qxdzbc.p6.app.document.workbook

import com.qxdzbc.p6.proto.DocProtos
import com.qxdzbc.p6.proto.DocProtos.WorkbookKeyProto
import java.nio.file.Path
import java.util.*

interface WorkbookKey{
    val name:String
    val path:Path?
    fun toProto(): WorkbookKeyProto
    fun setName(i: String): WorkbookKey
    fun setPath(i: Path?): WorkbookKey

    companion object{
        fun random():WorkbookKey{
            return WorkbookKey("Worksheet-"+UUID.randomUUID().toString())
        }
    }
}

fun WorkbookKey(name:String,path:Path?=null):WorkbookKey{
    return WorkbookKeyImp(name,path)
}

data class WorkbookKeyImp(
    override val name:String,
    override val path:Path? = null
):WorkbookKey {
    companion object {
        fun fromProto(proto:WorkbookKeyProto):WorkbookKey{
            return proto.toModel()
        }
    }

    override fun toProto(): WorkbookKeyProto{
        val builder= WorkbookKeyProto.newBuilder().setName(name)
        if(this.path!=null){
            builder.setPath(this.path.toAbsolutePath().toString())
        }
        return builder.build()
    }

    override fun setName(i: String): WorkbookKey {
        return this.copy(name = i)
    }

    override fun setPath(i: Path?): WorkbookKey {
        return this.copy(path=i)
    }
}

fun DocProtos.WorkbookKeyProto.toModel(): WorkbookKey {
    return WorkbookKey(
        name = name,
        path = if (this.hasPath()) {
            Path.of(path)
        } else {
            null
        }
    )
}
