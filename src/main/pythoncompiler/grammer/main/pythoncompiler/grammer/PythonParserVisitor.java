// Generated from C:/Users/maria/IdeaProjects/Compiler2/src/main/pythoncompiler/grammer/PythonParser.g4 by ANTLR 4.13.2
package main.pythoncompiler.grammer;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PythonParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PythonParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code programRoot}
	 * labeled alternative in {@link PythonParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgramRoot(PythonParser.ProgramRootContext ctx);
	/**
	 * Visit a parse tree produced by the {@code importStmtNode}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportStmtNode(PythonParser.ImportStmtNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code routeDefNode}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRouteDefNode(PythonParser.RouteDefNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code blockStmtWrapper}
	 * labeled alternative in {@link PythonParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStmtWrapper(PythonParser.BlockStmtWrapperContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStmtNode(PythonParser.AssignStmtNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code augAssignStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAugAssignStmtNode(PythonParser.AugAssignStmtNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalStmtNode(PythonParser.GlobalStmtNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code returnStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStmtNode(PythonParser.ReturnStmtNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmtNode(PythonParser.IfStmtNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whileStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmtNode(PythonParser.WhileStmtNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStmtNode(PythonParser.ForStmtNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tryStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryStmtNode(PythonParser.TryStmtNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code withStmtWrapper}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithStmtWrapper(PythonParser.WithStmtWrapperContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionDefNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDefNode(PythonParser.FunctionDefNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code classDefNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDefNode(PythonParser.ClassDefNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code controlStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlStmtNode(PythonParser.ControlStmtNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprStmtNode(PythonParser.ExprStmtNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyStmtNode}
	 * labeled alternative in {@link PythonParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStmtNode(PythonParser.EmptyStmtNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tryExceptNode}
	 * labeled alternative in {@link PythonParser#tryStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryExceptNode(PythonParser.TryExceptNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code withStmtNode}
	 * labeled alternative in {@link PythonParser#withStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithStmtNode(PythonParser.WithStmtNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptyLine}
	 * labeled alternative in {@link PythonParser#emptyStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyLine(PythonParser.EmptyLineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fromImportNode}
	 * labeled alternative in {@link PythonParser#importStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromImportNode(PythonParser.FromImportNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleAssignNode}
	 * labeled alternative in {@link PythonParser#assignmentStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleAssignNode(PythonParser.SimpleAssignNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code augAssignNode}
	 * labeled alternative in {@link PythonParser#augAssignStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAugAssignNode(PythonParser.AugAssignNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globalDeclNode}
	 * labeled alternative in {@link PythonParser#globalStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalDeclNode(PythonParser.GlobalDeclNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decoratedFunctionNode}
	 * labeled alternative in {@link PythonParser#routeDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecoratedFunctionNode(PythonParser.DecoratedFunctionNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decoratorNode}
	 * labeled alternative in {@link PythonParser#decorator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecoratorNode(PythonParser.DecoratorNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionNode}
	 * labeled alternative in {@link PythonParser#functionDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionNode(PythonParser.FunctionNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code paramListNode}
	 * labeled alternative in {@link PythonParser#paramList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamListNode(PythonParser.ParamListNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code classNode}
	 * labeled alternative in {@link PythonParser#classDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassNode(PythonParser.ClassNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code blockNode}
	 * labeled alternative in {@link PythonParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockNode(PythonParser.BlockNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifElseNode}
	 * labeled alternative in {@link PythonParser#ifStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElseNode(PythonParser.IfElseNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whileNode}
	 * labeled alternative in {@link PythonParser#whileStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileNode(PythonParser.WhileNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forNode}
	 * labeled alternative in {@link PythonParser#forStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForNode(PythonParser.ForNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code passNode}
	 * labeled alternative in {@link PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPassNode(PythonParser.PassNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code breakNode}
	 * labeled alternative in {@link PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakNode(PythonParser.BreakNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code continueNode}
	 * labeled alternative in {@link PythonParser#controlStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueNode(PythonParser.ContinueNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code returnNode}
	 * labeled alternative in {@link PythonParser#returnStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnNode(PythonParser.ReturnNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionStmtNode}
	 * labeled alternative in {@link PythonParser#exprStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStmtNode(PythonParser.ExpressionStmtNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionRoot}
	 * labeled alternative in {@link PythonParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionRoot(PythonParser.ExpressionRootContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicalExprNode}
	 * labeled alternative in {@link PythonParser#logicalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalExprNode(PythonParser.LogicalExprNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code comparisonExprNode}
	 * labeled alternative in {@link PythonParser#comparisonExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExprNode(PythonParser.ComparisonExprNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arithmeticExprNode}
	 * labeled alternative in {@link PythonParser#arithmeticExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticExprNode(PythonParser.ArithmeticExprNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code termNode}
	 * labeled alternative in {@link PythonParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermNode(PythonParser.TermNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link PythonParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(PythonParser.NotExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code factorPrimary}
	 * labeled alternative in {@link PythonParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactorPrimary(PythonParser.FactorPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringExpr(PythonParser.StringExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code indexExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexExpr(PythonParser.IndexExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code trueExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueExpr(PythonParser.TrueExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberExpr(PythonParser.NumberExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code noneExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoneExpr(PythonParser.NoneExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code attributeExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeExpr(PythonParser.AttributeExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code falseExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseExpr(PythonParser.FalseExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dictExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictExpr(PythonParser.DictExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallExpr(PythonParser.CallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code listExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListExpr(PythonParser.ListExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(PythonParser.ParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierExpr}
	 * labeled alternative in {@link PythonParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierExpr(PythonParser.IdentifierExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code listLiteralNode}
	 * labeled alternative in {@link PythonParser#listLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListLiteralNode(PythonParser.ListLiteralNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dictLiteralNode}
	 * labeled alternative in {@link PythonParser#dictLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictLiteralNode(PythonParser.DictLiteralNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dictEntryNode}
	 * labeled alternative in {@link PythonParser#dictEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictEntryNode(PythonParser.DictEntryNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code argListNode}
	 * labeled alternative in {@link PythonParser#argList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgListNode(PythonParser.ArgListNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code namedArgNode}
	 * labeled alternative in {@link PythonParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedArgNode(PythonParser.NamedArgNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code positionalArgNode}
	 * labeled alternative in {@link PythonParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositionalArgNode(PythonParser.PositionalArgNodeContext ctx);
}