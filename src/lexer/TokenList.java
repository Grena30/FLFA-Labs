package lexer;


import java.util.Arrays;
import java.util.List;

public class TokenList {
    public static final List<TokenPattern> TOKEN_PATTERNS = Arrays.asList(
            new TokenPattern("\\+", "PLUS"),
            new TokenPattern("\\-", "MINUS"),
            new TokenPattern("\\*", "MULTIPLY"),
            new TokenPattern("\\/", "DIVIDE"),
            new TokenPattern("\\(", "LPAREN"),
            new TokenPattern("\\)", "RPAREN"),
            new TokenPattern("\\s+", null),
            new TokenPattern("\\d+", "NUMBER")
    );
}
