package lexer;


import java.util.Arrays;
import java.util.List;

public class TokenList {
    public static final List<TokenPattern> TOKEN_PATTERNS = Arrays.asList(
            new TokenPattern("\\+", "PLUS"),
            new TokenPattern("\\-", "MINUS"),
            new TokenPattern("\\*", "MULTIPLY"),
            new TokenPattern("\\/", "DIVIDE"),
            new TokenPattern("\\%", "MODULO"),
            new TokenPattern("\\(", "LPAREN"),
            new TokenPattern("\\)", "RPAREN"),
            new TokenPattern("\\{", "LBRACE"),
            new TokenPattern("\\}", "RBRACE"),
            new TokenPattern("\\d+\\.\\d+", "FLOAT"),
            new TokenPattern("\\d+", "INTEGER"),
            new TokenPattern("if", "IF"),
            new TokenPattern("else", "ELSE"),
            new TokenPattern("while", "WHILE"),
            new TokenPattern("for", "FOR"),
            new TokenPattern("[a-zA-Z_][a-zA-Z_0-9]*", "IDENTIFIER"),
            new TokenPattern("==", "EQUALS"),
            new TokenPattern("\\=", "ASSIGNMENT"),
            new TokenPattern("\\!=", "NOT_EQUALS"),
            new TokenPattern("\\>=", "GREATER_THAN_EQUALS"),
            new TokenPattern("\\>", "GREATER_THAN"),
            new TokenPattern("\\<=", "LESS_THAN_EQUALS"),
            new TokenPattern("\\<", "LESS_THAN"),
            new TokenPattern("\\&&", "LOGICAL_AND"),
            new TokenPattern("\\|\\|", "LOGICAL_OR"),
            new TokenPattern("\\!", "LOGICAL_NOT"),
            new TokenPattern("\\;", "SEMICOLON"),
            new TokenPattern("\\,", "COMMA"),
            new TokenPattern("\\:", "COLON"),
            new TokenPattern("\\s+", null)
    );
}
