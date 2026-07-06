// Generated from C:/Users/maria/IdeaProjects/Compiler2/src/main/pythoncompiler/PythonParser.g4 by ANTLR 4.13.2
package main.pythoncompiler;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PythonParser}.
 */
public interface PythonParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code programRoot}
	 * labeled alternative in {@link PythonParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgramRoot(PythonParser.ProgramRootContext ctx);
	/**
	 * Exit a parse tree produced by the {@code programRoot}
	 * labeled alternative in {@link PythonParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgramRoot(PythonParser.ProgramRootContext ctx);
	/**
	 * Enter a parse tree produced by the {@code importStmtNode}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterImportStmtNode(PythonParser.ImportStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code importStmtNode}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitImportStmtNode(PythonParser.ImportStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routeDefNode}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterRouteDefNode(PythonParser.RouteDefNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routeDefNode}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitRouteDefNode(PythonParser.RouteDefNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blockStmtWrapper}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterBlockStmtWrapper(PythonParser.BlockStmtWrapperContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blockStmtWrapper}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitBlockStmtWrapper(PythonParser.BlockStmtWrapperContext ctx);
	/**
	 * Enter a parse tree produced by the {@code annotatedAssignStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterAnnotatedAssignStmtNode(PythonParser.AnnotatedAssignStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code annotatedAssignStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitAnnotatedAssignStmtNode(PythonParser.AnnotatedAssignStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterAssignStmtNode(PythonParser.AssignStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitAssignStmtNode(PythonParser.AssignStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code augAssignStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterAugAssignStmtNode(PythonParser.AugAssignStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code augAssignStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitAugAssignStmtNode(PythonParser.AugAssignStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterGlobalStmtNode(PythonParser.GlobalStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitGlobalStmtNode(PythonParser.GlobalStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code returnStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmtNode(PythonParser.ReturnStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code returnStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmtNode(PythonParser.ReturnStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmtNode(PythonParser.IfStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmtNode(PythonParser.IfStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmtNode(PythonParser.WhileStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmtNode(PythonParser.WhileStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterForStmtNode(PythonParser.ForStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitForStmtNode(PythonParser.ForStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tryStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterTryStmtNode(PythonParser.TryStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tryStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitTryStmtNode(PythonParser.TryStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code withStmtWrapper}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterWithStmtWrapper(PythonParser.WithStmtWrapperContext ctx);
	/**
	 * Exit a parse tree produced by the {@code withStmtWrapper}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitWithStmtWrapper(PythonParser.WithStmtWrapperContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionDefNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDefNode(PythonParser.FunctionDefNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionDefNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDefNode(PythonParser.FunctionDefNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code classDefNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterClassDefNode(PythonParser.ClassDefNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code classDefNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitClassDefNode(PythonParser.ClassDefNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code controlStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterControlStmtNode(PythonParser.ControlStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code controlStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitControlStmtNode(PythonParser.ControlStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterExprStmtNode(PythonParser.ExprStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitExprStmtNode(PythonParser.ExprStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStmtNode(PythonParser.EmptyStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStmtNode(PythonParser.EmptyStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tryExceptNode}
	 * labeled alternative in {@link PythonParser#tryStmt}.
	 * @param ctx the parse tree
	 */
	void enterTryExceptNode(PythonParser.TryExceptNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tryExceptNode}
	 * labeled alternative in {@link PythonParser#tryStmt}.
	 * @param ctx the parse tree
	 */
	void exitTryExceptNode(PythonParser.TryExceptNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code withStmtNode}
	 * labeled alternative in {@link PythonParser#withStmt}.
	 * @param ctx the parse tree
	 */
	void enterWithStmtNode(PythonParser.WithStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code withStmtNode}
	 * labeled alternative in {@link PythonParser#withStmt}.
	 * @param ctx the parse tree
	 */
	void exitWithStmtNode(PythonParser.WithStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyLine}
	 * labeled alternative in {@link PythonParser#emptyStmt}.
	 * @param ctx the parse tree
	 */
	void enterEmptyLine(PythonParser.EmptyLineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyLine}
	 * labeled alternative in {@link PythonParser#emptyStmt}.
	 * @param ctx the parse tree
	 */
	void exitEmptyLine(PythonParser.EmptyLineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fromImportNode}
	 * labeled alternative in {@link PythonParser#importStmt}.
	 * @param ctx the parse tree
	 */
	void enterFromImportNode(PythonParser.FromImportNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fromImportNode}
	 * labeled alternative in {@link PythonParser#importStmt}.
	 * @param ctx the parse tree
	 */
	void exitFromImportNode(PythonParser.FromImportNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleAssignNode}
	 * labeled alternative in {@link PythonParser#assignmentStmt}.
	 * @param ctx the parse tree
	 */
	void enterSimpleAssignNode(PythonParser.SimpleAssignNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleAssignNode}
	 * labeled alternative in {@link PythonParser#assignmentStmt}.
	 * @param ctx the parse tree
	 */
	void exitSimpleAssignNode(PythonParser.SimpleAssignNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code annotatedAssignNode}
	 * labeled alternative in {@link PythonParser#annotatedAssignStmt}.
	 * @param ctx the parse tree
	 */
	void enterAnnotatedAssignNode(PythonParser.AnnotatedAssignNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code annotatedAssignNode}
	 * labeled alternative in {@link PythonParser#annotatedAssignStmt}.
	 * @param ctx the parse tree
	 */
	void exitAnnotatedAssignNode(PythonParser.AnnotatedAssignNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code augAssignNode}
	 * labeled alternative in {@link PythonParser#augAssignStmt}.
	 * @param ctx the parse tree
	 */
	void enterAugAssignNode(PythonParser.AugAssignNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code augAssignNode}
	 * labeled alternative in {@link PythonParser#augAssignStmt}.
	 * @param ctx the parse tree
	 */
	void exitAugAssignNode(PythonParser.AugAssignNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalDeclNode}
	 * labeled alternative in {@link PythonParser#globalStmt}.
	 * @param ctx the parse tree
	 */
	void enterGlobalDeclNode(PythonParser.GlobalDeclNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalDeclNode}
	 * labeled alternative in {@link PythonParser#globalStmt}.
	 * @param ctx the parse tree
	 */
	void exitGlobalDeclNode(PythonParser.GlobalDeclNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decoratedFunctionNode}
	 * labeled alternative in {@link PythonParser#routeDef}.
	 * @param ctx the parse tree
	 */
	void enterDecoratedFunctionNode(PythonParser.DecoratedFunctionNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decoratedFunctionNode}
	 * labeled alternative in {@link PythonParser#routeDef}.
	 * @param ctx the parse tree
	 */
	void exitDecoratedFunctionNode(PythonParser.DecoratedFunctionNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decoratorNode}
	 * labeled alternative in {@link PythonParser#decorator}.
	 * @param ctx the parse tree
	 */
	void enterDecoratorNode(PythonParser.DecoratorNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decoratorNode}
	 * labeled alternative in {@link PythonParser#decorator}.
	 * @param ctx the parse tree
	 */
	void exitDecoratorNode(PythonParser.DecoratorNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionNode}
	 * labeled alternative in {@link PythonParser#functionDef}.
	 * @param ctx the parse tree
	 */
	void enterFunctionNode(PythonParser.FunctionNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionNode}
	 * labeled alternative in {@link PythonParser#functionDef}.
	 * @param ctx the parse tree
	 */
	void exitFunctionNode(PythonParser.FunctionNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code paramListNode}
	 * labeled alternative in {@link PythonParser#paramList}.
	 * @param ctx the parse tree
	 */
	void enterParamListNode(PythonParser.ParamListNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code paramListNode}
	 * labeled alternative in {@link PythonParser#paramList}.
	 * @param ctx the parse tree
	 */
	void exitParamListNode(PythonParser.ParamListNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code classNode}
	 * labeled alternative in {@link PythonParser#classDef}.
	 * @param ctx the parse tree
	 */
	void enterClassNode(PythonParser.ClassNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code classNode}
	 * labeled alternative in {@link PythonParser#classDef}.
	 * @param ctx the parse tree
	 */
	void exitClassNode(PythonParser.ClassNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blockNode}
	 * labeled alternative in {@link PythonParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlockNode(PythonParser.BlockNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blockNode}
	 * labeled alternative in {@link PythonParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlockNode(PythonParser.BlockNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifElseNode}
	 * labeled alternative in {@link PythonParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfElseNode(PythonParser.IfElseNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifElseNode}
	 * labeled alternative in {@link PythonParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfElseNode(PythonParser.IfElseNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileNode}
	 * labeled alternative in {@link PythonParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileNode(PythonParser.WhileNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileNode}
	 * labeled alternative in {@link PythonParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileNode(PythonParser.WhileNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forNode}
	 * labeled alternative in {@link PythonParser#forStmt}.
	 * @param ctx the parse tree
	 */
	void enterForNode(PythonParser.ForNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forNode}
	 * labeled alternative in {@link PythonParser#forStmt}.
	 * @param ctx the parse tree
	 */
	void exitForNode(PythonParser.ForNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code passNode}
	 * labeled alternative in {@link PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 */
	void enterPassNode(PythonParser.PassNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code passNode}
	 * labeled alternative in {@link PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 */
	void exitPassNode(PythonParser.PassNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code breakNode}
	 * labeled alternative in {@link PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 */
	void enterBreakNode(PythonParser.BreakNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code breakNode}
	 * labeled alternative in {@link PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 */
	void exitBreakNode(PythonParser.BreakNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code continueNode}
	 * labeled alternative in {@link PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 */
	void enterContinueNode(PythonParser.ContinueNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code continueNode}
	 * labeled alternative in {@link PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 */
	void exitContinueNode(PythonParser.ContinueNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code returnNode}
	 * labeled alternative in {@link PythonParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnNode(PythonParser.ReturnNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code returnNode}
	 * labeled alternative in {@link PythonParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnNode(PythonParser.ReturnNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionStmtNode}
	 * labeled alternative in {@link PythonParser#exprStmt}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStmtNode(PythonParser.ExpressionStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionStmtNode}
	 * labeled alternative in {@link PythonParser#exprStmt}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStmtNode(PythonParser.ExpressionStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionRoot}
	 * labeled alternative in {@link PythonParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionRoot(PythonParser.ExpressionRootContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionRoot}
	 * labeled alternative in {@link PythonParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionRoot(PythonParser.ExpressionRootContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalExprNode}
	 * labeled alternative in {@link PythonParser#logicalExpr}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExprNode(PythonParser.LogicalExprNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalExprNode}
	 * labeled alternative in {@link PythonParser#logicalExpr}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExprNode(PythonParser.LogicalExprNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code comparisonExprNode}
	 * labeled alternative in {@link PythonParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExprNode(PythonParser.ComparisonExprNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code comparisonExprNode}
	 * labeled alternative in {@link PythonParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExprNode(PythonParser.ComparisonExprNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithmeticExprNode}
	 * labeled alternative in {@link PythonParser#arithmeticExpr}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticExprNode(PythonParser.ArithmeticExprNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithmeticExprNode}
	 * labeled alternative in {@link PythonParser#arithmeticExpr}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticExprNode(PythonParser.ArithmeticExprNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code termNode}
	 * labeled alternative in {@link PythonParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTermNode(PythonParser.TermNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code termNode}
	 * labeled alternative in {@link PythonParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTermNode(PythonParser.TermNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link PythonParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(PythonParser.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link PythonParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(PythonParser.NotExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code factorPrimary}
	 * labeled alternative in {@link PythonParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactorPrimary(PythonParser.FactorPrimaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code factorPrimary}
	 * labeled alternative in {@link PythonParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactorPrimary(PythonParser.FactorPrimaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterStringExpr(PythonParser.StringExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitStringExpr(PythonParser.StringExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code indexExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterIndexExpr(PythonParser.IndexExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code indexExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitIndexExpr(PythonParser.IndexExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code trueExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterTrueExpr(PythonParser.TrueExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code trueExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitTrueExpr(PythonParser.TrueExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numberExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterNumberExpr(PythonParser.NumberExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numberExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitNumberExpr(PythonParser.NumberExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code noneExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterNoneExpr(PythonParser.NoneExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code noneExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitNoneExpr(PythonParser.NoneExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code attributeExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterAttributeExpr(PythonParser.AttributeExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code attributeExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitAttributeExpr(PythonParser.AttributeExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code falseExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterFalseExpr(PythonParser.FalseExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code falseExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitFalseExpr(PythonParser.FalseExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dictExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterDictExpr(PythonParser.DictExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dictExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitDictExpr(PythonParser.DictExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterCallExpr(PythonParser.CallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitCallExpr(PythonParser.CallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code listExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterListExpr(PythonParser.ListExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code listExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitListExpr(PythonParser.ListExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterParenExpr(PythonParser.ParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitParenExpr(PythonParser.ParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code identifierExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierExpr(PythonParser.IdentifierExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code identifierExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierExpr(PythonParser.IdentifierExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code listLiteralNode}
	 * labeled alternative in {@link PythonParser#listLiteral}.
	 * @param ctx the parse tree
	 */
	void enterListLiteralNode(PythonParser.ListLiteralNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code listLiteralNode}
	 * labeled alternative in {@link PythonParser#listLiteral}.
	 * @param ctx the parse tree
	 */
	void exitListLiteralNode(PythonParser.ListLiteralNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dictLiteralNode}
	 * labeled alternative in {@link PythonParser#dictLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDictLiteralNode(PythonParser.DictLiteralNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dictLiteralNode}
	 * labeled alternative in {@link PythonParser#dictLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDictLiteralNode(PythonParser.DictLiteralNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dictEntryNode}
	 * labeled alternative in {@link PythonParser#dictEntry}.
	 * @param ctx the parse tree
	 */
	void enterDictEntryNode(PythonParser.DictEntryNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dictEntryNode}
	 * labeled alternative in {@link PythonParser#dictEntry}.
	 * @param ctx the parse tree
	 */
	void exitDictEntryNode(PythonParser.DictEntryNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code argListNode}
	 * labeled alternative in {@link PythonParser#argList}.
	 * @param ctx the parse tree
	 */
	void enterArgListNode(PythonParser.ArgListNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code argListNode}
	 * labeled alternative in {@link PythonParser#argList}.
	 * @param ctx the parse tree
	 */
	void exitArgListNode(PythonParser.ArgListNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code namedArgNode}
	 * labeled alternative in {@link PythonParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterNamedArgNode(PythonParser.NamedArgNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code namedArgNode}
	 * labeled alternative in {@link PythonParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitNamedArgNode(PythonParser.NamedArgNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code positionalArgNode}
	 * labeled alternative in {@link PythonParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterPositionalArgNode(PythonParser.PositionalArgNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code positionalArgNode}
	 * labeled alternative in {@link PythonParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitPositionalArgNode(PythonParser.PositionalArgNodeContext ctx);
}