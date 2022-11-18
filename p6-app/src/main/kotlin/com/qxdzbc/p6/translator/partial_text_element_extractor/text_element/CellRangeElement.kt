package com.qxdzbc.p6.translator.partial_text_element_extractor.text_element



data class CellRangeElement(
    val rangeAddress:BasicTextElement,
    val wsSuffix:BasicTextElement? = null,
    val wbSuffix:BasicTextElement? =null,
): TextElement {

    @Deprecated("don't use, just here so that old code is not broken")
    constructor(
        cellRangeLabel:String,
        startTP:TokenPosition,
        stopTP: TokenPosition,
    ):this(
        rangeAddress = BasicTextElement(cellRangeLabel,startTP.charIndex,stopTP.charIndex)
    )
    val cellRangeLabel:String get()=rangeAddress.text
    val cellRangeSuffix:String? get(){
        val wsSf= wsSuffix?.text
        val wbSf = wbSuffix?.text
        if(wsSf!=null){
            if(wbSf!=null){
                return "$wsSf$wbSf"
            }else{
                return wsSf
            }
        }else{
            return null
        }
    }
    val startTP: TokenPosition get()= TokenPosition(rangeAddress.start)
    val stopTP: TokenPosition get(){
        val rt= listOfNotNull(rangeAddress.stop, wsSuffix?.stop, wbSuffix?.stop).maxBy { it }
        return TokenPosition(rt)
    }

    fun contains(i:Int):Boolean{
        return i >= startTP.charIndex && i <= stopTP.charIndex
    }
    fun iRange():IntRange{
        return range
    }

    override val text: String
        get() = cellRangeLabel + (this.cellRangeSuffix?:"")
    override val range: IntRange
        get() = this.startTP.charIndex .. this.stopTP.charIndex
}
