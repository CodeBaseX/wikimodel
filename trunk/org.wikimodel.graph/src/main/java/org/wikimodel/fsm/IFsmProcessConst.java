/**
 * 
 */
package org.wikimodel.fsm;

/**
 * @author kotelnikov
 */
public interface IFsmProcessConst {

    /**
     * Process returns control when it enters in a new state
     */
    int DEBUG_IN = 1; // STEP_DOWN

    /**
     * Process returns control when it goes out of a state
     */
    int DEBUG_OUT = 4; // STEP_UP

    /**
     * If this flag is used then the process returns the control when the
     * process enters in a terminal state (a state without children)
     */
    int DEBUG_TERMINAL = 1 | 4; // STEP_DOWN | STEP_NO_NEXT

    /**
     * 
     */
    String STATE_ANY = "*";

    /**
     *
     */
    String STATE_FINAL = "{final}";

    /**
     * The key of initial states
     */
    String STATE_INITIAL = "{initial}";

    /**
     * Returns control if the last visited node has at least one child node.
     */
    int STEP_DOWN = 1;

    /**
     * This bit is used to mark that the process has a next state to visit; it
     * means that the topmost state in the stack has at least one more
     * sub-state.
     */
    int STEP_NEXT = 2;

    /**
     * This bit is used to mark that the process has a next state to visit; it
     * means that the topmost state in the stack has at least one more
     * sub-state.
     */
    int STEP_NO_NEXT = 4;

    /**
     * Returns control if the last visited had no more non visited sibling on
     * the same level.
     */
    int STEP_UP = 8;

}
