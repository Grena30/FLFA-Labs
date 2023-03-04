package automaton;


public class Transition {

    private String currentState;
    private String nextState;
    private String transitionLabel;

    public Transition(String currentState, String nextState, String transitionLabel) {
        this.currentState = currentState;
        this.nextState = nextState;
        this.transitionLabel = transitionLabel;
    }
    public String getCurrentState() {
        return this.currentState;
    }
    public String getNextState() {
        return this.nextState;
    }
    public String getTransitionLabel() {
        return this.transitionLabel;
    }

    @Override
    public String toString() {
        return this.nextState;
    }
}