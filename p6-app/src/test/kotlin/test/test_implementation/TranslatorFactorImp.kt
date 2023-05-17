package test.test_implementation

import com.qxdzbc.p6.formula.translator.antlr.FormulaBaseVisitor
import com.qxdzbc.p6.translator.formula.execution_unit.ExUnit
import com.qxdzbc.p6.translator.jvm_translator.ExUnitFormulaTranslator
import com.qxdzbc.p6.translator.jvm_translator.ExUnitFormulaTranslatorFactory
import com.qxdzbc.p6.translator.jvm_translator.tree_extractor.TreeExtractorImp

//val translatorFactory: JvmFormulaTranslatorFactory,
//val visitorFactory: JvmFormulaVisitorFactory,
class TranslatorFactorImp : ExUnitFormulaTranslatorFactory {
    override fun create(visitor: FormulaBaseVisitor<ExUnit>): ExUnitFormulaTranslator {
        return ExUnitFormulaTranslator(visitor = visitor, treeExtractor = TreeExtractorImp())
    }
}

//class FormulaVisitorFactory : JvmFormulaVisitorFactory{
//    override fun create(wbKey: WorkbookKey, worksheetName: String): JvmFormulaVisitor {
//        return JvmFormulaVisitor(
//            wbKey,worksheetName,FunctionMapImp(P6FunctionDefsImp().functionMap)
//        )
//    }
//}
