import automaton.FiniteAutomaton;
import automaton.Transition;
import grammar.Grammar;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        String startingCharacter = "S";
        Set<String> nonTerminals = new HashSet<>(Set.of("S", "A", "B", "C"));
        Set<String> terminals = new HashSet<>(Set.of("a", "b", "c", "d"));
        Map<String, List<String>> productionRules = new HashMap<>() {{
            put("S", new ArrayList<>(List.of("dA")));
            put("A", new ArrayList<>(List.of("d","aB")));
            put("B", new ArrayList<>(List.of("bC")));
            put("C", new ArrayList<>(List.of("cA","aS")));
        }};
        System.out.println("\n1. Grammar\n");
        System.out.println("Non-terminal symbols = " + nonTerminals);
        System.out.println("Terminal symbols = " + terminals);
        System.out.println("Starting character = " + startingCharacter);
        System.out.println("Production rules = " + productionRules);

        Grammar g1 = new Grammar(startingCharacter, terminals, nonTerminals, productionRules);
        String type = g1.getGrammarType();
        System.out.println(type);
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
        System.out.println(f1.getTransitions());

        String word1 = g1.generateWord();
        String word2 = g1.generateWord();
        System.out.println("\n4. Testing word validity\n");
        System.out.println("dd" + " - " + f1.wordIsValid("dd") );
        System.out.println("acccd" + " - " + f1.wordIsValid("acccd") );
        System.out.println("str" + " - " + f1.wordIsValid("str") );
        System.out.println("dabcabadd" + " - " + f1.wordIsValid("dabcabadd") );
        System.out.println("" + " - " + f1.wordIsValid("") );
        System.out.println("dabcabadabadd" + " - " + f1.wordIsValid("dabcabadabadd") );
        System.out.println("dabadd" + " - " + f1.wordIsValid("dabadd") );
        System.out.println("dabcd" + " - " + f1.wordIsValid("dabcd") );
        System.out.println("001" + " - " + f1.wordIsValid("001") );
        System.out.println(word1 + " - " + f1.wordIsValid(word1) );
        System.out.println("dabcabcd" + " - " + f1.wordIsValid("dabcabcd") );
        System.out.println(word2 + " - " + f1.wordIsValid(word2) );


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

        */

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


        Grammar g2 = f2.toGrammar();
        String symbol = g2.getStartSymbol();
        Set<String> nonTerminal = g2.getNonTerminals();
        Set<String> terminal = g2.getTerminals();
        Map<String, List<String>> productionRule = g2.getProductionRules();
        System.out.println(symbol);
        System.out.println(nonTerminal);
        System.out.println(terminal);
        System.out.println(productionRule);
        System.out.println(f2.isDeterministic());
    }
}