// Generated from C:/Users/HP/Downloads/Compiler2/src/antlr/TemplateParser.g4 by ANTLR 4.13.2
package antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TemplateParser}.
 */
public interface TemplateParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TemplateParser#page}.
	 * @param ctx the parse tree
	 */
	void enterPage(TemplateParser.PageContext ctx);
	/**
	 * Exit a parse tree produced by {@link TemplateParser#page}.
	 * @param ctx the parse tree
	 */
	void exitPage(TemplateParser.PageContext ctx);
	/**
	 * Enter a parse tree produced by {@link TemplateParser#content}.
	 * @param ctx the parse tree
	 */
	void enterContent(TemplateParser.ContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TemplateParser#content}.
	 * @param ctx the parse tree
	 */
	void exitContent(TemplateParser.ContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TemplateParser#node}.
	 * @param ctx the parse tree
	 */
	void enterNode(TemplateParser.NodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TemplateParser#node}.
	 * @param ctx the parse tree
	 */
	void exitNode(TemplateParser.NodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code textContentNode}
	 * labeled alternative in {@link TemplateParser#textContent}.
	 * @param ctx the parse tree
	 */
	void enterTextContentNode(TemplateParser.TextContentNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code textContentNode}
	 * labeled alternative in {@link TemplateParser#textContent}.
	 * @param ctx the parse tree
	 */
	void exitTextContentNode(TemplateParser.TextContentNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TemplateParser#htmlDocument}.
	 * @param ctx the parse tree
	 */
	void enterHtmlDocument(TemplateParser.HtmlDocumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TemplateParser#htmlDocument}.
	 * @param ctx the parse tree
	 */
	void exitHtmlDocument(TemplateParser.HtmlDocumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doctypeNode}
	 * labeled alternative in {@link TemplateParser#doctype}.
	 * @param ctx the parse tree
	 */
	void enterDoctypeNode(TemplateParser.DoctypeNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doctypeNode}
	 * labeled alternative in {@link TemplateParser#doctype}.
	 * @param ctx the parse tree
	 */
	void exitDoctypeNode(TemplateParser.DoctypeNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pairedHtmlElement}
	 * labeled alternative in {@link TemplateParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterPairedHtmlElement(TemplateParser.PairedHtmlElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pairedHtmlElement}
	 * labeled alternative in {@link TemplateParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitPairedHtmlElement(TemplateParser.PairedHtmlElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code openingTagNode}
	 * labeled alternative in {@link TemplateParser#openingTag}.
	 * @param ctx the parse tree
	 */
	void enterOpeningTagNode(TemplateParser.OpeningTagNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code openingTagNode}
	 * labeled alternative in {@link TemplateParser#openingTag}.
	 * @param ctx the parse tree
	 */
	void exitOpeningTagNode(TemplateParser.OpeningTagNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code closingTagNode}
	 * labeled alternative in {@link TemplateParser#closingTag}.
	 * @param ctx the parse tree
	 */
	void enterClosingTagNode(TemplateParser.ClosingTagNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code closingTagNode}
	 * labeled alternative in {@link TemplateParser#closingTag}.
	 * @param ctx the parse tree
	 */
	void exitClosingTagNode(TemplateParser.ClosingTagNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selfClosingElement}
	 * labeled alternative in {@link TemplateParser#selfClosingTag}.
	 * @param ctx the parse tree
	 */
	void enterSelfClosingElement(TemplateParser.SelfClosingElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selfClosingElement}
	 * labeled alternative in {@link TemplateParser#selfClosingTag}.
	 * @param ctx the parse tree
	 */
	void exitSelfClosingElement(TemplateParser.SelfClosingElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code htmlAttribute}
	 * labeled alternative in {@link TemplateParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterHtmlAttribute(TemplateParser.HtmlAttributeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code htmlAttribute}
	 * labeled alternative in {@link TemplateParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitHtmlAttribute(TemplateParser.HtmlAttributeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doubleQuotedAttribute}
	 * labeled alternative in {@link TemplateParser#attributeValue}.
	 * @param ctx the parse tree
	 */
	void enterDoubleQuotedAttribute(TemplateParser.DoubleQuotedAttributeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doubleQuotedAttribute}
	 * labeled alternative in {@link TemplateParser#attributeValue}.
	 * @param ctx the parse tree
	 */
	void exitDoubleQuotedAttribute(TemplateParser.DoubleQuotedAttributeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code singleQuotedAttribute}
	 * labeled alternative in {@link TemplateParser#attributeValue}.
	 * @param ctx the parse tree
	 */
	void enterSingleQuotedAttribute(TemplateParser.SingleQuotedAttributeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code singleQuotedAttribute}
	 * labeled alternative in {@link TemplateParser#attributeValue}.
	 * @param ctx the parse tree
	 */
	void exitSingleQuotedAttribute(TemplateParser.SingleQuotedAttributeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unquotedAttribute}
	 * labeled alternative in {@link TemplateParser#attributeValue}.
	 * @param ctx the parse tree
	 */
	void enterUnquotedAttribute(TemplateParser.UnquotedAttributeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unquotedAttribute}
	 * labeled alternative in {@link TemplateParser#attributeValue}.
	 * @param ctx the parse tree
	 */
	void exitUnquotedAttribute(TemplateParser.UnquotedAttributeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code styleBlockNode}
	 * labeled alternative in {@link TemplateParser#styleBlock}.
	 * @param ctx the parse tree
	 */
	void enterStyleBlockNode(TemplateParser.StyleBlockNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code styleBlockNode}
	 * labeled alternative in {@link TemplateParser#styleBlock}.
	 * @param ctx the parse tree
	 */
	void exitStyleBlockNode(TemplateParser.StyleBlockNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code styleRuleNode}
	 * labeled alternative in {@link TemplateParser#styleRule}.
	 * @param ctx the parse tree
	 */
	void enterStyleRuleNode(TemplateParser.StyleRuleNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code styleRuleNode}
	 * labeled alternative in {@link TemplateParser#styleRule}.
	 * @param ctx the parse tree
	 */
	void exitStyleRuleNode(TemplateParser.StyleRuleNodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TemplateParser#selectorList}.
	 * @param ctx the parse tree
	 */
	void enterSelectorList(TemplateParser.SelectorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TemplateParser#selectorList}.
	 * @param ctx the parse tree
	 */
	void exitSelectorList(TemplateParser.SelectorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TemplateParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterSelector(TemplateParser.SelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TemplateParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitSelector(TemplateParser.SelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code universalSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void enterUniversalSelector(TemplateParser.UniversalSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code universalSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void exitUniversalSelector(TemplateParser.UniversalSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code idSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void enterIdSelector(TemplateParser.IdSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code idSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void exitIdSelector(TemplateParser.IdSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code classSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void enterClassSelector(TemplateParser.ClassSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code classSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void exitClassSelector(TemplateParser.ClassSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code typeSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void enterTypeSelector(TemplateParser.TypeSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code typeSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void exitTypeSelector(TemplateParser.TypeSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pseudoClassSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void enterPseudoClassSelector(TemplateParser.PseudoClassSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pseudoClassSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void exitPseudoClassSelector(TemplateParser.PseudoClassSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pseudoElementSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void enterPseudoElementSelector(TemplateParser.PseudoElementSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pseudoElementSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void exitPseudoElementSelector(TemplateParser.PseudoElementSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code attributeSelectorPart}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void enterAttributeSelectorPart(TemplateParser.AttributeSelectorPartContext ctx);
	/**
	 * Exit a parse tree produced by the {@code attributeSelectorPart}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void exitAttributeSelectorPart(TemplateParser.AttributeSelectorPartContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pseudoClassWithArgSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void enterPseudoClassWithArgSelector(TemplateParser.PseudoClassWithArgSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pseudoClassWithArgSelector}
	 * labeled alternative in {@link TemplateParser#selectorPart}.
	 * @param ctx the parse tree
	 */
	void exitPseudoClassWithArgSelector(TemplateParser.PseudoClassWithArgSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleAttributeSelector}
	 * labeled alternative in {@link TemplateParser#attributeSelector}.
	 * @param ctx the parse tree
	 */
	void enterSimpleAttributeSelector(TemplateParser.SimpleAttributeSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleAttributeSelector}
	 * labeled alternative in {@link TemplateParser#attributeSelector}.
	 * @param ctx the parse tree
	 */
	void exitSimpleAttributeSelector(TemplateParser.SimpleAttributeSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code comparisonAttributeSelector}
	 * labeled alternative in {@link TemplateParser#attributeSelector}.
	 * @param ctx the parse tree
	 */
	void enterComparisonAttributeSelector(TemplateParser.ComparisonAttributeSelectorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code comparisonAttributeSelector}
	 * labeled alternative in {@link TemplateParser#attributeSelector}.
	 * @param ctx the parse tree
	 */
	void exitComparisonAttributeSelector(TemplateParser.ComparisonAttributeSelectorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code declarationListNode}
	 * labeled alternative in {@link TemplateParser#declarationList}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationListNode(TemplateParser.DeclarationListNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code declarationListNode}
	 * labeled alternative in {@link TemplateParser#declarationList}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationListNode(TemplateParser.DeclarationListNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code declarationNode}
	 * labeled alternative in {@link TemplateParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationNode(TemplateParser.DeclarationNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code declarationNode}
	 * labeled alternative in {@link TemplateParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationNode(TemplateParser.DeclarationNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code declarationValueNode}
	 * labeled alternative in {@link TemplateParser#declarationValue}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationValueNode(TemplateParser.DeclarationValueNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code declarationValueNode}
	 * labeled alternative in {@link TemplateParser#declarationValue}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationValueNode(TemplateParser.DeclarationValueNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterStringStyleValue(TemplateParser.StringStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitStringStyleValue(TemplateParser.StringStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numericStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterNumericStyleValue(TemplateParser.NumericStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numericStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitNumericStyleValue(TemplateParser.NumericStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code identifierStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierStyleValue(TemplateParser.IdentifierStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code identifierStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierStyleValue(TemplateParser.IdentifierStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code colorStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterColorStyleValue(TemplateParser.ColorStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code colorStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitColorStyleValue(TemplateParser.ColorStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterFunctionStyleValue(TemplateParser.FunctionStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitFunctionStyleValue(TemplateParser.FunctionStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code urlStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterUrlStyleValue(TemplateParser.UrlStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code urlStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitUrlStyleValue(TemplateParser.UrlStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code hashStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterHashStyleValue(TemplateParser.HashStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code hashStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitHashStyleValue(TemplateParser.HashStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code commaStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterCommaStyleValue(TemplateParser.CommaStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code commaStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitCommaStyleValue(TemplateParser.CommaStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lparenStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterLparenStyleValue(TemplateParser.LparenStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lparenStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitLparenStyleValue(TemplateParser.LparenStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rparenStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterRparenStyleValue(TemplateParser.RparenStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rparenStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitRparenStyleValue(TemplateParser.RparenStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lbracketStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterLbracketStyleValue(TemplateParser.LbracketStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lbracketStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitLbracketStyleValue(TemplateParser.LbracketStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rbracketStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterRbracketStyleValue(TemplateParser.RbracketStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rbracketStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitRbracketStyleValue(TemplateParser.RbracketStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dotStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterDotStyleValue(TemplateParser.DotStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dotStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitDotStyleValue(TemplateParser.DotStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code starStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterStarStyleValue(TemplateParser.StarStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code starStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitStarStyleValue(TemplateParser.StarStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code colonStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterColonStyleValue(TemplateParser.ColonStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code colonStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitColonStyleValue(TemplateParser.ColonStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code otherStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void enterOtherStyleValue(TemplateParser.OtherStyleValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code otherStyleValue}
	 * labeled alternative in {@link TemplateParser#styleValuePart}.
	 * @param ctx the parse tree
	 */
	void exitOtherStyleValue(TemplateParser.OtherStyleValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code integerNumType}
	 * labeled alternative in {@link TemplateParser#numericValueType}.
	 * @param ctx the parse tree
	 */
	void enterIntegerNumType(TemplateParser.IntegerNumTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code integerNumType}
	 * labeled alternative in {@link TemplateParser#numericValueType}.
	 * @param ctx the parse tree
	 */
	void exitIntegerNumType(TemplateParser.IntegerNumTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decimalNumType}
	 * labeled alternative in {@link TemplateParser#numericValueType}.
	 * @param ctx the parse tree
	 */
	void enterDecimalNumType(TemplateParser.DecimalNumTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decimalNumType}
	 * labeled alternative in {@link TemplateParser#numericValueType}.
	 * @param ctx the parse tree
	 */
	void exitDecimalNumType(TemplateParser.DecimalNumTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionCallNode}
	 * labeled alternative in {@link TemplateParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCallNode(TemplateParser.FunctionCallNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionCallNode}
	 * labeled alternative in {@link TemplateParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCallNode(TemplateParser.FunctionCallNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code valueListNode}
	 * labeled alternative in {@link TemplateParser#valueList}.
	 * @param ctx the parse tree
	 */
	void enterValueListNode(TemplateParser.ValueListNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueListNode}
	 * labeled alternative in {@link TemplateParser#valueList}.
	 * @param ctx the parse tree
	 */
	void exitValueListNode(TemplateParser.ValueListNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaExpression}
	 * labeled alternative in {@link TemplateParser#jinjaExpr}.
	 * @param ctx the parse tree
	 */
	void enterJinjaExpression(TemplateParser.JinjaExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaExpression}
	 * labeled alternative in {@link TemplateParser#jinjaExpr}.
	 * @param ctx the parse tree
	 */
	void exitJinjaExpression(TemplateParser.JinjaExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaExpressionBodyNode}
	 * labeled alternative in {@link TemplateParser#jinjaExpressionBody}.
	 * @param ctx the parse tree
	 */
	void enterJinjaExpressionBodyNode(TemplateParser.JinjaExpressionBodyNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaExpressionBodyNode}
	 * labeled alternative in {@link TemplateParser#jinjaExpressionBody}.
	 * @param ctx the parse tree
	 */
	void exitJinjaExpressionBodyNode(TemplateParser.JinjaExpressionBodyNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code containerJinjaBlock}
	 * labeled alternative in {@link TemplateParser#jinjaBlock}.
	 * @param ctx the parse tree
	 */
	void enterContainerJinjaBlock(TemplateParser.ContainerJinjaBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code containerJinjaBlock}
	 * labeled alternative in {@link TemplateParser#jinjaBlock}.
	 * @param ctx the parse tree
	 */
	void exitContainerJinjaBlock(TemplateParser.ContainerJinjaBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleJinjaBlock}
	 * labeled alternative in {@link TemplateParser#jinjaBlock}.
	 * @param ctx the parse tree
	 */
	void enterSimpleJinjaBlock(TemplateParser.SimpleJinjaBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleJinjaBlock}
	 * labeled alternative in {@link TemplateParser#jinjaBlock}.
	 * @param ctx the parse tree
	 */
	void exitSimpleJinjaBlock(TemplateParser.SimpleJinjaBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaSimpleNode}
	 * labeled alternative in {@link TemplateParser#jinjaSimple}.
	 * @param ctx the parse tree
	 */
	void enterJinjaSimpleNode(TemplateParser.JinjaSimpleNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaSimpleNode}
	 * labeled alternative in {@link TemplateParser#jinjaSimple}.
	 * @param ctx the parse tree
	 */
	void exitJinjaSimpleNode(TemplateParser.JinjaSimpleNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaSimpleBodyNode}
	 * labeled alternative in {@link TemplateParser#jinjaSimpleBody}.
	 * @param ctx the parse tree
	 */
	void enterJinjaSimpleBodyNode(TemplateParser.JinjaSimpleBodyNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaSimpleBodyNode}
	 * labeled alternative in {@link TemplateParser#jinjaSimpleBody}.
	 * @param ctx the parse tree
	 */
	void exitJinjaSimpleBodyNode(TemplateParser.JinjaSimpleBodyNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaContainerNode}
	 * labeled alternative in {@link TemplateParser#jinjaContainer}.
	 * @param ctx the parse tree
	 */
	void enterJinjaContainerNode(TemplateParser.JinjaContainerNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaContainerNode}
	 * labeled alternative in {@link TemplateParser#jinjaContainer}.
	 * @param ctx the parse tree
	 */
	void exitJinjaContainerNode(TemplateParser.JinjaContainerNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaStatementBodyNode}
	 * labeled alternative in {@link TemplateParser#jinjaStatementBody}.
	 * @param ctx the parse tree
	 */
	void enterJinjaStatementBodyNode(TemplateParser.JinjaStatementBodyNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaStatementBodyNode}
	 * labeled alternative in {@link TemplateParser#jinjaStatementBody}.
	 * @param ctx the parse tree
	 */
	void exitJinjaStatementBodyNode(TemplateParser.JinjaStatementBodyNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 */
	void enterForStatementStart(TemplateParser.ForStatementStartContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 */
	void exitForStatementStart(TemplateParser.ForStatementStartContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 */
	void enterIfStatementStart(TemplateParser.IfStatementStartContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 */
	void exitIfStatementStart(TemplateParser.IfStatementStartContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blockStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatementStart(TemplateParser.BlockStatementStartContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blockStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatementStart(TemplateParser.BlockStatementStartContext ctx);
	/**
	 * Enter a parse tree produced by the {@code macroStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 */
	void enterMacroStatementStart(TemplateParser.MacroStatementStartContext ctx);
	/**
	 * Exit a parse tree produced by the {@code macroStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 */
	void exitMacroStatementStart(TemplateParser.MacroStatementStartContext ctx);
	/**
	 * Enter a parse tree produced by the {@code elseStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 */
	void enterElseStatementStart(TemplateParser.ElseStatementStartContext ctx);
	/**
	 * Exit a parse tree produced by the {@code elseStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 */
	void exitElseStatementStart(TemplateParser.ElseStatementStartContext ctx);
	/**
	 * Enter a parse tree produced by the {@code elifStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 */
	void enterElifStatementStart(TemplateParser.ElifStatementStartContext ctx);
	/**
	 * Exit a parse tree produced by the {@code elifStatementStart}
	 * labeled alternative in {@link TemplateParser#jinjaContainerStart}.
	 * @param ctx the parse tree
	 */
	void exitElifStatementStart(TemplateParser.ElifStatementStartContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifStatementEnd}
	 * labeled alternative in {@link TemplateParser#jinjaContainerEnd}.
	 * @param ctx the parse tree
	 */
	void enterIfStatementEnd(TemplateParser.IfStatementEndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifStatementEnd}
	 * labeled alternative in {@link TemplateParser#jinjaContainerEnd}.
	 * @param ctx the parse tree
	 */
	void exitIfStatementEnd(TemplateParser.IfStatementEndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forStatementEnd}
	 * labeled alternative in {@link TemplateParser#jinjaContainerEnd}.
	 * @param ctx the parse tree
	 */
	void enterForStatementEnd(TemplateParser.ForStatementEndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forStatementEnd}
	 * labeled alternative in {@link TemplateParser#jinjaContainerEnd}.
	 * @param ctx the parse tree
	 */
	void exitForStatementEnd(TemplateParser.ForStatementEndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blockStatementEnd}
	 * labeled alternative in {@link TemplateParser#jinjaContainerEnd}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatementEnd(TemplateParser.BlockStatementEndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blockStatementEnd}
	 * labeled alternative in {@link TemplateParser#jinjaContainerEnd}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatementEnd(TemplateParser.BlockStatementEndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code macroStatementEnd}
	 * labeled alternative in {@link TemplateParser#jinjaContainerEnd}.
	 * @param ctx the parse tree
	 */
	void enterMacroStatementEnd(TemplateParser.MacroStatementEndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code macroStatementEnd}
	 * labeled alternative in {@link TemplateParser#jinjaContainerEnd}.
	 * @param ctx the parse tree
	 */
	void exitMacroStatementEnd(TemplateParser.MacroStatementEndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordIf}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordIf(TemplateParser.JinjaKeywordIfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordIf}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordIf(TemplateParser.JinjaKeywordIfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordElse}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordElse(TemplateParser.JinjaKeywordElseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordElse}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordElse(TemplateParser.JinjaKeywordElseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordElif}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordElif(TemplateParser.JinjaKeywordElifContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordElif}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordElif(TemplateParser.JinjaKeywordElifContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordEndIf}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordEndIf(TemplateParser.JinjaKeywordEndIfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordEndIf}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordEndIf(TemplateParser.JinjaKeywordEndIfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordFor}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordFor(TemplateParser.JinjaKeywordForContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordFor}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordFor(TemplateParser.JinjaKeywordForContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordEndFor}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordEndFor(TemplateParser.JinjaKeywordEndForContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordEndFor}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordEndFor(TemplateParser.JinjaKeywordEndForContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordBlock}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordBlock(TemplateParser.JinjaKeywordBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordBlock}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordBlock(TemplateParser.JinjaKeywordBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordEndBlock}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordEndBlock(TemplateParser.JinjaKeywordEndBlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordEndBlock}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordEndBlock(TemplateParser.JinjaKeywordEndBlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordMacro}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordMacro(TemplateParser.JinjaKeywordMacroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordMacro}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordMacro(TemplateParser.JinjaKeywordMacroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordEndMacro}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordEndMacro(TemplateParser.JinjaKeywordEndMacroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordEndMacro}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordEndMacro(TemplateParser.JinjaKeywordEndMacroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordSet}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordSet(TemplateParser.JinjaKeywordSetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordSet}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordSet(TemplateParser.JinjaKeywordSetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordInclude}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordInclude(TemplateParser.JinjaKeywordIncludeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordInclude}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordInclude(TemplateParser.JinjaKeywordIncludeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordExtends}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordExtends(TemplateParser.JinjaKeywordExtendsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordExtends}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordExtends(TemplateParser.JinjaKeywordExtendsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordImport}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordImport(TemplateParser.JinjaKeywordImportContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordImport}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordImport(TemplateParser.JinjaKeywordImportContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordEq}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordEq(TemplateParser.JinjaKeywordEqContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordEq}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordEq(TemplateParser.JinjaKeywordEqContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordNe}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordNe(TemplateParser.JinjaKeywordNeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordNe}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordNe(TemplateParser.JinjaKeywordNeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordGe}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordGe(TemplateParser.JinjaKeywordGeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordGe}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordGe(TemplateParser.JinjaKeywordGeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordLe}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordLe(TemplateParser.JinjaKeywordLeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordLe}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordLe(TemplateParser.JinjaKeywordLeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordAnd}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordAnd(TemplateParser.JinjaKeywordAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordAnd}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordAnd(TemplateParser.JinjaKeywordAndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordOr}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordOr(TemplateParser.JinjaKeywordOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordOr}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordOr(TemplateParser.JinjaKeywordOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordNot}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordNot(TemplateParser.JinjaKeywordNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordNot}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordNot(TemplateParser.JinjaKeywordNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code jinjaKeywordIn}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void enterJinjaKeywordIn(TemplateParser.JinjaKeywordInContext ctx);
	/**
	 * Exit a parse tree produced by the {@code jinjaKeywordIn}
	 * labeled alternative in {@link TemplateParser#jinjaKeyword}.
	 * @param ctx the parse tree
	 */
	void exitJinjaKeywordIn(TemplateParser.JinjaKeywordInContext ctx);
}