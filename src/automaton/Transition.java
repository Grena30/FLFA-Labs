package automaton;


public class Transition {

    private char currentState;
    private char nextState;
    private char transitionLabel;

    public Transition(char currentState, char nextState, char transitionLabel) {
        this.currentState = currentState;
        this.nextState = nextState;
        this.transitionLabel = transitionLabel;
    }
    public char getCurrentState() {
        return this.currentState;
    }
    public char getNextState() {
        return this.nextState;
    }
    public char getTransitionLabel() {
        return this.transitionLabel;
    }

    @Override
    public String toString() {
        return Character.toString(this.nextState);
    }
}