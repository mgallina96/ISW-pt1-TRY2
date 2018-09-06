package main.model.user;

import main.utility.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for user personal data management.
 *
 * @author Manuel Gallina
 */
class UserDatabaseTest {
    private UserDatabase testDatabase1;
    private UserDatabase testDatabase2;
    private UserDatabase testDatabase3;
    private UserDatabase testDatabase4;

    @BeforeEach
    void setUp() {
        testDatabase1 = new UserDatabase(false);
        testDatabase2 = new UserDatabase(false);
        testDatabase3 = new UserDatabase(false);
        testDatabase4 = new UserDatabase(false);
    }

    @Test
    void onSignUp() {
        Executable correct = () -> testDatabase1.onSignUp("Giorgio", "Franchi", "giorgiofranchi",
                    "12345", "01/01/1970");
        Executable duplicate = () -> testDatabase1.onSignUp("Giorgio", "Clone", "giorgiofranchi",
                "54321", "01/01/1960");
        Executable underage = () -> testDatabase1.onSignUp("Giorgio", "Franchino", "giorgiofranchino",
                "11111", "02/09/2018");

        assertDoesNotThrow(correct);
        assertThrows(UserAlreadyPresentException.class, duplicate);
        assertThrows(IllegalDateFormatException.class, underage);
    }

    @Test
    void onLogin() {
        Executable userNotFound = () -> testDatabase2.onLogin("mariorossi", "12345");
        Executable wrongPassword = () -> testDatabase2.onLogin("giorgiofranchi", "54321");
        Executable correct = () -> testDatabase2.onLogin("giorgiofranchi", "12345");

        User testUser = new User("giorgiofranchi", "12345",
                "Giorgio", "Franchi", new GregorianCalendar(1970, Calendar.JANUARY, 1));

        testDatabase2.addUser(testUser);

        assertThrows(UserNotFoundException.class, userNotFound);
        assertThrows(WrongPasswordException.class, wrongPassword);
        assertDoesNotThrow(correct);

        assertEquals(testUser, testDatabase2.getCurrentUser());
    }

    @Test
    void sweep() {
        User testUser1 = new User(false,"giorgiofranchi", "11111",
                "Giorgio", "Franchi", new GregorianCalendar(1970, Calendar.JANUARY, 1),
                new GregorianCalendar(1970, Calendar.JANUARY, 1));
        User testUser2 = new User(true,"mariorossi", "22222",
                "Mario", "Rossi", new GregorianCalendar(1970, Calendar.JANUARY, 1),
                new GregorianCalendar(1970, Calendar.JANUARY, 1));
        User testUser3 = new User(false,"mariabianchi", "33333",
                "Maria", "Bianchi", new GregorianCalendar(1970, Calendar.JANUARY, 1),
                new GregorianCalendar());

        testDatabase3.addUser(testUser1);
        testDatabase3.addUser(testUser2);
        testDatabase3.addUser(testUser3);

        testDatabase3.sweep();

        assertEquals(3, testDatabase3.getUserDataSize());
    }

    @Test
    void renew() {
        User testUser1 = new User(false,"giorgiofranchi", "11111",
                "Giorgio", "Franchi", new GregorianCalendar(1970, Calendar.JANUARY, 1)
                );
        User testUser2 = new User(false,"mariorossi", "22222",
                "Mario", "Rossi", new GregorianCalendar(1970, Calendar.JANUARY, 1)
                );

        testUser1.getSubscriptionExpiryDate().add(Calendar.DAY_OF_YEAR, 5);
        testUser1.getSubscriptionExpiryDate().add(Calendar.YEAR, -5);

        Executable canRenew = () -> {
            testDatabase4.renewSubscription();
        };

        Executable cannotRenew = () -> {
            testDatabase4.onLogin(testUser2.getUsername(), testUser2.getPassword());
            testDatabase4.renewSubscription();
        };

        testDatabase4.addUser(testUser1);
        testDatabase4.addUser(testUser2);
        try {
            testDatabase4.onLogin(testUser1.getUsername(), testUser1.getPassword());
        } catch(UserNotFoundException | WrongPasswordException | SubscriptionExpiryImminentException ignored) {
        }

        assertDoesNotThrow(canRenew);
        assertThrows(CannotRenewException.class, cannotRenew);
    }
}