package main.view.listeners;

import main.utility.exceptions.SubscriptionExpiryImminentException;
import main.utility.exceptions.UserNotFoundException;
import main.utility.exceptions.WrongPasswordException;

/**
 * @author Manuel Gallina
 * @since Version 1
 */
public interface LoginScreenListener {
    String onLogin(String username, String password)
            throws UserNotFoundException, WrongPasswordException, SubscriptionExpiryImminentException;
}
