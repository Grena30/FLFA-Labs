import automaton.FiniteAutomaton;
import grammar.Grammar;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        char startingCharacter = 'S';
        Set<Character> nonTerminals = new HashSet<>(Set.of('S', 'A', 'B', 'C'));
        Set<Character> terminals = new HashSet<>(Set.of('a', 'b', 'c', 'd'));
        Map<Character, List<String>> productionRules = new HashMap<>() {{
            put('S', new ArrayList<>(List.of("dA")));
            put('A', new ArrayList<>(List.of("d","aB")));
            put('B', new ArrayList<>(List.of("bC")));
            put('C', new ArrayList<>(List.of("cA","aS")));
        }};
        System.out.println("\n1. Grammar\n");
        System.out.println("Non-terminal symbols = " + nonTerminals);
        System.out.println("Terminal symbols = " + terminals);
        System.out.println("Starting character = " + startingCharacter);
        System.out.println("Production rules = " + productionRules);

        Grammar g1 = new Grammar(startingCharacter, terminals, nonTerminals, productionRules);

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
        System.out.println("001" + " - " + f1.wordIsValid("001") );
        System.out.println(word2 + " - " + f1.wordIsValid(word2) );
    }
}