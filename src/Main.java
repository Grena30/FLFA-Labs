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

        // Lexer
        String text1 = "x = x + 1;";
        Lexer lexer1 = new Lexer(text1);
        List<Token> tokens1 = lexer1.lex();
        System.out.println("Input 1: " +  text1 + "\nTokenized form: " + tokens1);

        // Parser
        tokens1.removeIf(token -> token.getTokenType() == null); // Remove tokens with null types
        Parser parser = new Parser(tokens1);
        parser.parse();
        parser.printAST();


    }
}