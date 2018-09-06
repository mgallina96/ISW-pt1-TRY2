package main.model.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static main.GlobalParameters.EXPIRY_TIME_IN_YEARS;

/**
 *
 *
 * @author Manuel Gallina
 * @since Version 1
 */
public class User implements Serializable {
    private static final boolean DEFAULT_OPERATOR_STATE = false;

    private static final GregorianCalendar DEFAULT_SUBSCRIPTION_DATE = new GregorianCalendar();
    private static final GregorianCalendar DEFAULT_SUBSCRIPTION_EXPIRY_DATE = new GregorianCalendar();

    private String username;
    private String password;
    private boolean operator;

    private String firstName;
    private String lastName;
    private GregorianCalendar birthDate;
    private GregorianCalendar subscriptionDate;
    private ArrayList<GregorianCalendar> renewalDates;

    private GregorianCalendar subscriptionExpiryDate;

    public User(boolean operator, String username, String password,
                String firstName, String lastName, GregorianCalendar birthDate, GregorianCalendar subscriptionDate,
                GregorianCalendar subscriptionExpiryDate) {
        this.operator = operator;
        this.username = username;
        this.password = password;

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.subscriptionDate = subscriptionDate;

        this.subscriptionExpiryDate = subscriptionExpiryDate;
        this.renewalDates = new ArrayList<>();
    }

    public User(boolean operator, String username, String password,
                String firstName, String lastName, GregorianCalendar birthDate, GregorianCalendar subscriptionDate) {
        this(operator, username, password,
                firstName, lastName, birthDate, subscriptionDate,
                DEFAULT_SUBSCRIPTION_EXPIRY_DATE);
        this.subscriptionExpiryDate = (GregorianCalendar) subscriptionDate.clone();
        this.subscriptionExpiryDate.add(Calendar.YEAR, EXPIRY_TIME_IN_YEARS);
    }

    public User(boolean operator, String username, String password,
                String firstName, String lastName, GregorianCalendar birthDate) {
        this(operator, username, password,
                firstName, lastName, birthDate, DEFAULT_SUBSCRIPTION_DATE);
    }

    public User(String username, String password,
                String firstName, String lastName, GregorianCalendar birthDate) {
        this(DEFAULT_OPERATOR_STATE, username, password,
                firstName, lastName, birthDate);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOperator() {
        return operator;
    }

    public void setOperator(boolean operator) {
        this.operator = operator;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public GregorianCalendar getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(GregorianCalendar birthDate) {
        this.birthDate = birthDate;
    }

    public GregorianCalendar getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(GregorianCalendar subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public GregorianCalendar getSubscriptionExpiryDate() {
        return subscriptionExpiryDate;
    }

    public void setSubscriptionExpiryDate(GregorianCalendar subscriptionExpiryDate) {
        this.subscriptionExpiryDate = subscriptionExpiryDate;
    }

    public ArrayList<GregorianCalendar> getRenewalDates() {
        return renewalDates;
    }

    @Override
    public String toString() {
        return "\nFirst Name: " + this.firstName + "\t\t" +
                "Last Name: " + this.lastName + "\t\t" +
                "Username: " + this.username + "\t\t" +
                "Password: " + this.password + "\t\t" +
                "Birth Date: " + this.birthDate.get(Calendar.DAY_OF_MONTH) + "/" +
                (this.birthDate.get(Calendar.MONTH) + 1) + "/" +
                    this.birthDate.get(Calendar.YEAR) + "\t\t" +
                "Subscription Date: " + this.subscriptionDate.get(Calendar.DAY_OF_MONTH) + "/" +
                    (this.subscriptionDate.get(Calendar.MONTH) + 1) + "/" +
                    this.subscriptionDate.get(Calendar.YEAR);
    }
}
