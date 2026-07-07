// Generated from C:/Users/maria/IdeaProjects/Compiler2/src/main/pythoncompiler/PythonParser.g4 by ANTLR 4.13.2
package main.pythoncompiler.grammer.main.pythoncompiler.grammer;
import main.pythoncompiler.PythonParser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link main.pythoncompiler.PythonParser}.
 */
public interface PythonParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code programRoot}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgramRoot(main.pythoncompiler.PythonParser.ProgramRootContext ctx);
	/**
	 * Exit a parse tree produced by the {@code programRoot}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgramRoot(main.pythoncompiler.PythonParser.ProgramRootContext ctx);
	/**
	 * Enter a parse tree produced by the {@code importStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterImportStmtNode(main.pythoncompiler.PythonParser.ImportStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code importStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitImportStmtNode(main.pythoncompiler.PythonParser.ImportStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code routeDefNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterRouteDefNode(main.pythoncompiler.PythonParser.RouteDefNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code routeDefNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitRouteDefNode(main.pythoncompiler.PythonParser.RouteDefNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blockStmtWrapper}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterBlockStmtWrapper(main.pythoncompiler.PythonParser.BlockStmtWrapperContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blockStmtWrapper}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitBlockStmtWrapper(main.pythoncompiler.PythonParser.BlockStmtWrapperContext ctx);
	/**
	 * Enter a parse tree produced by the {@code annotatedAssignStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterAnnotatedAssignStmtNode(main.pythoncompiler.PythonParser.AnnotatedAssignStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code annotatedAssignStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitAnnotatedAssignStmtNode(main.pythoncompiler.PythonParser.AnnotatedAssignStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterAssignStmtNode(main.pythoncompiler.PythonParser.AssignStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitAssignStmtNode(main.pythoncompiler.PythonParser.AssignStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code augAssignStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterAugAssignStmtNode(main.pythoncompiler.PythonParser.AugAssignStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code augAssignStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitAugAssignStmtNode(main.pythoncompiler.PythonParser.AugAssignStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterGlobalStmtNode(main.pythoncompiler.PythonParser.GlobalStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitGlobalStmtNode(main.pythoncompiler.PythonParser.GlobalStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code returnStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmtNode(main.pythoncompiler.PythonParser.ReturnStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code returnStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmtNode(main.pythoncompiler.PythonParser.ReturnStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmtNode(main.pythoncompiler.PythonParser.IfStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmtNode(main.pythoncompiler.PythonParser.IfStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmtNode(main.pythoncompiler.PythonParser.WhileStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmtNode(main.pythoncompiler.PythonParser.WhileStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterForStmtNode(main.pythoncompiler.PythonParser.ForStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitForStmtNode(main.pythoncompiler.PythonParser.ForStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tryStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterTryStmtNode(main.pythoncompiler.PythonParser.TryStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tryStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitTryStmtNode(main.pythoncompiler.PythonParser.TryStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code withStmtWrapper}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterWithStmtWrapper(main.pythoncompiler.PythonParser.WithStmtWrapperContext ctx);
	/**
	 * Exit a parse tree produced by the {@code withStmtWrapper}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitWithStmtWrapper(main.pythoncompiler.PythonParser.WithStmtWrapperContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionDefNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDefNode(main.pythoncompiler.PythonParser.FunctionDefNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionDefNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDefNode(main.pythoncompiler.PythonParser.FunctionDefNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code classDefNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterClassDefNode(main.pythoncompiler.PythonParser.ClassDefNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code classDefNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitClassDefNode(main.pythoncompiler.PythonParser.ClassDefNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code controlStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterControlStmtNode(main.pythoncompiler.PythonParser.ControlStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code controlStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitControlStmtNode(main.pythoncompiler.PythonParser.ControlStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterExprStmtNode(main.pythoncompiler.PythonParser.ExprStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitExprStmtNode(main.pythoncompiler.PythonParser.ExprStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStmtNode(main.pythoncompiler.PythonParser.EmptyStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStmtNode(main.pythoncompiler.PythonParser.EmptyStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tryExceptNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#tryStmt}.
	 * @param ctx the parse tree
	 */
	void enterTryExceptNode(main.pythoncompiler.PythonParser.TryExceptNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tryExceptNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#tryStmt}.
	 * @param ctx the parse tree
	 */
	void exitTryExceptNode(main.pythoncompiler.PythonParser.TryExceptNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code withStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#withStmt}.
	 * @param ctx the parse tree
	 */
	void enterWithStmtNode(main.pythoncompiler.PythonParser.WithStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code withStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#withStmt}.
	 * @param ctx the parse tree
	 */
	void exitWithStmtNode(main.pythoncompiler.PythonParser.WithStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyLine}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#emptyStmt}.
	 * @param ctx the parse tree
	 */
	void enterEmptyLine(main.pythoncompiler.PythonParser.EmptyLineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyLine}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#emptyStmt}.
	 * @param ctx the parse tree
	 */
	void exitEmptyLine(main.pythoncompiler.PythonParser.EmptyLineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fromImportNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#importStmt}.
	 * @param ctx the parse tree
	 */
	void enterFromImportNode(main.pythoncompiler.PythonParser.FromImportNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fromImportNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#importStmt}.
	 * @param ctx the parse tree
	 */
	void exitFromImportNode(main.pythoncompiler.PythonParser.FromImportNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleAssignNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#assignmentStmt}.
	 * @param ctx the parse tree
	 */
	void enterSimpleAssignNode(main.pythoncompiler.PythonParser.SimpleAssignNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleAssignNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#assignmentStmt}.
	 * @param ctx the parse tree
	 */
	void exitSimpleAssignNode(main.pythoncompiler.PythonParser.SimpleAssignNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code annotatedAssignNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#annotatedAssignStmt}.
	 * @param ctx the parse tree
	 */
	void enterAnnotatedAssignNode(main.pythoncompiler.PythonParser.AnnotatedAssignNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code annotatedAssignNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#annotatedAssignStmt}.
	 * @param ctx the parse tree
	 */
	void exitAnnotatedAssignNode(main.pythoncompiler.PythonParser.AnnotatedAssignNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code augAssignNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#augAssignStmt}.
	 * @param ctx the parse tree
	 */
	void enterAugAssignNode(main.pythoncompiler.PythonParser.AugAssignNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code augAssignNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#augAssignStmt}.
	 * @param ctx the parse tree
	 */
	void exitAugAssignNode(main.pythoncompiler.PythonParser.AugAssignNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globalDeclNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#globalStmt}.
	 * @param ctx the parse tree
	 */
	void enterGlobalDeclNode(main.pythoncompiler.PythonParser.GlobalDeclNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globalDeclNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#globalStmt}.
	 * @param ctx the parse tree
	 */
	void exitGlobalDeclNode(main.pythoncompiler.PythonParser.GlobalDeclNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decoratedFunctionNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#routeDef}.
	 * @param ctx the parse tree
	 */
	void enterDecoratedFunctionNode(main.pythoncompiler.PythonParser.DecoratedFunctionNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decoratedFunctionNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#routeDef}.
	 * @param ctx the parse tree
	 */
	void exitDecoratedFunctionNode(main.pythoncompiler.PythonParser.DecoratedFunctionNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decoratorNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#decorator}.
	 * @param ctx the parse tree
	 */
	void enterDecoratorNode(main.pythoncompiler.PythonParser.DecoratorNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decoratorNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#decorator}.
	 * @param ctx the parse tree
	 */
	void exitDecoratorNode(main.pythoncompiler.PythonParser.DecoratorNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#functionDef}.
	 * @param ctx the parse tree
	 */
	void enterFunctionNode(main.pythoncompiler.PythonParser.FunctionNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#functionDef}.
	 * @param ctx the parse tree
	 */
	void exitFunctionNode(main.pythoncompiler.PythonParser.FunctionNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code paramListNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#paramList}.
	 * @param ctx the parse tree
	 */
	void enterParamListNode(main.pythoncompiler.PythonParser.ParamListNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code paramListNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#paramList}.
	 * @param ctx the parse tree
	 */
	void exitParamListNode(main.pythoncompiler.PythonParser.ParamListNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code classNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#classDef}.
	 * @param ctx the parse tree
	 */
	void enterClassNode(main.pythoncompiler.PythonParser.ClassNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code classNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#classDef}.
	 * @param ctx the parse tree
	 */
	void exitClassNode(main.pythoncompiler.PythonParser.ClassNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blockNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlockNode(main.pythoncompiler.PythonParser.BlockNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blockNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlockNode(main.pythoncompiler.PythonParser.BlockNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifElseNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfElseNode(main.pythoncompiler.PythonParser.IfElseNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifElseNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfElseNode(main.pythoncompiler.PythonParser.IfElseNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileNode(main.pythoncompiler.PythonParser.WhileNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileNode(main.pythoncompiler.PythonParser.WhileNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#forStmt}.
	 * @param ctx the parse tree
	 */
	void enterForNode(main.pythoncompiler.PythonParser.ForNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#forStmt}.
	 * @param ctx the parse tree
	 */
	void exitForNode(main.pythoncompiler.PythonParser.ForNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code passNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 */
	void enterPassNode(main.pythoncompiler.PythonParser.PassNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code passNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 */
	void exitPassNode(main.pythoncompiler.PythonParser.PassNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code breakNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 */
	void enterBreakNode(main.pythoncompiler.PythonParser.BreakNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code breakNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 */
	void exitBreakNode(main.pythoncompiler.PythonParser.BreakNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code continueNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 */
	void enterContinueNode(main.pythoncompiler.PythonParser.ContinueNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code continueNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 */
	void exitContinueNode(main.pythoncompiler.PythonParser.ContinueNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code returnNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnNode(main.pythoncompiler.PythonParser.ReturnNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code returnNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#returnStmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnNode(main.pythoncompiler.PythonParser.ReturnNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#exprStmt}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStmtNode(main.pythoncompiler.PythonParser.ExpressionStmtNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionStmtNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#exprStmt}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStmtNode(main.pythoncompiler.PythonParser.ExpressionStmtNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionRoot}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpressionRoot(main.pythoncompiler.PythonParser.ExpressionRootContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionRoot}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpressionRoot(main.pythoncompiler.PythonParser.ExpressionRootContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicalExprNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#logicalExpr}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExprNode(main.pythoncompiler.PythonParser.LogicalExprNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicalExprNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#logicalExpr}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExprNode(main.pythoncompiler.PythonParser.LogicalExprNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code comparisonExprNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExprNode(main.pythoncompiler.PythonParser.ComparisonExprNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code comparisonExprNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExprNode(main.pythoncompiler.PythonParser.ComparisonExprNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithmeticExprNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#arithmeticExpr}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticExprNode(main.pythoncompiler.PythonParser.ArithmeticExprNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithmeticExprNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#arithmeticExpr}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticExprNode(main.pythoncompiler.PythonParser.ArithmeticExprNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code termNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTermNode(main.pythoncompiler.PythonParser.TermNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code termNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTermNode(main.pythoncompiler.PythonParser.TermNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpr(main.pythoncompiler.PythonParser.UnaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpr(main.pythoncompiler.PythonParser.UnaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(main.pythoncompiler.PythonParser.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(main.pythoncompiler.PythonParser.NotExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code factorPrimary}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactorPrimary(main.pythoncompiler.PythonParser.FactorPrimaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code factorPrimary}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactorPrimary(main.pythoncompiler.PythonParser.FactorPrimaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterStringExpr(main.pythoncompiler.PythonParser.StringExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitStringExpr(main.pythoncompiler.PythonParser.StringExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code indexExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterIndexExpr(main.pythoncompiler.PythonParser.IndexExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code indexExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitIndexExpr(main.pythoncompiler.PythonParser.IndexExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code trueExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterTrueExpr(main.pythoncompiler.PythonParser.TrueExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code trueExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitTrueExpr(main.pythoncompiler.PythonParser.TrueExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numberExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterNumberExpr(main.pythoncompiler.PythonParser.NumberExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numberExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitNumberExpr(main.pythoncompiler.PythonParser.NumberExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code noneExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterNoneExpr(main.pythoncompiler.PythonParser.NoneExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code noneExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitNoneExpr(main.pythoncompiler.PythonParser.NoneExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code attributeExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterAttributeExpr(main.pythoncompiler.PythonParser.AttributeExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code attributeExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitAttributeExpr(main.pythoncompiler.PythonParser.AttributeExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code falseExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterFalseExpr(main.pythoncompiler.PythonParser.FalseExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code falseExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitFalseExpr(main.pythoncompiler.PythonParser.FalseExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dictExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterDictExpr(main.pythoncompiler.PythonParser.DictExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dictExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitDictExpr(main.pythoncompiler.PythonParser.DictExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterCallExpr(main.pythoncompiler.PythonParser.CallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitCallExpr(main.pythoncompiler.PythonParser.CallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code listExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterListExpr(main.pythoncompiler.PythonParser.ListExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code listExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitListExpr(main.pythoncompiler.PythonParser.ListExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterParenExpr(main.pythoncompiler.PythonParser.ParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitParenExpr(main.pythoncompiler.PythonParser.ParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code identifierExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierExpr(main.pythoncompiler.PythonParser.IdentifierExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code identifierExpr}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierExpr(main.pythoncompiler.PythonParser.IdentifierExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code listLiteralNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#listLiteral}.
	 * @param ctx the parse tree
	 */
	void enterListLiteralNode(main.pythoncompiler.PythonParser.ListLiteralNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code listLiteralNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#listLiteral}.
	 * @param ctx the parse tree
	 */
	void exitListLiteralNode(main.pythoncompiler.PythonParser.ListLiteralNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dictLiteralNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#dictLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDictLiteralNode(main.pythoncompiler.PythonParser.DictLiteralNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dictLiteralNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#dictLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDictLiteralNode(main.pythoncompiler.PythonParser.DictLiteralNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dictEntryNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#dictEntry}.
	 * @param ctx the parse tree
	 */
	void enterDictEntryNode(main.pythoncompiler.PythonParser.DictEntryNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dictEntryNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#dictEntry}.
	 * @param ctx the parse tree
	 */
	void exitDictEntryNode(main.pythoncompiler.PythonParser.DictEntryNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code argListNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#argList}.
	 * @param ctx the parse tree
	 */
	void enterArgListNode(main.pythoncompiler.PythonParser.ArgListNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code argListNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#argList}.
	 * @param ctx the parse tree
	 */
	void exitArgListNode(main.pythoncompiler.PythonParser.ArgListNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code namedArgNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterNamedArgNode(main.pythoncompiler.PythonParser.NamedArgNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code namedArgNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitNamedArgNode(main.pythoncompiler.PythonParser.NamedArgNodeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code positionalArgNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterPositionalArgNode(main.pythoncompiler.PythonParser.PositionalArgNodeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code positionalArgNode}
	 * labeled alternative in {@link main.pythoncompiler.PythonParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitPositionalArgNode(PythonParser.PositionalArgNodeContext ctx);
}