package automaton;
import grammar.Grammar;
import java.util.*;

public class FiniteAutomaton {
    private Set<String> states;
    private Set<String> alphabet;
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

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(Set<String> alphabet) {
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
                if (Objects.equals(t.getCurrentState(), currentState) && Objects.equals(t.getTransitionLabel(), Character.toString(c))) {
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

    public Grammar toGrammar(){
        Set<String> nonTerminals = this.states;
        Set<String> terminals = this.alphabet;
        String startSymbol = this.startState;
        Map<String, List<String>> productionRules = new HashMap<>();

        for (String state : this.states){
            for (Transition transition: this.transitions){
                if (Objects.equals(transition.getCurrentState(), state) && !Objects.equals(transition.getTransitionLabel(), "")) {
                    if (productionRules.containsKey(state)) {
                        productionRules.get(state).add(transition.getTransitionLabel() + transition.getNextState());
                    } else {
                        productionRules.put(state, new ArrayList<>(List.of(transition.getTransitionLabel() + transition.getNextState())));
                    }
                }
            }
        }
        for (String state: this.acceptStates){
            if (productionRules.containsKey(state)) {
                productionRules.get(state).add("e");
            } else {
                productionRules.put(state, new ArrayList<>(List.of("e")));
            }
        }
        return new Grammar(startSymbol, terminals, nonTerminals, productionRules);
    }


    public boolean isDeterministic() {
        Set<String> seenStates = new HashSet<>();
        for (Transition transition : transitions) {
            String fromState = transition.getCurrentState();
            String input = transition.getTransitionLabel();
            String toState = transition.getNextState();

            // Check if the transition is valid
            if (!states.contains(fromState) || !states.contains(toState) || !alphabet.contains(input)) {
                return false;
            }

            // Check if this is the first transition we've seen for this state and input
            if (seenStates.contains(fromState + input)) {
                return false;
            }
            seenStates.add(fromState + input);

            // Check if this state/input pair leads to multiple states
            for (Transition otherTransition : transitions) {
                if (otherTransition.getCurrentState().equals(fromState) &&
                        otherTransition.getTransitionLabel().equals(input) &&
                        !otherTransition.getNextState().equals(toState)) {
                    return false;
                }
            }
        }

        return true;
    }



}
