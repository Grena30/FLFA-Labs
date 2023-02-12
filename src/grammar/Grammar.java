package grammar;
import java.util.*;

public class Grammar {

    private String startSymbol;
    private Set<String> terminals;
    private Set<String> nonTerminals;
    private Map<String, List<List<String>>> productionRules;

    public Grammar(String startSymbol) {
        this.startSymbol = startSymbol;
        this.terminals = new HashSet<>();
        this.nonTerminals = new HashSet<>();
        this.productionRules = new HashMap<>();
    }

    public void addTerminal(String terminal) {
        terminals.add(terminal);
    }

    public void addNonTerminal(String nonTerminal) {
        nonTerminals.add(nonTerminal);
    }

    public void addProductionRule(String left, List<String> right) {
        if (!productionRules.containsKey(left)) {
            productionRules.put(left, new ArrayList<>());
        }
        productionRules.get(left).add(right);
    }

    public String generateWord() {
        return generateWord(startSymbol);
    }

    private String generateWord(String symbol) {
        if (terminals.contains(symbol)) {
            return symbol;
        }
        List<List<String>> rightHandSides = productionRules.get(symbol);
        List<String> randomRightHandSide = rightHandSides.get(new Random().nextInt(rightHandSides.size()));
        StringBuilder word = new StringBuilder();
        for (String rightSymbol : randomRightHandSide) {
            word.append(generateWord(rightSymbol));
        }
        return word.toString();
    }

}
