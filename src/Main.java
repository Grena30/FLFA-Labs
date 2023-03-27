import automaton.FiniteAutomaton;
import automaton.Transition;
import grammar.Grammar;
import lexer.Lexer;
import lexer.Token;
import lexer.TokenPattern;

import java.util.*;
import java.util.regex.Matcher;

import static lexer.TokenList.TOKEN_PATTERNS;

public class Main {
    public static void main(String[] args) throws Exception {

        String text1 = "if (num >= 23.1 * 15): num = 23.1 * 15; else: num = num * 2;";
        Lexer lexer1 = new Lexer(text1);
        List<Token> tokens1 = lexer1.lex();
        System.out.println("Input 1: " +  text1 + ". Tokenized form: " + tokens1);

        String text2 = "count = 0; coef_1 = 1.25;";
        Lexer lexer2 = new Lexer(text2);
        List<Token> tokens2 = lexer2.lex();
        System.out.println("Input 2: " +  text2 + ". Tokenized form: " + tokens2);

        String text3 = "while ( i <= 10): i = i - 1; prod = prod * 3/2; ";
        Lexer lexer3 = new Lexer(text3);
        List<Token> tokens3 = lexer3.lex();
        System.out.println("Input 3: " +  text3 + ". Tokenized form: " +tokens3);

        String text4 = "for (i = 0; i <= 15; i = i + 1): decimal = decimal * 3;";
        Lexer lexer4 = new Lexer(text4);
        List<Token> tokens4 = lexer4.lex();
        System.out.println("Input 4: " +  text4 + ". Tokenized form: " +tokens4);
    }
}