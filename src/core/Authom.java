package core;

import java.util.Set;

public class Authom {

    public Authom(Set<String> states, Set<String> alphabet, TransitionFunction function, String initState, Set<String> finalStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.function = function;
        this.initState = this.currentState = initState;
        this.finalStates = finalStates;
    }

    public boolean compute(String input) {
        for (int i = 0; i < input.length(); i++) {
            char e = input.charAt(i);
            currentState = function.produce(currentState, e);
        }
        return finalStates.contains(currentState);
    }

    public Set<String> getStates() {
        return states;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public TransitionFunction getFunction() {
        return function;
    }

    public String getInitState() {
        return initState;
    }

    public Set<String> getFinalStates() {
        return finalStates;
    }

    private Set<String> states;
    private Set<String> alphabet;
    private TransitionFunction function;
    private String initState;
    private Set<String> finalStates;
    private String currentState;
}
