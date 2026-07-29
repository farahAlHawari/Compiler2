// Generated from C:/Users/HP/Downloads/Compiler2/src/antlr/TemplateParser.g4 by ANTLR 4.13.2
package antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class TemplateParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		DOCTYPE=1, HTML_COMMENT=2, CDATA=3, STYLE_OPEN=4, JINJA_STMT_OPEN=5, JINJA_EXPR_OPEN=6, 
		JINJA_COMMENT=7, TAG_OPEN=8, WS=9, TEXT=10, UNEXPECTED_CHAR=11, TAG_NAME=12, 
		SLASH=13, EQUAL=14, DOUBLE_QUOTE_STRING=15, SINGLE_QUOTE_STRING=16, UNQUOTED_VALUE=17, 
		TAG_CLOSE=18, TAG_WS=19, STYLE_CLOSE=20, LBRACE=21, RBRACE=22, COLON=23, 
		SEMI=24, COMMA=25, DOT=26, HASH=27, STAR=28, LPAREN=29, RPAREN=30, LBRACKET=31, 
		RBRACKET=32, DOUBLE_COLON=33, TILDE_EQUAL=34, PIPE_EQUAL=35, CARET_EQUAL=36, 
		DOLLAR_EQUAL=37, STAR_EQUAL=38, DECIMAL=39, NUMBER=40, UNIT=41, COLOR_HEX=42, 
		STYLE_IDENT=43, STYLE_STRING=44, URL=45, STYLE_WS=46, STYLE_OTHER=47, 
		JINJA_STMT_CLOSE=48, JINJA_IF=49, JINJA_ELSE=50, JINJA_ELIF=51, JINJA_ENDIF=52, 
		JINJA_FOR=53, JINJA_ENDFOR=54, JINJA_BLOCK=55, JINJA_ENDBLOCK=56, JINJA_MACRO=57, 
		JINJA_ENDMACRO=58, JINJA_SET=59, JINJA_INCLUDE=60, JINJA_EXTENDS=61, JINJA_IMPORT=62, 
		JINJA_EQ=63, JINJA_NE=64, JINJA_GE=65, JINJA_LE=66, JINJA_AND=67, JINJA_OR=68, 
		JINJA_NOT=69, JINJA_IN=70, JINJA_IDENT=71, JINJA_STRING=72, JINJA_NUMBER=73, 
		JINJA_OPERATOR=74, JINJA_PUNCT=75, JINJA_WS=76, JINJA_EXPR_CLOSE=77, JINJA_EXPR_EQ=78, 
		JINJA_EXPR_NE=79, JINJA_EXPR_GT=80, JINJA_EXPR_LT=81, JINJA_EXPR_GE=82, 
		JINJA_EXPR_LE=83, JINJA_EXPR_AND=84, JINJA_EXPR_OR=85, JINJA_EXPR_NOT=86, 
		JINJA_EXPR_IN=87, JINJA_EXPR_IDENT=88, JINJA_EXPR_NUMBER=89, JINJA_EXPR_STRING=90, 
		JINJA_EXPR_OPERATOR=91, JINJA_EXPR_PUNCT=92, JINJA_EXPR_WS=93;
	public static final int
		RULE_page = 0, RULE_content = 1, RULE_node = 2, RULE_textContent = 3, 
		RULE_htmlDocument = 4, RULE_doctype = 5, RULE_htmlElement = 6, RULE_openingTag = 7, 
		RULE_closingTag = 8, RULE_selfClosingTag = 9, RULE_attribute = 10, RULE_attributeValue = 11, 
		RULE_styleBlock = 12, RULE_styleRule = 13, RULE_selectorList = 14, RULE_selector = 15, 
		RULE_selectorPart = 16, RULE_attributeSelector = 17, RULE_declarationList = 18, 
		RULE_declaration = 19, RULE_declarationValue = 20, RULE_styleValuePart = 21, 
		RULE_numericValueType = 22, RULE_functionCall = 23, RULE_valueList = 24, 
		RULE_jinjaExpr = 25, RULE_jinjaExpressionBody = 26, RULE_jinjaBlock = 27, 
		RULE_jinjaSimple = 28, RULE_jinjaSimpleBody = 29, RULE_jinjaContainer = 30, 
		RULE_jinjaStatementBody = 31, RULE_jinjaContainerStart = 32, RULE_jinjaContainerEnd = 33, 
		RULE_jinjaKeyword = 34;
	private static String[] makeRuleNames() {
		return new String[] {
			"page", "content", "node", "textContent", "htmlDocument", "doctype", 
			"htmlElement", "openingTag", "closingTag", "selfClosingTag", "attribute", 
			"attributeValue", "styleBlock", "styleRule", "selectorList", "selector", 
			"selectorPart", "attributeSelector", "declarationList", "declaration", 
			"declarationValue", "styleValuePart", "numericValueType", "functionCall", 
			"valueList", "jinjaExpr", "jinjaExpressionBody", "jinjaBlock", "jinjaSimple", 
			"jinjaSimpleBody", "jinjaContainer", "jinjaStatementBody", "jinjaContainerStart", 
			"jinjaContainerEnd", "jinjaKeyword"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, "'<style>'", "'{%'", "'{{'", null, null, null, 
			null, null, null, "'/'", "'='", null, null, null, null, null, "'</style>'", 
			"'{'", "'}'", "':'", "';'", "','", "'.'", "'#'", "'*'", "'('", "')'", 
			"'['", "']'", "'::'", "'~='", "'|='", "'^='", "'$='", "'*='", null, null, 
			null, null, null, null, null, null, null, "'%}'", "'if'", "'else'", "'elif'", 
			"'endif'", "'for'", "'endfor'", "'block'", "'endblock'", "'macro'", "'endmacro'", 
			"'set'", "'include'", "'extends'", "'import'", null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, "'}}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "DOCTYPE", "HTML_COMMENT", "CDATA", "STYLE_OPEN", "JINJA_STMT_OPEN", 
			"JINJA_EXPR_OPEN", "JINJA_COMMENT", "TAG_OPEN", "WS", "TEXT", "UNEXPECTED_CHAR", 
			"TAG_NAME", "SLASH", "EQUAL", "DOUBLE_QUOTE_STRING", "SINGLE_QUOTE_STRING", 
			"UNQUOTED_VALUE", "TAG_CLOSE", "TAG_WS", "STYLE_CLOSE", "LBRACE", "RBRACE", 
			"COLON", "SEMI", "COMMA", "DOT", "HASH", "STAR", "LPAREN", "RPAREN", 
			"LBRACKET", "RBRACKET", "DOUBLE_COLON", "TILDE_EQUAL", "PIPE_EQUAL", 
			"CARET_EQUAL", "DOLLAR_EQUAL", "STAR_EQUAL", "DECIMAL", "NUMBER", "UNIT", 
			"COLOR_HEX", "STYLE_IDENT", "STYLE_STRING", "URL", "STYLE_WS", "STYLE_OTHER", 
			"JINJA_STMT_CLOSE", "JINJA_IF", "JINJA_ELSE", "JINJA_ELIF", "JINJA_ENDIF", 
			"JINJA_FOR", "JINJA_ENDFOR", "JINJA_BLOCK", "JINJA_ENDBLOCK", "JINJA_MACRO", 
			"JINJA_ENDMACRO", "JINJA_SET", "JINJA_INCLUDE", "JINJA_EXTENDS", "JINJA_IMPORT", 
			"JINJA_EQ", "JINJA_NE", "JINJA_GE", "JINJA_LE", "JINJA_AND", "JINJA_OR", 
			"JINJA_NOT", "JINJA_IN", "JINJA_IDENT", "JINJA_STRING", "JINJA_NUMBER", 
			"JINJA_OPERATOR", "JINJA_PUNCT", "JINJA_WS", "JINJA_EXPR_CLOSE", "JINJA_EXPR_EQ", 
			"JINJA_EXPR_NE", "JINJA_EXPR_GT", "JINJA_EXPR_LT", "JINJA_EXPR_GE", "JINJA_EXPR_LE", 
			"JINJA_EXPR_AND", "JINJA_EXPR_OR", "JINJA_EXPR_NOT", "JINJA_EXPR_IN", 
			"JINJA_EXPR_IDENT", "JINJA_EXPR_NUMBER", "JINJA_EXPR_STRING", "JINJA_EXPR_OPERATOR", 
			"JINJA_EXPR_PUNCT", "JINJA_EXPR_WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "TemplateParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


	    // HTML5 Void Elements (These cannot have content and should not be parsed as containers)
	    private boolean isVoidTag(String tagName) {
	        return tagName.equals("area") || tagName.equals("base") || tagName.equals("br")
	            || tagName.equals("col") || tagName.equals("embed") || tagName.equals("hr")
	            || tagName.equals("img") || tagName.equals("input") || tagName.equals("link")
	            || tagName.equals("meta") || tagName.equals("param") || tagName.equals("source")
	            || tagName.equals("track") || tagName.equals("wbr");
	    }

	public TemplateParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PageContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(TemplateParser.EOF, 0); }
		public DoctypeContext doctype() {
			return getRuleContext(DoctypeContext.class,0);
		}
		public List<ContentContext> content() {
			return getRuleContexts(ContentContext.class);
		}
		public ContentContext content(int i) {
			return getRuleContext(ContentContext.class,i);
		}
		public PageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_page; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterPage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitPage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitPage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PageContext page() throws RecognitionException {
		PageContext _localctx = new PageContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_page);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(70);
				doctype();
				}
				break;
			}
			setState(76);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(73);
					content();
					}
					} 
				}
				setState(78);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			setState(79);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContentContext extends ParserRuleContext {
		public NodeContext node() {
			return getRuleContext(NodeContext.class,0);
		}
		public ContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_content; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContentContext content() throws RecognitionException {
		ContentContext _localctx = new ContentContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_content);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			node();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NodeContext extends ParserRuleContext {
		public JinjaBlockContext jinjaBlock() {
			return getRuleContext(JinjaBlockContext.class,0);
		}
		public JinjaExprContext jinjaExpr() {
			return getRuleContext(JinjaExprContext.class,0);
		}
		public StyleBlockContext styleBlock() {
			return getRuleContext(StyleBlockContext.class,0);
		}
		public HtmlElementContext htmlElement() {
			return getRuleContext(HtmlElementContext.class,0);
		}
		public SelfClosingTagContext selfClosingTag() {
			return getRuleContext(SelfClosingTagContext.class,0);
		}
		public TextContentContext textContent() {
			return getRuleContext(TextContentContext.class,0);
		}
		public NodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_node; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NodeContext node() throws RecognitionException {
		NodeContext _localctx = new NodeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_node);
		try {
			setState(89);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(83);
				jinjaBlock();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(84);
				jinjaExpr();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(85);
				styleBlock();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(86);
				htmlElement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(87);
				selfClosingTag();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(88);
				textContent();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TextContentContext extends ParserRuleContext {
		public TextContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_textContent; }
	 
		public TextContentContext() { }
		public void copyFrom(TextContentContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TextContentNodeContext extends TextContentContext {
		public List<TerminalNode> TEXT() { return getTokens(TemplateParser.TEXT); }
		public TerminalNode TEXT(int i) {
			return getToken(TemplateParser.TEXT, i);
		}
		public List<TerminalNode> WS() { return getTokens(TemplateParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(TemplateParser.WS, i);
		}
		public TextContentNodeContext(TextContentContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterTextContentNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitTextContentNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitTextContentNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TextContentContext textContent() throws RecognitionException {
		TextContentContext _localctx = new TextContentContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_textContent);
		int _la;
		try {
			int _alt;
			_localctx = new TextContentNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(92); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(91);
					_la = _input.LA(1);
					if ( !(_la==WS || _la==TEXT) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(94); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HtmlDocumentContext extends ParserRuleContext {
		public HtmlElementContext htmlElement() {
			return getRuleContext(HtmlElementContext.class,0);
		}
		public DoctypeContext doctype() {
			return getRuleContext(DoctypeContext.class,0);
		}
		public HtmlDocumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlDocument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterHtmlDocument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitHtmlDocument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitHtmlDocument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HtmlDocumentContext htmlDocument() throws RecognitionException {
		HtmlDocumentContext _localctx = new HtmlDocumentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_htmlDocument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(96);
				doctype();
				}
				break;
			}
			setState(99);
			htmlElement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DoctypeContext extends ParserRuleContext {
		public DoctypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doctype; }
	 
		public DoctypeContext() { }
		public void copyFrom(DoctypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DoctypeNodeContext extends DoctypeContext {
		public TerminalNode DOCTYPE() { return getToken(TemplateParser.DOCTYPE, 0); }
		public DoctypeNodeContext(DoctypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterDoctypeNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitDoctypeNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitDoctypeNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DoctypeContext doctype() throws RecognitionException {
		DoctypeContext _localctx = new DoctypeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_doctype);
		try {
			_localctx = new DoctypeNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			match(DOCTYPE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class HtmlElementContext extends ParserRuleContext {
		public HtmlElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlElement; }
	 
		public HtmlElementContext() { }
		public void copyFrom(HtmlElementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PairedHtmlElementContext extends HtmlElementContext {
		public OpeningTagContext openingTag() {
			return getRuleContext(OpeningTagContext.class,0);
		}
		public ClosingTagContext closingTag() {
			return getRuleContext(ClosingTagContext.class,0);
		}
		public List<ContentContext> content() {
			return getRuleContexts(ContentContext.class);
		}
		public ContentContext content(int i) {
			return getRuleContext(ContentContext.class,i);
		}
		public PairedHtmlElementContext(HtmlElementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterPairedHtmlElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitPairedHtmlElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitPairedHtmlElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HtmlElementContext htmlElement() throws RecognitionException {
		HtmlElementContext _localctx = new HtmlElementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_htmlElement);
		try {
			int _alt;
			_localctx = new PairedHtmlElementContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			if (!( !isVoidTag(_input.LT(2).getText()) )) throw new FailedPredicateException(this, " !isVoidTag(_input.LT(2).getText()) ");
			setState(104);
			openingTag();
			setState(108);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(105);
					content();
					}
					} 
				}
				setState(110);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			setState(111);
			closingTag();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OpeningTagContext extends ParserRuleContext {
		public OpeningTagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_openingTag; }
	 
		public OpeningTagContext() { }
		public void copyFrom(OpeningTagContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OpeningTagNodeContext extends OpeningTagContext {
		public TerminalNode TAG_OPEN() { return getToken(TemplateParser.TAG_OPEN, 0); }
		public TerminalNode TAG_NAME() { return getToken(TemplateParser.TAG_NAME, 0); }
		public TerminalNode TAG_CLOSE() { return getToken(TemplateParser.TAG_CLOSE, 0); }
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public OpeningTagNodeContext(OpeningTagContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterOpeningTagNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitOpeningTagNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitOpeningTagNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OpeningTagContext openingTag() throws RecognitionException {
		OpeningTagContext _localctx = new OpeningTagContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_openingTag);
		int _la;
		try {
			_localctx = new OpeningTagNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			match(TAG_OPEN);
			setState(114);
			match(TAG_NAME);
			setState(118);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TAG_NAME) {
				{
				{
				setState(115);
				attribute();
				}
				}
				setState(120);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(121);
			match(TAG_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClosingTagContext extends ParserRuleContext {
		public ClosingTagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_closingTag; }
	 
		public ClosingTagContext() { }
		public void copyFrom(ClosingTagContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ClosingTagNodeContext extends ClosingTagContext {
		public TerminalNode TAG_OPEN() { return getToken(TemplateParser.TAG_OPEN, 0); }
		public TerminalNode SLASH() { return getToken(TemplateParser.SLASH, 0); }
		public TerminalNode TAG_NAME() { return getToken(TemplateParser.TAG_NAME, 0); }
		public TerminalNode TAG_CLOSE() { return getToken(TemplateParser.TAG_CLOSE, 0); }
		public ClosingTagNodeContext(ClosingTagContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterClosingTagNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitClosingTagNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitClosingTagNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClosingTagContext closingTag() throws RecognitionException {
		ClosingTagContext _localctx = new ClosingTagContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_closingTag);
		try {
			_localctx = new ClosingTagNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			match(TAG_OPEN);
			setState(124);
			match(SLASH);
			setState(125);
			match(TAG_NAME);
			setState(126);
			match(TAG_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelfClosingTagContext extends ParserRuleContext {
		public SelfClosingTagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selfClosingTag; }
	 
		public SelfClosingTagContext() { }
		public void copyFrom(SelfClosingTagContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SelfClosingElementContext extends SelfClosingTagContext {
		public Token tagName;
		public AttributeContext attrs;
		public TerminalNode TAG_OPEN() { return getToken(TemplateParser.TAG_OPEN, 0); }
		public TerminalNode TAG_NAME() { return getToken(TemplateParser.TAG_NAME, 0); }
		public TerminalNode SLASH() { return getToken(TemplateParser.SLASH, 0); }
		public TerminalNode TAG_CLOSE() { return getToken(TemplateParser.TAG_CLOSE, 0); }
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public SelfClosingElementContext(SelfClosingTagContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterSelfClosingElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitSelfClosingElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitSelfClosingElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelfClosingTagContext selfClosingTag() throws RecognitionException {
		SelfClosingTagContext _localctx = new SelfClosingTagContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_selfClosingTag);
		int _la;
		try {
			_localctx = new SelfClosingElementContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(128);
			match(TAG_OPEN);
			setState(129);
			((SelfClosingElementContext)_localctx).tagName = match(TAG_NAME);
			setState(133);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TAG_NAME) {
				{
				{
				setState(130);
				((SelfClosingElementContext)_localctx).attrs = attribute();
				}
				}
				setState(135);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(139);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SLASH:
				{
				setState(136);
				match(SLASH);
				setState(137);
				match(TAG_CLOSE);
				}
				break;
			case TAG_CLOSE:
				{
				setState(138);
				match(TAG_CLOSE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeContext extends ParserRuleContext {
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
	 
		public AttributeContext() { }
		public void copyFrom(AttributeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class HtmlAttributeContext extends AttributeContext {
		public Token attrName;
		public AttributeValueContext attrValue;
		public TerminalNode TAG_NAME() { return getToken(TemplateParser.TAG_NAME, 0); }
		public TerminalNode EQUAL() { return getToken(TemplateParser.EQUAL, 0); }
		public AttributeValueContext attributeValue() {
			return getRuleContext(AttributeValueContext.class,0);
		}
		public HtmlAttributeContext(AttributeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterHtmlAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitHtmlAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitHtmlAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_attribute);
		int _la;
		try {
			_localctx = new HtmlAttributeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			((HtmlAttributeContext)_localctx).attrName = match(TAG_NAME);
			setState(144);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EQUAL) {
				{
				setState(142);
				match(EQUAL);
				setState(143);
				((HtmlAttributeContext)_localctx).attrValue = attributeValue();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeValueContext extends ParserRuleContext {
		public AttributeValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeValue; }
	 
		public AttributeValueContext() { }
		public void copyFrom(AttributeValueContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnquotedAttributeContext extends AttributeValueContext {
		public TerminalNode UNQUOTED_VALUE() { return getToken(TemplateParser.UNQUOTED_VALUE, 0); }
		public UnquotedAttributeContext(AttributeValueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterUnquotedAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitUnquotedAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitUnquotedAttribute(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DoubleQuotedAttributeContext extends AttributeValueContext {
		public TerminalNode DOUBLE_QUOTE_STRING() { return getToken(TemplateParser.DOUBLE_QUOTE_STRING, 0); }
		public DoubleQuotedAttributeContext(AttributeValueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterDoubleQuotedAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitDoubleQuotedAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitDoubleQuotedAttribute(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SingleQuotedAttributeContext extends AttributeValueContext {
		public TerminalNode SINGLE_QUOTE_STRING() { return getToken(TemplateParser.SINGLE_QUOTE_STRING, 0); }
		public SingleQuotedAttributeContext(AttributeValueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterSingleQuotedAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitSingleQuotedAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitSingleQuotedAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeValueContext attributeValue() throws RecognitionException {
		AttributeValueContext _localctx = new AttributeValueContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_attributeValue);
		try {
			setState(149);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DOUBLE_QUOTE_STRING:
				_localctx = new DoubleQuotedAttributeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(146);
				match(DOUBLE_QUOTE_STRING);
				}
				break;
			case SINGLE_QUOTE_STRING:
				_localctx = new SingleQuotedAttributeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(147);
				match(SINGLE_QUOTE_STRING);
				}
				break;
			case UNQUOTED_VALUE:
				_localctx = new UnquotedAttributeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(148);
				match(UNQUOTED_VALUE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StyleBlockContext extends ParserRuleContext {
		public StyleBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_styleBlock; }
	 
		public StyleBlockContext() { }
		public void copyFrom(StyleBlockContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StyleBlockNodeContext extends StyleBlockContext {
		public StyleRuleContext rules;
		public TerminalNode STYLE_OPEN() { return getToken(TemplateParser.STYLE_OPEN, 0); }
		public TerminalNode STYLE_CLOSE() { return getToken(TemplateParser.STYLE_CLOSE, 0); }
		public List<StyleRuleContext> styleRule() {
			return getRuleContexts(StyleRuleContext.class);
		}
		public StyleRuleContext styleRule(int i) {
			return getRuleContext(StyleRuleContext.class,i);
		}
		public StyleBlockNodeContext(StyleBlockContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterStyleBlockNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitStyleBlockNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitStyleBlockNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StyleBlockContext styleBlock() throws RecognitionException {
		StyleBlockContext _localctx = new StyleBlockContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_styleBlock);
		int _la;
		try {
			_localctx = new StyleBlockNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			match(STYLE_OPEN);
			setState(155);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8807308591104L) != 0)) {
				{
				{
				setState(152);
				((StyleBlockNodeContext)_localctx).rules = styleRule();
				}
				}
				setState(157);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(158);
			match(STYLE_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StyleRuleContext extends ParserRuleContext {
		public StyleRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_styleRule; }
	 
		public StyleRuleContext() { }
		public void copyFrom(StyleRuleContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StyleRuleNodeContext extends StyleRuleContext {
		public SelectorListContext selectorList() {
			return getRuleContext(SelectorListContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(TemplateParser.LBRACE, 0); }
		public DeclarationListContext declarationList() {
			return getRuleContext(DeclarationListContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(TemplateParser.RBRACE, 0); }
		public StyleRuleNodeContext(StyleRuleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterStyleRuleNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitStyleRuleNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitStyleRuleNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StyleRuleContext styleRule() throws RecognitionException {
		StyleRuleContext _localctx = new StyleRuleContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_styleRule);
		try {
			_localctx = new StyleRuleNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			selectorList();
			setState(161);
			match(LBRACE);
			setState(162);
			declarationList();
			setState(163);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectorListContext extends ParserRuleContext {
		public List<SelectorContext> selector() {
			return getRuleContexts(SelectorContext.class);
		}
		public SelectorContext selector(int i) {
			return getRuleContext(SelectorContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TemplateParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TemplateParser.COMMA, i);
		}
		public List<TerminalNode> STYLE_WS() { return getTokens(TemplateParser.STYLE_WS); }
		public TerminalNode STYLE_WS(int i) {
			return getToken(TemplateParser.STYLE_WS, i);
		}
		public SelectorListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectorList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterSelectorList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitSelectorList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitSelectorList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectorListContext selectorList() throws RecognitionException {
		SelectorListContext _localctx = new SelectorListContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_selectorList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(165);
			selector();
			setState(176);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(166);
				match(COMMA);
				setState(170);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STYLE_WS) {
					{
					{
					setState(167);
					match(STYLE_WS);
					}
					}
					setState(172);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(173);
				selector();
				}
				}
				setState(178);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectorContext extends ParserRuleContext {
		public List<SelectorPartContext> selectorPart() {
			return getRuleContexts(SelectorPartContext.class);
		}
		public SelectorPartContext selectorPart(int i) {
			return getRuleContext(SelectorPartContext.class,i);
		}
		public List<TerminalNode> STYLE_WS() { return getTokens(TemplateParser.STYLE_WS); }
		public TerminalNode STYLE_WS(int i) {
			return getToken(TemplateParser.STYLE_WS, i);
		}
		public SelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectorContext selector() throws RecognitionException {
		SelectorContext _localctx = new SelectorContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_selector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			selectorPart();
			setState(189);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 79176052768768L) != 0)) {
				{
				{
				setState(183);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STYLE_WS) {
					{
					{
					setState(180);
					match(STYLE_WS);
					}
					}
					setState(185);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(186);
				selectorPart();
				}
				}
				setState(191);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SelectorPartContext extends ParserRuleContext {
		public SelectorPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectorPart; }
	 
		public SelectorPartContext() { }
		public void copyFrom(SelectorPartContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TypeSelectorContext extends SelectorPartContext {
		public TerminalNode STYLE_IDENT() { return getToken(TemplateParser.STYLE_IDENT, 0); }
		public TypeSelectorContext(SelectorPartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterTypeSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitTypeSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitTypeSelector(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IdSelectorContext extends SelectorPartContext {
		public TerminalNode HASH() { return getToken(TemplateParser.HASH, 0); }
		public TerminalNode STYLE_IDENT() { return getToken(TemplateParser.STYLE_IDENT, 0); }
		public IdSelectorContext(SelectorPartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterIdSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitIdSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitIdSelector(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AttributeSelectorPartContext extends SelectorPartContext {
		public TerminalNode LBRACKET() { return getToken(TemplateParser.LBRACKET, 0); }
		public AttributeSelectorContext attributeSelector() {
			return getRuleContext(AttributeSelectorContext.class,0);
		}
		public TerminalNode RBRACKET() { return getToken(TemplateParser.RBRACKET, 0); }
		public AttributeSelectorPartContext(SelectorPartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterAttributeSelectorPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitAttributeSelectorPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitAttributeSelectorPart(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ClassSelectorContext extends SelectorPartContext {
		public TerminalNode DOT() { return getToken(TemplateParser.DOT, 0); }
		public TerminalNode STYLE_IDENT() { return getToken(TemplateParser.STYLE_IDENT, 0); }
		public ClassSelectorContext(SelectorPartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterClassSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitClassSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitClassSelector(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PseudoClassSelectorContext extends SelectorPartContext {
		public TerminalNode COLON() { return getToken(TemplateParser.COLON, 0); }
		public TerminalNode STYLE_IDENT() { return getToken(TemplateParser.STYLE_IDENT, 0); }
		public PseudoClassSelectorContext(SelectorPartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterPseudoClassSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitPseudoClassSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitPseudoClassSelector(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PseudoClassWithArgSelectorContext extends SelectorPartContext {
		public TerminalNode COLON() { return getToken(TemplateParser.COLON, 0); }
		public List<TerminalNode> STYLE_IDENT() { return getTokens(TemplateParser.STYLE_IDENT); }
		public TerminalNode STYLE_IDENT(int i) {
			return getToken(TemplateParser.STYLE_IDENT, i);
		}
		public TerminalNode LPAREN() { return getToken(TemplateParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TemplateParser.RPAREN, 0); }
		public TerminalNode NUMBER() { return getToken(TemplateParser.NUMBER, 0); }
		public PseudoClassWithArgSelectorContext(SelectorPartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterPseudoClassWithArgSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitPseudoClassWithArgSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitPseudoClassWithArgSelector(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UniversalSelectorContext extends SelectorPartContext {
		public TerminalNode STAR() { return getToken(TemplateParser.STAR, 0); }
		public UniversalSelectorContext(SelectorPartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterUniversalSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitUniversalSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitUniversalSelector(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PseudoElementSelectorContext extends SelectorPartContext {
		public TerminalNode DOUBLE_COLON() { return getToken(TemplateParser.DOUBLE_COLON, 0); }
		public TerminalNode STYLE_IDENT() { return getToken(TemplateParser.STYLE_IDENT, 0); }
		public PseudoElementSelectorContext(SelectorPartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterPseudoElementSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitPseudoElementSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitPseudoElementSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectorPartContext selectorPart() throws RecognitionException {
		SelectorPartContext _localctx = new SelectorPartContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_selectorPart);
		int _la;
		try {
			setState(211);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				_localctx = new UniversalSelectorContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(192);
				match(STAR);
				}
				break;
			case 2:
				_localctx = new IdSelectorContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(193);
				match(HASH);
				setState(194);
				match(STYLE_IDENT);
				}
				break;
			case 3:
				_localctx = new ClassSelectorContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(195);
				match(DOT);
				setState(196);
				match(STYLE_IDENT);
				}
				break;
			case 4:
				_localctx = new TypeSelectorContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(197);
				match(STYLE_IDENT);
				}
				break;
			case 5:
				_localctx = new PseudoClassSelectorContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(198);
				match(COLON);
				setState(199);
				match(STYLE_IDENT);
				}
				break;
			case 6:
				_localctx = new PseudoElementSelectorContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(200);
				match(DOUBLE_COLON);
				setState(201);
				match(STYLE_IDENT);
				}
				break;
			case 7:
				_localctx = new AttributeSelectorPartContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(202);
				match(LBRACKET);
				setState(203);
				attributeSelector();
				setState(204);
				match(RBRACKET);
				}
				break;
			case 8:
				_localctx = new PseudoClassWithArgSelectorContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(206);
				match(COLON);
				setState(207);
				match(STYLE_IDENT);
				setState(208);
				match(LPAREN);
				setState(209);
				_la = _input.LA(1);
				if ( !(_la==NUMBER || _la==STYLE_IDENT) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(210);
				match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeSelectorContext extends ParserRuleContext {
		public AttributeSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeSelector; }
	 
		public AttributeSelectorContext() { }
		public void copyFrom(AttributeSelectorContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ComparisonAttributeSelectorContext extends AttributeSelectorContext {
		public Token op;
		public Token value;
		public TerminalNode STYLE_IDENT() { return getToken(TemplateParser.STYLE_IDENT, 0); }
		public TerminalNode STYLE_STRING() { return getToken(TemplateParser.STYLE_STRING, 0); }
		public TerminalNode EQUAL() { return getToken(TemplateParser.EQUAL, 0); }
		public TerminalNode TILDE_EQUAL() { return getToken(TemplateParser.TILDE_EQUAL, 0); }
		public TerminalNode PIPE_EQUAL() { return getToken(TemplateParser.PIPE_EQUAL, 0); }
		public TerminalNode CARET_EQUAL() { return getToken(TemplateParser.CARET_EQUAL, 0); }
		public TerminalNode DOLLAR_EQUAL() { return getToken(TemplateParser.DOLLAR_EQUAL, 0); }
		public TerminalNode STAR_EQUAL() { return getToken(TemplateParser.STAR_EQUAL, 0); }
		public ComparisonAttributeSelectorContext(AttributeSelectorContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterComparisonAttributeSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitComparisonAttributeSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitComparisonAttributeSelector(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SimpleAttributeSelectorContext extends AttributeSelectorContext {
		public TerminalNode STYLE_IDENT() { return getToken(TemplateParser.STYLE_IDENT, 0); }
		public SimpleAttributeSelectorContext(AttributeSelectorContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterSimpleAttributeSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitSimpleAttributeSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitSimpleAttributeSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeSelectorContext attributeSelector() throws RecognitionException {
		AttributeSelectorContext _localctx = new AttributeSelectorContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_attributeSelector);
		int _la;
		try {
			setState(217);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				_localctx = new SimpleAttributeSelectorContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(213);
				match(STYLE_IDENT);
				}
				break;
			case 2:
				_localctx = new ComparisonAttributeSelectorContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(214);
				match(STYLE_IDENT);
				setState(215);
				((ComparisonAttributeSelectorContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 532575961088L) != 0)) ) {
					((ComparisonAttributeSelectorContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(216);
				((ComparisonAttributeSelectorContext)_localctx).value = match(STYLE_STRING);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclarationListContext extends ParserRuleContext {
		public DeclarationListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarationList; }
	 
		public DeclarationListContext() { }
		public void copyFrom(DeclarationListContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DeclarationListNodeContext extends DeclarationListContext {
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public DeclarationListNodeContext(DeclarationListContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterDeclarationListNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitDeclarationListNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitDeclarationListNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationListContext declarationList() throws RecognitionException {
		DeclarationListContext _localctx = new DeclarationListContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_declarationList);
		int _la;
		try {
			_localctx = new DeclarationListNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(222);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==STYLE_IDENT) {
				{
				{
				setState(219);
				declaration();
				}
				}
				setState(224);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclarationContext extends ParserRuleContext {
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
	 
		public DeclarationContext() { }
		public void copyFrom(DeclarationContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DeclarationNodeContext extends DeclarationContext {
		public Token property;
		public DeclarationValueContext val;
		public TerminalNode COLON() { return getToken(TemplateParser.COLON, 0); }
		public TerminalNode SEMI() { return getToken(TemplateParser.SEMI, 0); }
		public TerminalNode STYLE_IDENT() { return getToken(TemplateParser.STYLE_IDENT, 0); }
		public DeclarationValueContext declarationValue() {
			return getRuleContext(DeclarationValueContext.class,0);
		}
		public DeclarationNodeContext(DeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterDeclarationNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitDeclarationNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitDeclarationNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_declaration);
		try {
			_localctx = new DeclarationNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(225);
			((DeclarationNodeContext)_localctx).property = match(STYLE_IDENT);
			setState(226);
			match(COLON);
			setState(227);
			((DeclarationNodeContext)_localctx).val = declarationValue();
			setState(228);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclarationValueContext extends ParserRuleContext {
		public DeclarationValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarationValue; }
	 
		public DeclarationValueContext() { }
		public void copyFrom(DeclarationValueContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DeclarationValueNodeContext extends DeclarationValueContext {
		public StyleValuePartContext valPart;
		public List<StyleValuePartContext> styleValuePart() {
			return getRuleContexts(StyleValuePartContext.class);
		}
		public StyleValuePartContext styleValuePart(int i) {
			return getRuleContext(StyleValuePartContext.class,i);
		}
		public List<TerminalNode> STYLE_WS() { return getTokens(TemplateParser.STYLE_WS); }
		public TerminalNode STYLE_WS(int i) {
			return getToken(TemplateParser.STYLE_WS, i);
		}
		public DeclarationValueNodeContext(DeclarationValueContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterDeclarationValueNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitDeclarationValueNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitDeclarationValueNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationValueContext declarationValue() throws RecognitionException {
		DeclarationValueContext _localctx = new DeclarationValueContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_declarationValue);
		int _la;
		try {
			_localctx = new DeclarationValueNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			((DeclarationValueNodeContext)_localctx).valPart = styleValuePart();
			setState(240);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 278734762409984L) != 0)) {
				{
				{
				setState(234);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==STYLE_WS) {
					{
					{
					setState(231);
					match(STYLE_WS);
					}
					}
					setState(236);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(237);
				((DeclarationValueNodeContext)_localctx).valPart = styleValuePart();
				}
				}
				setState(242);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StyleValuePartContext extends ParserRuleContext {
		public StyleValuePartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_styleValuePart; }
	 
		public StyleValuePartContext() { }
		public void copyFrom(StyleValuePartContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringStyleValueContext extends StyleValuePartContext {
		public TerminalNode STYLE_STRING() { return getToken(TemplateParser.STYLE_STRING, 0); }
		public StringStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterStringStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitStringStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitStringStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ColonStyleValueContext extends StyleValuePartContext {
		public TerminalNode COLON() { return getToken(TemplateParser.COLON, 0); }
		public ColonStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterColonStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitColonStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitColonStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CommaStyleValueContext extends StyleValuePartContext {
		public TerminalNode COMMA() { return getToken(TemplateParser.COMMA, 0); }
		public CommaStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterCommaStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitCommaStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitCommaStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RparenStyleValueContext extends StyleValuePartContext {
		public TerminalNode RPAREN() { return getToken(TemplateParser.RPAREN, 0); }
		public RparenStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterRparenStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitRparenStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitRparenStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ColorStyleValueContext extends StyleValuePartContext {
		public TerminalNode COLOR_HEX() { return getToken(TemplateParser.COLOR_HEX, 0); }
		public ColorStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterColorStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitColorStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitColorStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FunctionStyleValueContext extends StyleValuePartContext {
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public FunctionStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterFunctionStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitFunctionStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitFunctionStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RbracketStyleValueContext extends StyleValuePartContext {
		public TerminalNode RBRACKET() { return getToken(TemplateParser.RBRACKET, 0); }
		public RbracketStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterRbracketStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitRbracketStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitRbracketStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumericStyleValueContext extends StyleValuePartContext {
		public NumericValueTypeContext numVal;
		public Token unit;
		public NumericValueTypeContext numericValueType() {
			return getRuleContext(NumericValueTypeContext.class,0);
		}
		public TerminalNode UNIT() { return getToken(TemplateParser.UNIT, 0); }
		public NumericStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterNumericStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitNumericStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitNumericStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UrlStyleValueContext extends StyleValuePartContext {
		public TerminalNode URL() { return getToken(TemplateParser.URL, 0); }
		public UrlStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterUrlStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitUrlStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitUrlStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LbracketStyleValueContext extends StyleValuePartContext {
		public TerminalNode LBRACKET() { return getToken(TemplateParser.LBRACKET, 0); }
		public LbracketStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterLbracketStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitLbracketStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitLbracketStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DotStyleValueContext extends StyleValuePartContext {
		public TerminalNode DOT() { return getToken(TemplateParser.DOT, 0); }
		public DotStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterDotStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitDotStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitDotStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IdentifierStyleValueContext extends StyleValuePartContext {
		public TerminalNode STYLE_IDENT() { return getToken(TemplateParser.STYLE_IDENT, 0); }
		public IdentifierStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterIdentifierStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitIdentifierStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitIdentifierStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class HashStyleValueContext extends StyleValuePartContext {
		public TerminalNode HASH() { return getToken(TemplateParser.HASH, 0); }
		public HashStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterHashStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitHashStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitHashStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LparenStyleValueContext extends StyleValuePartContext {
		public TerminalNode LPAREN() { return getToken(TemplateParser.LPAREN, 0); }
		public LparenStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterLparenStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitLparenStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitLparenStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StarStyleValueContext extends StyleValuePartContext {
		public TerminalNode STAR() { return getToken(TemplateParser.STAR, 0); }
		public StarStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterStarStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitStarStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitStarStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OtherStyleValueContext extends StyleValuePartContext {
		public TerminalNode STYLE_OTHER() { return getToken(TemplateParser.STYLE_OTHER, 0); }
		public OtherStyleValueContext(StyleValuePartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterOtherStyleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitOtherStyleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitOtherStyleValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StyleValuePartContext styleValuePart() throws RecognitionException {
		StyleValuePartContext _localctx = new StyleValuePartContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_styleValuePart);
		int _la;
		try {
			setState(262);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				_localctx = new StringStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(243);
				match(STYLE_STRING);
				}
				break;
			case 2:
				_localctx = new NumericStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(244);
				((NumericStyleValueContext)_localctx).numVal = numericValueType();
				setState(246);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==UNIT) {
					{
					setState(245);
					((NumericStyleValueContext)_localctx).unit = match(UNIT);
					}
				}

				}
				break;
			case 3:
				_localctx = new IdentifierStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(248);
				match(STYLE_IDENT);
				}
				break;
			case 4:
				_localctx = new ColorStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(249);
				match(COLOR_HEX);
				}
				break;
			case 5:
				_localctx = new FunctionStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(250);
				functionCall();
				}
				break;
			case 6:
				_localctx = new UrlStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(251);
				match(URL);
				}
				break;
			case 7:
				_localctx = new HashStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(252);
				match(HASH);
				}
				break;
			case 8:
				_localctx = new CommaStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(253);
				match(COMMA);
				}
				break;
			case 9:
				_localctx = new LparenStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(254);
				match(LPAREN);
				}
				break;
			case 10:
				_localctx = new RparenStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(255);
				match(RPAREN);
				}
				break;
			case 11:
				_localctx = new LbracketStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(256);
				match(LBRACKET);
				}
				break;
			case 12:
				_localctx = new RbracketStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(257);
				match(RBRACKET);
				}
				break;
			case 13:
				_localctx = new DotStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(258);
				match(DOT);
				}
				break;
			case 14:
				_localctx = new StarStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(259);
				match(STAR);
				}
				break;
			case 15:
				_localctx = new ColonStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(260);
				match(COLON);
				}
				break;
			case 16:
				_localctx = new OtherStyleValueContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(261);
				match(STYLE_OTHER);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NumericValueTypeContext extends ParserRuleContext {
		public NumericValueTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numericValueType; }
	 
		public NumericValueTypeContext() { }
		public void copyFrom(NumericValueTypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DecimalNumTypeContext extends NumericValueTypeContext {
		public TerminalNode DECIMAL() { return getToken(TemplateParser.DECIMAL, 0); }
		public DecimalNumTypeContext(NumericValueTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterDecimalNumType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitDecimalNumType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitDecimalNumType(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IntegerNumTypeContext extends NumericValueTypeContext {
		public TerminalNode NUMBER() { return getToken(TemplateParser.NUMBER, 0); }
		public IntegerNumTypeContext(NumericValueTypeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterIntegerNumType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitIntegerNumType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitIntegerNumType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumericValueTypeContext numericValueType() throws RecognitionException {
		NumericValueTypeContext _localctx = new NumericValueTypeContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_numericValueType);
		try {
			setState(266);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUMBER:
				_localctx = new IntegerNumTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(264);
				match(NUMBER);
				}
				break;
			case DECIMAL:
				_localctx = new DecimalNumTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(265);
				match(DECIMAL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallContext extends ParserRuleContext {
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
	 
		public FunctionCallContext() { }
		public void copyFrom(FunctionCallContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallNodeContext extends FunctionCallContext {
		public TerminalNode STYLE_IDENT() { return getToken(TemplateParser.STYLE_IDENT, 0); }
		public TerminalNode LPAREN() { return getToken(TemplateParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TemplateParser.RPAREN, 0); }
		public ValueListContext valueList() {
			return getRuleContext(ValueListContext.class,0);
		}
		public FunctionCallNodeContext(FunctionCallContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterFunctionCallNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitFunctionCallNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitFunctionCallNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_functionCall);
		try {
			_localctx = new FunctionCallNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(268);
			match(STYLE_IDENT);
			setState(269);
			match(LPAREN);
			setState(271);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				{
				setState(270);
				valueList();
				}
				break;
			}
			setState(273);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueListContext extends ParserRuleContext {
		public ValueListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueList; }
	 
		public ValueListContext() { }
		public void copyFrom(ValueListContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ValueListNodeContext extends ValueListContext {
		public List<StyleValuePartContext> styleValuePart() {
			return getRuleContexts(StyleValuePartContext.class);
		}
		public StyleValuePartContext styleValuePart(int i) {
			return getRuleContext(StyleValuePartContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TemplateParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TemplateParser.COMMA, i);
		}
		public ValueListNodeContext(ValueListContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterValueListNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitValueListNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitValueListNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueListContext valueList() throws RecognitionException {
		ValueListContext _localctx = new ValueListContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_valueList);
		int _la;
		try {
			_localctx = new ValueListNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(275);
			styleValuePart();
			setState(280);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(276);
				match(COMMA);
				setState(277);
				styleValuePart();
				}
				}
				setState(282);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JinjaExprContext extends ParserRuleContext {
		public JinjaExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaExpr; }
	 
		public JinjaExprContext() { }
		public void copyFrom(JinjaExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaExpressionContext extends JinjaExprContext {
		public JinjaExpressionBodyContext body;
		public TerminalNode JINJA_EXPR_OPEN() { return getToken(TemplateParser.JINJA_EXPR_OPEN, 0); }
		public TerminalNode JINJA_EXPR_CLOSE() { return getToken(TemplateParser.JINJA_EXPR_CLOSE, 0); }
		public JinjaExpressionBodyContext jinjaExpressionBody() {
			return getRuleContext(JinjaExpressionBodyContext.class,0);
		}
		public JinjaExpressionContext(JinjaExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaExprContext jinjaExpr() throws RecognitionException {
		JinjaExprContext _localctx = new JinjaExprContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_jinjaExpr);
		try {
			_localctx = new JinjaExpressionContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			match(JINJA_EXPR_OPEN);
			setState(284);
			((JinjaExpressionContext)_localctx).body = jinjaExpressionBody();
			setState(285);
			match(JINJA_EXPR_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JinjaExpressionBodyContext extends ParserRuleContext {
		public JinjaExpressionBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaExpressionBody; }
	 
		public JinjaExpressionBodyContext() { }
		public void copyFrom(JinjaExpressionBodyContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaExpressionBodyNodeContext extends JinjaExpressionBodyContext {
		public List<TerminalNode> JINJA_EXPR_IDENT() { return getTokens(TemplateParser.JINJA_EXPR_IDENT); }
		public TerminalNode JINJA_EXPR_IDENT(int i) {
			return getToken(TemplateParser.JINJA_EXPR_IDENT, i);
		}
		public List<TerminalNode> JINJA_EXPR_NUMBER() { return getTokens(TemplateParser.JINJA_EXPR_NUMBER); }
		public TerminalNode JINJA_EXPR_NUMBER(int i) {
			return getToken(TemplateParser.JINJA_EXPR_NUMBER, i);
		}
		public List<TerminalNode> JINJA_EXPR_STRING() { return getTokens(TemplateParser.JINJA_EXPR_STRING); }
		public TerminalNode JINJA_EXPR_STRING(int i) {
			return getToken(TemplateParser.JINJA_EXPR_STRING, i);
		}
		public List<TerminalNode> JINJA_EXPR_OPERATOR() { return getTokens(TemplateParser.JINJA_EXPR_OPERATOR); }
		public TerminalNode JINJA_EXPR_OPERATOR(int i) {
			return getToken(TemplateParser.JINJA_EXPR_OPERATOR, i);
		}
		public List<TerminalNode> JINJA_EXPR_PUNCT() { return getTokens(TemplateParser.JINJA_EXPR_PUNCT); }
		public TerminalNode JINJA_EXPR_PUNCT(int i) {
			return getToken(TemplateParser.JINJA_EXPR_PUNCT, i);
		}
		public List<TerminalNode> JINJA_EXPR_WS() { return getTokens(TemplateParser.JINJA_EXPR_WS); }
		public TerminalNode JINJA_EXPR_WS(int i) {
			return getToken(TemplateParser.JINJA_EXPR_WS, i);
		}
		public List<TerminalNode> JINJA_EXPR_EQ() { return getTokens(TemplateParser.JINJA_EXPR_EQ); }
		public TerminalNode JINJA_EXPR_EQ(int i) {
			return getToken(TemplateParser.JINJA_EXPR_EQ, i);
		}
		public List<TerminalNode> JINJA_EXPR_NE() { return getTokens(TemplateParser.JINJA_EXPR_NE); }
		public TerminalNode JINJA_EXPR_NE(int i) {
			return getToken(TemplateParser.JINJA_EXPR_NE, i);
		}
		public List<TerminalNode> JINJA_EXPR_GT() { return getTokens(TemplateParser.JINJA_EXPR_GT); }
		public TerminalNode JINJA_EXPR_GT(int i) {
			return getToken(TemplateParser.JINJA_EXPR_GT, i);
		}
		public List<TerminalNode> JINJA_EXPR_LT() { return getTokens(TemplateParser.JINJA_EXPR_LT); }
		public TerminalNode JINJA_EXPR_LT(int i) {
			return getToken(TemplateParser.JINJA_EXPR_LT, i);
		}
		public List<TerminalNode> JINJA_EXPR_GE() { return getTokens(TemplateParser.JINJA_EXPR_GE); }
		public TerminalNode JINJA_EXPR_GE(int i) {
			return getToken(TemplateParser.JINJA_EXPR_GE, i);
		}
		public List<TerminalNode> JINJA_EXPR_LE() { return getTokens(TemplateParser.JINJA_EXPR_LE); }
		public TerminalNode JINJA_EXPR_LE(int i) {
			return getToken(TemplateParser.JINJA_EXPR_LE, i);
		}
		public List<TerminalNode> JINJA_EXPR_AND() { return getTokens(TemplateParser.JINJA_EXPR_AND); }
		public TerminalNode JINJA_EXPR_AND(int i) {
			return getToken(TemplateParser.JINJA_EXPR_AND, i);
		}
		public List<TerminalNode> JINJA_EXPR_OR() { return getTokens(TemplateParser.JINJA_EXPR_OR); }
		public TerminalNode JINJA_EXPR_OR(int i) {
			return getToken(TemplateParser.JINJA_EXPR_OR, i);
		}
		public List<TerminalNode> JINJA_EXPR_NOT() { return getTokens(TemplateParser.JINJA_EXPR_NOT); }
		public TerminalNode JINJA_EXPR_NOT(int i) {
			return getToken(TemplateParser.JINJA_EXPR_NOT, i);
		}
		public List<TerminalNode> JINJA_EXPR_IN() { return getTokens(TemplateParser.JINJA_EXPR_IN); }
		public TerminalNode JINJA_EXPR_IN(int i) {
			return getToken(TemplateParser.JINJA_EXPR_IN, i);
		}
		public JinjaExpressionBodyNodeContext(JinjaExpressionBodyContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaExpressionBodyNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaExpressionBodyNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaExpressionBodyNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaExpressionBodyContext jinjaExpressionBody() throws RecognitionException {
		JinjaExpressionBodyContext _localctx = new JinjaExpressionBodyContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_jinjaExpressionBody);
		int _la;
		try {
			_localctx = new JinjaExpressionBodyNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(290);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & 65535L) != 0)) {
				{
				{
				setState(287);
				_la = _input.LA(1);
				if ( !(((((_la - 78)) & ~0x3f) == 0 && ((1L << (_la - 78)) & 65535L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(292);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JinjaBlockContext extends ParserRuleContext {
		public JinjaBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaBlock; }
	 
		public JinjaBlockContext() { }
		public void copyFrom(JinjaBlockContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ContainerJinjaBlockContext extends JinjaBlockContext {
		public JinjaContainerContext jinjaContainer() {
			return getRuleContext(JinjaContainerContext.class,0);
		}
		public ContainerJinjaBlockContext(JinjaBlockContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterContainerJinjaBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitContainerJinjaBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitContainerJinjaBlock(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SimpleJinjaBlockContext extends JinjaBlockContext {
		public JinjaSimpleContext jinjaSimple() {
			return getRuleContext(JinjaSimpleContext.class,0);
		}
		public SimpleJinjaBlockContext(JinjaBlockContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterSimpleJinjaBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitSimpleJinjaBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitSimpleJinjaBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaBlockContext jinjaBlock() throws RecognitionException {
		JinjaBlockContext _localctx = new JinjaBlockContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_jinjaBlock);
		try {
			setState(295);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				_localctx = new ContainerJinjaBlockContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(293);
				jinjaContainer();
				}
				break;
			case 2:
				_localctx = new SimpleJinjaBlockContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(294);
				jinjaSimple();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JinjaSimpleContext extends ParserRuleContext {
		public JinjaSimpleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaSimple; }
	 
		public JinjaSimpleContext() { }
		public void copyFrom(JinjaSimpleContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaSimpleNodeContext extends JinjaSimpleContext {
		public JinjaStatementBodyContext body;
		public TerminalNode JINJA_STMT_OPEN() { return getToken(TemplateParser.JINJA_STMT_OPEN, 0); }
		public TerminalNode JINJA_STMT_CLOSE() { return getToken(TemplateParser.JINJA_STMT_CLOSE, 0); }
		public JinjaStatementBodyContext jinjaStatementBody() {
			return getRuleContext(JinjaStatementBodyContext.class,0);
		}
		public JinjaSimpleNodeContext(JinjaSimpleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaSimpleNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaSimpleNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaSimpleNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaSimpleContext jinjaSimple() throws RecognitionException {
		JinjaSimpleContext _localctx = new JinjaSimpleContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_jinjaSimple);
		try {
			_localctx = new JinjaSimpleNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(297);
			match(JINJA_STMT_OPEN);
			setState(298);
			((JinjaSimpleNodeContext)_localctx).body = jinjaStatementBody();
			setState(299);
			match(JINJA_STMT_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JinjaSimpleBodyContext extends ParserRuleContext {
		public JinjaSimpleBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaSimpleBody; }
	 
		public JinjaSimpleBodyContext() { }
		public void copyFrom(JinjaSimpleBodyContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaSimpleBodyNodeContext extends JinjaSimpleBodyContext {
		public List<TerminalNode> JINJA_EXTENDS() { return getTokens(TemplateParser.JINJA_EXTENDS); }
		public TerminalNode JINJA_EXTENDS(int i) {
			return getToken(TemplateParser.JINJA_EXTENDS, i);
		}
		public List<TerminalNode> JINJA_INCLUDE() { return getTokens(TemplateParser.JINJA_INCLUDE); }
		public TerminalNode JINJA_INCLUDE(int i) {
			return getToken(TemplateParser.JINJA_INCLUDE, i);
		}
		public List<TerminalNode> JINJA_IMPORT() { return getTokens(TemplateParser.JINJA_IMPORT); }
		public TerminalNode JINJA_IMPORT(int i) {
			return getToken(TemplateParser.JINJA_IMPORT, i);
		}
		public List<TerminalNode> JINJA_SET() { return getTokens(TemplateParser.JINJA_SET); }
		public TerminalNode JINJA_SET(int i) {
			return getToken(TemplateParser.JINJA_SET, i);
		}
		public List<TerminalNode> JINJA_IDENT() { return getTokens(TemplateParser.JINJA_IDENT); }
		public TerminalNode JINJA_IDENT(int i) {
			return getToken(TemplateParser.JINJA_IDENT, i);
		}
		public List<TerminalNode> JINJA_STRING() { return getTokens(TemplateParser.JINJA_STRING); }
		public TerminalNode JINJA_STRING(int i) {
			return getToken(TemplateParser.JINJA_STRING, i);
		}
		public List<TerminalNode> JINJA_NUMBER() { return getTokens(TemplateParser.JINJA_NUMBER); }
		public TerminalNode JINJA_NUMBER(int i) {
			return getToken(TemplateParser.JINJA_NUMBER, i);
		}
		public List<TerminalNode> JINJA_OPERATOR() { return getTokens(TemplateParser.JINJA_OPERATOR); }
		public TerminalNode JINJA_OPERATOR(int i) {
			return getToken(TemplateParser.JINJA_OPERATOR, i);
		}
		public List<TerminalNode> JINJA_PUNCT() { return getTokens(TemplateParser.JINJA_PUNCT); }
		public TerminalNode JINJA_PUNCT(int i) {
			return getToken(TemplateParser.JINJA_PUNCT, i);
		}
		public List<TerminalNode> JINJA_WS() { return getTokens(TemplateParser.JINJA_WS); }
		public TerminalNode JINJA_WS(int i) {
			return getToken(TemplateParser.JINJA_WS, i);
		}
		public List<TerminalNode> JINJA_EQ() { return getTokens(TemplateParser.JINJA_EQ); }
		public TerminalNode JINJA_EQ(int i) {
			return getToken(TemplateParser.JINJA_EQ, i);
		}
		public List<TerminalNode> JINJA_NE() { return getTokens(TemplateParser.JINJA_NE); }
		public TerminalNode JINJA_NE(int i) {
			return getToken(TemplateParser.JINJA_NE, i);
		}
		public List<TerminalNode> JINJA_GE() { return getTokens(TemplateParser.JINJA_GE); }
		public TerminalNode JINJA_GE(int i) {
			return getToken(TemplateParser.JINJA_GE, i);
		}
		public List<TerminalNode> JINJA_LE() { return getTokens(TemplateParser.JINJA_LE); }
		public TerminalNode JINJA_LE(int i) {
			return getToken(TemplateParser.JINJA_LE, i);
		}
		public List<TerminalNode> JINJA_AND() { return getTokens(TemplateParser.JINJA_AND); }
		public TerminalNode JINJA_AND(int i) {
			return getToken(TemplateParser.JINJA_AND, i);
		}
		public List<TerminalNode> JINJA_OR() { return getTokens(TemplateParser.JINJA_OR); }
		public TerminalNode JINJA_OR(int i) {
			return getToken(TemplateParser.JINJA_OR, i);
		}
		public List<TerminalNode> JINJA_NOT() { return getTokens(TemplateParser.JINJA_NOT); }
		public TerminalNode JINJA_NOT(int i) {
			return getToken(TemplateParser.JINJA_NOT, i);
		}
		public List<TerminalNode> JINJA_IN() { return getTokens(TemplateParser.JINJA_IN); }
		public TerminalNode JINJA_IN(int i) {
			return getToken(TemplateParser.JINJA_IN, i);
		}
		public JinjaSimpleBodyNodeContext(JinjaSimpleBodyContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaSimpleBodyNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaSimpleBodyNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaSimpleBodyNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaSimpleBodyContext jinjaSimpleBody() throws RecognitionException {
		JinjaSimpleBodyContext _localctx = new JinjaSimpleBodyContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_jinjaSimpleBody);
		int _la;
		try {
			_localctx = new JinjaSimpleBodyNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(302); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(301);
				_la = _input.LA(1);
				if ( !(((((_la - 59)) & ~0x3f) == 0 && ((1L << (_la - 59)) & 262143L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(304); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 59)) & ~0x3f) == 0 && ((1L << (_la - 59)) & 262143L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JinjaContainerContext extends ParserRuleContext {
		public JinjaContainerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaContainer; }
	 
		public JinjaContainerContext() { }
		public void copyFrom(JinjaContainerContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaContainerNodeContext extends JinjaContainerContext {
		public JinjaContainerStartContext start;
		public JinjaContainerEndContext end;
		public List<TerminalNode> JINJA_STMT_OPEN() { return getTokens(TemplateParser.JINJA_STMT_OPEN); }
		public TerminalNode JINJA_STMT_OPEN(int i) {
			return getToken(TemplateParser.JINJA_STMT_OPEN, i);
		}
		public List<TerminalNode> JINJA_STMT_CLOSE() { return getTokens(TemplateParser.JINJA_STMT_CLOSE); }
		public TerminalNode JINJA_STMT_CLOSE(int i) {
			return getToken(TemplateParser.JINJA_STMT_CLOSE, i);
		}
		public JinjaContainerStartContext jinjaContainerStart() {
			return getRuleContext(JinjaContainerStartContext.class,0);
		}
		public JinjaContainerEndContext jinjaContainerEnd() {
			return getRuleContext(JinjaContainerEndContext.class,0);
		}
		public List<ContentContext> content() {
			return getRuleContexts(ContentContext.class);
		}
		public ContentContext content(int i) {
			return getRuleContext(ContentContext.class,i);
		}
		public JinjaContainerNodeContext(JinjaContainerContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaContainerNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaContainerNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaContainerNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaContainerContext jinjaContainer() throws RecognitionException {
		JinjaContainerContext _localctx = new JinjaContainerContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_jinjaContainer);
		try {
			int _alt;
			_localctx = new JinjaContainerNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(306);
			match(JINJA_STMT_OPEN);
			setState(307);
			((JinjaContainerNodeContext)_localctx).start = jinjaContainerStart();
			setState(308);
			match(JINJA_STMT_CLOSE);
			setState(312);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(309);
					content();
					}
					} 
				}
				setState(314);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			}
			setState(315);
			match(JINJA_STMT_OPEN);
			setState(316);
			((JinjaContainerNodeContext)_localctx).end = jinjaContainerEnd();
			setState(317);
			match(JINJA_STMT_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JinjaStatementBodyContext extends ParserRuleContext {
		public JinjaStatementBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaStatementBody; }
	 
		public JinjaStatementBodyContext() { }
		public void copyFrom(JinjaStatementBodyContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaStatementBodyNodeContext extends JinjaStatementBodyContext {
		public List<JinjaKeywordContext> jinjaKeyword() {
			return getRuleContexts(JinjaKeywordContext.class);
		}
		public JinjaKeywordContext jinjaKeyword(int i) {
			return getRuleContext(JinjaKeywordContext.class,i);
		}
		public List<TerminalNode> JINJA_IDENT() { return getTokens(TemplateParser.JINJA_IDENT); }
		public TerminalNode JINJA_IDENT(int i) {
			return getToken(TemplateParser.JINJA_IDENT, i);
		}
		public List<TerminalNode> JINJA_STRING() { return getTokens(TemplateParser.JINJA_STRING); }
		public TerminalNode JINJA_STRING(int i) {
			return getToken(TemplateParser.JINJA_STRING, i);
		}
		public List<TerminalNode> JINJA_NUMBER() { return getTokens(TemplateParser.JINJA_NUMBER); }
		public TerminalNode JINJA_NUMBER(int i) {
			return getToken(TemplateParser.JINJA_NUMBER, i);
		}
		public List<TerminalNode> JINJA_OPERATOR() { return getTokens(TemplateParser.JINJA_OPERATOR); }
		public TerminalNode JINJA_OPERATOR(int i) {
			return getToken(TemplateParser.JINJA_OPERATOR, i);
		}
		public List<TerminalNode> JINJA_PUNCT() { return getTokens(TemplateParser.JINJA_PUNCT); }
		public TerminalNode JINJA_PUNCT(int i) {
			return getToken(TemplateParser.JINJA_PUNCT, i);
		}
		public List<TerminalNode> JINJA_WS() { return getTokens(TemplateParser.JINJA_WS); }
		public TerminalNode JINJA_WS(int i) {
			return getToken(TemplateParser.JINJA_WS, i);
		}
		public JinjaStatementBodyNodeContext(JinjaStatementBodyContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaStatementBodyNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaStatementBodyNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaStatementBodyNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaStatementBodyContext jinjaStatementBody() throws RecognitionException {
		JinjaStatementBodyContext _localctx = new JinjaStatementBodyContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_jinjaStatementBody);
		int _la;
		try {
			_localctx = new JinjaStatementBodyNodeContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(328);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 49)) & ~0x3f) == 0 && ((1L << (_la - 49)) & 268435455L) != 0)) {
				{
				setState(326);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case JINJA_IF:
				case JINJA_ELSE:
				case JINJA_ELIF:
				case JINJA_ENDIF:
				case JINJA_FOR:
				case JINJA_ENDFOR:
				case JINJA_BLOCK:
				case JINJA_ENDBLOCK:
				case JINJA_MACRO:
				case JINJA_ENDMACRO:
				case JINJA_SET:
				case JINJA_INCLUDE:
				case JINJA_EXTENDS:
				case JINJA_IMPORT:
				case JINJA_EQ:
				case JINJA_NE:
				case JINJA_GE:
				case JINJA_LE:
				case JINJA_AND:
				case JINJA_OR:
				case JINJA_NOT:
				case JINJA_IN:
					{
					setState(319);
					jinjaKeyword();
					}
					break;
				case JINJA_IDENT:
					{
					setState(320);
					match(JINJA_IDENT);
					}
					break;
				case JINJA_STRING:
					{
					setState(321);
					match(JINJA_STRING);
					}
					break;
				case JINJA_NUMBER:
					{
					setState(322);
					match(JINJA_NUMBER);
					}
					break;
				case JINJA_OPERATOR:
					{
					setState(323);
					match(JINJA_OPERATOR);
					}
					break;
				case JINJA_PUNCT:
					{
					setState(324);
					match(JINJA_PUNCT);
					}
					break;
				case JINJA_WS:
					{
					setState(325);
					match(JINJA_WS);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(330);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JinjaContainerStartContext extends ParserRuleContext {
		public JinjaContainerStartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaContainerStart; }
	 
		public JinjaContainerStartContext() { }
		public void copyFrom(JinjaContainerStartContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MacroStatementStartContext extends JinjaContainerStartContext {
		public Token name;
		public TerminalNode JINJA_MACRO() { return getToken(TemplateParser.JINJA_MACRO, 0); }
		public TerminalNode JINJA_IDENT() { return getToken(TemplateParser.JINJA_IDENT, 0); }
		public MacroStatementStartContext(JinjaContainerStartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterMacroStatementStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitMacroStatementStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitMacroStatementStart(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IfStatementStartContext extends JinjaContainerStartContext {
		public JinjaStatementBodyContext body;
		public TerminalNode JINJA_IF() { return getToken(TemplateParser.JINJA_IF, 0); }
		public JinjaStatementBodyContext jinjaStatementBody() {
			return getRuleContext(JinjaStatementBodyContext.class,0);
		}
		public IfStatementStartContext(JinjaContainerStartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterIfStatementStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitIfStatementStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitIfStatementStart(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ForStatementStartContext extends JinjaContainerStartContext {
		public JinjaStatementBodyContext body;
		public TerminalNode JINJA_FOR() { return getToken(TemplateParser.JINJA_FOR, 0); }
		public JinjaStatementBodyContext jinjaStatementBody() {
			return getRuleContext(JinjaStatementBodyContext.class,0);
		}
		public ForStatementStartContext(JinjaContainerStartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterForStatementStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitForStatementStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitForStatementStart(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ElifStatementStartContext extends JinjaContainerStartContext {
		public JinjaStatementBodyContext body;
		public TerminalNode JINJA_ELIF() { return getToken(TemplateParser.JINJA_ELIF, 0); }
		public JinjaStatementBodyContext jinjaStatementBody() {
			return getRuleContext(JinjaStatementBodyContext.class,0);
		}
		public ElifStatementStartContext(JinjaContainerStartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterElifStatementStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitElifStatementStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitElifStatementStart(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BlockStatementStartContext extends JinjaContainerStartContext {
		public Token name;
		public TerminalNode JINJA_BLOCK() { return getToken(TemplateParser.JINJA_BLOCK, 0); }
		public TerminalNode JINJA_IDENT() { return getToken(TemplateParser.JINJA_IDENT, 0); }
		public BlockStatementStartContext(JinjaContainerStartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterBlockStatementStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitBlockStatementStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitBlockStatementStart(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ElseStatementStartContext extends JinjaContainerStartContext {
		public JinjaStatementBodyContext body;
		public TerminalNode JINJA_ELSE() { return getToken(TemplateParser.JINJA_ELSE, 0); }
		public JinjaStatementBodyContext jinjaStatementBody() {
			return getRuleContext(JinjaStatementBodyContext.class,0);
		}
		public ElseStatementStartContext(JinjaContainerStartContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterElseStatementStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitElseStatementStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitElseStatementStart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaContainerStartContext jinjaContainerStart() throws RecognitionException {
		JinjaContainerStartContext _localctx = new JinjaContainerStartContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_jinjaContainerStart);
		try {
			setState(345);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case JINJA_FOR:
				_localctx = new ForStatementStartContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(331);
				match(JINJA_FOR);
				setState(332);
				((ForStatementStartContext)_localctx).body = jinjaStatementBody();
				}
				break;
			case JINJA_IF:
				_localctx = new IfStatementStartContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(333);
				match(JINJA_IF);
				setState(334);
				((IfStatementStartContext)_localctx).body = jinjaStatementBody();
				}
				break;
			case JINJA_BLOCK:
				_localctx = new BlockStatementStartContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(335);
				match(JINJA_BLOCK);
				setState(336);
				((BlockStatementStartContext)_localctx).name = match(JINJA_IDENT);
				}
				break;
			case JINJA_MACRO:
				_localctx = new MacroStatementStartContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(337);
				match(JINJA_MACRO);
				setState(338);
				((MacroStatementStartContext)_localctx).name = match(JINJA_IDENT);
				}
				break;
			case JINJA_ELSE:
				_localctx = new ElseStatementStartContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(339);
				match(JINJA_ELSE);
				setState(341);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
				case 1:
					{
					setState(340);
					((ElseStatementStartContext)_localctx).body = jinjaStatementBody();
					}
					break;
				}
				}
				break;
			case JINJA_ELIF:
				_localctx = new ElifStatementStartContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(343);
				match(JINJA_ELIF);
				setState(344);
				((ElifStatementStartContext)_localctx).body = jinjaStatementBody();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JinjaContainerEndContext extends ParserRuleContext {
		public JinjaContainerEndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaContainerEnd; }
	 
		public JinjaContainerEndContext() { }
		public void copyFrom(JinjaContainerEndContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MacroStatementEndContext extends JinjaContainerEndContext {
		public TerminalNode JINJA_ENDMACRO() { return getToken(TemplateParser.JINJA_ENDMACRO, 0); }
		public MacroStatementEndContext(JinjaContainerEndContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterMacroStatementEnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitMacroStatementEnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitMacroStatementEnd(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IfStatementEndContext extends JinjaContainerEndContext {
		public TerminalNode JINJA_ENDIF() { return getToken(TemplateParser.JINJA_ENDIF, 0); }
		public IfStatementEndContext(JinjaContainerEndContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterIfStatementEnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitIfStatementEnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitIfStatementEnd(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BlockStatementEndContext extends JinjaContainerEndContext {
		public TerminalNode JINJA_ENDBLOCK() { return getToken(TemplateParser.JINJA_ENDBLOCK, 0); }
		public BlockStatementEndContext(JinjaContainerEndContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterBlockStatementEnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitBlockStatementEnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitBlockStatementEnd(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ForStatementEndContext extends JinjaContainerEndContext {
		public TerminalNode JINJA_ENDFOR() { return getToken(TemplateParser.JINJA_ENDFOR, 0); }
		public ForStatementEndContext(JinjaContainerEndContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterForStatementEnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitForStatementEnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitForStatementEnd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaContainerEndContext jinjaContainerEnd() throws RecognitionException {
		JinjaContainerEndContext _localctx = new JinjaContainerEndContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_jinjaContainerEnd);
		try {
			setState(351);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case JINJA_ENDIF:
				_localctx = new IfStatementEndContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(347);
				match(JINJA_ENDIF);
				}
				break;
			case JINJA_ENDFOR:
				_localctx = new ForStatementEndContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(348);
				match(JINJA_ENDFOR);
				}
				break;
			case JINJA_ENDBLOCK:
				_localctx = new BlockStatementEndContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(349);
				match(JINJA_ENDBLOCK);
				}
				break;
			case JINJA_ENDMACRO:
				_localctx = new MacroStatementEndContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(350);
				match(JINJA_ENDMACRO);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordContext extends ParserRuleContext {
		public JinjaKeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaKeyword; }
	 
		public JinjaKeywordContext() { }
		public void copyFrom(JinjaKeywordContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordElseContext extends JinjaKeywordContext {
		public TerminalNode JINJA_ELSE() { return getToken(TemplateParser.JINJA_ELSE, 0); }
		public JinjaKeywordElseContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordElse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordElse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordElse(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordOrContext extends JinjaKeywordContext {
		public TerminalNode JINJA_OR() { return getToken(TemplateParser.JINJA_OR, 0); }
		public JinjaKeywordOrContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordOr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordEndForContext extends JinjaKeywordContext {
		public TerminalNode JINJA_ENDFOR() { return getToken(TemplateParser.JINJA_ENDFOR, 0); }
		public JinjaKeywordEndForContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordEndFor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordEndFor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordEndFor(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordGeContext extends JinjaKeywordContext {
		public TerminalNode JINJA_GE() { return getToken(TemplateParser.JINJA_GE, 0); }
		public JinjaKeywordGeContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordGe(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordGe(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordGe(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordIfContext extends JinjaKeywordContext {
		public TerminalNode JINJA_IF() { return getToken(TemplateParser.JINJA_IF, 0); }
		public JinjaKeywordIfContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordIf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordIf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordIf(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordIncludeContext extends JinjaKeywordContext {
		public TerminalNode JINJA_INCLUDE() { return getToken(TemplateParser.JINJA_INCLUDE, 0); }
		public JinjaKeywordIncludeContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordInclude(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordInclude(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordInclude(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordImportContext extends JinjaKeywordContext {
		public TerminalNode JINJA_IMPORT() { return getToken(TemplateParser.JINJA_IMPORT, 0); }
		public JinjaKeywordImportContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordImport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordImport(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordImport(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordEqContext extends JinjaKeywordContext {
		public TerminalNode JINJA_EQ() { return getToken(TemplateParser.JINJA_EQ, 0); }
		public JinjaKeywordEqContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordEq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordEq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordEq(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordForContext extends JinjaKeywordContext {
		public TerminalNode JINJA_FOR() { return getToken(TemplateParser.JINJA_FOR, 0); }
		public JinjaKeywordForContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordFor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordFor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordFor(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordInContext extends JinjaKeywordContext {
		public TerminalNode JINJA_IN() { return getToken(TemplateParser.JINJA_IN, 0); }
		public JinjaKeywordInContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordIn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordIn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordIn(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordEndIfContext extends JinjaKeywordContext {
		public TerminalNode JINJA_ENDIF() { return getToken(TemplateParser.JINJA_ENDIF, 0); }
		public JinjaKeywordEndIfContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordEndIf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordEndIf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordEndIf(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordBlockContext extends JinjaKeywordContext {
		public TerminalNode JINJA_BLOCK() { return getToken(TemplateParser.JINJA_BLOCK, 0); }
		public JinjaKeywordBlockContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordBlock(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordExtendsContext extends JinjaKeywordContext {
		public TerminalNode JINJA_EXTENDS() { return getToken(TemplateParser.JINJA_EXTENDS, 0); }
		public JinjaKeywordExtendsContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordExtends(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordExtends(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordExtends(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordEndMacroContext extends JinjaKeywordContext {
		public TerminalNode JINJA_ENDMACRO() { return getToken(TemplateParser.JINJA_ENDMACRO, 0); }
		public JinjaKeywordEndMacroContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordEndMacro(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordEndMacro(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordEndMacro(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordSetContext extends JinjaKeywordContext {
		public TerminalNode JINJA_SET() { return getToken(TemplateParser.JINJA_SET, 0); }
		public JinjaKeywordSetContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordSet(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordElifContext extends JinjaKeywordContext {
		public TerminalNode JINJA_ELIF() { return getToken(TemplateParser.JINJA_ELIF, 0); }
		public JinjaKeywordElifContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordElif(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordElif(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordElif(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordMacroContext extends JinjaKeywordContext {
		public TerminalNode JINJA_MACRO() { return getToken(TemplateParser.JINJA_MACRO, 0); }
		public JinjaKeywordMacroContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordMacro(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordMacro(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordMacro(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordLeContext extends JinjaKeywordContext {
		public TerminalNode JINJA_LE() { return getToken(TemplateParser.JINJA_LE, 0); }
		public JinjaKeywordLeContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordLe(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordLe(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordLe(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordNotContext extends JinjaKeywordContext {
		public TerminalNode JINJA_NOT() { return getToken(TemplateParser.JINJA_NOT, 0); }
		public JinjaKeywordNotContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordNot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordNot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordNot(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordEndBlockContext extends JinjaKeywordContext {
		public TerminalNode JINJA_ENDBLOCK() { return getToken(TemplateParser.JINJA_ENDBLOCK, 0); }
		public JinjaKeywordEndBlockContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordEndBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordEndBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordEndBlock(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordNeContext extends JinjaKeywordContext {
		public TerminalNode JINJA_NE() { return getToken(TemplateParser.JINJA_NE, 0); }
		public JinjaKeywordNeContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordNe(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordNe(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordNe(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaKeywordAndContext extends JinjaKeywordContext {
		public TerminalNode JINJA_AND() { return getToken(TemplateParser.JINJA_AND, 0); }
		public JinjaKeywordAndContext(JinjaKeywordContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).enterJinjaKeywordAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TemplateParserListener ) ((TemplateParserListener)listener).exitJinjaKeywordAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TemplateParserVisitor ) return ((TemplateParserVisitor<? extends T>)visitor).visitJinjaKeywordAnd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaKeywordContext jinjaKeyword() throws RecognitionException {
		JinjaKeywordContext _localctx = new JinjaKeywordContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_jinjaKeyword);
		try {
			setState(375);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case JINJA_IF:
				_localctx = new JinjaKeywordIfContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(353);
				match(JINJA_IF);
				}
				break;
			case JINJA_ELSE:
				_localctx = new JinjaKeywordElseContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(354);
				match(JINJA_ELSE);
				}
				break;
			case JINJA_ELIF:
				_localctx = new JinjaKeywordElifContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(355);
				match(JINJA_ELIF);
				}
				break;
			case JINJA_ENDIF:
				_localctx = new JinjaKeywordEndIfContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(356);
				match(JINJA_ENDIF);
				}
				break;
			case JINJA_FOR:
				_localctx = new JinjaKeywordForContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(357);
				match(JINJA_FOR);
				}
				break;
			case JINJA_ENDFOR:
				_localctx = new JinjaKeywordEndForContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(358);
				match(JINJA_ENDFOR);
				}
				break;
			case JINJA_BLOCK:
				_localctx = new JinjaKeywordBlockContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(359);
				match(JINJA_BLOCK);
				}
				break;
			case JINJA_ENDBLOCK:
				_localctx = new JinjaKeywordEndBlockContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(360);
				match(JINJA_ENDBLOCK);
				}
				break;
			case JINJA_MACRO:
				_localctx = new JinjaKeywordMacroContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(361);
				match(JINJA_MACRO);
				}
				break;
			case JINJA_ENDMACRO:
				_localctx = new JinjaKeywordEndMacroContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(362);
				match(JINJA_ENDMACRO);
				}
				break;
			case JINJA_SET:
				_localctx = new JinjaKeywordSetContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(363);
				match(JINJA_SET);
				}
				break;
			case JINJA_INCLUDE:
				_localctx = new JinjaKeywordIncludeContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(364);
				match(JINJA_INCLUDE);
				}
				break;
			case JINJA_EXTENDS:
				_localctx = new JinjaKeywordExtendsContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(365);
				match(JINJA_EXTENDS);
				}
				break;
			case JINJA_IMPORT:
				_localctx = new JinjaKeywordImportContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(366);
				match(JINJA_IMPORT);
				}
				break;
			case JINJA_EQ:
				_localctx = new JinjaKeywordEqContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(367);
				match(JINJA_EQ);
				}
				break;
			case JINJA_NE:
				_localctx = new JinjaKeywordNeContext(_localctx);
				enterOuterAlt(_localctx, 16);
				{
				setState(368);
				match(JINJA_NE);
				}
				break;
			case JINJA_GE:
				_localctx = new JinjaKeywordGeContext(_localctx);
				enterOuterAlt(_localctx, 17);
				{
				setState(369);
				match(JINJA_GE);
				}
				break;
			case JINJA_LE:
				_localctx = new JinjaKeywordLeContext(_localctx);
				enterOuterAlt(_localctx, 18);
				{
				setState(370);
				match(JINJA_LE);
				}
				break;
			case JINJA_AND:
				_localctx = new JinjaKeywordAndContext(_localctx);
				enterOuterAlt(_localctx, 19);
				{
				setState(371);
				match(JINJA_AND);
				}
				break;
			case JINJA_OR:
				_localctx = new JinjaKeywordOrContext(_localctx);
				enterOuterAlt(_localctx, 20);
				{
				setState(372);
				match(JINJA_OR);
				}
				break;
			case JINJA_NOT:
				_localctx = new JinjaKeywordNotContext(_localctx);
				enterOuterAlt(_localctx, 21);
				{
				setState(373);
				match(JINJA_NOT);
				}
				break;
			case JINJA_IN:
				_localctx = new JinjaKeywordInContext(_localctx);
				enterOuterAlt(_localctx, 22);
				{
				setState(374);
				match(JINJA_IN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 6:
			return htmlElement_sempred((HtmlElementContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean htmlElement_sempred(HtmlElementContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return  !isVoidTag(_input.LT(2).getText()) ;
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001]\u017a\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0001"+
		"\u0000\u0003\u0000H\b\u0000\u0001\u0000\u0005\u0000K\b\u0000\n\u0000\f"+
		"\u0000N\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003"+
		"\u0002Z\b\u0002\u0001\u0003\u0004\u0003]\b\u0003\u000b\u0003\f\u0003^"+
		"\u0001\u0004\u0003\u0004b\b\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006k\b\u0006"+
		"\n\u0006\f\u0006n\t\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0005\u0007u\b\u0007\n\u0007\f\u0007x\t\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001"+
		"\t\u0005\t\u0084\b\t\n\t\f\t\u0087\t\t\u0001\t\u0001\t\u0001\t\u0003\t"+
		"\u008c\b\t\u0001\n\u0001\n\u0001\n\u0003\n\u0091\b\n\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0003\u000b\u0096\b\u000b\u0001\f\u0001\f\u0005\f\u009a"+
		"\b\f\n\f\f\f\u009d\t\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r"+
		"\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0005\u000e\u00a9\b\u000e"+
		"\n\u000e\f\u000e\u00ac\t\u000e\u0001\u000e\u0005\u000e\u00af\b\u000e\n"+
		"\u000e\f\u000e\u00b2\t\u000e\u0001\u000f\u0001\u000f\u0005\u000f\u00b6"+
		"\b\u000f\n\u000f\f\u000f\u00b9\t\u000f\u0001\u000f\u0005\u000f\u00bc\b"+
		"\u000f\n\u000f\f\u000f\u00bf\t\u000f\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0003\u0010\u00d4\b\u0010"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u00da\b\u0011"+
		"\u0001\u0012\u0005\u0012\u00dd\b\u0012\n\u0012\f\u0012\u00e0\t\u0012\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001"+
		"\u0014\u0005\u0014\u00e9\b\u0014\n\u0014\f\u0014\u00ec\t\u0014\u0001\u0014"+
		"\u0005\u0014\u00ef\b\u0014\n\u0014\f\u0014\u00f2\t\u0014\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0003\u0015\u00f7\b\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003"+
		"\u0015\u0107\b\u0015\u0001\u0016\u0001\u0016\u0003\u0016\u010b\b\u0016"+
		"\u0001\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u0110\b\u0017\u0001\u0017"+
		"\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0005\u0018\u0117\b\u0018"+
		"\n\u0018\f\u0018\u011a\t\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u001a\u0005\u001a\u0121\b\u001a\n\u001a\f\u001a\u0124\t\u001a"+
		"\u0001\u001b\u0001\u001b\u0003\u001b\u0128\b\u001b\u0001\u001c\u0001\u001c"+
		"\u0001\u001c\u0001\u001c\u0001\u001d\u0004\u001d\u012f\b\u001d\u000b\u001d"+
		"\f\u001d\u0130\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0005\u001e"+
		"\u0137\b\u001e\n\u001e\f\u001e\u013a\t\u001e\u0001\u001e\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0005\u001f\u0147\b\u001f\n\u001f\f\u001f"+
		"\u014a\t\u001f\u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001"+
		" \u0001 \u0001 \u0003 \u0156\b \u0001 \u0001 \u0003 \u015a\b \u0001!\u0001"+
		"!\u0001!\u0001!\u0003!\u0160\b!\u0001\"\u0001\"\u0001\"\u0001\"\u0001"+
		"\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001"+
		"\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0003"+
		"\"\u0178\b\"\u0001\"\u0000\u0000#\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BD\u0000"+
		"\u0005\u0001\u0000\t\n\u0002\u0000((++\u0002\u0000\u000e\u000e\"&\u0001"+
		"\u0000N]\u0001\u0000;L\u01b2\u0000G\u0001\u0000\u0000\u0000\u0002Q\u0001"+
		"\u0000\u0000\u0000\u0004Y\u0001\u0000\u0000\u0000\u0006\\\u0001\u0000"+
		"\u0000\u0000\ba\u0001\u0000\u0000\u0000\ne\u0001\u0000\u0000\u0000\fg"+
		"\u0001\u0000\u0000\u0000\u000eq\u0001\u0000\u0000\u0000\u0010{\u0001\u0000"+
		"\u0000\u0000\u0012\u0080\u0001\u0000\u0000\u0000\u0014\u008d\u0001\u0000"+
		"\u0000\u0000\u0016\u0095\u0001\u0000\u0000\u0000\u0018\u0097\u0001\u0000"+
		"\u0000\u0000\u001a\u00a0\u0001\u0000\u0000\u0000\u001c\u00a5\u0001\u0000"+
		"\u0000\u0000\u001e\u00b3\u0001\u0000\u0000\u0000 \u00d3\u0001\u0000\u0000"+
		"\u0000\"\u00d9\u0001\u0000\u0000\u0000$\u00de\u0001\u0000\u0000\u0000"+
		"&\u00e1\u0001\u0000\u0000\u0000(\u00e6\u0001\u0000\u0000\u0000*\u0106"+
		"\u0001\u0000\u0000\u0000,\u010a\u0001\u0000\u0000\u0000.\u010c\u0001\u0000"+
		"\u0000\u00000\u0113\u0001\u0000\u0000\u00002\u011b\u0001\u0000\u0000\u0000"+
		"4\u0122\u0001\u0000\u0000\u00006\u0127\u0001\u0000\u0000\u00008\u0129"+
		"\u0001\u0000\u0000\u0000:\u012e\u0001\u0000\u0000\u0000<\u0132\u0001\u0000"+
		"\u0000\u0000>\u0148\u0001\u0000\u0000\u0000@\u0159\u0001\u0000\u0000\u0000"+
		"B\u015f\u0001\u0000\u0000\u0000D\u0177\u0001\u0000\u0000\u0000FH\u0003"+
		"\n\u0005\u0000GF\u0001\u0000\u0000\u0000GH\u0001\u0000\u0000\u0000HL\u0001"+
		"\u0000\u0000\u0000IK\u0003\u0002\u0001\u0000JI\u0001\u0000\u0000\u0000"+
		"KN\u0001\u0000\u0000\u0000LJ\u0001\u0000\u0000\u0000LM\u0001\u0000\u0000"+
		"\u0000MO\u0001\u0000\u0000\u0000NL\u0001\u0000\u0000\u0000OP\u0005\u0000"+
		"\u0000\u0001P\u0001\u0001\u0000\u0000\u0000QR\u0003\u0004\u0002\u0000"+
		"R\u0003\u0001\u0000\u0000\u0000SZ\u00036\u001b\u0000TZ\u00032\u0019\u0000"+
		"UZ\u0003\u0018\f\u0000VZ\u0003\f\u0006\u0000WZ\u0003\u0012\t\u0000XZ\u0003"+
		"\u0006\u0003\u0000YS\u0001\u0000\u0000\u0000YT\u0001\u0000\u0000\u0000"+
		"YU\u0001\u0000\u0000\u0000YV\u0001\u0000\u0000\u0000YW\u0001\u0000\u0000"+
		"\u0000YX\u0001\u0000\u0000\u0000Z\u0005\u0001\u0000\u0000\u0000[]\u0007"+
		"\u0000\u0000\u0000\\[\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000\u0000"+
		"^\\\u0001\u0000\u0000\u0000^_\u0001\u0000\u0000\u0000_\u0007\u0001\u0000"+
		"\u0000\u0000`b\u0003\n\u0005\u0000a`\u0001\u0000\u0000\u0000ab\u0001\u0000"+
		"\u0000\u0000bc\u0001\u0000\u0000\u0000cd\u0003\f\u0006\u0000d\t\u0001"+
		"\u0000\u0000\u0000ef\u0005\u0001\u0000\u0000f\u000b\u0001\u0000\u0000"+
		"\u0000gh\u0004\u0006\u0000\u0000hl\u0003\u000e\u0007\u0000ik\u0003\u0002"+
		"\u0001\u0000ji\u0001\u0000\u0000\u0000kn\u0001\u0000\u0000\u0000lj\u0001"+
		"\u0000\u0000\u0000lm\u0001\u0000\u0000\u0000mo\u0001\u0000\u0000\u0000"+
		"nl\u0001\u0000\u0000\u0000op\u0003\u0010\b\u0000p\r\u0001\u0000\u0000"+
		"\u0000qr\u0005\b\u0000\u0000rv\u0005\f\u0000\u0000su\u0003\u0014\n\u0000"+
		"ts\u0001\u0000\u0000\u0000ux\u0001\u0000\u0000\u0000vt\u0001\u0000\u0000"+
		"\u0000vw\u0001\u0000\u0000\u0000wy\u0001\u0000\u0000\u0000xv\u0001\u0000"+
		"\u0000\u0000yz\u0005\u0012\u0000\u0000z\u000f\u0001\u0000\u0000\u0000"+
		"{|\u0005\b\u0000\u0000|}\u0005\r\u0000\u0000}~\u0005\f\u0000\u0000~\u007f"+
		"\u0005\u0012\u0000\u0000\u007f\u0011\u0001\u0000\u0000\u0000\u0080\u0081"+
		"\u0005\b\u0000\u0000\u0081\u0085\u0005\f\u0000\u0000\u0082\u0084\u0003"+
		"\u0014\n\u0000\u0083\u0082\u0001\u0000\u0000\u0000\u0084\u0087\u0001\u0000"+
		"\u0000\u0000\u0085\u0083\u0001\u0000\u0000\u0000\u0085\u0086\u0001\u0000"+
		"\u0000\u0000\u0086\u008b\u0001\u0000\u0000\u0000\u0087\u0085\u0001\u0000"+
		"\u0000\u0000\u0088\u0089\u0005\r\u0000\u0000\u0089\u008c\u0005\u0012\u0000"+
		"\u0000\u008a\u008c\u0005\u0012\u0000\u0000\u008b\u0088\u0001\u0000\u0000"+
		"\u0000\u008b\u008a\u0001\u0000\u0000\u0000\u008c\u0013\u0001\u0000\u0000"+
		"\u0000\u008d\u0090\u0005\f\u0000\u0000\u008e\u008f\u0005\u000e\u0000\u0000"+
		"\u008f\u0091\u0003\u0016\u000b\u0000\u0090\u008e\u0001\u0000\u0000\u0000"+
		"\u0090\u0091\u0001\u0000\u0000\u0000\u0091\u0015\u0001\u0000\u0000\u0000"+
		"\u0092\u0096\u0005\u000f\u0000\u0000\u0093\u0096\u0005\u0010\u0000\u0000"+
		"\u0094\u0096\u0005\u0011\u0000\u0000\u0095\u0092\u0001\u0000\u0000\u0000"+
		"\u0095\u0093\u0001\u0000\u0000\u0000\u0095\u0094\u0001\u0000\u0000\u0000"+
		"\u0096\u0017\u0001\u0000\u0000\u0000\u0097\u009b\u0005\u0004\u0000\u0000"+
		"\u0098\u009a\u0003\u001a\r\u0000\u0099\u0098\u0001\u0000\u0000\u0000\u009a"+
		"\u009d\u0001\u0000\u0000\u0000\u009b\u0099\u0001\u0000\u0000\u0000\u009b"+
		"\u009c\u0001\u0000\u0000\u0000\u009c\u009e\u0001\u0000\u0000\u0000\u009d"+
		"\u009b\u0001\u0000\u0000\u0000\u009e\u009f\u0005\u0014\u0000\u0000\u009f"+
		"\u0019\u0001\u0000\u0000\u0000\u00a0\u00a1\u0003\u001c\u000e\u0000\u00a1"+
		"\u00a2\u0005\u0015\u0000\u0000\u00a2\u00a3\u0003$\u0012\u0000\u00a3\u00a4"+
		"\u0005\u0016\u0000\u0000\u00a4\u001b\u0001\u0000\u0000\u0000\u00a5\u00b0"+
		"\u0003\u001e\u000f\u0000\u00a6\u00aa\u0005\u0019\u0000\u0000\u00a7\u00a9"+
		"\u0005.\u0000\u0000\u00a8\u00a7\u0001\u0000\u0000\u0000\u00a9\u00ac\u0001"+
		"\u0000\u0000\u0000\u00aa\u00a8\u0001\u0000\u0000\u0000\u00aa\u00ab\u0001"+
		"\u0000\u0000\u0000\u00ab\u00ad\u0001\u0000\u0000\u0000\u00ac\u00aa\u0001"+
		"\u0000\u0000\u0000\u00ad\u00af\u0003\u001e\u000f\u0000\u00ae\u00a6\u0001"+
		"\u0000\u0000\u0000\u00af\u00b2\u0001\u0000\u0000\u0000\u00b0\u00ae\u0001"+
		"\u0000\u0000\u0000\u00b0\u00b1\u0001\u0000\u0000\u0000\u00b1\u001d\u0001"+
		"\u0000\u0000\u0000\u00b2\u00b0\u0001\u0000\u0000\u0000\u00b3\u00bd\u0003"+
		" \u0010\u0000\u00b4\u00b6\u0005.\u0000\u0000\u00b5\u00b4\u0001\u0000\u0000"+
		"\u0000\u00b6\u00b9\u0001\u0000\u0000\u0000\u00b7\u00b5\u0001\u0000\u0000"+
		"\u0000\u00b7\u00b8\u0001\u0000\u0000\u0000\u00b8\u00ba\u0001\u0000\u0000"+
		"\u0000\u00b9\u00b7\u0001\u0000\u0000\u0000\u00ba\u00bc\u0003 \u0010\u0000"+
		"\u00bb\u00b7\u0001\u0000\u0000\u0000\u00bc\u00bf\u0001\u0000\u0000\u0000"+
		"\u00bd\u00bb\u0001\u0000\u0000\u0000\u00bd\u00be\u0001\u0000\u0000\u0000"+
		"\u00be\u001f\u0001\u0000\u0000\u0000\u00bf\u00bd\u0001\u0000\u0000\u0000"+
		"\u00c0\u00d4\u0005\u001c\u0000\u0000\u00c1\u00c2\u0005\u001b\u0000\u0000"+
		"\u00c2\u00d4\u0005+\u0000\u0000\u00c3\u00c4\u0005\u001a\u0000\u0000\u00c4"+
		"\u00d4\u0005+\u0000\u0000\u00c5\u00d4\u0005+\u0000\u0000\u00c6\u00c7\u0005"+
		"\u0017\u0000\u0000\u00c7\u00d4\u0005+\u0000\u0000\u00c8\u00c9\u0005!\u0000"+
		"\u0000\u00c9\u00d4\u0005+\u0000\u0000\u00ca\u00cb\u0005\u001f\u0000\u0000"+
		"\u00cb\u00cc\u0003\"\u0011\u0000\u00cc\u00cd\u0005 \u0000\u0000\u00cd"+
		"\u00d4\u0001\u0000\u0000\u0000\u00ce\u00cf\u0005\u0017\u0000\u0000\u00cf"+
		"\u00d0\u0005+\u0000\u0000\u00d0\u00d1\u0005\u001d\u0000\u0000\u00d1\u00d2"+
		"\u0007\u0001\u0000\u0000\u00d2\u00d4\u0005\u001e\u0000\u0000\u00d3\u00c0"+
		"\u0001\u0000\u0000\u0000\u00d3\u00c1\u0001\u0000\u0000\u0000\u00d3\u00c3"+
		"\u0001\u0000\u0000\u0000\u00d3\u00c5\u0001\u0000\u0000\u0000\u00d3\u00c6"+
		"\u0001\u0000\u0000\u0000\u00d3\u00c8\u0001\u0000\u0000\u0000\u00d3\u00ca"+
		"\u0001\u0000\u0000\u0000\u00d3\u00ce\u0001\u0000\u0000\u0000\u00d4!\u0001"+
		"\u0000\u0000\u0000\u00d5\u00da\u0005+\u0000\u0000\u00d6\u00d7\u0005+\u0000"+
		"\u0000\u00d7\u00d8\u0007\u0002\u0000\u0000\u00d8\u00da\u0005,\u0000\u0000"+
		"\u00d9\u00d5\u0001\u0000\u0000\u0000\u00d9\u00d6\u0001\u0000\u0000\u0000"+
		"\u00da#\u0001\u0000\u0000\u0000\u00db\u00dd\u0003&\u0013\u0000\u00dc\u00db"+
		"\u0001\u0000\u0000\u0000\u00dd\u00e0\u0001\u0000\u0000\u0000\u00de\u00dc"+
		"\u0001\u0000\u0000\u0000\u00de\u00df\u0001\u0000\u0000\u0000\u00df%\u0001"+
		"\u0000\u0000\u0000\u00e0\u00de\u0001\u0000\u0000\u0000\u00e1\u00e2\u0005"+
		"+\u0000\u0000\u00e2\u00e3\u0005\u0017\u0000\u0000\u00e3\u00e4\u0003(\u0014"+
		"\u0000\u00e4\u00e5\u0005\u0018\u0000\u0000\u00e5\'\u0001\u0000\u0000\u0000"+
		"\u00e6\u00f0\u0003*\u0015\u0000\u00e7\u00e9\u0005.\u0000\u0000\u00e8\u00e7"+
		"\u0001\u0000\u0000\u0000\u00e9\u00ec\u0001\u0000\u0000\u0000\u00ea\u00e8"+
		"\u0001\u0000\u0000\u0000\u00ea\u00eb\u0001\u0000\u0000\u0000\u00eb\u00ed"+
		"\u0001\u0000\u0000\u0000\u00ec\u00ea\u0001\u0000\u0000\u0000\u00ed\u00ef"+
		"\u0003*\u0015\u0000\u00ee\u00ea\u0001\u0000\u0000\u0000\u00ef\u00f2\u0001"+
		"\u0000\u0000\u0000\u00f0\u00ee\u0001\u0000\u0000\u0000\u00f0\u00f1\u0001"+
		"\u0000\u0000\u0000\u00f1)\u0001\u0000\u0000\u0000\u00f2\u00f0\u0001\u0000"+
		"\u0000\u0000\u00f3\u0107\u0005,\u0000\u0000\u00f4\u00f6\u0003,\u0016\u0000"+
		"\u00f5\u00f7\u0005)\u0000\u0000\u00f6\u00f5\u0001\u0000\u0000\u0000\u00f6"+
		"\u00f7\u0001\u0000\u0000\u0000\u00f7\u0107\u0001\u0000\u0000\u0000\u00f8"+
		"\u0107\u0005+\u0000\u0000\u00f9\u0107\u0005*\u0000\u0000\u00fa\u0107\u0003"+
		".\u0017\u0000\u00fb\u0107\u0005-\u0000\u0000\u00fc\u0107\u0005\u001b\u0000"+
		"\u0000\u00fd\u0107\u0005\u0019\u0000\u0000\u00fe\u0107\u0005\u001d\u0000"+
		"\u0000\u00ff\u0107\u0005\u001e\u0000\u0000\u0100\u0107\u0005\u001f\u0000"+
		"\u0000\u0101\u0107\u0005 \u0000\u0000\u0102\u0107\u0005\u001a\u0000\u0000"+
		"\u0103\u0107\u0005\u001c\u0000\u0000\u0104\u0107\u0005\u0017\u0000\u0000"+
		"\u0105\u0107\u0005/\u0000\u0000\u0106\u00f3\u0001\u0000\u0000\u0000\u0106"+
		"\u00f4\u0001\u0000\u0000\u0000\u0106\u00f8\u0001\u0000\u0000\u0000\u0106"+
		"\u00f9\u0001\u0000\u0000\u0000\u0106\u00fa\u0001\u0000\u0000\u0000\u0106"+
		"\u00fb\u0001\u0000\u0000\u0000\u0106\u00fc\u0001\u0000\u0000\u0000\u0106"+
		"\u00fd\u0001\u0000\u0000\u0000\u0106\u00fe\u0001\u0000\u0000\u0000\u0106"+
		"\u00ff\u0001\u0000\u0000\u0000\u0106\u0100\u0001\u0000\u0000\u0000\u0106"+
		"\u0101\u0001\u0000\u0000\u0000\u0106\u0102\u0001\u0000\u0000\u0000\u0106"+
		"\u0103\u0001\u0000\u0000\u0000\u0106\u0104\u0001\u0000\u0000\u0000\u0106"+
		"\u0105\u0001\u0000\u0000\u0000\u0107+\u0001\u0000\u0000\u0000\u0108\u010b"+
		"\u0005(\u0000\u0000\u0109\u010b\u0005\'\u0000\u0000\u010a\u0108\u0001"+
		"\u0000\u0000\u0000\u010a\u0109\u0001\u0000\u0000\u0000\u010b-\u0001\u0000"+
		"\u0000\u0000\u010c\u010d\u0005+\u0000\u0000\u010d\u010f\u0005\u001d\u0000"+
		"\u0000\u010e\u0110\u00030\u0018\u0000\u010f\u010e\u0001\u0000\u0000\u0000"+
		"\u010f\u0110\u0001\u0000\u0000\u0000\u0110\u0111\u0001\u0000\u0000\u0000"+
		"\u0111\u0112\u0005\u001e\u0000\u0000\u0112/\u0001\u0000\u0000\u0000\u0113"+
		"\u0118\u0003*\u0015\u0000\u0114\u0115\u0005\u0019\u0000\u0000\u0115\u0117"+
		"\u0003*\u0015\u0000\u0116\u0114\u0001\u0000\u0000\u0000\u0117\u011a\u0001"+
		"\u0000\u0000\u0000\u0118\u0116\u0001\u0000\u0000\u0000\u0118\u0119\u0001"+
		"\u0000\u0000\u0000\u01191\u0001\u0000\u0000\u0000\u011a\u0118\u0001\u0000"+
		"\u0000\u0000\u011b\u011c\u0005\u0006\u0000\u0000\u011c\u011d\u00034\u001a"+
		"\u0000\u011d\u011e\u0005M\u0000\u0000\u011e3\u0001\u0000\u0000\u0000\u011f"+
		"\u0121\u0007\u0003\u0000\u0000\u0120\u011f\u0001\u0000\u0000\u0000\u0121"+
		"\u0124\u0001\u0000\u0000\u0000\u0122\u0120\u0001\u0000\u0000\u0000\u0122"+
		"\u0123\u0001\u0000\u0000\u0000\u01235\u0001\u0000\u0000\u0000\u0124\u0122"+
		"\u0001\u0000\u0000\u0000\u0125\u0128\u0003<\u001e\u0000\u0126\u0128\u0003"+
		"8\u001c\u0000\u0127\u0125\u0001\u0000\u0000\u0000\u0127\u0126\u0001\u0000"+
		"\u0000\u0000\u01287\u0001\u0000\u0000\u0000\u0129\u012a\u0005\u0005\u0000"+
		"\u0000\u012a\u012b\u0003>\u001f\u0000\u012b\u012c\u00050\u0000\u0000\u012c"+
		"9\u0001\u0000\u0000\u0000\u012d\u012f\u0007\u0004\u0000\u0000\u012e\u012d"+
		"\u0001\u0000\u0000\u0000\u012f\u0130\u0001\u0000\u0000\u0000\u0130\u012e"+
		"\u0001\u0000\u0000\u0000\u0130\u0131\u0001\u0000\u0000\u0000\u0131;\u0001"+
		"\u0000\u0000\u0000\u0132\u0133\u0005\u0005\u0000\u0000\u0133\u0134\u0003"+
		"@ \u0000\u0134\u0138\u00050\u0000\u0000\u0135\u0137\u0003\u0002\u0001"+
		"\u0000\u0136\u0135\u0001\u0000\u0000\u0000\u0137\u013a\u0001\u0000\u0000"+
		"\u0000\u0138\u0136\u0001\u0000\u0000\u0000\u0138\u0139\u0001\u0000\u0000"+
		"\u0000\u0139\u013b\u0001\u0000\u0000\u0000\u013a\u0138\u0001\u0000\u0000"+
		"\u0000\u013b\u013c\u0005\u0005\u0000\u0000\u013c\u013d\u0003B!\u0000\u013d"+
		"\u013e\u00050\u0000\u0000\u013e=\u0001\u0000\u0000\u0000\u013f\u0147\u0003"+
		"D\"\u0000\u0140\u0147\u0005G\u0000\u0000\u0141\u0147\u0005H\u0000\u0000"+
		"\u0142\u0147\u0005I\u0000\u0000\u0143\u0147\u0005J\u0000\u0000\u0144\u0147"+
		"\u0005K\u0000\u0000\u0145\u0147\u0005L\u0000\u0000\u0146\u013f\u0001\u0000"+
		"\u0000\u0000\u0146\u0140\u0001\u0000\u0000\u0000\u0146\u0141\u0001\u0000"+
		"\u0000\u0000\u0146\u0142\u0001\u0000\u0000\u0000\u0146\u0143\u0001\u0000"+
		"\u0000\u0000\u0146\u0144\u0001\u0000\u0000\u0000\u0146\u0145\u0001\u0000"+
		"\u0000\u0000\u0147\u014a\u0001\u0000\u0000\u0000\u0148\u0146\u0001\u0000"+
		"\u0000\u0000\u0148\u0149\u0001\u0000\u0000\u0000\u0149?\u0001\u0000\u0000"+
		"\u0000\u014a\u0148\u0001\u0000\u0000\u0000\u014b\u014c\u00055\u0000\u0000"+
		"\u014c\u015a\u0003>\u001f\u0000\u014d\u014e\u00051\u0000\u0000\u014e\u015a"+
		"\u0003>\u001f\u0000\u014f\u0150\u00057\u0000\u0000\u0150\u015a\u0005G"+
		"\u0000\u0000\u0151\u0152\u00059\u0000\u0000\u0152\u015a\u0005G\u0000\u0000"+
		"\u0153\u0155\u00052\u0000\u0000\u0154\u0156\u0003>\u001f\u0000\u0155\u0154"+
		"\u0001\u0000\u0000\u0000\u0155\u0156\u0001\u0000\u0000\u0000\u0156\u015a"+
		"\u0001\u0000\u0000\u0000\u0157\u0158\u00053\u0000\u0000\u0158\u015a\u0003"+
		">\u001f\u0000\u0159\u014b\u0001\u0000\u0000\u0000\u0159\u014d\u0001\u0000"+
		"\u0000\u0000\u0159\u014f\u0001\u0000\u0000\u0000\u0159\u0151\u0001\u0000"+
		"\u0000\u0000\u0159\u0153\u0001\u0000\u0000\u0000\u0159\u0157\u0001\u0000"+
		"\u0000\u0000\u015aA\u0001\u0000\u0000\u0000\u015b\u0160\u00054\u0000\u0000"+
		"\u015c\u0160\u00056\u0000\u0000\u015d\u0160\u00058\u0000\u0000\u015e\u0160"+
		"\u0005:\u0000\u0000\u015f\u015b\u0001\u0000\u0000\u0000\u015f\u015c\u0001"+
		"\u0000\u0000\u0000\u015f\u015d\u0001\u0000\u0000\u0000\u015f\u015e\u0001"+
		"\u0000\u0000\u0000\u0160C\u0001\u0000\u0000\u0000\u0161\u0178\u00051\u0000"+
		"\u0000\u0162\u0178\u00052\u0000\u0000\u0163\u0178\u00053\u0000\u0000\u0164"+
		"\u0178\u00054\u0000\u0000\u0165\u0178\u00055\u0000\u0000\u0166\u0178\u0005"+
		"6\u0000\u0000\u0167\u0178\u00057\u0000\u0000\u0168\u0178\u00058\u0000"+
		"\u0000\u0169\u0178\u00059\u0000\u0000\u016a\u0178\u0005:\u0000\u0000\u016b"+
		"\u0178\u0005;\u0000\u0000\u016c\u0178\u0005<\u0000\u0000\u016d\u0178\u0005"+
		"=\u0000\u0000\u016e\u0178\u0005>\u0000\u0000\u016f\u0178\u0005?\u0000"+
		"\u0000\u0170\u0178\u0005@\u0000\u0000\u0171\u0178\u0005A\u0000\u0000\u0172"+
		"\u0178\u0005B\u0000\u0000\u0173\u0178\u0005C\u0000\u0000\u0174\u0178\u0005"+
		"D\u0000\u0000\u0175\u0178\u0005E\u0000\u0000\u0176\u0178\u0005F\u0000"+
		"\u0000\u0177\u0161\u0001\u0000\u0000\u0000\u0177\u0162\u0001\u0000\u0000"+
		"\u0000\u0177\u0163\u0001\u0000\u0000\u0000\u0177\u0164\u0001\u0000\u0000"+
		"\u0000\u0177\u0165\u0001\u0000\u0000\u0000\u0177\u0166\u0001\u0000\u0000"+
		"\u0000\u0177\u0167\u0001\u0000\u0000\u0000\u0177\u0168\u0001\u0000\u0000"+
		"\u0000\u0177\u0169\u0001\u0000\u0000\u0000\u0177\u016a\u0001\u0000\u0000"+
		"\u0000\u0177\u016b\u0001\u0000\u0000\u0000\u0177\u016c\u0001\u0000\u0000"+
		"\u0000\u0177\u016d\u0001\u0000\u0000\u0000\u0177\u016e\u0001\u0000\u0000"+
		"\u0000\u0177\u016f\u0001\u0000\u0000\u0000\u0177\u0170\u0001\u0000\u0000"+
		"\u0000\u0177\u0171\u0001\u0000\u0000\u0000\u0177\u0172\u0001\u0000\u0000"+
		"\u0000\u0177\u0173\u0001\u0000\u0000\u0000\u0177\u0174\u0001\u0000\u0000"+
		"\u0000\u0177\u0175\u0001\u0000\u0000\u0000\u0177\u0176\u0001\u0000\u0000"+
		"\u0000\u0178E\u0001\u0000\u0000\u0000$GLY^alv\u0085\u008b\u0090\u0095"+
		"\u009b\u00aa\u00b0\u00b7\u00bd\u00d3\u00d9\u00de\u00ea\u00f0\u00f6\u0106"+
		"\u010a\u010f\u0118\u0122\u0127\u0130\u0138\u0146\u0148\u0155\u0159\u015f"+
		"\u0177";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}