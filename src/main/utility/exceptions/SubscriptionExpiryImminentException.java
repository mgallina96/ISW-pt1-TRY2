package main.utility.exceptions;

/**
 * @author Manuel Gallina
 */
public class SubscriptionExpiryImminentException extends Exception {
    private int daysLeft;

    public SubscriptionExpiryImminentException(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public int getDaysLeft() {
        return daysLeft;
    }
}
