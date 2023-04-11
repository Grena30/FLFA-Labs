import automaton.FiniteAutomaton;
import automaton.Transition;
import grammar.Grammar;
import lexer.Lexer;
import lexer.Token;
import lexer.TokenPattern;

import java.util.*;
import java.util.regex.Matcher;

import static lexer.TokenList.TOKEN_PATTERNS;


/*
    Varian 19
    1. Eliminate epsilon productions
    2. Eliminate any unit productions
    3. Eliminate inaccessible symbols
    4. Eliminate non-productive symbols
    5. Obtain CNF
    G = {Vn, Vt, P, S}
    Vn = {S, A, B, C, E}
    Vt = {a, d}
    P = {S -> dB
    S -> B
    A -> d
    A -> dS
    A -> aAdCB
    B -> aC
    B -> dA
    B -> AC
    C -> epsilon
    E -> AS}

 */

public class Main {
    public static void main(String[] args) throws Exception {

        String startingCharacter = "S";
        Set<String> nonTerminals = new HashSet<>(Set.of("S", "A", "B", "C", "E"));
        Set<String> terminals = new HashSet<>(Set.of("a", "d"));
        Map<String, List<String>> productionRules = new HashMap<>() {{
            put("S", new ArrayList<>(List.of("dB", "B")));
            put("A", new ArrayList<>(List.of("d","dS", "aAdCB")));
            put("B", new ArrayList<>(List.of("aC", "dA", "AC")));
            put("C", new ArrayList<>(List.of("")));
            put("E", new ArrayList<>(List.of("AS")));
        }};

        Grammar g = new Grammar(startingCharacter, terminals, nonTerminals, productionRules);
        System.out.println();
        System.out.println("Starting symbol: " + g.getStartSymbol());
        System.out.println("Terminals: " + g.getTerminals());
        System.out.println("Non-terminals: " + g.getNonTerminals());
        System.out.println("Production rules: " + g.getProductionRules());

        g.convertToChomskyNormalForm();
        System.out.println();
        System.out.println("Starting symbol: " + g.getStartSymbol());
        System.out.println("Terminals: " + g.getTerminals());
        System.out.println("Non-terminals: " + g.getNonTerminals());
        System.out.println("Production rules: " + g.getProductionRules());
    }
}