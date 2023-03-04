package automaton;
import grammar.Grammar;
import java.util.*;
import java.util.Arrays;

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

    public String printTransitions() {
        return "Transitions = " + Arrays.toString(this.transitions);
    }

    public void setTransitions(Transition[] newTransitions) {this.transitions = newTransitions;}

    public Transition[] getTransitions() {return this.transitions;}

    public void addTransition(Transition toAddTransition) {
        Transition[] transitionsList = new Transition[this.transitions.length + 1];
        for (int i = 0; i < this.transitions.length; i++){
            transitionsList[i] = this.transitions[i];
        }
        transitionsList[this.transitions.length] = toAddTransition;
        setTransitions(transitionsList);
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

    public FiniteAutomaton convertToDFA() {
        // Create the new DFA
        FiniteAutomaton dfa = new FiniteAutomaton(new Transition[0]);

        // Create a mapping of states to their epsilon closures
        Map<Set<String>, Set<String>> epsilonClosures = new HashMap<>();
        for (String state : states) {
            Set<String> closure = getEpsilonClosure(state);
            epsilonClosures.put(closure, closure);
        }

        // Initialize the DFA with the start state
        Set<String> startClosure = epsilonClosures.get(getEpsilonClosure(this.startState));
        dfa.states.add(setToString(startClosure));
        dfa.startState = setToString(startClosure);
        dfa.alphabet = this.alphabet;

        // Compute the transitions for the DFA
        Set<Set<String>> unmarkedStates = new HashSet<>();
        unmarkedStates.add(startClosure);
        while (!unmarkedStates.isEmpty()) {
            Set<String> currentStateSet = unmarkedStates.iterator().next();
            unmarkedStates.remove(currentStateSet);

            for (String label : this.alphabet) {
                Set<String> nextStateSet = new HashSet<>();
                for (String state : currentStateSet) {
                    for (Transition t : this.transitions) {
                        if (t.getCurrentState().equals(state) && t.getTransitionLabel().equals(label)) {
                            Set<String> targetClosure = epsilonClosures.get(getEpsilonClosure(t.getNextState()));
                            nextStateSet.addAll(targetClosure);
                        }
                    }
                }

                if (!nextStateSet.isEmpty()) {
                    String nextStateName = setToString(nextStateSet);
                    dfa.states.add(nextStateName);

                    if (dfa.startState == null && nextStateSet.contains(this.startState)) {
                        dfa.startState = nextStateName;
                    }

                    if (isAcceptState(nextStateSet)) {
                        dfa.acceptStates.add(nextStateName);
                    }

                    String currentStateName = setToString(currentStateSet);
                    dfa.transitions = Arrays.copyOf(dfa.transitions, dfa.transitions.length + 1);
                    dfa.transitions[dfa.transitions.length - 1] = new Transition(currentStateName, nextStateName, label);

                    if (!epsilonClosures.containsKey(nextStateSet)) {
                        epsilonClosures.put(nextStateSet, nextStateSet);
                        unmarkedStates.add(nextStateSet);
                    }
                }
            }
        }

        return dfa;
    }

    private Set<String> getEpsilonClosure(String state) {
        Set<String> closure = new HashSet<>();
        closure.add(state);
        boolean changed;
        do {
            changed = false;
            for (Transition t : transitions) {
                if (t.getCurrentState().equals(state) && t.getTransitionLabel().equals("")) {
                    if (closure.add(t.getNextState())) {
                        changed = true;
                    }
                }
            }
        } while (changed);
        return closure;
    }

    private boolean isAcceptState(Set<String> stateSet) {
        for (String state : stateSet) {
            if (acceptStates.contains(state)) {
                return true;
            }
        }
        return false;
    }

    private String setToString(Set<String> set) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (String element : set) {
            if (!first) {
                sb.append(",");
            }
            sb.append(element);
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }


}
