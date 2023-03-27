#  Lexer & Scanner

## Course: Formal Languages & Finite Automata
## Author: Gutu Dinu

---

## Theory

A lexer, also known as a lexical analyzer, 
is an important component of a compiler or 
interpreter that performs the task of breaking down 
source code into meaningful tokens. The lexer scans the 
input source code, identifies each lexeme (or sequence of 
characters with a specific meaning), and categorizes them into 
different token types.
It operates at the lowest level of the 
compiler or interpreter, and serves as the first step 
in the process of transforming source code into executable code. 
Its output being is a stream of tokens, which are then 
passed on to the parser for further analysis.

Furthermore, the lexer uses a set of rules or regular expressions to 
define the syntax of the programming language being compiled 
or interpreted. These rules are defined in a specification known 
as a lexer grammar. The lexer grammar describes the different 
lexemes that can be found in the source code, and specifies how 
they should be tokenized.
Typically, a lexer can handle a wide variety of input sources, 
including files, input streams, or even command line arguments. 
Once the lexer has identified the tokens in the source code, 
it can perform further analysis on the syntax of the code to
check for errors or generate an abstract syntax tree that can be 
used for further processing.

## Objectives:

1. Understand what lexical analysis is.
2. Get familiar with the inner workings of a lexer/scanner/tokenizer.
3. Implement a sample lexer and show how it works.

## Implementation description

### Lexer

* The lexer in my case is mostly based on this function, its constructor having as input only the text.
* The function begins by looking through the token patterns and comparing them to the input text.
* If there is match found and such a token type exists then a token is added to the token list.
* The position of the scanned text is then updated to the end of the previous matched text for it to not be counted again.
* If a match is not found then it returns an error since the character was not recognized.
* Otherwise, if the process is successful it returns the source code in its tokenized form.
* For conventional norms I added the null token at the end of the token list to signal the end of the input.

```
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
```


### Token

* The token class is simply a container for the token type and respective value.

```
public class Token {
    private final String tokenType;
    private final String value;
    public Token(String tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }
```

### Token Pattern

* The token pattern class just like the token one is a container for the token 
types and patterns that can be recognized by the lexer.

```
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
```

### Token List

* The token list class can also be directly integrated 
into the lexer, but it is easier to view in this form.
* It is a list of token patterns which contain the available 
token types for the lexer and all the patterns that work with regex.

```
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
```

## Results
```
Input 1: if (num >= 23.1 * 15): 
            num = 23.1 * 15; 
         else: 
            num = num * 2; 
            
Tokenized form: [(IF, if), (LPAREN, (), (IDENTIFIER, num), (GREATER_THAN_EQUALS, >=), 
(FLOAT, 23.1), (MULTIPLY, *), (INTEGER, 15), (RPAREN, )), (COLON, :), 
(IDENTIFIER, num), (ASSIGNMENT, =), (FLOAT, 23.1), (MULTIPLY, *), 
(INTEGER, 15), (SEMICOLON, ;), (ELSE, else), (COLON, :), 
(IDENTIFIER, num), (ASSIGNMENT, =), (IDENTIFIER, num), 
(MULTIPLY, *), (INTEGER, 2), (null, )]

Input 2: count = 0; 
         coef_1 = 1.25; 
         
Tokenized form: [(IDENTIFIER, count), (ASSIGNMENT, =), (INTEGER, 0), 
(SEMICOLON, ;), (IDENTIFIER, coef_1), (ASSIGNMENT, =), (FLOAT, 1.25), 
(null, )]

Input 3: while ( i <= 10): 
            i = i - 1; 
            prod = prod * 3/2; 
            
Tokenized form: [(WHILE, while), (LPAREN, (), (IDENTIFIER, i), (LESS_THAN_EQUALS, <=), 
(INTEGER, 10), (RPAREN, )), (COLON, :), (IDENTIFIER, i), (ASSIGNMENT, =), 
(IDENTIFIER, i), (MINUS, -), (INTEGER, 1), (SEMICOLON, ;), 
(IDENTIFIER, prod), (ASSIGNMENT, =), (IDENTIFIER, prod), (MULTIPLY, *), 
(INTEGER, 3), (DIVIDE, /), (INTEGER, 2), (SEMICOLON, ;), (null, )]

Input 4: for (i = 0; i <= 15; i = i + 1): 
            decimal = decimal * 3;
             
Tokenized form: [(FOR, for), (LPAREN, (), (IDENTIFIER, i), (ASSIGNMENT, =), (INTEGER, 0), 
(SEMICOLON, ;), (IDENTIFIER, i), (LESS_THAN_EQUALS, <=), (INTEGER, 15), 
(SEMICOLON, ;), (IDENTIFIER, i), (ASSIGNMENT, =), (IDENTIFIER, i), 
(PLUS, +), (INTEGER, 1), (RPAREN, )), (COLON, :), (IDENTIFIER, decimal), 
(ASSIGNMENT, =), (IDENTIFIER, decimal), (MULTIPLY, *), (INTEGER, 3), 
(SEMICOLON, ;), (null, )]
```
## Conclusion

In conclusion, using a lexer in a lab project has given me 
invaluable knowledge about the parsing and tokenization of source code. 
I gained knowledge about the value of lexical 
analysis and how it functions as the first stage in the 
compilation or interpretation of a programming language 
through this assignment.
Based on a set of predefined rules, the lexer used in the 
lab work was able to successfully parse the source code into 
understandable tokens. This enabled me to examine the source 
code in greater detail.
Also, this laboratory work aided in my comprehension of the 
various parts that go into a compiler or interpreter.
It gave me the chance to hone my coding abilities and 
increase my capacity for critical and methodical thought when 
tackling challenging issues.
Overall, using a lexer in this lab work 
has been a worthwhile learning experience 
that has assisted me in gaining information and 
abilities that will be helpful in programming projects in the future. 