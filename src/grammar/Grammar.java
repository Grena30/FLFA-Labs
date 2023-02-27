package grammar;

import automaton.FiniteAutomaton;
import automaton.Transition;
import java.util.*;

public class Grammar {
    private char startSymbol;
    private Set<Character> terminals;
    private Set<String> nonTerminals;
    private Map<String, List<String>> productionRules;

    public Grammar(char startSymbol, Set<Character> terminals, Set<String> nonTerminals, Map<String, List<String>> productionRules) {
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
        List<String> rightHandSides = productionRules.get(Character.toString(symbol));
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

        for (String nonTerminal : nonTerminals) {
            dimension += productionRules.get(nonTerminal).size();
        }

        Transition[] transition = new Transition[dimension];

        for (String nonTerminal : nonTerminals) {
            states.add(nonTerminal);
            List<String> rightHandSides = productionRules.get(nonTerminal);
            for ( String str: rightHandSides){
                char nextState;
                if (str.length() > 1){
                    nextState = str.charAt(1);
                } else {
                    nextState = 'X';
                }
                char transitionLabel = str.charAt(0);
                transition[dimCheck] = new Transition(nonTerminal, Character.toString(nextState), transitionLabel);
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

public String getGrammarType() {
        if (isRegularGrammar()) {
            return "Regular (Type 3)";
        } else if (isContextFreeGrammar()) {
            return "Context-Free (Type 2)";
        } else if (isContextSensitiveGrammar()) {
            return "Context-Sensitive (Type 1)";
        } else {
            return "Recursively enumerable (Type 0)";
        }
    }

    public boolean isRegularGrammar() {
        for (String nonTerminal: nonTerminals) {
            List<String> rightHandSides = productionRules.get(nonTerminal);
            for (String rightSide: rightHandSides ) {
                if (rightSide.length() == 1 && terminals.contains(rightSide.charAt(0))) {
                    continue;
                } else if (rightSide.length() == 2) {
                    if (nonTerminals.contains(rightSide.charAt(0)) && terminals.contains(rightSide.charAt(1))){
                        continue;
                } else if (terminals.contains(rightSide.charAt(1)) && nonTerminals.contains(rightSide.charAt(0))){
                        continue;
                    }
                }
                return false;
            }
        }
        return true;
    }

    public boolean isContextFreeGrammar() {
        for (String nonTerminal: nonTerminals) {
            List<String> rightHandSides = productionRules.get(nonTerminal);
            for (String rightSide: rightHandSides ) {
                for (int i = 0; i < rightSide.length(); i++){
                    char symbol = rightSide.charAt(i);
                    if (!nonTerminals.contains(symbol) && !terminals.contains(symbol)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isContextSensitiveGrammar() {
        for (String nonTerminal: nonTerminals) {
            List<String> rightHandSides = productionRules.get(nonTerminal);
            for (String rightSide: rightHandSides ) {
                if (rightSide.length() > 1){
                    return false;
                }
            }
        }
        return true;
    }


}
