package main.view.listeners;

import main.utility.exceptions.CannotRenewException;

/**
 * @author Manuel Gallina
 * @since Version 1
 */
public interface CustomerScreenListener {
    String getCurrentUserName();
    void renewSubscription() throws CannotRenewException;
}
