package automaton;
import java.util.*;

public class FiniteAutomaton {
    private Set<String> states;
    private Set<Character> alphabet;
    private Map<String, Map<Character, String>> transitions;
    private String startState;
    private Set<String> acceptStates;

    public FiniteAutomaton() {
        this.states = new HashSet<>();
        this.alphabet = new HashSet<>();
        this.transitions = new HashMap<>();
        this.startState = null;
        this.acceptStates = new HashSet<>();
    }

    public Set<String> getStates() {
        return states;
    }

    public void setStates(Set<String> states) {
        this.states = states;
    }

    public Set<Character> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Set<Character> alphabet) {
        this.alphabet = alphabet;
    }

    public Map<String, Map<Character, String>> getTransitions() {
        return transitions;
    }

    public void setTransitions(Map<String, Map<Character, String>> transitions) {
        this.transitions = transitions;
    }

    public String getStartState() {
        return startState;
    }

    public void setStartState(String startState) {
        this.startState = startState;
    }

    public Set<String> getAcceptStates() {
        return acceptStates;
    }

    public void setAcceptStates(Set<String> acceptStates) {
        this.acceptStates = acceptStates;
    }

    public boolean wordIsValid(String word) {
        String currentState = startState;
        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            if (!alphabet.contains(currentChar)) {
                return false;
            }
            Map<Character, String> stateTransitions = transitions.get(currentState);
            if (stateTransitions == null || !stateTransitions.containsKey(currentChar)) {
                return false;
            }
            currentState = stateTransitions.get(currentChar);
        }
        return acceptStates.contains(currentState);
    }


}
