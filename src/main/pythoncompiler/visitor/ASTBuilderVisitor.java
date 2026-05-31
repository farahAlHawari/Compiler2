package main.pythoncompiler.visitor;

import main.pythoncompiler.ast.*;
import main.pythoncompiler.grammer.PythonParser;
import main.pythoncompiler.grammer.PythonParserBaseVisitor;

public class ASTBuilderVisitor extends PythonParserBaseVisitor<ASTNode> {


    @Override
    public ASTNode visitProgramRoot(PythonParser.ProgramRootContext ctx) {
        ProgramNode program = new ProgramNode();
        program.lineNumber = ctx.start.getLine();

        if (ctx.stmt() != null) {
            for (var stmtCtx : ctx.stmt()) {
                ASTNode child = visit(stmtCtx);
                if (child != null) {
                    program.addChild(child);
                }
            }
        }
        return program;
    }

    @Override
    public ASTNode visitBlockStmtWrapper(PythonParser.BlockStmtWrapperContext ctx) {
        return visit(ctx.blockStmt());
    }

    @Override
    public ASTNode visitBlockNode(PythonParser.BlockNodeContext ctx) {
        BlockNode block = new BlockNode();
        block.lineNumber = ctx.start.getLine();

        if (ctx.blockStmt() != null) {
            for (var stmtCtx : ctx.blockStmt()) {
                ASTNode child = visit(stmtCtx);
                if (child != null) {
                    block.addChild(child);
                }
            }

        }
        return block;
    }


    @Override
    public ASTNode visitDecoratedFunctionNode(PythonParser.DecoratedFunctionNodeContext ctx) {
        FunctionDefNode func = (FunctionDefNode) visit(ctx.functionDef());

        if (ctx.decorator() != null) {
            func.nodeName = "RouteFunction";
        }
        return func;
    }



    @Override
    public ASTNode visitFunctionNode(PythonParser.FunctionNodeContext ctx) {
        String funcName = ctx.IDENTIFIER().getText();
        FunctionDefNode funcNode = new FunctionDefNode(funcName);
        funcNode.lineNumber = ctx.start.getLine();

        if (ctx.paramList() != null) {
            funcNode.addChild(visit(ctx.paramList()));
        }

        if (ctx.block() != null) {
            funcNode.addChild(visit(ctx.block()));
        }

        return funcNode;
    }

    @Override
    public ASTNode visitParamListNode(PythonParser.ParamListNodeContext ctx) {
        ASTNode paramsNode = new ASTNode("Parameters") {
            @Override
            protected String getDetails() {
                return "";
            }
        };
        paramsNode.lineNumber = ctx.start.getLine();

        for (var id : ctx.IDENTIFIER()) {
            IdentifierNode param = new IdentifierNode(id.getText());
            param.lineNumber = id.getSymbol().getLine();
            paramsNode.addChild(param);
        }

        return paramsNode;
    }

    @Override
    public ASTNode visitClassNode(PythonParser.ClassNodeContext ctx) {
        String className = ctx.IDENTIFIER().getText();
        ClassDefNode classNode = new ClassDefNode(className);
        classNode.lineNumber = ctx.start.getLine();

        classNode.addChild(visit(ctx.block()));

        return classNode;
    }


@Override
public ASTNode visitIfElseNode(PythonParser.IfElseNodeContext ctx) {

    IfNode ifNode = new IfNode();
    ifNode.lineNumber = ctx.start.getLine();

    int exprIndex = 0;
    int blockIndex = 0;


    ASTNode ifBranch = new ASTNode("IfBranch") {
        @Override
        protected String getDetails() {
            return "";
        }
    };
    ifBranch.lineNumber = ctx.start.getLine();

    if (ctx.expression(exprIndex) != null) {
        ifBranch.addChild(visit(ctx.expression(exprIndex)));
    }

    if (ctx.block(blockIndex) != null) {
        ifBranch.addChild(visit(ctx.block(blockIndex)));
    }

    ifNode.addChild(ifBranch);

    exprIndex++;
    blockIndex++;


    if (ctx.ELIF() != null && !ctx.ELIF().isEmpty()) {

        for (int i = 0; i < ctx.ELIF().size(); i++) {

            ASTNode elifBranch = new ASTNode("ElifBranch") {
                @Override
                protected String getDetails() {
                    return "";
                }
            };

            elifBranch.lineNumber = ctx.ELIF(i).getSymbol().getLine();

            if (ctx.expression(exprIndex) != null) {
                elifBranch.addChild(visit(ctx.expression(exprIndex)));
            }

            if (ctx.block(blockIndex) != null) {
                elifBranch.addChild(visit(ctx.block(blockIndex)));
            }

            ifNode.addChild(elifBranch);

            exprIndex++;
            blockIndex++;
        }
    }


    if (ctx.ELSE() != null) {

        ElseNode elseNode = new ElseNode();
        elseNode.lineNumber = ctx.ELSE().getSymbol().getLine();

        if (blockIndex < ctx.block().size() && ctx.block(blockIndex) != null) {
            elseNode.addChild(visit(ctx.block(blockIndex)));
        }

        ifNode.addChild(elseNode);
    }

    return ifNode;
}


    @Override
    public ASTNode visitWhileNode(PythonParser.WhileNodeContext ctx) {
        WhileNode whileNode = new WhileNode();
        whileNode.lineNumber = ctx.start.getLine();

        if (ctx.expression() != null) {
            ASTNode cond = visit(ctx.expression());
            if (cond != null) {
                whileNode.addChild(cond);
            }
        }

        if (ctx.block() != null) {
            ASTNode body = visit(ctx.block());
            if (body != null) {
                whileNode.addChild(body);
            }
        }



        return whileNode;
    }

    @Override
    public ASTNode visitForNode(PythonParser.ForNodeContext ctx) {
        ForNode forNode = new ForNode();
        forNode.lineNumber = ctx.start.getLine();
        forNode.iteratorName = ctx.IDENTIFIER().getText();

        if (ctx.expression() != null) {
            ASTNode iterable = visit(ctx.expression());
            if (iterable != null) {
                forNode.addChild(iterable);
            }
        }


        if (ctx.block() != null) {
            ASTNode body = visit(ctx.block());
            if (body != null) {
                forNode.addChild(body);
            }
        }



        return forNode;
    }

    @Override
    public ASTNode visitReturnNode(PythonParser.ReturnNodeContext ctx) {
        ReturnNode returnNode = new ReturnNode();
        returnNode.lineNumber = ctx.start.getLine();

        if (ctx.expression() != null) {
            returnNode.addChild(visit(ctx.expression()));
        }

        return returnNode;
    }

    @Override
    public ASTNode visitPassNode(PythonParser.PassNodeContext ctx) {
        ASTNode passNode = new ASTNode("PassStmt") {
            @Override
            protected String getDetails() {
                return "";
            }
        };
        passNode.lineNumber = ctx.start.getLine();
        return passNode;
    }

    @Override
    public ASTNode visitBreakNode(PythonParser.BreakNodeContext ctx) {
        ASTNode breakNode = new ASTNode("BreakStmt") {
            @Override
            protected String getDetails() {
                return "";
            }
        };
        breakNode.lineNumber = ctx.start.getLine();
        return breakNode;
    }

    @Override
    public ASTNode visitContinueNode(PythonParser.ContinueNodeContext ctx) {
        ASTNode continueNode = new ASTNode("ContinueStmt") {
            @Override
            protected String getDetails() {
                return "";
            }
        };
        continueNode.lineNumber = ctx.start.getLine();
        return continueNode;
    }




    @Override
    public ASTNode visitSimpleAssignNode(PythonParser.SimpleAssignNodeContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        String op = ctx.ASSIGN().getText();

        AssignNode assignNode = new AssignNode(varName, op);
        assignNode.lineNumber = ctx.start.getLine();

        ASTNode value = visit(ctx.expression());
        if (value != null) {
            assignNode.addChild(value);
        }

        return assignNode;
    }

    @Override
    public ASTNode visitAugAssignNode(PythonParser.AugAssignNodeContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        String op = ctx.getChild(1).getText();

        AssignNode assignNode = new AssignNode(varName, op);
        assignNode.lineNumber = ctx.start.getLine();

        assignNode.addChild(visit(ctx.expression()));

        return assignNode;
    }

    @Override
    public ASTNode visitGlobalDeclNode(PythonParser.GlobalDeclNodeContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        ASTNode globalNode = new ASTNode("GlobalDecl") {
            @Override
            protected String getDetails() {
                return " (" + varName + ")";
            }
        };
        globalNode.lineNumber = ctx.start.getLine();
        return globalNode;
    }

    @Override
    public ASTNode visitFromImportNode(PythonParser.FromImportNodeContext ctx) {
        ASTNode importNode = new ASTNode("ImportStmt") {
            @Override
            protected String getDetails() {
                return " (from " + ctx.IDENTIFIER(0).getText() + ")";
            }
        };
        importNode.lineNumber = ctx.start.getLine();
        return importNode;
    }

    @Override
    public ASTNode visitExpressionStmtNode(PythonParser.ExpressionStmtNodeContext ctx) {
        ASTNode expr = visit(ctx.expression());
        if (expr == null) {
            return new ASTNode("UnsupportedExpr") {
                @Override
                protected String getDetails() {
                    return "";
                }
            };
        }
        return expr;

    }




    @Override
    public ASTNode visitLogicalExprNode(PythonParser.LogicalExprNodeContext ctx) {
        ASTNode result = visit(ctx.comparisonExpr(0));

        for (int i = 1; i < ctx.getChildCount(); i += 2) {
            String op = ctx.getChild(i).getText();
            ASTNode right = visit(ctx.getChild(i + 1));

            BinaryOpNode opNode = new BinaryOpNode(op);
            opNode.lineNumber = ctx.start.getLine();
            opNode.addChild(result);
            opNode.addChild(right);

            result = opNode;
        }
        return result;
    }

    @Override
    public ASTNode visitComparisonExprNode(PythonParser.ComparisonExprNodeContext ctx) {
        ASTNode result = visit(ctx.arithmeticExpr(0));

        for (int i = 1; i < ctx.getChildCount(); i += 2) {
            String op = ctx.getChild(i).getText();
            ASTNode right = visit(ctx.arithmeticExpr((i + 1) / 2));

            BinaryOpNode opNode = new BinaryOpNode(op);
            opNode.lineNumber = ctx.start.getLine();
            opNode.addChild(result);
            opNode.addChild(right);

            result = opNode;
        }
        return result;
    }

    @Override
    public ASTNode visitArithmeticExprNode(PythonParser.ArithmeticExprNodeContext ctx) {
        ASTNode result = visit(ctx.term(0));

        for (int i = 1; i < ctx.getChildCount(); i += 2) {
            String op = ctx.getChild(i).getText();
            ASTNode right = visit(ctx.getChild(i + 1));

            BinaryOpNode opNode = new BinaryOpNode(op);
            opNode.lineNumber = ctx.start.getLine();
            opNode.addChild(result);
            opNode.addChild(right);

            result = opNode;
        }
        return result;
    }

    @Override
    public ASTNode visitTermNode(PythonParser.TermNodeContext ctx) {
        ASTNode result = visit(ctx.factor(0));

        for (int i = 1; i < ctx.getChildCount(); i += 2) {
            String op = ctx.getChild(i).getText();
            ASTNode right = visit(ctx.factor((i + 1) / 2));

            BinaryOpNode opNode = new BinaryOpNode(op);
            opNode.lineNumber = ctx.start.getLine();
            opNode.addChild(result);
            opNode.addChild(right);

            result = opNode;
        }
        return result;
    }

    @Override
    public ASTNode visitNotExpr(PythonParser.NotExprContext ctx) {
        UnaryOpNode notNode = new UnaryOpNode("not");
        notNode.lineNumber = ctx.start.getLine();
        notNode.addChild(visit(ctx.factor()));
        return notNode;
    }




    @Override
    public ASTNode visitIdentifierExpr(PythonParser.IdentifierExprContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        IdentifierNode idNode = new IdentifierNode(name);
        idNode.lineNumber = ctx.start.getLine();
        return idNode;
    }


    @Override
    public ASTNode visitCallExpr(PythonParser.CallExprContext ctx) {
        ASTNode primaryNode = visit(ctx.primary());
        String funcName = "UnknownCall";

        if (primaryNode instanceof IdentifierNode) {
            funcName = ((IdentifierNode) primaryNode).name;
        } else if (primaryNode instanceof AttributeNode) {
            funcName = ((AttributeNode) primaryNode).attributeName;
        }

        CallNode callNode = new CallNode(funcName);
        callNode.lineNumber = ctx.start.getLine();

        if (primaryNode instanceof AttributeNode) {
            callNode.addChild(primaryNode);
        }

        if (ctx.argList() != null) {
            callNode.addChild(visit(ctx.argList()));
        }

        return callNode;
    }

    @Override
    public ASTNode visitAttributeExpr(PythonParser.AttributeExprContext ctx) {
        String attrName = ctx.IDENTIFIER().getText();

        AttributeNode attrNode = new AttributeNode(attrName);
        attrNode.lineNumber = ctx.start.getLine();

        attrNode.addChild(visit(ctx.primary()));

        return attrNode;
    }

    @Override
    public ASTNode visitIndexExpr(PythonParser.IndexExprContext ctx) {
        IndexNode indexNode = new IndexNode();
        indexNode.lineNumber = ctx.start.getLine();

        indexNode.addChild(visit(ctx.primary()));
        indexNode.addChild(visit(ctx.expression()));

        return indexNode;
    }

    @Override
    public ASTNode visitParenExpr(PythonParser.ParenExprContext ctx) {
        return visit(ctx.expression());
    }




    @Override
    public ASTNode visitNumberExpr(PythonParser.NumberExprContext ctx) {
        String val = ctx.NUMBER().getText();
        LiteralNode lit = new LiteralNode(val, "INT");
        lit.lineNumber = ctx.start.getLine();
        return lit;
    }

    @Override
    public ASTNode visitStringExpr(PythonParser.StringExprContext ctx) {
        String val = ctx.STRING().getText();
        LiteralNode lit = new LiteralNode(val, "STRING");
        lit.lineNumber = ctx.start.getLine();
        return lit;
    }

    @Override
    public ASTNode visitTrueExpr(PythonParser.TrueExprContext ctx) {
        LiteralNode lit = new LiteralNode("True", "BOOL");
        lit.lineNumber = ctx.start.getLine();
        return lit;
    }

    @Override
    public ASTNode visitFalseExpr(PythonParser.FalseExprContext ctx) {
        LiteralNode lit = new LiteralNode("False", "BOOL");
        lit.lineNumber = ctx.start.getLine();
        return lit;
    }

    @Override
    public ASTNode visitNoneExpr(PythonParser.NoneExprContext ctx) {
        LiteralNode lit = new LiteralNode("None", "NONE");
        lit.lineNumber = ctx.start.getLine();
        return lit;
    }




    @Override
    public ASTNode visitListLiteralNode(PythonParser.ListLiteralNodeContext ctx) {
        ListNode listNode = new ListNode();
        listNode.lineNumber = ctx.start.getLine();

        if (ctx.expression() != null) {
            for (PythonParser.ExpressionContext exprCtx : ctx.expression()) {
                listNode.addChild(visit(exprCtx));
            }
        }

        return listNode;
    }

    @Override
    public ASTNode visitDictLiteralNode(PythonParser.DictLiteralNodeContext ctx) {
        DictNode dictNode = new DictNode();
        dictNode.lineNumber = ctx.start.getLine();

        if (ctx.dictEntry() != null) {
            for (PythonParser.DictEntryContext entryCtx : ctx.dictEntry()) {
                dictNode.addChild(visit(entryCtx));
            }
        }

        return dictNode;
    }

    @Override
    public ASTNode visitDictEntryNode(PythonParser.DictEntryNodeContext ctx) {
        ASTNode entryNode = new ASTNode("DictEntry") {
            @Override
            protected String getDetails() {
                return "";
            }
        };
        entryNode.lineNumber = ctx.start.getLine();

        entryNode.addChild(visit(ctx.expression(0)));
        entryNode.addChild(visit(ctx.expression(1)));

        return entryNode;
    }



    @Override
    public ASTNode visitArgListNode(PythonParser.ArgListNodeContext ctx) {
        ASTNode argsNode = new ASTNode("Arguments") {
            @Override
            protected String getDetails() {
                return "";
            }
        };
        argsNode.lineNumber = ctx.start.getLine();

        for (PythonParser.ArgumentContext argCtx : ctx.argument()) {
            argsNode.addChild(visit(argCtx));
        }

        return argsNode;
    }

    @Override
    public ASTNode visitPositionalArgNode(PythonParser.PositionalArgNodeContext ctx) {
        ASTNode argNode = new ASTNode("PositionalArg") {
            @Override
            protected String getDetails() {
                return "";
            }
        };
        argNode.lineNumber = ctx.start.getLine();
        argNode.addChild(visit(ctx.expression()));
        return argNode;
    }

    @Override
    public ASTNode visitNamedArgNode(PythonParser.NamedArgNodeContext ctx) {
        String paramName = ctx.IDENTIFIER().getText();
        ASTNode argNode = new ASTNode("NamedArg") {
            @Override
            protected String getDetails() {
                return " (" + paramName + "=...)";
            }
        };
        argNode.lineNumber = ctx.start.getLine();
        argNode.addChild(visit(ctx.expression()));
        return argNode;
    }


    @Override public ASTNode visitFactorPrimary(PythonParser.FactorPrimaryContext ctx) {
        return visit(ctx.primary());
    }

    @Override
    public ASTNode visitEmptyLine(PythonParser.EmptyLineContext ctx) {
        return null;
    }

}

