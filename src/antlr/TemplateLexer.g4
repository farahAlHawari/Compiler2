lexer grammar TemplateLexer;

//  CHANNELS
channels {
    WHITESPACE_CHANNEL,
    COMMENTS_CHANNEL
}

// FRAGMENT RULES
fragment DIGIT : [0-9];
fragment LETTER : [a-zA-Z];
fragment LETTER_OR_DIGIT : [a-zA-Z0-9_];
fragment HEX_DIGIT : [0-9a-fA-F];
fragment WHITESPACE : [ \t\r\n];
fragment TAG_START_CHAR : [a-zA-Z];
fragment TAG_CHAR : [a-zA-Z0-9_-];


DOCTYPE
    : '<!DOCTYPE' .*? '>'
    ;

/*
 HTML COMMENTS & CDATA
  */
HTML_COMMENT: '<!--' .*? '-->' -> channel(COMMENTS_CHANNEL);
CDATA: '<![CDATA[' .*? ']]>' -> skip;

/* OPENINGS
 */

STYLE_OPEN
    : '<style>' -> pushMode(STYLE)
    ;

JINJA_STMT_OPEN
    : '{%' -> pushMode(JINJA)
    ;

JINJA_EXPR_OPEN
    : '{{' -> pushMode(JINJA_EXPR)
    ;

JINJA_COMMENT
    : '{#' .*? '#}' -> channel(COMMENTS_CHANNEL)
    ;


TAG_OPEN
    : '<' -> pushMode(TAG)
    ;

WS
    : WHITESPACE+ -> channel(WHITESPACE_CHANNEL)
    ;

TEXT
    : ( ~[<{\t\r\n]          // Normal text characters
      | '<' ~[!a-zA-Z/]      // < not followed by tag start
      | '{' ~[{%#]           // { not followed by Jinja start
      )+
    ;



UNEXPECTED_CHAR
    : .
    ;

/*
 TAG MODE
  */
mode TAG;

TAG_NAME
    : TAG_START_CHAR TAG_CHAR*
    ;

SLASH
    : '/'
    ;

EQUAL
    : '='
    ;

DOUBLE_QUOTE_STRING
    : '"' (~["\\] | '\\' .)* '"'
    ;

SINGLE_QUOTE_STRING
    : '\'' (~['\\] | '\\' .)* '\''
    ;

UNQUOTED_VALUE
    : ~[ \t\r\n>"'=</]+
    ;

TAG_CLOSE
    : '>' -> popMode
    ;

TAG_WS
    : WHITESPACE+ -> channel(WHITESPACE_CHANNEL)
    ;


/*
 STYLE MODE
  */
mode STYLE;

STYLE_CLOSE
    : '</style>' -> popMode
    ;

LBRACE  : '{' ;
RBRACE  : '}' ;
COLON   : ':' ;
SEMI    : ';' ;
COMMA   : ',' ;
DOT     : '.' ;
HASH    : '#' ;
STAR    : '*' ;
LPAREN  : '(' ;
RPAREN  : ')' ;
LBRACKET: '[' ;
RBRACKET: ']' ;
DOUBLE_COLON: '::' ;
TILDE_EQUAL: '~=' ;
PIPE_EQUAL: '|=' ;
CARET_EQUAL: '^=' ;
DOLLAR_EQUAL: '$=' ;
STAR_EQUAL: '*=' ;

//  FRAGMENTS FOR STYLE
fragment SIGN : [+-];
fragment FRACTION : '.' DIGIT+;
fragment EXPONENT : [eE] SIGN? DIGIT+;

DECIMAL : DIGIT+ FRACTION EXPONENT?
        | DIGIT* FRACTION EXPONENT?
        | DIGIT+ EXPONENT
        ;

NUMBER  : DIGIT+ ;

UNIT    : 'px' | '%' | 'em' | 'rem' | 'vh' | 'vw' | 'vmin' | 'vmax' | 'deg' | 's' | 'ms'
        | 'cm' | 'mm' | 'in' | 'pt' | 'pc' | 'ch' | 'ex'
        ;  // Removed duplicate vmin, vmax

COLOR_HEX: '#' HEX_DIGIT HEX_DIGIT HEX_DIGIT
             (HEX_DIGIT HEX_DIGIT HEX_DIGIT (HEX_DIGIT HEX_DIGIT)?)?
         ;
STYLE_IDENT : [a-zA-Z_-][a-zA-Z0-9_-]* ;
STYLE_STRING: '"' (~["\\] | '\\' .)* '"' ;


// URL FUNCTION
URL: 'url(' (~[()] | '(' ~[()]* ')')* ')' ;

STYLE_WS    : WHITESPACE+ -> channel(WHITESPACE_CHANNEL) ;
STYLE_OTHER : . ;

/*
JINJA STATEMENT MODE
 */
mode JINJA;

JINJA_STMT_CLOSE
    : '%}' -> popMode
    ;

// Keywords
JINJA_IF: 'if';
JINJA_ELSE: 'else';
JINJA_ELIF: 'elif';
JINJA_ENDIF: 'endif';
JINJA_FOR: 'for';
JINJA_ENDFOR: 'endfor';
JINJA_BLOCK: 'block';
JINJA_ENDBLOCK: 'endblock';
JINJA_MACRO: 'macro';
JINJA_ENDMACRO: 'endmacro';
JINJA_SET: 'set';
JINJA_INCLUDE: 'include';
JINJA_EXTENDS: 'extends';
JINJA_IMPORT: 'import';

// Operators
JINJA_EQ: '==';
JINJA_NE: '!=';
JINJA_GE: '>=';
JINJA_LE: '<=';
JINJA_AND: 'and';
JINJA_OR: 'or';
JINJA_NOT: 'not';
JINJA_IN: 'in';

// Identifiers & values
JINJA_IDENT
    : [a-zA-Z_][a-zA-Z0-9_]*
    ;

JINJA_STRING
    : '"' (~["\\] | '\\' .)* '"'
    | '\'' (~['\\] | '\\' .)* '\''
    ;

JINJA_NUMBER
    : DIGIT+ ('.' DIGIT+)?
    ;

JINJA_OPERATOR
    : '+' | '-' | '*' | '/' | '%'
    ;

JINJA_PUNCT
    : '(' | ')' | '[' | ']' | '{' | '}' | ',' | '.' | ':' | '|'
    ;

JINJA_WS
    : WHITESPACE+ -> channel(WHITESPACE_CHANNEL)
    ;


/*
    JINJA EXPRESSION MODE
  */
mode JINJA_EXPR;

JINJA_EXPR_CLOSE
    : '}}' -> popMode
    ;

// Expression operators
JINJA_EXPR_EQ: '==';
JINJA_EXPR_NE: '!=';
JINJA_EXPR_GT: '>';
JINJA_EXPR_LT: '<';
JINJA_EXPR_GE: '>=';
JINJA_EXPR_LE: '<=';
JINJA_EXPR_AND: 'and';
JINJA_EXPR_OR: 'or';
JINJA_EXPR_NOT: 'not';
JINJA_EXPR_IN: 'in';

//  FRAGMENTS FOR JINJA EXPR
fragment JINJA_EXPR_IDENT_START : [a-zA-Z_];
fragment JINJA_EXPR_IDENT_CHAR : [a-zA-Z0-9_];

JINJA_EXPR_IDENT
    : JINJA_EXPR_IDENT_START JINJA_EXPR_IDENT_CHAR*
    ;

JINJA_EXPR_NUMBER
    : DIGIT+ ('.' DIGIT+)?
    ;

JINJA_EXPR_STRING
    : '"' (~["\\] | '\\' .)* '"'
    | '\'' (~['\\] | '\\' .)* '\''
    ;

JINJA_EXPR_OPERATOR
    : '+' | '-' | '*' | '/' | '%'
    ;

JINJA_EXPR_PUNCT
    : '(' | ')' | '[' | ']' | '{' | '}' | ',' | '.' | ':' | '|'
    ;

JINJA_EXPR_WS
    : WHITESPACE+ -> channel(WHITESPACE_CHANNEL)
    ;
