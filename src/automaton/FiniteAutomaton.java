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
        // Create a new DFA with the same alphabet
        FiniteAutomaton dfa = new FiniteAutomaton(new Transition[0]);
        dfa.setAlphabet(this.alphabet);

        // Compute epsilon-closure of the start state
        Set<String> startClosure = epsilonClosure(Collections.singleton(startState));

        // Initialize the worklist with the start closure
        Queue<Set<String>> worklist = new LinkedList<>();
        worklist.add(startClosure);

        // Keep track of visited states in the DFA
        Map<Set<String>, String> stateMap = new HashMap<>();

        // While there are unprocessed sets of states in the worklist
        while (!worklist.isEmpty()) {
            Set<String> currentStateSet = worklist.poll();

            // If the set of states has already been added to the DFA, skip it
            if (stateMap.containsKey(currentStateSet)) {
                continue;
            }

            // Add the current set of states as a new state in the DFA
            String currentState = String.join(",", currentStateSet);
            stateMap.put(currentStateSet, currentState);
            dfa.getStates().add(currentState);

            // Check if the current set of states contains an accept state
            for (String acceptState : acceptStates) {
                if (currentStateSet.contains(acceptState)) {
                    dfa.getAcceptStates().add(currentState);
                    break;
                }
            }

            // For each symbol in the alphabet
            for (String symbol : alphabet) {
                Set<String> nextStateSet = new HashSet<>();

                // Compute the set of states reachable from the current set of states
                // by following transitions labeled with the current symbol
                for (String state : currentStateSet) {
                    for (Transition transition : transitions) {
                        if (transition.getCurrentState().equals(state) && Objects.equals(transition.getTransitionLabel(), symbol)) {
                            nextStateSet.add(transition.getNextState());
                        }
                    }
                }

                // Compute the epsilon-closure of the next set of states
                nextStateSet = epsilonClosure(nextStateSet);

                // If the next set of states is not empty, add a transition to the DFA
                if (!nextStateSet.isEmpty()) {
                    String nextState = stateMap.get(nextStateSet);
                    if (nextState == null) {
                        nextState = String.join(",", nextStateSet);
                        stateMap.put(nextStateSet, nextState);
                        worklist.add(nextStateSet);
                        dfa.getStates().add(nextState);
                    }
                    dfa.addTransition(new Transition(currentState, nextState, symbol));
                }
            }
        }

        // Set the start state of the DFA to the epsilon-closure of the original start state
        dfa.setStartState(stateMap.get(startClosure));

        return dfa;
    }

    private Set<String> epsilonClosure(Set<String> states) {
        Set<String> closure = new HashSet<>(states);
        Queue<String> worklist = new LinkedList<>(states);

        // While there are unprocessed states in the worklist
        while (!worklist.isEmpty()) {
            String currentState = worklist.poll();

            // For each epsilon transition from the current state
            for (Transition transition : transitions) {
                if (transition.getCurrentState().equals(currentState) && Objects.equals(transition.getTransitionLabel(), "ε")) {
                    String nextState = transition.getNextState();
                    if (!closure.contains(nextState)) {
                        closure.add(nextState);
                        worklist.add(nextState);
                    }
                }
            }
        }

        return closure;
    }




}
