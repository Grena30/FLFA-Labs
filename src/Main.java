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

        System.out.println(productionRules);
        System.out.println(terminals);
        System.out.println(nonTerminals);

        Grammar g1 = new Grammar(startingCharacter, terminals, nonTerminals, productionRules);
        FiniteAutomaton f1 = g1.toFiniteAutomaton();
        System.out.println(f1.getAlphabet());
        System.out.println(f1.getAcceptStates());
        System.out.println(f1.getStates());
        System.out.println(f1.getStartState());
        System.out.println(f1.getTransitions());
        for (int i = 0; i<1; i++) {
            String word = g1.generateWord();
            //System.out.println(f1.wordIsValid(word));
        }
    }
}