package main.utility.exceptions;

/**
 * @author Manuel Gallina
 */
public class CannotRenewException extends Exception {
    private String supscriptionDate;
    private String lastRenewalDate;
    private String expiryDate;

    public CannotRenewException(String supscriptionDate, String lastRenewalDate, String expiryDate) {
        this.supscriptionDate = supscriptionDate;
        this.lastRenewalDate = lastRenewalDate;
        this.expiryDate = expiryDate;
    }

    public String getSupscriptionDate() {
        return supscriptionDate;
    }

    public String getLastRenewalDate() {
        return lastRenewalDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
