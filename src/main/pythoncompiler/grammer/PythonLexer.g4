lexer grammar PythonLexer;

tokens { INDENT, DEDENT }

@header {
import java.util.Stack;
import java.util.Deque;
import java.util.ArrayDeque;
import org.antlr.v4.runtime.*;
}

@members {
    private Stack<Integer> indents = new Stack<>();
    private int opened = 0;
    private Deque<Token> pendingTokens = new ArrayDeque<>();

    private Token commonToken(int type, String text) {
        int stop = getCharIndex() - 1;
        int start = text.isEmpty() ? stop : stop - text.length() + 1;
        return new CommonToken(_tokenFactorySourcePair, type,
                DEFAULT_TOKEN_CHANNEL, start, stop);
    }

    @Override
    public Token nextToken() {
        // Return pending tokens first (INDENT/DEDENT from previous NEWLINE)
        if (!pendingTokens.isEmpty()) {
            return pendingTokens.poll();
        }

        Token next = super.nextToken();

        // At EOF, emit pending DEDENT tokens for remaining indentation levels
        if (next.getType() == EOF && !indents.isEmpty()) {
            pendingTokens.offer(commonToken(NEWLINE, "\n"));
            while (!indents.isEmpty()) {
                pendingTokens.offer(commonToken(DEDENT, ""));
                indents.pop();
            }
            pendingTokens.offer(next); // EOF at the end
            return pendingTokens.poll();
        }

        return next;
    }
}


/* ========= Keywords ========= */

FROM        : 'from';
IMPORT      : 'import';
DEF         : 'def';
RETURN      : 'return';
IF          : 'if';
ELIF        : 'elif';
ELSE        : 'else';
FOR         : 'for';
WHILE       : 'while';
IN          : 'in';
GLOBAL      : 'global';
CLASS       : 'class';
PASS        : 'pass';
BREAK       : 'break';
CONTINUE    : 'continue';
WITH        : 'with';
TRY         : 'try';
EXCEPT      : 'except';
FINALLY     : 'finally';

TRUE        : 'True';
FALSE       : 'False';
NONE        : 'None';


/*  Operators */

ASSIGN      : '=';
PLUS_ASSIGN : '+=';
MINUS_ASSIGN: '-=';
MULT_ASSIGN : '*=';
DIV_ASSIGN  : '/=';

EQ          : '==';
NEQ         : '!=';
LT          : '<';
GT          : '>';
LE          : '<=';
GE          : '>=';

PLUS        : '+';
MINUS       : '-';
MULT        : '*';
DIV         : '/';
MOD         : '%';

AND         : 'and';
OR          : 'or';
NOT         : 'not';


/*  Delimiters  */

LPAREN  : '(' {opened++;};
RPAREN  : ')' {opened--;};
LBRACK  : '[' {opened++;};
RBRACK  : ']' {opened--;};
LBRACE  : '{' {opened++;};
RBRACE  : '}' {opened--;};
COLON   : ':';
COMMA   : ',';
DOT     : '.';
AT      : '@';


/*  Literals  */

STRING
    : '"' (~["\r\n])* '"'
    | '\'' (~['\r\n])* '\''
    ;

NUMBER
    : [0-9]+
    ;


/* Identifier  */

IDENTIFIER
    : [a-zA-Z_][a-zA-Z_0-9]*
    ;


/*  Newlines & Indentation  */

NEWLINE
    : ('\r'? '\n')+
      {
        String spaces = "";
        int next = _input.LA(1);
        while (next == ' ') {
            spaces += " ";
            _input.consume();
            next = _input.LA(1);
        }

        if (opened > 0) {
            skip();
        } else {
                      int indent = spaces.length();
                      int prev = indents.isEmpty() ? 0 : indents.peek();

                      // Add INDENT/DEDENT tokens to pending queue (not emit, which overwrites _token)
                      // The NEWLINE token itself comes from this rule's match naturally
                      if (indent > prev) {
                          indents.push(indent);
                          pendingTokens.offer(commonToken(INDENT, ""));
                      } else {
                          while (!indents.isEmpty() && indents.peek() > indent) {
                              indents.pop();
                              pendingTokens.offer(commonToken(DEDENT, ""));
                          }
                      }
                  }
                }
              ;


/*  Skip  */

WS      : [ \t]+ -> skip ;
COMMENT : '#' ~[\r\n]* -> skip ;
