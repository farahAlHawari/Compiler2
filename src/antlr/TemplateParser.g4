parser grammar TemplateParser;

options { tokenVocab=TemplateLexer; }
@members {
    // HTML5 Void Elements (These cannot have content and should not be parsed as containers)
    private boolean isVoidTag(String tagName) {
        return tagName.equals("area") || tagName.equals("base") || tagName.equals("br")
            || tagName.equals("col") || tagName.equals("embed") || tagName.equals("hr")
            || tagName.equals("img") || tagName.equals("input") || tagName.equals("link")
            || tagName.equals("meta") || tagName.equals("param") || tagName.equals("source")
            || tagName.equals("track") || tagName.equals("wbr");
    }
}


/*  PAGE  */
page
    : doctype? content* EOF
    ;

 /*  GENERAL CONTENT  */
content
    : node
    ;

 /*
    NODES - FIXED ORDER for priority
   */
node
    : jinjaBlock
    | jinjaExpr
    | styleBlock
    | htmlElement
    | selfClosingTag
    | textContent
    ;

 /* TEXT CONTENT */
 textContent
     : (TEXT | WS)+ #textContentNode
     ;

 /*   HTML DOCUMENT */
 htmlDocument
     : doctype? htmlElement
     ;

 /*
    DOCTYPE
  */
 doctype
     : DOCTYPE #doctypeNode
     ;

 /*
         HTML Parsing Rules
 */

       htmlElement
           // The predicate { !isVoidTag(...) }? prevents void tags like <input> from entering this rule
           : { !isVoidTag(_input.LT(2).getText()) }?
             openingTag content* closingTag #pairedHtmlElement
           ;
      // If there is no closing tag, this rule fails, and 'selfClosingTag' will handle it.

 openingTag
     : TAG_OPEN TAG_NAME attribute* TAG_CLOSE
       #openingTagNode
     ;

 closingTag
     : TAG_OPEN SLASH TAG_NAME TAG_CLOSE
       #closingTagNode
     ;

 selfClosingTag
     : TAG_OPEN tagName=TAG_NAME attrs=attribute* (SLASH TAG_CLOSE | TAG_CLOSE)
       #selfClosingElement
     ;

  attribute
      : attrName=TAG_NAME (EQUAL attrValue=attributeValue)? #htmlAttribute
      ;

 attributeValue
     : DOUBLE_QUOTE_STRING #doubleQuotedAttribute
     | SINGLE_QUOTE_STRING #singleQuotedAttribute
     | UNQUOTED_VALUE #unquotedAttribute
     ;


 /*
  STYLE BLOCK Wich is the CSS Parsing Rules
  */

styleBlock
    : STYLE_OPEN rules=styleRule* STYLE_CLOSE
      #styleBlockNode
    ;


styleRule
    : selectorList LBRACE declarationList RBRACE
      #styleRuleNode
    ;


 // SELECTOR LIST
 selectorList
     : selector (COMMA STYLE_WS* selector)*
     ;

selector
    : selectorPart (STYLE_WS* selectorPart)*
    ;

 selectorPart
     : STAR #universalSelector
     | HASH STYLE_IDENT #idSelector
     | DOT STYLE_IDENT #classSelector
     | STYLE_IDENT #typeSelector
     | COLON STYLE_IDENT #pseudoClassSelector
     | DOUBLE_COLON STYLE_IDENT #pseudoElementSelector
     | LBRACKET attributeSelector RBRACKET #attributeSelectorPart
     | COLON STYLE_IDENT LPAREN (NUMBER | STYLE_IDENT) RPAREN #pseudoClassWithArgSelector
     ;

 attributeSelector
     : STYLE_IDENT #simpleAttributeSelector
     | STYLE_IDENT
     op=(EQUAL | TILDE_EQUAL | PIPE_EQUAL | CARET_EQUAL | DOLLAR_EQUAL | STAR_EQUAL) value=STYLE_STRING #comparisonAttributeSelector
     ;

 //  DECLARATIONS
declarationList
    : declaration*
      #declarationListNode
    ;

declaration
    : property=STYLE_IDENT COLON val=declarationValue SEMI
      #declarationNode
    ;


 // DECLARATION VALUE
declarationValue
    : valPart=styleValuePart (STYLE_WS* valPart=styleValuePart)*
      #declarationValueNode
    ;


 styleValuePart
     : STYLE_STRING #stringStyleValue
     | numVal=numericValueType unit=UNIT? #numericStyleValue
     | STYLE_IDENT #identifierStyleValue
     | COLOR_HEX #colorStyleValue
     | functionCall #functionStyleValue
     | URL #urlStyleValue
     | HASH #hashStyleValue
     | COMMA #commaStyleValue
     | LPAREN #lparenStyleValue
     | RPAREN #rparenStyleValue
     | LBRACKET #lbracketStyleValue
     | RBRACKET #rbracketStyleValue
     | DOT #dotStyleValue
     | STAR #starStyleValue
     | COLON #colonStyleValue
     | STYLE_OTHER #otherStyleValue
     ;

 numericValueType
     : NUMBER #integerNumType
     | DECIMAL #decimalNumType
     ;

functionCall
    : STYLE_IDENT LPAREN valueList? RPAREN
      #functionCallNode
    ;


valueList
    : styleValuePart (COMMA styleValuePart)*
      #valueListNode
    ;


 /*
        JINJA Parsing Rules
  */

  jinjaExpr
      : JINJA_EXPR_OPEN body=jinjaExpressionBody JINJA_EXPR_CLOSE
        #jinjaExpression
      ;

  jinjaExpressionBody
      : (JINJA_EXPR_IDENT
        | JINJA_EXPR_NUMBER
        | JINJA_EXPR_STRING
        | JINJA_EXPR_OPERATOR
        | JINJA_EXPR_PUNCT
        | JINJA_EXPR_WS
        | JINJA_EXPR_EQ
        | JINJA_EXPR_NE
        | JINJA_EXPR_GT
        | JINJA_EXPR_LT
        | JINJA_EXPR_GE
        | JINJA_EXPR_LE
        | JINJA_EXPR_AND
        | JINJA_EXPR_OR
        | JINJA_EXPR_NOT
        | JINJA_EXPR_IN
        )*
        #jinjaExpressionBodyNode
      ;


  jinjaBlock
      :  jinjaContainer #containerJinjaBlock
      |  jinjaSimple #simpleJinjaBlock
      ;

  // Simple Jinja statements (no body)
jinjaSimple
    : JINJA_STMT_OPEN body=jinjaStatementBody JINJA_STMT_CLOSE
      #jinjaSimpleNode
    ;

jinjaSimpleBody
    : (
        // ONLY these keywords are allowed in 'simple' statements
        JINJA_EXTENDS
      | JINJA_INCLUDE
      | JINJA_IMPORT
      | JINJA_SET

      // Plus standard values and operators
      | JINJA_IDENT
      | JINJA_STRING
      | JINJA_NUMBER
      | JINJA_OPERATOR
      | JINJA_PUNCT
      | JINJA_WS
      | JINJA_EQ
      | JINJA_NE
      | JINJA_GE
      | JINJA_LE
      | JINJA_AND
      | JINJA_OR
      | JINJA_NOT
      | JINJA_IN
      )+
      #jinjaSimpleBodyNode
    ;


  // Container Jinja statements (with body)
 jinjaContainer
     : JINJA_STMT_OPEN start=jinjaContainerStart JINJA_STMT_CLOSE
       content*
       JINJA_STMT_OPEN end=jinjaContainerEnd JINJA_STMT_CLOSE
       #jinjaContainerNode
     ;
// This rule parses the content of any {% ... %} block
jinjaStatementBody
    : ( jinjaKeyword       // Matches 'if', 'for', 'in', 'extends', etc.
      | JINJA_IDENT        // Matches variable names like 'p', 'products', 'name'
      | JINJA_STRING       // Matches "base.html"
      | JINJA_NUMBER       // Matches 123
      | JINJA_OPERATOR     // Matches +, -, *, /
      | JINJA_PUNCT        // Matches (, ), [, ], etc.
      | JINJA_WS           // Matches whitespace
      )*
      #jinjaStatementBodyNode
    ;
 jinjaContainerStart
       : JINJA_FOR body=jinjaStatementBody #forStatementStart
       | JINJA_IF body=jinjaStatementBody #ifStatementStart
       | JINJA_BLOCK name=JINJA_IDENT #blockStatementStart
       | JINJA_MACRO name=JINJA_IDENT #macroStatementStart
       //  handling else/elif
       | JINJA_ELSE body=jinjaStatementBody? #elseStatementStart
       | JINJA_ELIF body=jinjaStatementBody #elifStatementStart
       ;

 jinjaContainerEnd
       : JINJA_ENDIF #ifStatementEnd
       | JINJA_ENDFOR #forStatementEnd
       | JINJA_ENDBLOCK #blockStatementEnd
       | JINJA_ENDMACRO #macroStatementEnd
       ;

 jinjaKeyword
     : JINJA_IF       #jinjaKeywordIf
     | JINJA_ELSE     #jinjaKeywordElse
     | JINJA_ELIF     #jinjaKeywordElif
     | JINJA_ENDIF    #jinjaKeywordEndIf
     | JINJA_FOR      #jinjaKeywordFor
     | JINJA_ENDFOR   #jinjaKeywordEndFor
     | JINJA_BLOCK    #jinjaKeywordBlock
     | JINJA_ENDBLOCK #jinjaKeywordEndBlock
     | JINJA_MACRO    #jinjaKeywordMacro
     | JINJA_ENDMACRO #jinjaKeywordEndMacro
     | JINJA_SET      #jinjaKeywordSet
     | JINJA_INCLUDE  #jinjaKeywordInclude
     | JINJA_EXTENDS  #jinjaKeywordExtends
     | JINJA_IMPORT   #jinjaKeywordImport
     | JINJA_EQ       #jinjaKeywordEq
     | JINJA_NE       #jinjaKeywordNe
     | JINJA_GE       #jinjaKeywordGe
     | JINJA_LE       #jinjaKeywordLe
     | JINJA_AND      #jinjaKeywordAnd
     | JINJA_OR       #jinjaKeywordOr
     | JINJA_NOT      #jinjaKeywordNot
     | JINJA_IN       #jinjaKeywordIn
     ;
