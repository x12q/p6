package test.test_implementation

import com.emeraldblast.p6.formula.translator.antlr.FormulaBaseVisitor
import com.emeraldblast.p6.translator.formula.execution_unit.ExUnit
import com.emeraldblast.p6.translator.jvm_translator.JvmFormulaTranslator
import com.emeraldblast.p6.translator.jvm_translator.JvmFormulaTranslatorFactory
import com.emeraldblast.p6.translator.jvm_translator.tree_extractor.TreeExtractorImp

//val translatorFactory: JvmFormulaTranslatorFactory,
//val visitorFactory: JvmFormulaVisitorFactory,
class TranslatorFactorImp : JvmFormulaTranslatorFactory {
    override fun create(visitor: FormulaBaseVisitor<ExUnit>): JvmFormulaTranslator {
        return JvmFormulaTranslator(visitor = visitor, treeExtractor = TreeExtractorImp())
    }
}

//class FormulaVisitorFactory : JvmFormulaVisitorFactory{
//    override fun create(wbKey: WorkbookKey, worksheetName: String): JvmFormulaVisitor {
//        return JvmFormulaVisitor(
//            wbKey,worksheetName,FunctionMapImp(P6FunctionDefsImp().functionMap)
//        )
//    }
//}
