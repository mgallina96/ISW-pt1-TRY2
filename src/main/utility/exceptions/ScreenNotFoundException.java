package main.utility.exceptions;

/**
 * @author Manuel Gallina
 */
public class ScreenNotFoundException extends Exception {
    /**
     * Constructor.
     *
     * @param name The name of the unavailable scree.
     */
    public ScreenNotFoundException(String name) {
        super("The " + name + " screen is not available");
    }
}
