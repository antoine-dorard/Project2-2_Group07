package backend;

/**
 * This interface is to be implemented by input processors. It can be used when
 * the text response is processed in multiple steps. This is useful when the
 * response computation is time-consuming, because it allows the GUI to update
 * regularly and gives a smooth user experience.
 */
public interface MultiStepInputProcessor {

    /**
     * This method is called when the user presses the send button. It should
     * start the computation of the response.
     * @param input The user input.
     */
    void startResponseComputation(String input);

    /**
     * This method checks if the response computation is finished.
     * @return True if the response computation is finished, false otherwise.
     */
    boolean isResponseComplete();

    /**
     * This method returns the next part of the response. It should be called
     * repeatedly until the response computation is finished.
     * @return The next part of the response.
     */
    String getNextTextPart();

    /**
     * This method returns the current response. It should be called after the
     * response computation is finished.
     * @return The current response.
     */
    String getCurrentResponse();
}
