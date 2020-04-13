package core;

/**
 * This interface defines an arbitrary transition function of an authom.
 * The function is defined as f: Q x E -> Q
 * */
public interface TransitionFunction {

    /**
     * This is the computation behaviour
     * @param state the state on which the authom is
     * @param c the symbol reed by the authom
     * @return the state on which the computation moves
     * */
    String produce(String state, Character c);
}
