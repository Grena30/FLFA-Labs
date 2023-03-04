package grammar;

import automaton.FiniteAutomaton;
import automaton.Transition;
import java.util.*;

public class Grammar {
    private String startSymbol;
    private Set<String> terminals;
    private Set<String> nonTerminals;
    private Map<String, List<String>> productionRules;

    public Grammar(String startSymbol, Set<String> terminals, Set<String> nonTerminals, Map<String, List<String>> productionRules) {
        this.startSymbol = startSymbol;
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.productionRules = productionRules;
    }
    public Map<String, List<String>> getProductionRules(){
        return productionRules;
    }

    public Set<String> getNonTerminals(){
        return nonTerminals;
    }

    public Set<String> getTerminals(){
        return terminals;
    }

    public String getStartSymbol(){
        return startSymbol;
    }

    public String generateWord() {
        return generateWord(startSymbol);
    }

    private String generateWord(String symbol) {
        if (terminals.contains(symbol)) {
            return symbol;
        }
        List<String> rightHandSides = productionRules.get(symbol);
        String randomRightHandSide = rightHandSides.get(new Random().nextInt(rightHandSides.size()));
        StringBuilder word = new StringBuilder();
        for (char rightSymbol : randomRightHandSide.toCharArray()) {
            word.append(generateWord(Character.toString(rightSymbol)));
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
                transition[dimCheck] = new Transition(nonTerminal, Character.toString(nextState), Character.toString(transitionLabel));
                dimCheck++;
            }
        }

        FiniteAutomaton automaton = new FiniteAutomaton(transition);
        states.add("X");
        automaton.setStates(states);
        automaton.setStartState(startSymbol);
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
                if (rightSide.length() == 1 && Character.isLowerCase(rightSide.charAt(0))) {
                    continue;
                } else if (rightSide.length() == 2) {
                    char firstSymbol = rightSide.charAt(0);
                    char secondSymbol = rightSide.charAt(1);
                    if (Character.isUpperCase(firstSymbol) && Character.isLowerCase(secondSymbol)){
                        continue;
                } else if (Character.isLowerCase(firstSymbol) && Character.isUpperCase(secondSymbol)){
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
                if (nonTerminal.length() != 1 || !Character.isUpperCase(nonTerminal.charAt(0))) {
                    return false;
                }
                for (int i = 0; i<rightSide.length(); i++){
                    char symbol = rightSide.charAt(i);
                    if (!Character.isUpperCase(symbol) && !Character.isLowerCase(symbol)){
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
                if (nonTerminal.length() > rightSide.length()){
                    return false;
                }
            }
        }
        return true;
    }


}
