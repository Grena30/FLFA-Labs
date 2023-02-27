package automaton;
import grammar.Grammar;
import java.util.*;

public class FiniteAutomaton {
    private Set<String> states;
    private Set<Character> alphabet;
    private Transition[] transitions;
    private String startState;
    private Set<String> acceptStates;

    public FiniteAutomaton(Transition[] transitions) {
        this.states = new HashSet<>();
        this.alphabet = new HashSet<>();
        this.transitions = transitions;
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

    public String getTransitions() {
        return "Transitions = " + Arrays.toString(transitions);
    }

    public boolean wordIsValid(String word) {
        String currentState = startState;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            boolean foundTransition = false;
            for (Transition t : transitions) {
                if (Objects.equals(t.getCurrentState(), currentState) && t.getTransitionLabel() == c) {
                    currentState = t.getNextState();
                    foundTransition = true;
                    break;
                }
            }
            if (!foundTransition) {
                return false;
            }
        }
        return acceptStates.contains(currentState);
    }


}
