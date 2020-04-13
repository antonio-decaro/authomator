package core;

import java.util.Set;

/**
 * This class rapresents the concept of a DFA (Deterministic Finite Authome)
 * */
public class Authom {

    /**
     * Class constructor
     * @param states the set of all the states of the authom
     * @param alphabet the set of all simbols that the authom can compute
     * @param function the transition function: f(state, simbol) -> state
     * @param initState the initial state; initState must be inside the set 'states';
     * @param finalStates the set of all final-states of the authom; this set must be a subset of the set 'states'
     * */
    public Authom(Set<String> states, Set<String> alphabet, TransitionFunction function, String initState, Set<String> finalStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.function = function;
        this.initState = this.currentState = initState;
        this.finalStates = finalStates;
    }

    /**
     * Execs the transition function over the input string and then return a result
     * @param input the input string
     * @return  - true if the computation ends in a final state;
     *          - false if the computation ends in a non-final state or compute a simbol not incluse in the alphabet;
     * */
    public boolean compute(String input) {
        for (int i = 0; i < input.length(); i++) {
            char e = input.charAt(i);
            currentState = function.produce(currentState, e);
            if (currentState == null || !states.contains(currentState))
                return false;
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
