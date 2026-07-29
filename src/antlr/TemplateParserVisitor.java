// Generated from C:/Users/HP/Downloads/Compiler2/src/antlr/TemplateParser.g4 by ANTLR 4.13.2
package antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TemplateParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TemplateParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TemplateParser#page}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPage(TemplateParser.PageContext ctx);
	/**
	 * Visit a parse tree produced by {@link TemplateParser#content}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContent(TemplateParser.ContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TemplateParser#node}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNode(TemplateParser.NodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code textContentNode}
	 * labeled alternative in {@link TemplateParser#textContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextContentNode(TemplateParser.TextContentNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link TemplateParser#htmlDocument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtmlDocument(TemplateParser.HtmlDocumentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code doctypeNode}
	 * labeled alternative in {@link TemplateParser#doctype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoctypeNode(TemplateParser.DoctypeNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pairedHtmlElement}
	 * labeled alternative in {@link TemplateParser#htmlElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairedHtmlElement(TemplateParser.PairedHtmlElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code openingTagNode}
	 * labeled alternative in {@link TemplateParser#openingTag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpeningTagNode(TemplateParser.OpeningTagNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code closingTagNode}
	 * labeled alternative in {@link TemplateParser#closingTag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClosingTagNode(TemplateParser.ClosingTagNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code selfClosingElement}
	 * labeled alternative in {@link TemplateParser#selfClosingTag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelfClosingElement(TemplateParser.SelfClosingElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code htmlAttribute}
	 * labeled alternative in {@link TemplateParser#attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtmlAttribute(TemplateParser.HtmlAttributeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code doubleQuotedAttribute}
	 * labeled alternative in {@link TemplateParser#attributeValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleQuotedAttribute(TemplateParser.DoubleQuotedAttributeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code singleQuotedAttribute}
	 * labeled alternative in {@link TemplateParser#attributeValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleQuotedAttribute(TemplateParser.SingleQuotedAttributeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unquotedAttribute}
	 * labeled alternative in {@link TemplateParser#attributeValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnquotedAttribute(TemplateParser.UnquotedAttributeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code styleBlockNode}
	 * labeled alternative in {@link TemplateParser#styleBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStyleBlockNode(TemplateParser.StyleBlockNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code styleRuleNode}
	 * labeled alternative in {@link TemplateParser#styleRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStyleRuleNode(TemplateParser.StyleRuleNodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link TemplateParser#selectorList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectorList(TemplateParser.SelectorListContext ctx);
	/**
	 * Visit a parse tree produced by {@link TemplateParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelector(TemplateParser.SelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code universalSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUniversalSelector(TemplateParser.UniversalSelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code idSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdSelector(TemplateParser.IdSelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code classSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassSelector(TemplateParser.ClassSelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code typeSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSelector(TemplateParser.TypeSelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pseudoClassSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPseudoClassSelector(TemplateParser.PseudoClassSelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pseudoElementSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPseudoElementSelector(TemplateParser.PseudoElementSelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code attributeSelectorPart}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeSelectorPart(TemplateParser.AttributeSelectorPartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pseudoClassWithArgSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPseudoClassWithArgSelector(TemplateParser.PseudoClassWithArgSelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleAttributeSelector}
	 * labeled alternative in {@link TemplateParser#attributeSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleAttributeSelector(TemplateParser.SimpleAttributeSelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code comparisonAttributeSelector}
	 * labeled alternative in {@link TemplateParser#attributeSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonAttributeSelector(TemplateParser.ComparisonAttributeSelectorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code declarationListNode}
	 * labeled alternative in {@link TemplateParser#declarationList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationListNode(TemplateParser.DeclarationListNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code declarationNode}
	 * labeled alternative in {@link TemplateParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationNode(TemplateParser.DeclarationNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code declarationValueNode}
	 * labeled alternative in {@link TemplateParser#declarationValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationValueNode(TemplateParser.DeclarationValueNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringStyleValue(TemplateParser.StringStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numericStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericStyleValue(TemplateParser.NumericStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierStyleValue(TemplateParser.IdentifierStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code colorStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColorStyleValue(TemplateParser.ColorStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionStyleValue(TemplateParser.FunctionStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code urlStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUrlStyleValue(TemplateParser.UrlStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code hashStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHashStyleValue(TemplateParser.HashStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code commaStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommaStyleValue(TemplateParser.CommaStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code lparenStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLparenStyleValue(TemplateParser.LparenStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rparenStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRparenStyleValue(TemplateParser.RparenStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code lbracketStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLbracketStyleValue(TemplateParser.LbracketStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rbracketStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRbracketStyleValue(TemplateParser.RbracketStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dotStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotStyleValue(TemplateParser.DotStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code starStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStarStyleValue(TemplateParser.StarStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code colonStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColonStyleValue(TemplateParser.ColonStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code otherStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOtherStyleValue(TemplateParser.OtherStyleValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code integerNumType}
	 * labeled alternative in {@link TemplateParser#numericValueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerNumType(TemplateParser.IntegerNumTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decimalNumType}
	 * labeled alternative in {@link TemplateParser#numericValueType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalNumType(TemplateParser.DecimalNumTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCallNode}
	 * labeled alternative in {@link TemplateParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCallNode(TemplateParser.FunctionCallNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code valueListNode}
	 * labeled alternative in {@link TemplateParser#valueList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueListNode(TemplateParser.ValueListNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaExpression}
	 * labeled alternative in {@link TemplateParser#jinjaExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaExpression(TemplateParser.JinjaExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaExpressionBodyNode}
	 * labeled alternative in {@link TemplateParser#jinjaExpressionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaExpressionBodyNode(TemplateParser.JinjaExpressionBodyNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code containerJinjaBlock}
	 * labeled alternative in {@link TemplateParser#jinjaBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainerJinjaBlock(TemplateParser.ContainerJinjaBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleJinjaBlock}
	 * labeled alternative in {@link TemplateParser#jinjaBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleJinjaBlock(TemplateParser.SimpleJinjaBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaSimpleNode}
	 * labeled alternative in {@link TemplateParser#jinjaSimple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaSimpleNode(TemplateParser.JinjaSimpleNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaSimpleBodyNode}
	 * labeled alternative in {@link TemplateParser#jinjaSimpleBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaSimpleBodyNode(TemplateParser.JinjaSimpleBodyNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaContainerNode}
	 * labeled alternative in {@link TemplateParser#jinjaContainer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaContainerNode(TemplateParser.JinjaContainerNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaStatementBodyNode}
	 * labeled alternative in {@link TemplateParser#jinjaStatementBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaStatementBodyNode(TemplateParser.JinjaStatementBodyNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatementStart(TemplateParser.ForStatementStartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatementStart(TemplateParser.IfStatementStartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code blockStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatementStart(TemplateParser.BlockStatementStartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code macroStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroStatementStart(TemplateParser.MacroStatementStartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code elseStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseStatementStart(TemplateParser.ElseStatementStartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code elifStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElifStatementStart(TemplateParser.ElifStatementStartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifStatementEnd}
	 * labeled alternative in {@link TemplateParser#jinjaContainerEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatementEnd(TemplateParser.IfStatementEndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forStatementEnd}
	 * labeled alternative in {@link TemplateParser#jinjaContainerEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatementEnd(TemplateParser.ForStatementEndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code blockStatementEnd}
	 * labeled alternative in {@link TemplateParser#jinjaContainerEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatementEnd(TemplateParser.BlockStatementEndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code macroStatementEnd}
	 * labeled alternative in {@link TemplateParser#jinjaContainerEnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroStatementEnd(TemplateParser.MacroStatementEndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordIf}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordIf(TemplateParser.JinjaKeywordIfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordElse}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordElse(TemplateParser.JinjaKeywordElseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordElif}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordElif(TemplateParser.JinjaKeywordElifContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordEndIf}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordEndIf(TemplateParser.JinjaKeywordEndIfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordFor}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordFor(TemplateParser.JinjaKeywordForContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordEndFor}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordEndFor(TemplateParser.JinjaKeywordEndForContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordBlock}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordBlock(TemplateParser.JinjaKeywordBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordEndBlock}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordEndBlock(TemplateParser.JinjaKeywordEndBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordMacro}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordMacro(TemplateParser.JinjaKeywordMacroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordEndMacro}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordEndMacro(TemplateParser.JinjaKeywordEndMacroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordSet}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordSet(TemplateParser.JinjaKeywordSetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordInclude}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordInclude(TemplateParser.JinjaKeywordIncludeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordExtends}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordExtends(TemplateParser.JinjaKeywordExtendsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordImport}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordImport(TemplateParser.JinjaKeywordImportContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordEq}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordEq(TemplateParser.JinjaKeywordEqContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordNe}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordNe(TemplateParser.JinjaKeywordNeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordGe}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordGe(TemplateParser.JinjaKeywordGeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordLe}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordLe(TemplateParser.JinjaKeywordLeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordAnd}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordAnd(TemplateParser.JinjaKeywordAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordOr}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordOr(TemplateParser.JinjaKeywordOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordNot}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordNot(TemplateParser.JinjaKeywordNotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jinjaKeywordIn}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaKeywordIn(TemplateParser.JinjaKeywordInContext ctx);
}