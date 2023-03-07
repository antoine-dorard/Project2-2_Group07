package backend;

/**
 * This interface is to be implemented by input processors. It can be used when
 * the text response is computed in a single step (i.e. when the computation time is short).
 * Use {@link MultiStepInputProcessor} if the computation time is long.
 */
public interface InputProcessor {

    /**
     * This method processes the user input and returns the bot response.
     * @param input The user input.
     * @return The bot response.
     */
    String processInput(String input);
}
