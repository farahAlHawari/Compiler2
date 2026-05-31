// Generated from D:/My_Projects/Forth year/Compiler2/src/main/pythoncompiler/grammer/PythonLexer.g4 by ANTLR 4.13.2
package main.pythoncompiler.grammer;

import java.util.Stack;
import java.util.Deque;
import java.util.ArrayDeque;
import org.antlr.v4.runtime.*;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class PythonLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		INDENT=1, DEDENT=2, FROM=3, IMPORT=4, DEF=5, RETURN=6, IF=7, ELIF=8, ELSE=9, 
		FOR=10, WHILE=11, IN=12, GLOBAL=13, CLASS=14, PASS=15, BREAK=16, CONTINUE=17, 
		WITH=18, TRY=19, EXCEPT=20, FINALLY=21, TRUE=22, FALSE=23, NONE=24, ASSIGN=25, 
		PLUS_ASSIGN=26, MINUS_ASSIGN=27, MULT_ASSIGN=28, DIV_ASSIGN=29, EQ=30, 
		NEQ=31, LT=32, GT=33, LE=34, GE=35, PLUS=36, MINUS=37, MULT=38, DIV=39, 
		MOD=40, AND=41, OR=42, NOT=43, LPAREN=44, RPAREN=45, LBRACK=46, RBRACK=47, 
		LBRACE=48, RBRACE=49, COLON=50, COMMA=51, DOT=52, AT=53, STRING=54, NUMBER=55, 
		IDENTIFIER=56, NEWLINE=57, WS=58, COMMENT=59;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"FROM", "IMPORT", "DEF", "RETURN", "IF", "ELIF", "ELSE", "FOR", "WHILE", 
			"IN", "GLOBAL", "CLASS", "PASS", "BREAK", "CONTINUE", "WITH", "TRY", 
			"EXCEPT", "FINALLY", "TRUE", "FALSE", "NONE", "ASSIGN", "PLUS_ASSIGN", 
			"MINUS_ASSIGN", "MULT_ASSIGN", "DIV_ASSIGN", "EQ", "NEQ", "LT", "GT", 
			"LE", "GE", "PLUS", "MINUS", "MULT", "DIV", "MOD", "AND", "OR", "NOT", 
			"LPAREN", "RPAREN", "LBRACK", "RBRACK", "LBRACE", "RBRACE", "COLON", 
			"COMMA", "DOT", "AT", "STRING", "NUMBER", "IDENTIFIER", "NEWLINE", "WS", 
			"COMMENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'from'", "'import'", "'def'", "'return'", "'if'", 
			"'elif'", "'else'", "'for'", "'while'", "'in'", "'global'", "'class'", 
			"'pass'", "'break'", "'continue'", "'with'", "'try'", "'except'", "'finally'", 
			"'True'", "'False'", "'None'", "'='", "'+='", "'-='", "'*='", "'/='", 
			"'=='", "'!='", "'<'", "'>'", "'<='", "'>='", "'+'", "'-'", "'*'", "'/'", 
			"'%'", "'and'", "'or'", "'not'", "'('", "')'", "'['", "']'", "'{'", "'}'", 
			"':'", "','", "'.'", "'@'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "INDENT", "DEDENT", "FROM", "IMPORT", "DEF", "RETURN", "IF", "ELIF", 
			"ELSE", "FOR", "WHILE", "IN", "GLOBAL", "CLASS", "PASS", "BREAK", "CONTINUE", 
			"WITH", "TRY", "EXCEPT", "FINALLY", "TRUE", "FALSE", "NONE", "ASSIGN", 
			"PLUS_ASSIGN", "MINUS_ASSIGN", "MULT_ASSIGN", "DIV_ASSIGN", "EQ", "NEQ", 
			"LT", "GT", "LE", "GE", "PLUS", "MINUS", "MULT", "DIV", "MOD", "AND", 
			"OR", "NOT", "LPAREN", "RPAREN", "LBRACK", "RBRACK", "LBRACE", "RBRACE", 
			"COLON", "COMMA", "DOT", "AT", "STRING", "NUMBER", "IDENTIFIER", "NEWLINE", 
			"WS", "COMMENT"
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


	public PythonLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PythonLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 41:
			LPAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 42:
			RPAREN_action((RuleContext)_localctx, actionIndex);
			break;
		case 43:
			LBRACK_action((RuleContext)_localctx, actionIndex);
			break;
		case 44:
			RBRACK_action((RuleContext)_localctx, actionIndex);
			break;
		case 45:
			LBRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 46:
			RBRACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 54:
			NEWLINE_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void LPAREN_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			opened++;
			break;
		}
	}
	private void RPAREN_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			opened--;
			break;
		}
	}
	private void LBRACK_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:
			opened++;
			break;
		}
	}
	private void RBRACK_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:
			opened--;
			break;
		}
	}
	private void LBRACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 4:
			opened++;
			break;
		}
	}
	private void RBRACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 5:
			opened--;
			break;
		}
	}
	private void NEWLINE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 6:

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
			                
			break;
		}
	}

	public static final String _serializedATN =
		"\u0004\u0000;\u0172\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002"+
		"\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002"+
		"\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002"+
		"\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002"+
		"\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002"+
		"\u001e\u0007\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007"+
		"!\u0002\"\u0007\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007"+
		"&\u0002\'\u0007\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007"+
		"+\u0002,\u0007,\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u0007"+
		"0\u00021\u00071\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u0007"+
		"5\u00026\u00076\u00027\u00077\u00028\u00078\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001"+
		"\u001d\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0001"+
		" \u0001 \u0001 \u0001!\u0001!\u0001\"\u0001\"\u0001#\u0001#\u0001$\u0001"+
		"$\u0001%\u0001%\u0001&\u0001&\u0001&\u0001&\u0001\'\u0001\'\u0001\'\u0001"+
		"(\u0001(\u0001(\u0001(\u0001)\u0001)\u0001)\u0001*\u0001*\u0001*\u0001"+
		"+\u0001+\u0001+\u0001,\u0001,\u0001,\u0001-\u0001-\u0001-\u0001.\u0001"+
		".\u0001.\u0001/\u0001/\u00010\u00010\u00011\u00011\u00012\u00012\u0001"+
		"3\u00013\u00053\u013d\b3\n3\f3\u0140\t3\u00013\u00013\u00013\u00053\u0145"+
		"\b3\n3\f3\u0148\t3\u00013\u00033\u014b\b3\u00014\u00044\u014e\b4\u000b"+
		"4\f4\u014f\u00015\u00015\u00055\u0154\b5\n5\f5\u0157\t5\u00016\u00036"+
		"\u015a\b6\u00016\u00046\u015d\b6\u000b6\f6\u015e\u00016\u00016\u00017"+
		"\u00047\u0164\b7\u000b7\f7\u0165\u00017\u00017\u00018\u00018\u00058\u016c"+
		"\b8\n8\f8\u016f\t8\u00018\u00018\u0000\u00009\u0001\u0003\u0003\u0004"+
		"\u0005\u0005\u0007\u0006\t\u0007\u000b\b\r\t\u000f\n\u0011\u000b\u0013"+
		"\f\u0015\r\u0017\u000e\u0019\u000f\u001b\u0010\u001d\u0011\u001f\u0012"+
		"!\u0013#\u0014%\u0015\'\u0016)\u0017+\u0018-\u0019/\u001a1\u001b3\u001c"+
		"5\u001d7\u001e9\u001f; =!?\"A#C$E%G&I\'K(M)O*Q+S,U-W.Y/[0]1_2a3c4e5g6"+
		"i7k8m9o:q;\u0001\u0000\u0007\u0003\u0000\n\n\r\r\"\"\u0003\u0000\n\n\r"+
		"\r\'\'\u0001\u000009\u0003\u0000AZ__az\u0004\u000009AZ__az\u0002\u0000"+
		"\t\t  \u0002\u0000\n\n\r\r\u017a\u0000\u0001\u0001\u0000\u0000\u0000\u0000"+
		"\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000"+
		"\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b"+
		"\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001"+
		"\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001"+
		"\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001"+
		"\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001"+
		"\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001"+
		"\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000"+
		"\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000"+
		"\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000-"+
		"\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000\u0000\u00001\u0001\u0000"+
		"\u0000\u0000\u00003\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000\u0000"+
		"\u00007\u0001\u0000\u0000\u0000\u00009\u0001\u0000\u0000\u0000\u0000;"+
		"\u0001\u0000\u0000\u0000\u0000=\u0001\u0000\u0000\u0000\u0000?\u0001\u0000"+
		"\u0000\u0000\u0000A\u0001\u0000\u0000\u0000\u0000C\u0001\u0000\u0000\u0000"+
		"\u0000E\u0001\u0000\u0000\u0000\u0000G\u0001\u0000\u0000\u0000\u0000I"+
		"\u0001\u0000\u0000\u0000\u0000K\u0001\u0000\u0000\u0000\u0000M\u0001\u0000"+
		"\u0000\u0000\u0000O\u0001\u0000\u0000\u0000\u0000Q\u0001\u0000\u0000\u0000"+
		"\u0000S\u0001\u0000\u0000\u0000\u0000U\u0001\u0000\u0000\u0000\u0000W"+
		"\u0001\u0000\u0000\u0000\u0000Y\u0001\u0000\u0000\u0000\u0000[\u0001\u0000"+
		"\u0000\u0000\u0000]\u0001\u0000\u0000\u0000\u0000_\u0001\u0000\u0000\u0000"+
		"\u0000a\u0001\u0000\u0000\u0000\u0000c\u0001\u0000\u0000\u0000\u0000e"+
		"\u0001\u0000\u0000\u0000\u0000g\u0001\u0000\u0000\u0000\u0000i\u0001\u0000"+
		"\u0000\u0000\u0000k\u0001\u0000\u0000\u0000\u0000m\u0001\u0000\u0000\u0000"+
		"\u0000o\u0001\u0000\u0000\u0000\u0000q\u0001\u0000\u0000\u0000\u0001s"+
		"\u0001\u0000\u0000\u0000\u0003x\u0001\u0000\u0000\u0000\u0005\u007f\u0001"+
		"\u0000\u0000\u0000\u0007\u0083\u0001\u0000\u0000\u0000\t\u008a\u0001\u0000"+
		"\u0000\u0000\u000b\u008d\u0001\u0000\u0000\u0000\r\u0092\u0001\u0000\u0000"+
		"\u0000\u000f\u0097\u0001\u0000\u0000\u0000\u0011\u009b\u0001\u0000\u0000"+
		"\u0000\u0013\u00a1\u0001\u0000\u0000\u0000\u0015\u00a4\u0001\u0000\u0000"+
		"\u0000\u0017\u00ab\u0001\u0000\u0000\u0000\u0019\u00b1\u0001\u0000\u0000"+
		"\u0000\u001b\u00b6\u0001\u0000\u0000\u0000\u001d\u00bc\u0001\u0000\u0000"+
		"\u0000\u001f\u00c5\u0001\u0000\u0000\u0000!\u00ca\u0001\u0000\u0000\u0000"+
		"#\u00ce\u0001\u0000\u0000\u0000%\u00d5\u0001\u0000\u0000\u0000\'\u00dd"+
		"\u0001\u0000\u0000\u0000)\u00e2\u0001\u0000\u0000\u0000+\u00e8\u0001\u0000"+
		"\u0000\u0000-\u00ed\u0001\u0000\u0000\u0000/\u00ef\u0001\u0000\u0000\u0000"+
		"1\u00f2\u0001\u0000\u0000\u00003\u00f5\u0001\u0000\u0000\u00005\u00f8"+
		"\u0001\u0000\u0000\u00007\u00fb\u0001\u0000\u0000\u00009\u00fe\u0001\u0000"+
		"\u0000\u0000;\u0101\u0001\u0000\u0000\u0000=\u0103\u0001\u0000\u0000\u0000"+
		"?\u0105\u0001\u0000\u0000\u0000A\u0108\u0001\u0000\u0000\u0000C\u010b"+
		"\u0001\u0000\u0000\u0000E\u010d\u0001\u0000\u0000\u0000G\u010f\u0001\u0000"+
		"\u0000\u0000I\u0111\u0001\u0000\u0000\u0000K\u0113\u0001\u0000\u0000\u0000"+
		"M\u0115\u0001\u0000\u0000\u0000O\u0119\u0001\u0000\u0000\u0000Q\u011c"+
		"\u0001\u0000\u0000\u0000S\u0120\u0001\u0000\u0000\u0000U\u0123\u0001\u0000"+
		"\u0000\u0000W\u0126\u0001\u0000\u0000\u0000Y\u0129\u0001\u0000\u0000\u0000"+
		"[\u012c\u0001\u0000\u0000\u0000]\u012f\u0001\u0000\u0000\u0000_\u0132"+
		"\u0001\u0000\u0000\u0000a\u0134\u0001\u0000\u0000\u0000c\u0136\u0001\u0000"+
		"\u0000\u0000e\u0138\u0001\u0000\u0000\u0000g\u014a\u0001\u0000\u0000\u0000"+
		"i\u014d\u0001\u0000\u0000\u0000k\u0151\u0001\u0000\u0000\u0000m\u015c"+
		"\u0001\u0000\u0000\u0000o\u0163\u0001\u0000\u0000\u0000q\u0169\u0001\u0000"+
		"\u0000\u0000st\u0005f\u0000\u0000tu\u0005r\u0000\u0000uv\u0005o\u0000"+
		"\u0000vw\u0005m\u0000\u0000w\u0002\u0001\u0000\u0000\u0000xy\u0005i\u0000"+
		"\u0000yz\u0005m\u0000\u0000z{\u0005p\u0000\u0000{|\u0005o\u0000\u0000"+
		"|}\u0005r\u0000\u0000}~\u0005t\u0000\u0000~\u0004\u0001\u0000\u0000\u0000"+
		"\u007f\u0080\u0005d\u0000\u0000\u0080\u0081\u0005e\u0000\u0000\u0081\u0082"+
		"\u0005f\u0000\u0000\u0082\u0006\u0001\u0000\u0000\u0000\u0083\u0084\u0005"+
		"r\u0000\u0000\u0084\u0085\u0005e\u0000\u0000\u0085\u0086\u0005t\u0000"+
		"\u0000\u0086\u0087\u0005u\u0000\u0000\u0087\u0088\u0005r\u0000\u0000\u0088"+
		"\u0089\u0005n\u0000\u0000\u0089\b\u0001\u0000\u0000\u0000\u008a\u008b"+
		"\u0005i\u0000\u0000\u008b\u008c\u0005f\u0000\u0000\u008c\n\u0001\u0000"+
		"\u0000\u0000\u008d\u008e\u0005e\u0000\u0000\u008e\u008f\u0005l\u0000\u0000"+
		"\u008f\u0090\u0005i\u0000\u0000\u0090\u0091\u0005f\u0000\u0000\u0091\f"+
		"\u0001\u0000\u0000\u0000\u0092\u0093\u0005e\u0000\u0000\u0093\u0094\u0005"+
		"l\u0000\u0000\u0094\u0095\u0005s\u0000\u0000\u0095\u0096\u0005e\u0000"+
		"\u0000\u0096\u000e\u0001\u0000\u0000\u0000\u0097\u0098\u0005f\u0000\u0000"+
		"\u0098\u0099\u0005o\u0000\u0000\u0099\u009a\u0005r\u0000\u0000\u009a\u0010"+
		"\u0001\u0000\u0000\u0000\u009b\u009c\u0005w\u0000\u0000\u009c\u009d\u0005"+
		"h\u0000\u0000\u009d\u009e\u0005i\u0000\u0000\u009e\u009f\u0005l\u0000"+
		"\u0000\u009f\u00a0\u0005e\u0000\u0000\u00a0\u0012\u0001\u0000\u0000\u0000"+
		"\u00a1\u00a2\u0005i\u0000\u0000\u00a2\u00a3\u0005n\u0000\u0000\u00a3\u0014"+
		"\u0001\u0000\u0000\u0000\u00a4\u00a5\u0005g\u0000\u0000\u00a5\u00a6\u0005"+
		"l\u0000\u0000\u00a6\u00a7\u0005o\u0000\u0000\u00a7\u00a8\u0005b\u0000"+
		"\u0000\u00a8\u00a9\u0005a\u0000\u0000\u00a9\u00aa\u0005l\u0000\u0000\u00aa"+
		"\u0016\u0001\u0000\u0000\u0000\u00ab\u00ac\u0005c\u0000\u0000\u00ac\u00ad"+
		"\u0005l\u0000\u0000\u00ad\u00ae\u0005a\u0000\u0000\u00ae\u00af\u0005s"+
		"\u0000\u0000\u00af\u00b0\u0005s\u0000\u0000\u00b0\u0018\u0001\u0000\u0000"+
		"\u0000\u00b1\u00b2\u0005p\u0000\u0000\u00b2\u00b3\u0005a\u0000\u0000\u00b3"+
		"\u00b4\u0005s\u0000\u0000\u00b4\u00b5\u0005s\u0000\u0000\u00b5\u001a\u0001"+
		"\u0000\u0000\u0000\u00b6\u00b7\u0005b\u0000\u0000\u00b7\u00b8\u0005r\u0000"+
		"\u0000\u00b8\u00b9\u0005e\u0000\u0000\u00b9\u00ba\u0005a\u0000\u0000\u00ba"+
		"\u00bb\u0005k\u0000\u0000\u00bb\u001c\u0001\u0000\u0000\u0000\u00bc\u00bd"+
		"\u0005c\u0000\u0000\u00bd\u00be\u0005o\u0000\u0000\u00be\u00bf\u0005n"+
		"\u0000\u0000\u00bf\u00c0\u0005t\u0000\u0000\u00c0\u00c1\u0005i\u0000\u0000"+
		"\u00c1\u00c2\u0005n\u0000\u0000\u00c2\u00c3\u0005u\u0000\u0000\u00c3\u00c4"+
		"\u0005e\u0000\u0000\u00c4\u001e\u0001\u0000\u0000\u0000\u00c5\u00c6\u0005"+
		"w\u0000\u0000\u00c6\u00c7\u0005i\u0000\u0000\u00c7\u00c8\u0005t\u0000"+
		"\u0000\u00c8\u00c9\u0005h\u0000\u0000\u00c9 \u0001\u0000\u0000\u0000\u00ca"+
		"\u00cb\u0005t\u0000\u0000\u00cb\u00cc\u0005r\u0000\u0000\u00cc\u00cd\u0005"+
		"y\u0000\u0000\u00cd\"\u0001\u0000\u0000\u0000\u00ce\u00cf\u0005e\u0000"+
		"\u0000\u00cf\u00d0\u0005x\u0000\u0000\u00d0\u00d1\u0005c\u0000\u0000\u00d1"+
		"\u00d2\u0005e\u0000\u0000\u00d2\u00d3\u0005p\u0000\u0000\u00d3\u00d4\u0005"+
		"t\u0000\u0000\u00d4$\u0001\u0000\u0000\u0000\u00d5\u00d6\u0005f\u0000"+
		"\u0000\u00d6\u00d7\u0005i\u0000\u0000\u00d7\u00d8\u0005n\u0000\u0000\u00d8"+
		"\u00d9\u0005a\u0000\u0000\u00d9\u00da\u0005l\u0000\u0000\u00da\u00db\u0005"+
		"l\u0000\u0000\u00db\u00dc\u0005y\u0000\u0000\u00dc&\u0001\u0000\u0000"+
		"\u0000\u00dd\u00de\u0005T\u0000\u0000\u00de\u00df\u0005r\u0000\u0000\u00df"+
		"\u00e0\u0005u\u0000\u0000\u00e0\u00e1\u0005e\u0000\u0000\u00e1(\u0001"+
		"\u0000\u0000\u0000\u00e2\u00e3\u0005F\u0000\u0000\u00e3\u00e4\u0005a\u0000"+
		"\u0000\u00e4\u00e5\u0005l\u0000\u0000\u00e5\u00e6\u0005s\u0000\u0000\u00e6"+
		"\u00e7\u0005e\u0000\u0000\u00e7*\u0001\u0000\u0000\u0000\u00e8\u00e9\u0005"+
		"N\u0000\u0000\u00e9\u00ea\u0005o\u0000\u0000\u00ea\u00eb\u0005n\u0000"+
		"\u0000\u00eb\u00ec\u0005e\u0000\u0000\u00ec,\u0001\u0000\u0000\u0000\u00ed"+
		"\u00ee\u0005=\u0000\u0000\u00ee.\u0001\u0000\u0000\u0000\u00ef\u00f0\u0005"+
		"+\u0000\u0000\u00f0\u00f1\u0005=\u0000\u0000\u00f10\u0001\u0000\u0000"+
		"\u0000\u00f2\u00f3\u0005-\u0000\u0000\u00f3\u00f4\u0005=\u0000\u0000\u00f4"+
		"2\u0001\u0000\u0000\u0000\u00f5\u00f6\u0005*\u0000\u0000\u00f6\u00f7\u0005"+
		"=\u0000\u0000\u00f74\u0001\u0000\u0000\u0000\u00f8\u00f9\u0005/\u0000"+
		"\u0000\u00f9\u00fa\u0005=\u0000\u0000\u00fa6\u0001\u0000\u0000\u0000\u00fb"+
		"\u00fc\u0005=\u0000\u0000\u00fc\u00fd\u0005=\u0000\u0000\u00fd8\u0001"+
		"\u0000\u0000\u0000\u00fe\u00ff\u0005!\u0000\u0000\u00ff\u0100\u0005=\u0000"+
		"\u0000\u0100:\u0001\u0000\u0000\u0000\u0101\u0102\u0005<\u0000\u0000\u0102"+
		"<\u0001\u0000\u0000\u0000\u0103\u0104\u0005>\u0000\u0000\u0104>\u0001"+
		"\u0000\u0000\u0000\u0105\u0106\u0005<\u0000\u0000\u0106\u0107\u0005=\u0000"+
		"\u0000\u0107@\u0001\u0000\u0000\u0000\u0108\u0109\u0005>\u0000\u0000\u0109"+
		"\u010a\u0005=\u0000\u0000\u010aB\u0001\u0000\u0000\u0000\u010b\u010c\u0005"+
		"+\u0000\u0000\u010cD\u0001\u0000\u0000\u0000\u010d\u010e\u0005-\u0000"+
		"\u0000\u010eF\u0001\u0000\u0000\u0000\u010f\u0110\u0005*\u0000\u0000\u0110"+
		"H\u0001\u0000\u0000\u0000\u0111\u0112\u0005/\u0000\u0000\u0112J\u0001"+
		"\u0000\u0000\u0000\u0113\u0114\u0005%\u0000\u0000\u0114L\u0001\u0000\u0000"+
		"\u0000\u0115\u0116\u0005a\u0000\u0000\u0116\u0117\u0005n\u0000\u0000\u0117"+
		"\u0118\u0005d\u0000\u0000\u0118N\u0001\u0000\u0000\u0000\u0119\u011a\u0005"+
		"o\u0000\u0000\u011a\u011b\u0005r\u0000\u0000\u011bP\u0001\u0000\u0000"+
		"\u0000\u011c\u011d\u0005n\u0000\u0000\u011d\u011e\u0005o\u0000\u0000\u011e"+
		"\u011f\u0005t\u0000\u0000\u011fR\u0001\u0000\u0000\u0000\u0120\u0121\u0005"+
		"(\u0000\u0000\u0121\u0122\u0006)\u0000\u0000\u0122T\u0001\u0000\u0000"+
		"\u0000\u0123\u0124\u0005)\u0000\u0000\u0124\u0125\u0006*\u0001\u0000\u0125"+
		"V\u0001\u0000\u0000\u0000\u0126\u0127\u0005[\u0000\u0000\u0127\u0128\u0006"+
		"+\u0002\u0000\u0128X\u0001\u0000\u0000\u0000\u0129\u012a\u0005]\u0000"+
		"\u0000\u012a\u012b\u0006,\u0003\u0000\u012bZ\u0001\u0000\u0000\u0000\u012c"+
		"\u012d\u0005{\u0000\u0000\u012d\u012e\u0006-\u0004\u0000\u012e\\\u0001"+
		"\u0000\u0000\u0000\u012f\u0130\u0005}\u0000\u0000\u0130\u0131\u0006.\u0005"+
		"\u0000\u0131^\u0001\u0000\u0000\u0000\u0132\u0133\u0005:\u0000\u0000\u0133"+
		"`\u0001\u0000\u0000\u0000\u0134\u0135\u0005,\u0000\u0000\u0135b\u0001"+
		"\u0000\u0000\u0000\u0136\u0137\u0005.\u0000\u0000\u0137d\u0001\u0000\u0000"+
		"\u0000\u0138\u0139\u0005@\u0000\u0000\u0139f\u0001\u0000\u0000\u0000\u013a"+
		"\u013e\u0005\"\u0000\u0000\u013b\u013d\b\u0000\u0000\u0000\u013c\u013b"+
		"\u0001\u0000\u0000\u0000\u013d\u0140\u0001\u0000\u0000\u0000\u013e\u013c"+
		"\u0001\u0000\u0000\u0000\u013e\u013f\u0001\u0000\u0000\u0000\u013f\u0141"+
		"\u0001\u0000\u0000\u0000\u0140\u013e\u0001\u0000\u0000\u0000\u0141\u014b"+
		"\u0005\"\u0000\u0000\u0142\u0146\u0005\'\u0000\u0000\u0143\u0145\b\u0001"+
		"\u0000\u0000\u0144\u0143\u0001\u0000\u0000\u0000\u0145\u0148\u0001\u0000"+
		"\u0000\u0000\u0146\u0144\u0001\u0000\u0000\u0000\u0146\u0147\u0001\u0000"+
		"\u0000\u0000\u0147\u0149\u0001\u0000\u0000\u0000\u0148\u0146\u0001\u0000"+
		"\u0000\u0000\u0149\u014b\u0005\'\u0000\u0000\u014a\u013a\u0001\u0000\u0000"+
		"\u0000\u014a\u0142\u0001\u0000\u0000\u0000\u014bh\u0001\u0000\u0000\u0000"+
		"\u014c\u014e\u0007\u0002\u0000\u0000\u014d\u014c\u0001\u0000\u0000\u0000"+
		"\u014e\u014f\u0001\u0000\u0000\u0000\u014f\u014d\u0001\u0000\u0000\u0000"+
		"\u014f\u0150\u0001\u0000\u0000\u0000\u0150j\u0001\u0000\u0000\u0000\u0151"+
		"\u0155\u0007\u0003\u0000\u0000\u0152\u0154\u0007\u0004\u0000\u0000\u0153"+
		"\u0152\u0001\u0000\u0000\u0000\u0154\u0157\u0001\u0000\u0000\u0000\u0155"+
		"\u0153\u0001\u0000\u0000\u0000\u0155\u0156\u0001\u0000\u0000\u0000\u0156"+
		"l\u0001\u0000\u0000\u0000\u0157\u0155\u0001\u0000\u0000\u0000\u0158\u015a"+
		"\u0005\r\u0000\u0000\u0159\u0158\u0001\u0000\u0000\u0000\u0159\u015a\u0001"+
		"\u0000\u0000\u0000\u015a\u015b\u0001\u0000\u0000\u0000\u015b\u015d\u0005"+
		"\n\u0000\u0000\u015c\u0159\u0001\u0000\u0000\u0000\u015d\u015e\u0001\u0000"+
		"\u0000\u0000\u015e\u015c\u0001\u0000\u0000\u0000\u015e\u015f\u0001\u0000"+
		"\u0000\u0000\u015f\u0160\u0001\u0000\u0000\u0000\u0160\u0161\u00066\u0006"+
		"\u0000\u0161n\u0001\u0000\u0000\u0000\u0162\u0164\u0007\u0005\u0000\u0000"+
		"\u0163\u0162\u0001\u0000\u0000\u0000\u0164\u0165\u0001\u0000\u0000\u0000"+
		"\u0165\u0163\u0001\u0000\u0000\u0000\u0165\u0166\u0001\u0000\u0000\u0000"+
		"\u0166\u0167\u0001\u0000\u0000\u0000\u0167\u0168\u00067\u0007\u0000\u0168"+
		"p\u0001\u0000\u0000\u0000\u0169\u016d\u0005#\u0000\u0000\u016a\u016c\b"+
		"\u0006\u0000\u0000\u016b\u016a\u0001\u0000\u0000\u0000\u016c\u016f\u0001"+
		"\u0000\u0000\u0000\u016d\u016b\u0001\u0000\u0000\u0000\u016d\u016e\u0001"+
		"\u0000\u0000\u0000\u016e\u0170\u0001\u0000\u0000\u0000\u016f\u016d\u0001"+
		"\u0000\u0000\u0000\u0170\u0171\u00068\u0007\u0000\u0171r\u0001\u0000\u0000"+
		"\u0000\n\u0000\u013e\u0146\u014a\u014f\u0155\u0159\u015e\u0165\u016d\b"+
		"\u0001)\u0000\u0001*\u0001\u0001+\u0002\u0001,\u0003\u0001-\u0004\u0001"+
		".\u0005\u00016\u0006\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}