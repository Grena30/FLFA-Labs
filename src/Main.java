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

        /*

        //Laboratory work nr.1

        String startingCharacter = "S";
        Set<String> nonTerminals = new HashSet<>(Set.of("S", "A", "B", "C"));
        Set<String> terminals = new HashSet<>(Set.of("a", "b", "c", "d"));
        Map<String, List<String>> productionRules = new HashMap<>() {{
            put("S", new ArrayList<>(List.of("dA")));
            put("A", new ArrayList<>(List.of("d","aB")));
            put("B", new ArrayList<>(List.of("bC")));
            put("C", new ArrayList<>(List.of("cA","aS")));
        }};
        System.out.println("\nLab 1\n");
        System.out.println("\nGrammar\n");
        System.out.println("Non-terminal symbols = " + nonTerminals);
        System.out.println("Terminal symbols = " + terminals);
        System.out.println("Starting character = " + startingCharacter);
        System.out.println("Production rules = " + productionRules);

        Grammar g1 = new Grammar(startingCharacter, terminals, nonTerminals, productionRules);
        String type = g1.getGrammarType();
        System.out.println(type);

        /*
        System.out.println("\n2. Generated words: \n");
        for (int i = 0; i<5; i++) {
            String word = g1.generateWord();
            System.out.println(word);
        }



        FiniteAutomaton f1 = g1.toFiniteAutomaton();
        System.out.println("\n3. Finite automaton\n");
        System.out.println("Alphabet = " + f1.getAlphabet());
        System.out.println("States = " + f1.getStates());
        System.out.println("Initial state = " + f1.getStartState());
        System.out.println("Accepting states = " + f1.getAcceptStates());
        System.out.println(f1.printTransitions());

        */


        // Laboratory work nr.2

        // Finite Automaton

        /*

        Variant 19
        Q = {q0,q1,q2},
        ∑ = {a,b},
        F = {q2},
        δ(q0,a) = q1,
        δ(q0,a) = q0,
        δ(q1,b) = q2,
        δ(q0,b) = q0,
        δ(q1,b) = q1,
        δ(q2,b) = q2.




        Set<String> states = new HashSet<>(Set.of("q0", "q1", "q2"));
        Set<String> alphabet = new HashSet<>(Set.of("a", "b"));
        String startState = "q0";
        Set<String> acceptStates = new HashSet<>(Set.of("q2"));
        Transition[] transitions = new Transition[6];
        transitions[0] = new Transition("q0", "q1","a");
        transitions[1] = new Transition("q0", "q0","a");
        transitions[2] = new Transition("q1", "q2","b");
        transitions[3] = new Transition("q0", "q0","b");
        transitions[4] = new Transition("q1", "q1","b");
        transitions[5] = new Transition("q2", "q2","b");
        FiniteAutomaton f2 = new FiniteAutomaton(transitions);
        f2.setStartState(startState);
        f2.setAlphabet(alphabet);
        f2.setAcceptStates(acceptStates);
        f2.setStates(states);
        System.out.println("\nLab 2\n");
        System.out.println("\nNon-deterministic finite automaton\n");
        System.out.println("Alphabet = " + f2.getAlphabet());
        System.out.println("States = " + f2.getStates());
        System.out.println("Initial state = " + f2.getStartState());
        System.out.println("Accepting states = " + f2.getAcceptStates());
        System.out.println(f2.printTransitions());
        System.out.println("Is the current final automaton deterministic: " + f2.isDeterministic());
        System.out.println("\nFinite automaton to grammar ");
        Grammar g2 = f2.toGrammar();
        String symbol = g2.getStartSymbol();
        Set<String> nonTerminal = g2.getNonTerminals();
        Set<String> terminal = g2.getTerminals();
        Map<String, List<String>> productionRule = g2.getProductionRules();
        System.out.println("\nGrammar\n");
        System.out.println("Staring symbol = " + symbol);
        System.out.println("Non-terminal symbols = " + nonTerminal);
        System.out.println("Terminal symbols = " + terminal);
        System.out.println("Production rules = " + productionRule);
        System.out.println("\nGenerate words: \n");
        for (int i = 0; i<5; i++) {
            System.out.println(g2.generateWord());
        }

        System.out.println("\nNFA to DFA ");
        FiniteAutomaton dfa2 = f2.convertToDFA();
        System.out.println("\nDeterministic Finite automaton\n");
        System.out.println("Alphabet = " + dfa2.getAlphabet());
        System.out.println("States = " + dfa2.getStates());
        System.out.println("Initial state = " + dfa2.getStartState());
        System.out.println("Accepting states = " + dfa2.getAcceptStates());
        System.out.println(dfa2.printTransitions());
        System.out.println("Is the current final automaton deterministic: " + dfa2.isDeterministic());
        */

        // Laboratory work nr.3

        String text = "22 + 3 * 51 * (2 + 3)";
        Lexer lexer = new Lexer(text);
        List<Token> tokens = lexer.lex();
        System.out.println(tokens);
    }
}