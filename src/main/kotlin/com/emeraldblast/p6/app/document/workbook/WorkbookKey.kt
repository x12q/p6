package com.emeraldblast.p6.app.document.workbook

import com.emeraldblast.p6.proto.DocProtos
import com.emeraldblast.p6.proto.DocProtos.WorkbookKeyProto
import java.nio.file.Path

interface WorkbookKey{
    val name:String
    val path:Path?
    fun toProto(): DocProtos.WorkbookKeyProto
    fun setName(i: String): WorkbookKey
    fun setPath(i: Path?): WorkbookKey
    @Deprecated("dont use")
    fun pathScript(): String
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
    override fun pathScript():String{
        if(path!=null){
            return "\"${ path.toAbsolutePath().toString() }\""
        }else{
            return "None"
        }
    }
    override fun toProto(): DocProtos.WorkbookKeyProto{
        val builder= DocProtos.WorkbookKeyProto.newBuilder().setName(name)
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
