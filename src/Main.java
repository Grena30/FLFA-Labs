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

        String text = "if (ab >= 23.1 * 15): ab = 23.1 * 15";
        Lexer lexer = new Lexer(text);
        List<Token> tokens = lexer.lex();
        System.out.println(tokens);
    }
}