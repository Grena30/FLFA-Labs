#  Types of Finite Automata

## Course: Formal Languages & Finite Automata
## Author: Gutu Dinu
## Variant: 19

---

## Theory

Finite automata (FA) are mathematical models used to recognize languages, which are sets of strings that can be generated by a formal grammar. 
FA can be deterministic or non-deterministic, depending on the set of rules they follow to recognize strings.
A deterministic finite automaton (DFA) is a finite-state machine that accepts or rejects strings of symbols, 
usually over an alphabet containing a finite number of letters. It is called deterministic because, for each input symbol, 
there is exactly one transition from a given state.
A DFA can recognize only regular languages, which are a subset of the set of all possible languages.
A non-deterministic finite automaton (NFA) is a finite-state machine that accepts or rejects strings of symbols, 
usually over an alphabet containing a finite number of letters. It is called non-deterministic because, 
for each input symbol, there can be multiple transitions from a given state.
An NFA can recognize regular languages and some non-regular languages, which are a superset of the set of all possible regular languages.

The main difference between DFAs and NFAs is that NFAs allow for multiple possible states to be reached from a given state and input symbol, 
whereas DFAs only allow for one state. This means that NFAs can recognize some languages that DFAs cannot. However, 
NFAs are more complex and harder to design and analyze than DFAs.

## Objectives:



- Understand what an automaton is and what it can be used for.

- Continuing the work in the same repository and the same project, the following need to be added: 

1. Provide a function in your grammar type/class that could classify the grammar based on Chomsky hierarchy.

2. For this you can use the variant from the previous lab.

- According to your variant number (by universal convention it is register ID), get the finite automaton definition and do the following tasks:

1. Implement conversion of a finite automaton to a regular grammar.

2. Determine whether your FA is deterministic or non-deterministic.

3. Implement some functionality that would convert an NFA to a DFA.

4. Represent the finite automaton graphically (Optional, and can be considered as a bonus point):


## Implementation description

### Grammar type

* The following method checks in decreasing order from the most restricted to the least the grammar type
```
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
```
* For the Regular Grammar type we have to check two cases
1. In the first one we have to verify if the length of the right side is equal to one 
and at right side there is only a terminal character
2. In the second case if the length of the right side is two there will 
also be two outcomes which we have to check, either the first symbol is non-terminal and the second is terminal 
or the opposite  
```
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
```
* In the case of Context Free Grammar we have to verify if there is only one 
non-terminal character at left side and also the right side can 
have both terminal and non-terminal symbols
```
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
```
* For the Context Sensitive Grammar only a single rule was checked, that being: If the 
left side's length is greater that the right side
```
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
```



### Finite automata to Grammar

* The FA to Grammar conversion simply takes all the parameters as they are 
in the FA, an exception being the production rules, and adds them to the grammar
* The production rules are done in two steps: 
1. First the method iterates through the states 
and then through the transition and checks whether there is a transition with a specific state
if there is then it is added to the production list
2. Second it iterates through the 
accepted states and also adds them to the production list 
```
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
                        productionRules.put(state, 
                        new ArrayList<>(List.of(transition.getTransitionLabel() + transition.getNextState())));
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
```

### Finite automata type

* To determine if the finite automaton is deterministic we have to verify three cases:
1. If the transitions are even valid and are in concordance with the states and alphabet
2. If the transition are unique, that is they have a unique state and label.
3. Lastly, if the transition lead to only one other transition or to multiple.
```
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
```

### NFA to DFA

* The convertToDFA method firstly has to find all the epsilon closures of all the states from NFA
* Then it computes the transitions for the DFA by going from the first state and taking the resulting states 
as the next state in each step
```
public FiniteAutomaton convertToDFA() {

        FiniteAutomaton dfa = new FiniteAutomaton(new Transition[0]);

        Map<Set<String>, Set<String>> epsilonClosures = new HashMap<>();
        for (String state : states) {
            Set<String> closure = getEpsilonClosure(state);
            epsilonClosures.put(closure, closure);
        }
        
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
```
* The Epsilon Closure function checks if there are any epsilon transition following from the input state given, 
which in turn have to be excluded.
```
    private Set<String> getEpsilonClosure(String state) {
        Set<String> closure = new HashSet<>();
        closure.add(state);
        Stack<String> stack = new Stack<>();
        stack.push(state);
        while(!stack.empty()) {
            String currentState = stack.pop();
            for(Transition t : transitions) {
                if(t.getCurrentState().equals(currentState) && t.getTransitionLabel().equals("") && !closure.contains(t.getNextState())) {
                    closure.add(t.getNextState());
                    stack.push(t.getNextState());
                }
            }
        }
        return closure;
    }
```
* This function simply verifies if a certain state is in the accepted states
```

    private boolean isAcceptState(Set<String> stateSet) {
        for (String state : stateSet) {
            if (acceptStates.contains(state)) {
                return true;
            }
        }
        return false;
    }
```
* The following method helps construct the states, transitions and so forth when there are multiple terms so 
that it is more concise and clear.  
```
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
```

## Results

```
Lab 1

Grammar

Non-terminal symbols = [A, B, C, S]
Terminal symbols = [a, b, c, d]
Starting character = S
Production rules = {A=[d, aB], B=[bC], S=[dA], C=[cA, aS]}
Regular (Type 3)

Lab 2

Non-deterministic finite automaton

Alphabet = [a, b]
States = [q1, q2, q0]
Initial state = q0
Accepting states = [q2]
Transitions = [{q0, a} -> {q1}, {q0, a} -> {q0}, {q1, b} -> {q2}, {q0, b} -> {q0}, {q1, b} -> {q1}, {q2, b} -> {q2}]
Is the current final automaton deterministic: false

Finite automaton to grammar 

Grammar

Staring symbol = q0
Non-terminal symbols = [q1, q2, q0]
Terminal symbols = [a, b]
Production rules = {q1=[bq2, bq1], q2=[bq2, e], q0=[aq1, aq0, bq0]}

Generate words: 

baaaabb
abbbb
aabb
abb
bbaaaabbb

NFA to DFA 

Deterministic Finite automaton

Alphabet = [a, b]
States = [{q1,q2,q0}, {q1,q0}, {q0}]
Initial state = {q0}
Accepting states = [{q1,q2,q0}]
Transitions = [{{q0}, a} -> {{q1,q0}}, {{q0}, b} -> {{q0}}, {{q1,q0}, a} -> {{q1,q0}}, 
{{q1,q0}, b} -> {{q1,q2,q0}}, {{q1,q2,q0}, a} -> {{q1,q0}}, {{q1,q2,q0}, b} -> {{q1,q2,q0}}]
Is the current final automaton deterministic: true
```

## Conclusion

Based on this laboratory work, I can conclude that I have successfully 
converted a Non-Deterministic Finite Automaton to a Deterministic 
Finite Automaton. 
This conversion allowed me to obtain a DFA that is equivalent to the original 
NFA in terms of language recognition. In addition to that I determined the Grammar type
of the previous Variant which turned out to be regular grammar.
Furthermore, I have also performed a Finite Automaton to Grammar conversion, 
where I have created a Grammar that generates the same 
language as the given FA. This conversion allowed me to represent the language 
recognized by the FA in a different form, which can be useful in various applications, 
including parsing.
After analyzing the obtained results, I have made a method
that determined that the original FA was a
Non-Deterministic Finite Automaton. Consequently, I made a function that converted it
to a Deterministic Finite Automaton.
In conclusion, this laboratory work has provided me with valuable insights 
into the conversion of NFAs to DFAs and FAs to CFGs, which are important concepts 
in theoretical computer science. I have successfully applied these concepts and 
techniques to solve problems related to language recognition and representation.