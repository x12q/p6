package com.qxdzbc.p6.app.action.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.qxdzbc.p6.di.P6Singleton
import com.qxdzbc.p6.di.anvil.P6AnvilScope
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.OtherElement
import com.qxdzbc.p6.translator.partial_text_element_extractor.text_element.TextElement
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@P6Singleton
@ContributesBinding(P6AnvilScope::class)
class BuildAnnotatedTextActionImp @Inject constructor() : BuildAnnotatedTextAction {
    override fun buildAnnotatedText(
        textElements: List<TextElement>,
        spans: List<SpanStyle>
    ): AnnotatedString {
        val sortedTextElements = textElements.sortedBy { it.start }
        val spanElements = textElements.filter { it !is OtherElement }
        var prevIndex = 0
        val rt=buildAnnotatedString {
            for ((i, e) in sortedTextElements.withIndex()) {
                if(e.start>prevIndex){
                    val dif = e.start - prevIndex -1
                    append(" ".repeat(dif))
                }
                when(e){
                    is OtherElement ->{
                        append(e.text)
                    }
                    else ->{
                        val spanIndex=spanElements.indexOf(e)
                        spans.getOrNull(spanIndex)?.also { span ->
                            withStyle(style = span) {
                                append(e.text)
                            }
                        } ?: run {
                            append(e.text)
                        }
                    }
                }
                prevIndex = e.stop
            }
        }
        return rt
    }
}
