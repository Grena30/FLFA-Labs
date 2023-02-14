package grammar;

import automaton.FiniteAutomaton;
import automaton.Transition;
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

        Set<String> states = new HashSet<>();
        Set<String> finalStates = new HashSet<>(Set.of("X"));
        int dimension = 0;
        int dimCheck = 0;

        for (char nonTerminal : nonTerminals) {
            dimension += productionRules.get(nonTerminal).size();
        }

        Transition[] transition = new Transition[dimension];

        for (char nonTerminal : nonTerminals) {
            states.add(Character.toString(nonTerminal));
            List<String> rightHandSides = productionRules.get(nonTerminal);
            for ( String str: rightHandSides){
                char nextState;
                if (str.length() > 1){
                    nextState = str.charAt(1);
                } else {
                    nextState = 'X';
                }
                char transitionLabel = str.charAt(0);
                transition[dimCheck] = new Transition(nonTerminal, nextState, transitionLabel);
                dimCheck++;
            }
        }

        FiniteAutomaton automaton = new FiniteAutomaton(transition);
        states.add("X");
        automaton.setStates(states);
        automaton.setStartState(Character.toString(startSymbol));
        automaton.setAcceptStates(finalStates);
        automaton.setAlphabet(terminals);

        return automaton;
    }
}
