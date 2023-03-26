package lexer;

import java.util.regex.Pattern;

public class TokenPattern {
    private final String pattern;
    private final String tokenType;

    public TokenPattern(String pattern, String tokenType) {
        this.pattern = pattern;
        this.tokenType = tokenType;
    }

    public Pattern getPattern() {
        return Pattern.compile(pattern);
    }

    public String getTokenType() {
        return tokenType;
    }
}
