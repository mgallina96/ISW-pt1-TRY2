package main.view.listeners;

import main.utility.exceptions.IllegalDateFormatException;
import main.utility.exceptions.UserAlreadyPresentException;

/**
 * @author Manuel Gallina
 * @since Version 1
 */
public interface SignUpScreenListener {
    void onSignUp(String firstName, String lastName, String username, String password, String birthDate)
            throws IllegalDateFormatException, UserAlreadyPresentException;
}
