import automaton.FiniteAutomaton;
import automaton.Transition;
import grammar.Grammar;
import lexer.Lexer;
import lexer.Token;
import lexer.TokenPattern;
import parser.Parser;

import java.util.*;
import java.util.regex.Matcher;

import static lexer.TokenList.TOKEN_PATTERNS;


public class Main {
    public static void main(String[] args) throws Exception {

        // Input 1
        // Lexer
        String text1 = "x = 0;\nwhile( x != 5) {\nprint(x);\nx = x + 1;}";
        Lexer lexer1 = new Lexer(text1);
        List<Token> tokens1 = lexer1.lex();
        //System.out.println("Input 1: " +  text1 + "\nTokenized form: " + tokens1);

        // Parser
        tokens1.removeIf(token -> token.getTokenType() == null); // Remove tokens with null types
        Parser parser = new Parser(tokens1);
        //parser.parse();
        //parser.printAST();


        // Input 2
        String text2 = "if ( x == 0) {\nprint(x);\n} else {\nprint(1);\n}";
        Lexer lexer2 = new Lexer(text2);
        List<Token> tokens2 = lexer2.lex();
        //System.out.println("Input 2: " +  text2 + "\nTokenized form: " + tokens2);


        tokens2.removeIf(token -> token.getTokenType() == null); // Remove tokens with null types
        Parser parser2 = new Parser(tokens2);
        //parser2.parse();
        //parser2.printAST();


        // Input 3
        String text3 = "y = 5;\nx = y + 2;\nprint(x);";
        Lexer lexer3 = new Lexer(text3);
        List<Token> tokens3 = lexer3.lex();
        //System.out.println("Input 3: " +  text3 + "\nTokenized form: " + tokens3);


        tokens3.removeIf(token -> token.getTokenType() == null); // Remove tokens with null types
        Parser parser3 = new Parser(tokens3);
        //parser3.parse();
        //parser3.printAST();


    }
}