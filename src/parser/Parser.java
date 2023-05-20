package parser;

import lexer.Token;
import lexer.TokenList;

import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int pos;
    private final StringBuilder ast = new StringBuilder();

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
    }

    public void parse() throws Exception {
        while (pos < tokens.size()) {
            Token token = tokens.get(pos);
            if (token.getTokenType().equals("IDENTIFIER")) {
                // Variable declaration
                parseVariableDeclaration();
            } else if (token.getTokenType().equals("PRINT")) {
                // Print statement
                parsePrintStatement();
            } else if (token.getTokenType().equals("WHILE")) {
                // While loop
                parseWhileLoop();
            } else if (token.getTokenType().equals("IF")) {
                // If statement
                parseIfStatement();
            } else if (token.getTokenType().equals("ELSE")) {
                // ELse statement
                parseElseStatement();
            } else if (token.getTokenType().equals("RBRACE")) {
                // Right bracket
                break;
            } else {
                throw new Exception("Unexpected token: " + token.getValue());
            }
        }
    }

    public void printAST(){
        System.out.println("Abstract Syntax Tree:");
        System.out.println(ast);
    }

    private void parseVariableDeclaration() throws Exception {
        Token identifier = consumeToken("IDENTIFIER");
        Token nextToken = tokens.get(pos);
        if (nextToken.getTokenType().equals("ASSIGNMENT")) {
            consumeToken("ASSIGNMENT");
            Token value = parseExpression();
            consumeToken("SEMICOLON");
            System.out.println("Variable assignment: " + identifier.getValue() + " = " + value.getValue());
        } else if (nextToken.getTokenType().equals("LPAREN")) {
            // Function call
            consumeToken("LPAREN");
            parseExpression(); // Parse the argument expression
            consumeToken("RPAREN");
            consumeToken("SEMICOLON");
            System.out.println("Function call: " + identifier.getValue() + "()");
        } else if (nextToken.getTokenType().equals("SEMICOLON")) {
            consumeToken("SEMICOLON");
            System.out.println("Variable declaration: " + identifier.getValue());
        } else {
            throw new Exception("Unexpected token: " + nextToken.getValue());
        }
    }

    private void parsePrintStatement() throws Exception {
        ast.append("\t\tPRINT STATEMENT {");
        ast.append("\n\t\t\tEXPRESSION {");
        consumeToken("PRINT");
        consumeToken("LPAREN");
        Token value = consumeToken("IDENTIFIER", "INTEGER", "FLOAT");
        ast.append("\n\t\t\t\t"+ value.getTokenType()+": "+ value.getValue());
        ast.append(" \n\t\t\t}");
        ast.append("\n\t\t}");
        consumeToken("RPAREN");
        consumeToken("SEMICOLON");
        System.out.println("Print statement: " + value.getValue());
    }

    private void parseWhileLoop() throws Exception {
        consumeToken("WHILE");
        ast.append("WHILE STATEMENT {");
        ast.append("\n\tEXPRESSION {");
        consumeToken("LPAREN");
        Token condition = parseExpression();
        consumeToken("RPAREN");
        consumeToken("LBRACE");
        System.out.println("While loop: " + condition.getValue());
        ast.append("\n\tSTATEMENT {");
        ast.append("\n");
        while (!tokens.get(pos).getTokenType().equals("RBRACE")) {
            parse();
        }
        consumeToken("RBRACE");
        ast.append("\n\t}");
    }

    private void parseIfStatement() throws Exception {
        consumeToken("IF");
        consumeToken("LPAREN");
        Token condition = parseExpression();
        consumeToken("RPAREN");
        consumeToken("LBRACE");
        System.out.println("If statement: " + condition.getValue());
        while (!tokens.get(pos).getTokenType().equals("RBRACE")) {
            parse();
        }
        consumeToken("RBRACE");
    }

    private void parseElseStatement() throws Exception {
        consumeToken("ELSE");
        consumeToken("LBRACE");
        System.out.println("Else statement:");
        while (!tokens.get(pos).getTokenType().equals("RBRACE")) {
            parse();
        }
        consumeToken("RBRACE");
    }

    private Token parseExpression() throws Exception {
        Token leftOperand = consumeToken("IDENTIFIER", "INTEGER", "FLOAT");
        ast.append("\n\t\t"+ leftOperand.getTokenType()+": "+ leftOperand.getValue());

        if (tokens.get(pos).getTokenType().equals("EQUALS")) {
            Token operator = consumeToken("EQUALS");
            Token rightOperand = consumeToken("IDENTIFIER", "INTEGER", "FLOAT");
            ast.append("\n\t\tOPERATOR: "+ operator.getValue());
            ast.append("\n\t\t"+ rightOperand.getTokenType()+": "+ rightOperand.getValue());
            ast.append("\n\t}");
            return new Token(operator.getTokenType(), leftOperand.getValue() + " " + operator.getValue() + " " + rightOperand.getValue());

        } else if (tokens.get(pos).getTokenType().equals("PLUS")) {
            Token operator = consumeToken("PLUS");
            Token rightOperand = parseExpression();
            ast.append("\n\t\tOPERATOR: "+ operator.getValue());
            ast.append("\n\t\t"+ rightOperand.getTokenType()+": "+ rightOperand.getValue());
            ast.append("\n\t}");
            return new Token(operator.getTokenType(), leftOperand.getValue() + " + " + rightOperand.getValue());

        } else if (tokens.get(pos).getTokenType().equals("MULTIPLY")) {
            Token operator = consumeToken("MULTIPLY");
            Token rightOperand = parseExpression();
            ast.append("\n\t\tOPERATOR: "+ operator.getValue());
            ast.append("\n\t\t"+ rightOperand.getTokenType()+": "+ rightOperand.getValue());
            ast.append("\n\t}");
            return new Token(operator.getTokenType(), leftOperand.getValue() + " * " + rightOperand.getValue());

        } else if (tokens.get(pos).getTokenType().equals("DIVIDE")) {
            Token operator = consumeToken("DIVIDE");
            Token rightOperand = parseExpression();
            ast.append("\n\t\tOPERATOR: "+ operator.getValue());
            ast.append("\n\t\t"+ rightOperand.getTokenType()+": "+ rightOperand.getValue());
            ast.append("\n\t}");
            return new Token(operator.getTokenType(), leftOperand.getValue() + " / " + rightOperand.getValue());
        }
        return leftOperand;
    }
    private Token consumeToken(String expectedTokenType) throws Exception {
        Token token = tokens.get(pos);
        if (token.getTokenType().equals(expectedTokenType)) {
            pos++;
            return token;
        } else {
            throw new Exception("Expected token type: " + expectedTokenType + ", but found: " + token.getTokenType());
        }
    }

    private Token consumeToken(String... expectedTokenTypes) throws Exception {
        Token token = tokens.get(pos);
        for (String expectedTokenType : expectedTokenTypes) {
            if (token.getTokenType().equals(expectedTokenType)) {
                pos++;
                return token;
            }
        }
        throw new Exception("Expected one of token types: " + String.join(", ", expectedTokenTypes) + ", but found: " + token.getTokenType());
    }

    public void displayAST() {
        System.out.println("Abstract Syntax Tree:");
        displayASTHelper(0, 0);
        System.out.println();
    }

    private void displayASTHelper(int nodeIndex, int depth) {
        if (nodeIndex >= tokens.size()) {
            return;
        }

        Token token = tokens.get(nodeIndex);
        String indent = " ".repeat(depth * 2);

        System.out.println(indent + "| " + token.getValue());
        if (token.getTokenType() != null) {
            System.out.println(indent + "|  Type: " + token.getTokenType());
        }

        if (token.getTokenType().equals("WHILE") || token.getTokenType().equals("IF")) {
            displayASTHelper(nodeIndex + 1, depth + 1);
        } else if (token.getTokenType().equals("IDENTIFIER") || token.getTokenType().equals("PRINT")) {
            displayASTHelper(nodeIndex + 1, depth + 1);
        }

        displayASTHelper(nodeIndex + 1, depth);
    }

}
