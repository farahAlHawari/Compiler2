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


//    @Override
//    public ASTNode visitDecoratedFunctionNode(PythonParser.DecoratedFunctionNodeContext ctx) {
//        FunctionDefNode func = (FunctionDefNode) visit(ctx.functionDef());
//
//        if (ctx.decorator() != null) {
//            func.nodeName = "RouteFunction";
//        }
//        return func;
//    }


    @Override
    public ASTNode visitDecoratorNode(PythonParser.DecoratorNodeContext ctx) {
        return null;
    }
    @Override
    public ASTNode visitDecoratedFunctionNode(PythonParser.DecoratedFunctionNodeContext ctx) {

        FunctionDefNode funcNode = (FunctionDefNode) visit(ctx.functionDef());
        funcNode.nodeName = "RouteFunction";

        DecoratorListNode decoratorList = new DecoratorListNode();
        decoratorList.lineNumber = ctx.start.getLine();

        DecoratorNode decorator = new DecoratorNode();
        decorator.lineNumber = ctx.start.getLine();

        ASTNode decoratorExpr = buildDecoratorExpression(
                (PythonParser.DecoratorNodeContext) ctx.decorator()
        );

        decorator.addChild(decoratorExpr);

        decoratorList.addChild(decorator);

        // نضيفها قبل البلوك
        funcNode.children.add(0, decoratorList);

        return funcNode;
    }

    private ASTNode buildDecoratorExpression(PythonParser.DecoratorNodeContext ctx) {

        // 1️⃣ بناء اسم decorator مثل app.route
        StringBuilder nameBuilder = new StringBuilder();

        for (int i = 0; i < ctx.IDENTIFIER().size(); i++) {
            nameBuilder.append(ctx.IDENTIFIER(i).getText());
            if (i < ctx.DOT().size()) {
                nameBuilder.append(".");
            }
        }

        String decoratorName = nameBuilder.toString();

        // 2️⃣ إنشاء CallNode
        CallNode callNode = new CallNode(decoratorName);
        callNode.lineNumber = ctx.start.getLine();

        // 3️⃣ arguments
        if (ctx.argList() != null) {
            ASTNode argsNode = visit(ctx.argList());
            if (argsNode != null) {
                callNode.addChild(argsNode);
            }
        }

        return callNode;
    }


    @Override
    public ASTNode visitFunctionNode(PythonParser.FunctionNodeContext ctx) {
        String funcName = ctx.IDENTIFIER(0).getText();
        FunctionDefNode funcNode = new FunctionDefNode(funcName);
        funcNode.lineNumber = ctx.start.getLine();

        // Visit paramList and count parameters from the built AST
        if (ctx.paramList() != null) {
            ASTNode paramsNode = visit(ctx.paramList());
            funcNode.paramCount = paramsNode.children.size();
            funcNode.addChild(paramsNode);
        }

        // Extract return type hint from grammar (ARROW IDENTIFIER)?
        if (ctx.ARROW() != null && ctx.IDENTIFIER().size() > 1) {
            funcNode.returnType = ctx.IDENTIFIER(1).getText();
        }

        if (ctx.block() != null) {
            funcNode.addChild(visit(ctx.block()));
        }

        return funcNode;
    }

    @Override
    public ASTNode visitParamListNode(PythonParser.ParamListNodeContext ctx) {
        final int line = ctx.start.getLine();
        ASTNode paramsNode = new ASTNode("Parameters",line) {
            @Override
            public String getDetails() {
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


    final int ifLine = ctx.start.getLine();
    ASTNode ifBranch = new ASTNode("IfBranch", ifLine) {
        @Override
        public String getDetails() { return ""; }
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

            final int elifLine = ctx.ELIF(i).getSymbol().getLine();
            ASTNode elifBranch = new ASTNode("ElifBranch", elifLine) {
                @Override
                public String getDetails() { return ""; }
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
        return new ASTNode("PassStmt", ctx.start.getLine()) {
            @Override
            public String getDetails() {
                return "";
            }
        };
    }

    @Override
    public ASTNode visitBreakNode(PythonParser.BreakNodeContext ctx) {
        return new ASTNode("BreakStmt", ctx.start.getLine()) {
            @Override
            public String getDetails() { return ""; }
        };
    }

    @Override
    public ASTNode visitContinueNode(PythonParser.ContinueNodeContext ctx) {
        return new ASTNode("ContinueStmt", ctx.start.getLine()) {
            @Override
            public String getDetails() { return ""; }
        };
    }




    @Override
    public ASTNode visitSimpleAssignNode(PythonParser.SimpleAssignNodeContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        String op = ctx.ASSIGN().getText();

        AssignNode assignNode = new AssignNode(varName, op);
        assignNode.lineNumber = ctx.start.getLine();

        // ← التعديل: تحقق إذا في type hint بالسطر الأصلي (x: int = value)
        // بما إن الـ grammar ما بيدعمها، نقرأ النص الأصلي للسطر ونستخرج النوع منه
        String originalText = ctx.start.getInputStream()
                .getText(new org.antlr.v4.runtime.misc.Interval(
                        ctx.start.getStartIndex(),
                        ctx.stop.getStopIndex()));
        if (originalText.contains(":")) {
            // الشكل: varName : type = value
            String beforeAssign = originalText.substring(0, originalText.indexOf("=")).trim();
            if (beforeAssign.contains(":")) {
                String declaredType = beforeAssign.substring(
                        beforeAssign.indexOf(":") + 1).trim();
                if (!declaredType.isEmpty()) {
                    assignNode.declaredType = declaredType;
                }
            }
        }

        ASTNode value = visit(ctx.expression());
        if (value != null) {
            assignNode.addChild(value);
        }

        return assignNode;
    }
    @Override
    public ASTNode visitAnnotatedAssignNode(PythonParser.AnnotatedAssignNodeContext ctx) {
        String varName = ctx.IDENTIFIER(0).getText();
        String declaredType = ctx.IDENTIFIER(1).getText();
        String op = ctx.ASSIGN().getText();

        AssignNode assignNode = new AssignNode(varName, op);
        assignNode.lineNumber = ctx.start.getLine();
        assignNode.declaredType = declaredType;

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
        final String globalVarName = varName;
        return new ASTNode("GlobalDecl", ctx.start.getLine()) {
            @Override
            public String getDetails() { return " (" + globalVarName + ")"; }
        };
    }

//    @Override
//    public ASTNode visitFromImportNode(PythonParser.FromImportNodeContext ctx) {
//        final String moduleName = ctx.IDENTIFIER(0).getText();
//        return new ASTNode("ImportStmt", ctx.start.getLine()) {
//            @Override
//            public String getDetails() { return " (from " + moduleName + ")"; }
//        };
//    }

    @Override
    public ASTNode visitFromImportNode(PythonParser.FromImportNodeContext ctx) {
        final String moduleName = ctx.IDENTIFIER(0).getText();
        final int line = ctx.start.getLine();

        ASTNode importNode = new ASTNode("ImportStmt", line) {
            @Override
            public String getDetails() {
                return " (from " + moduleName + ")";   // ← بس اسم الموديول
            }
        };

        for (int i = 1; i < ctx.IDENTIFIER().size(); i++) {
            String importedName = ctx.IDENTIFIER(i).getText();
            IdentifierNode nameNode = new IdentifierNode(importedName);
            nameNode.lineNumber = ctx.IDENTIFIER(i).getSymbol().getLine();
            importNode.addChild(nameNode);           // ← الأسماء كأطفال
        }

        return importNode;
    }

    @Override
    public ASTNode visitExpressionStmtNode(PythonParser.ExpressionStmtNodeContext ctx) {
        ASTNode expr = visit(ctx.expression());
        if (expr == null) {
            return new ASTNode("UnsupportedExpr", ctx.start.getLine()) {
                @Override
                public String getDetails() { return ""; }
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
            ASTNode argsNode = visit(ctx.argList());
            callNode.argCount = argsNode.children.size();
            callNode.addChild(argsNode);
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
        ASTNode entryNode = new ASTNode("DictEntry", ctx.start.getLine()) {
            @Override
            public String getDetails() { return ""; }
        };
        entryNode.lineNumber = ctx.start.getLine();

        entryNode.addChild(visit(ctx.expression(0)));
        entryNode.addChild(visit(ctx.expression(1)));

        return entryNode;
    }



    @Override
    public ASTNode visitArgListNode(PythonParser.ArgListNodeContext ctx) {
        ASTNode argsNode = new ASTNode("Arguments", ctx.start.getLine()) {
            @Override
            public String getDetails() { return ""; }
        };

        for (PythonParser.ArgumentContext argCtx : ctx.argument()) {
            argsNode.addChild(visit(argCtx));
        }

        return argsNode;
    }

    @Override
    public ASTNode visitPositionalArgNode(PythonParser.PositionalArgNodeContext ctx) {
        ASTNode argNode = new ASTNode("PositionalArg", ctx.start.getLine()) {
            @Override
            public String getDetails() { return ""; }
        };
        argNode.addChild(visit(ctx.expression()));
        return argNode;
    }

    @Override
    public ASTNode visitNamedArgNode(PythonParser.NamedArgNodeContext ctx) {
        String paramName = ctx.IDENTIFIER().getText();
        final String namedArgName = paramName;
        ASTNode argNode = new ASTNode("NamedArg", ctx.start.getLine()) {
            @Override
            public String getDetails() { return " (" + namedArgName + "=...)"; }
        };
        argNode.addChild(visit(ctx.expression()));
        return argNode;
    }


    @Override
    public ASTNode visitFactorPrimary(PythonParser.FactorPrimaryContext ctx) {
        return visit(ctx.primary());
    }

    @Override
    public ASTNode visitEmptyLine(PythonParser.EmptyLineContext ctx) {
        return null;
    }

    @Override
    public ASTNode visitTryExceptNode(PythonParser.TryExceptNodeContext ctx) {
        ASTNode tryNode = new ASTNode("TryExcept") {
            @Override
            public String getDetails() { return ""; }
        };
        tryNode.lineNumber = ctx.start.getLine();
        // ctx.block() بترجع List — كل الـ blocks (try + except + finally)
        for (PythonParser.BlockContext block : ctx.block()) {
            tryNode.addChild(visit(block));
        }
        return tryNode;
    }

    @Override
    public ASTNode visitWithStmtNode(PythonParser.WithStmtNodeContext ctx) {
        // ctx.IDENTIFIER() بيكون null لو ما في "as varName"
        String asName = (ctx.AS() != null && ctx.IDENTIFIER() != null)
                ? ctx.IDENTIFIER().getText()
                : "";
        ASTNode withNode = new ASTNode("WithStmt") {
            @Override
            public String getDetails() {
                return asName.isEmpty() ? "" : " (as " + asName + ")";
            }
        };
        withNode.lineNumber = ctx.start.getLine();
        withNode.addChild(visit(ctx.expression()));
        withNode.addChild(visit(ctx.block()));
        return withNode;
    }

}

