package lexer;

public class Token {
    private final String tokenType;
    private final String value;
    public Token(String tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }
    public String getTokenType() {
        return tokenType;
    }
    public String getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "(" + tokenType + ", " + value + ")";
    }
}
