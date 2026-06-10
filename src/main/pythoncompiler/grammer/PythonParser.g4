parser grammar PythonParser;

options { tokenVocab=PythonLexer; }


/*  Program  */

program
    : stmt* EOF                          #programRoot
    ;


/*  Statements */

stmt
    : importStmt                         #importStmtNode
    | routeDef                           #routeDefNode
    | blockStmt                          #blockStmtWrapper
    ;

/*  Block-level Statements (can appear inside a block)  */

blockStmt
    : assignmentStmt                     #assignStmtNode
    | augAssignStmt                      #augAssignStmtNode
    | globalStmt                         #globalStmtNode
    | returnStmt                         #returnStmtNode
    | ifStmt                             #ifStmtNode
    | whileStmt                          #whileStmtNode
    | forStmt                            #forStmtNode
    | tryStmt                            #tryStmtNode      
    | withStmt                           #withStmtWrapper
    | functionDef                        #functionDefNode
    | classDef                           #classDefNode
    | controlStmt                        #controlStmtNode
    | exprStmt                           #exprStmtNode
    | emptyStmt                          #emptyStmtNode
    ;

/* Try/Except */
tryStmt
    : TRY COLON block
      (EXCEPT expression? COLON block)*
      (FINALLY COLON block)?
                                         #tryExceptNode
    ;

/* With */
withStmt
    : WITH expression (AS IDENTIFIER)? COLON block
                                         #withStmtNode
    ;

/*  Empty */

emptyStmt
    : NEWLINE                            #emptyLine
    ;


/*  Import  */

importStmt
    : FROM IDENTIFIER IMPORT IDENTIFIER (COMMA IDENTIFIER)* NEWLINE
                                        #fromImportNode
    ;


/* Assignment  */

assignmentStmt
    : IDENTIFIER ASSIGN expression NEWLINE
                                        #simpleAssignNode
    ;

augAssignStmt
    : IDENTIFIER (PLUS_ASSIGN | MINUS_ASSIGN | MULT_ASSIGN | DIV_ASSIGN)
      expression NEWLINE
                                        #augAssignNode
    ;


/*  Global  */

globalStmt
    : GLOBAL IDENTIFIER NEWLINE          #globalDeclNode
    ;


/* Decorator */


routeDef
    : decorator functionDef              #decoratedFunctionNode
    ;

decorator
    : AT IDENTIFIER (DOT IDENTIFIER)* LPAREN argList? RPAREN NEWLINE
                                        #decoratorNode
    ;


/*  Function  */


functionDef
    : DEF IDENTIFIER LPAREN paramList? RPAREN (ARROW IDENTIFIER)? COLON block
                                        #functionNode
    ;

paramList
    : IDENTIFIER (COMMA IDENTIFIER)*     #paramListNode
    ;


/*  Class  */
classDef
    : CLASS IDENTIFIER COLON block       #classNode
    ;


/*  Block */


block
    : NEWLINE INDENT blockStmt+ DEDENT        #blockNode
    ;


/*  Control  */

ifStmt
    : IF expression COLON block
      (ELIF expression COLON block)*
      (ELSE COLON block)?
                                        #ifElseNode
    ;

whileStmt
    : WHILE expression COLON block       #whileNode
    ;

forStmt
    : FOR IDENTIFIER IN expression COLON block
                                        #forNode
    ;

controlStmt
    : PASS NEWLINE                       #passNode
    | BREAK NEWLINE                      #breakNode
    | CONTINUE NEWLINE                   #continueNode
    ;

/*  Return  */

returnStmt
    : RETURN expression? NEWLINE         #returnNode
    ;


/*  Expression Statement  */

exprStmt
    : expression NEWLINE                 #expressionStmtNode
    ;


/*  Expressions  */

expression
    : logicalExpr                        #expressionRoot
    ;

logicalExpr
    : comparisonExpr ((AND | OR) comparisonExpr)*
                                        #logicalExprNode
    ;

comparisonExpr
    : arithmeticExpr ((EQ | NEQ | LT | GT | LE | GE) arithmeticExpr)*
                                        #comparisonExprNode
    ;

arithmeticExpr
    : term ((PLUS | MINUS) term)*
                                        #arithmeticExprNode
    ;

term
    : factor ((MULT | DIV | MOD) factor)*
                                        #termNode
    ;

factor
    : NOT factor                         #notExpr
    | primary                            #factorPrimary
    ;


/*  Primary  */

primary
    : IDENTIFIER                         #identifierExpr
    | STRING                             #stringExpr
    | NUMBER                             #numberExpr
    | TRUE                               #trueExpr
    | FALSE                              #falseExpr
    | NONE                               #noneExpr
    | listLiteral                        #listExpr
    | dictLiteral                        #dictExpr
    | primary DOT IDENTIFIER             #attributeExpr
    | primary LBRACK expression RBRACK   #indexExpr
    | primary LPAREN argList? RPAREN     #callExpr
    | LPAREN expression RPAREN           #parenExpr
    ;

/*  List  */

listLiteral
    : LBRACK (expression (COMMA expression)*)? RBRACK
                                        #listLiteralNode
    ;


/*  Dict  */

dictLiteral
    : LBRACE (dictEntry (COMMA dictEntry)*)? RBRACE
                                        #dictLiteralNode
    ;

dictEntry
    : expression COLON expression        #dictEntryNode
    ;


/*  Arguments  */

argList
    : argument (COMMA argument)*         #argListNode
    ;

argument
    : IDENTIFIER ASSIGN expression       #namedArgNode
    | expression                        #positionalArgNode
    ;
