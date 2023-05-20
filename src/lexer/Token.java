package lexer;

import java.util.ArrayList;
import java.util.List;

public class Token {
    private final String tokenType;
    private final String value;
    private List<Token> children;
    public Token(String tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
        this.children = new ArrayList<>();
    }
    public String getTokenType() {
        return tokenType;
    }
    public String getValue() {
        return value;
    }

    public List<Token> getChildren() {
        return children;
    }
    @Override
    public String toString() {
        return "(" + tokenType + ", " + value + ")";
    }
}
