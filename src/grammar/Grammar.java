package grammar;
import automaton.FiniteAutomaton;

import java.util.*;

import java.util.*;

public class Grammar {
    private char startSymbol;
    private Set<Character> terminals;
    private Set<Character> nonTerminals;
    private Map<Character, List<String>> productionRules;

    public Grammar(char startSymbol, Set<Character> terminals, Set<Character> nonTerminals, Map<Character, List<String>> productionRules) {
        this.startSymbol = startSymbol;
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.productionRules = productionRules;
    }

    public String generateWord() {
        return generateWord(startSymbol);
    }

    private String generateWord(char symbol) {
        if (terminals.contains(symbol)) {
            return Character.toString(symbol);
        }
        List<String> rightHandSides = productionRules.get(symbol);
        String randomRightHandSide = rightHandSides.get(new Random().nextInt(rightHandSides.size()));
        StringBuilder word = new StringBuilder();
        for (char rightSymbol : randomRightHandSide.toCharArray()) {
            word.append(generateWord(rightSymbol));
        }
        return word.toString();
    }

    public FiniteAutomaton toFiniteAutomaton() {
        FiniteAutomaton automaton = new FiniteAutomaton();
        Set<String> states = new HashSet<>();
        Map<String, Map<Character, String>> transitions = new HashMap<>();

        for (char nonTerminal : nonTerminals) {
            for (String rightHandSide : productionRules.get(nonTerminal)) {
                String state = nonTerminal + "->" + rightHandSide;
                states.add(state);
                transitions.put(state, new HashMap<>());
                for (int i = 0; i < rightHandSide.length(); i++) {
                    char symbol = rightHandSide.charAt(i);
                    String nextState = nonTerminal + "->" + rightHandSide.substring(i + 1);
                    if (terminals.contains(symbol)) {
                        transitions.get(state).put(symbol, nextState);
                    } else {
                        String nextStateStart = symbol + "->" + productionRules.get(symbol).get(0);
                        transitions.get(state).put(symbol, nextStateStart);
                    }
                }
            }
        }

        automaton.setStates(states);
        automaton.setTransitions(transitions);
        automaton.setStartState(startSymbol + "->" + productionRules.get(startSymbol).get(0));
        automaton.setAcceptStates(states);
        automaton.setAlphabet(terminals);

        return automaton;
    }
}
