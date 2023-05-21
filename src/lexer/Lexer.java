package lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import parser.Parser;
import static lexer.TokenList.TOKEN_PATTERNS;

public class Lexer {

    private String text;

    public Lexer(String text) {
        this.text = text;
    }

    public List<Token> lex() throws Exception {
        List<Token> tokens = new ArrayList<Token>();
        int pos = 0;
        while (pos < text.length()) {
            Matcher matcher = null;
            for (TokenPattern pattern : TOKEN_PATTERNS) {
                matcher = pattern.getPattern().matcher(text);
                matcher.region(pos, text.length());
                if (matcher.lookingAt()) {
                    String matchedText = matcher.group();
                    if (pattern.getTokenType() != null) {
                        Token token = new Token(pattern.getTokenType(), matchedText);
                        tokens.add(token);
                    }
                    pos = matcher.end();
                    break;
                }
            }
            if (matcher != null && !matcher.lookingAt()) {
                throw new Exception("Unexpected character at position: " + matcher.start());
            }
        }
        tokens.add(new Token(null, ""));
        return tokens;
    }


}

