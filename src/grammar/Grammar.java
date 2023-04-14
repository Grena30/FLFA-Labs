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
            if (this.terminals.contains(Character.toString(rightSymbol))) {
                word.append(generateWord(Character.toString(rightSymbol)));
            } else if (rightSymbol == 'q'){
                word.append(generateWord(Character.toString(rightSymbol)+ randomRightHandSide.charAt(randomRightHandSide.length() - 1)));
            } else if (!this.nonTerminals.contains(Character.toString(rightSymbol))) {
                continue;
            } else {
                word.append(generateWord(Character.toString(rightSymbol)));
            }
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
                String nextState = "";
                if (str.length() > 1){
                    if (str.charAt(1) == 'q'){
                        nextState = "q" + str.charAt(2);
                    } else {
                        nextState = Character.toString(str.charAt(1));
                    }
                } else {
                    nextState = "X";
                }
                char transitionLabel = str.charAt(0);
                transition[dimCheck] = new Transition(nonTerminal, nextState, Character.toString(transitionLabel));
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

    public void grammar_info(String additional){

        System.out.println();
        if (additional.length() != 0) {
            System.out.println(additional);
        }
        System.out.println("Starting symbol: " + startSymbol);
        System.out.println("Terminals: " + terminals);
        System.out.println("Non-terminals: " + nonTerminals);
        System.out.println("Production rules: " + productionRules);
    }

    public void convertToChomskyNormalForm() {
        eliminateEpsilonProductions();
        grammar_info("After removing null symbols");
        eliminateUnitProductions();
        grammar_info("After removing unit productions");
        removeInaccessibleSymbols();
        grammar_info("After removing inaccessible symbols");
        //removeNonproductiveSymbols();
    }

    private void eliminateEpsilonProductions() {
        // Step 1: Eliminate epsilon productions
        Set<String> nullableSymbols = new HashSet<>();

        // Find nullable symbols
        for (String nonTerminal : nonTerminals) {
            if (productionRules.get(nonTerminal).contains("")) {
                nullableSymbols.add(nonTerminal);
            }
        }

        // Remove epsilon productions
        for (String nonTerminal : nonTerminals) {
            List<String> productions = new ArrayList<>(productionRules.get(nonTerminal));
            for (String production : productions) {
                if (production.isEmpty()) {
                    // Remove epsilon production
                    productionRules.get(nonTerminal).remove("");
                } else {
                    // Remove nullable symbols from production
                    for (int i = 0; i < production.length(); i++) {
                        if (nullableSymbols.contains(String.valueOf(production.charAt(i)))) {
                            String newProduction = production.substring(0, i) + production.substring(i + 1);
                            if (!newProduction.isEmpty() && !productionRules.get(nonTerminal).contains(newProduction)) {
                                productionRules.get(nonTerminal).add(newProduction);
                            }
                        }
                    }
                }
            }
        }

        grammar_info("After removing epsilon productions");


        // Remove non-terminal symbols that have no productions
        for (String nonTerminal_null: nullableSymbols) {
            List<String> productions_nullable = new ArrayList<>(productionRules.get(nonTerminal_null));

            // Check if there are no productions in this non-terminal form the null-list
            if (productions_nullable.isEmpty()){

                // Remove the non-terminal and all of its other usages from production rules and non-terminal symbols
                productionRules.remove(nonTerminal_null);
                nonTerminals.remove(nonTerminal_null);

                // Iterate through all the non-terminals and remove productions that contain the null-term
                for (String nonTerminal: nonTerminals) {
                    List<String> productions = new ArrayList<>(productionRules.get(nonTerminal));
                    for (String production: productions){
                        if (production.contains(nonTerminal_null)){
                            productionRules.get(nonTerminal).remove(production);
                        }
                    }
                }
            }
        }
    }


    private void eliminateUnitProductions() {

        Map<String, Set<String>> unitProductions = new HashMap<>();

        // Find unit productions
        for (String nonTerminal : nonTerminals) {
            Set<String> unitNonTerminals = new HashSet<>();
            for (String production : productionRules.get(nonTerminal)) {
                if (production.length() == 1 && nonTerminals.contains(production)) {
                    unitNonTerminals.add(production);
                }
            }
            unitProductions.put(nonTerminal, unitNonTerminals);

        }

        // Add the production rules of unit productions
        // And the remove unit productions
            for (String nonTerminal : nonTerminals) {
                Set<String> unitNonTerminals = unitProductions.get(nonTerminal);
                if (!unitNonTerminals.isEmpty()) {
                    for (String unit : unitNonTerminals) {
                        if (unitProductions.get(unit).isEmpty()) {
                            for (String production: productionRules.get(unit)) {
                                productionRules.get(nonTerminal).add(production);
                            }
                            productionRules.get(nonTerminal).remove(unit);
                            unitProductions.get(nonTerminal).remove(unit);
                        } // TODO for intertwined cases
                    }
                }
            }
        }
    private void removeInaccessibleSymbols() {

        ArrayList <String> accessibleSymbols = new ArrayList<>();

        for (String nonTerminal: nonTerminals ){
            String current = nonTerminal;
            for (String nonTerminal2: nonTerminals){
                String check = nonTerminal2;

                if (accessibleSymbols.contains(current)){
                    break;
                }

                if (Objects.equals(current, check)){
                    continue;
                }
                for (String production: productionRules.get(check)){
                    if (production.contains(current) && !accessibleSymbols.contains(current)){
                        accessibleSymbols.add(current);
                    } else {
                        break;
                    }
                }
            }
        }

        for (String nonTerminal: nonTerminals){
            if (!accessibleSymbols.contains(nonTerminal)){
                productionRules.remove(nonTerminal);
                nonTerminals.remove(nonTerminal);
            }
        }
    }

    private void removeNonproductiveSymbols(){

    }

}
