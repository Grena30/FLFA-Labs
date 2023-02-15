
#  Introduction to formal languages

## Course: Formal Languages & Finite Automata
## Author: Gutu Dinu

---

## Theory

Formal languages can be defined by a set of rules, 
and they represent a collection of strings. Finite automata, on 
the other hand, are computational models that use a defined set of rules 
to determine whether to accept or reject strings. If a finite automaton is 
capable of accepting every string in a language, then that language is
considered to be recognized by the automaton. Finite automata are used 
in many different applications, including lexical analysis in compilers 
and natural language processing, due to their ability to systematically 
analyze and process sets of strings.

## Objectives:
- Understand what a language is and what it needs to have in order to be considered a formal one.
- Provide the initial setup for the evolving project:
 1. Create a local && remote repository of a VCS hosting service
 2. Choose a programming language
 3. Create a separate folder the report will be kept
- Based on the given variant, my case 20, get the grammar definition and do the following tasks:
 1. Implement a type/class for the grammar;
 2. Add one function that would generate 5 valid strings from the language expressed by the given grammar
 3. Implement some functionality that would convert and object of type Grammar to one of type Finite Automaton;
 4. For the Finite Automaton, add a method that checks if an input string can be obtained via the state transition from it

## Implementation description
### Grammar Class

- The class has 4 variables for each of the grammar's components
- In the constructor it takes the hashsets and maps made manually in Main file

```
public class Grammar {

    private char startSymbol;
    private Set<Character> terminals;
    private Set<Character> nonTerminals;
    private Map<Character, List<String>> productionRules;

    public Grammar(char startSymbol, Set<Character> terminals, Set<Character> nonTerminals, 
                   Map<Character, List<String>> productionRules) {
                   
        this.startSymbol = startSymbol;
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.productionRules = productionRules;
    }
```
    

### Word generation

- The function generates recursively the word by looking through the production rules and applying them
- A random function looks through the options of a given non-terminal symbol's right side, that is the rules, and chooses which one to select
- With the use of the String Builder is created a variable to which will be attributed the terminal symbols that will be found in the production rule selected 
- In a for loop the generateWord function is recursively called for each symbol found and if it is a terminal it is being added to the word otherwise if it is a non-terminal symbol it goes through the same process of choosing the production rule of that symbol and doing the for loop all over again until it finds a single terminal symbol
```
  private String generateWord(char symbol) {

        if (terminals.contains(symbol)) {
            return Character.toString(symbol);
        }
        
        List<String> rightHandSides = productionRules.get(symbol);
        String randomRightHandSide = rightHandSides.get(new Random().nextInt(rightHandSides.size()));
        StringBuilder word = new StringBuilder();
        
        for (char rightSymbol : randomRightHandSide.toCharArray()) {
            word.append(generateWord(rightSymbol));
        }
        
        return word.toString();
    }
```
### Grammar to Finite Automaton
- The following method converts the above grammar to a finite automaton.
- The alphabet is simply all the terminal symbols.
- Initial state is the starting symbol.
- The accepted state or final state is simply "X", denoting the end of the transitions.
- It creates the possible states by taking the union between the non-terminal symbols and final state "X".
- The transitions are built by looping through each non-terminal symbol and viewing the production rules for each of them and adding them to a transition array
- After that the method returns an automaton of type FiniteAutomaton
```
public FiniteAutomaton toFiniteAutomaton() {

        Set<String> states = new HashSet<>();
        Set<String> finalStates = new HashSet<>(Set.of("X"));
        int dimension = 0;
        int dimCheck = 0;

        for (char nonTerminal : nonTerminals) {
            dimension += productionRules.get(nonTerminal).size();
        }

        Transition[] transition = new Transition[dimension];

        for (char nonTerminal : nonTerminals) {
            states.add(Character.toString(nonTerminal));
            List<String> rightHandSides = productionRules.get(nonTerminal);
            for ( String str: rightHandSides){
                char nextState;
                if (str.length() > 1){
                    nextState = str.charAt(1);
                } else {
                    nextState = 'X';
                }
                char transitionLabel = str.charAt(0);
                transition[dimCheck] = new Transition(nonTerminal, nextState, transitionLabel);
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
```

### Word validity

- The method verifies if a given string is a valid word based on the existing rules and transitions.
- It loops through each character of the string and checks if there exists a transition that satisfies the existence of this character.
- For each character it loops through all the transitions to get the current transition state and compare it with the one current to the method and also verify if the character is the same as in the transition label.
- If it finds a transition it continues until it reaches an accept state, otherwise there cannot be made such a word with the existing production rules and so it returns false
```
public boolean wordIsValid(String word) {
        char currentState = startState.charAt(0);
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            boolean foundTransition = false;
            for (Transition t : transitions) {
                if (t.getCurrentState() == currentState && t.getTransitionLabel() == c) {
                    currentState = t.getNextState();
                    foundTransition = true;
                    break;
                }
            }
            if (!foundTransition) {
                return false;
            }
        }
        return acceptStates.contains(Character.toString(currentState));
    }
```
## Results

```
1. Grammar

Non-terminal symbols = [A, B, S, C]
Terminal symbols = [a, b, c, d]
Starting character = S
Production rules = {A=[d, aB], B=[bC], S=[dA], C=[cA, aS]}

2. Generated words:

dabcd
dabadabcabcabcabcd
dd
dd
dabadabcd

3. Finite automaton

Alphabet = [a, b, c, d]
States = [A, B, S, C, X]
Initial state = S
Accepting states = [X]
Transitions = [X, B, C, A, A, S]

4. Testing word validity

dd - true
acccd - false
str - false
dabcabadd - true
- false (empty string)
dabcabadabadd - true
dabadd - true
dabcd - true
001 - false
dabadd - true
dabcabcd - true
dabadabcd - true

```


## Conclusion

By completing the laboratory work I can to attest to having 
gained a better grasp of formal languages and successfully implemented a 
grammar and finite automaton in code.
Throughout the project, I learned valuable skills such as creating a grammar class, 
generating valid strings, and converting an object to a finite automaton, 
which all helped me to solidify my understanding of the underlying theory. 
Creating the conversion method from grammar to a finite automaton and the one 
which verified the validity of a given word proved to be the most insightful 
as they both helped me understand the relations between them and how 
formal languages as whole are structured. Furthermore, it paved the 
way for more advanced work that is available in this subject's area.