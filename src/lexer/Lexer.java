package lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static lexer.TokenList.TOKEN_PATTERNS;

public class Lexer {

    private String text;
    private int pos;

    public Lexer(String text) {
        this.text = text;
        this.pos = 0;
    }

    public List<Token> lex() throws Exception {
        List<Token> tokens = new ArrayList<>();
        while (pos < text.length()) {
            boolean matchFound = false;
            for (TokenPattern pattern : TOKEN_PATTERNS) {
                Matcher matcher = pattern.getPattern().matcher(text.substring(pos));
                if (matcher.find()) {
                    String value = matcher.group();
                    System.out.println(value);
                    Token token = new Token(pattern.getTokenType(), value);
                    tokens.add(token);
                    pos += matcher.end();
                    matchFound = true;
                    break;
                }
            }
            if (!matchFound) {
                throw new Exception("Invalid token at position " + pos);
            }
        }
        return tokens;
    }

}

