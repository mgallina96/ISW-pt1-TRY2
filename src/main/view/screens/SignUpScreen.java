package main.view.screens;

import main.utility.exceptions.IllegalDateFormatException;
import main.utility.notifications.Notifications;
import main.utility.exceptions.ScreenNotFoundException;
import main.utility.exceptions.UserAlreadyPresentException;
import main.view.ScreenManager;
import main.view.listeners.SignUpScreenListener;

import java.util.ArrayList;
import java.util.Scanner;

import static main.utility.InputParserUtility.*;

/**
 * @author Manuel Gallina
 * @since Version 1
 */
public class SignUpScreen extends Screen {
    private ArrayList<SignUpScreenListener> listenersList;

    public SignUpScreen() {
        super("SignUp");
        this.listenersList = new ArrayList<>();
    }

    @Override
    public void initScreen(ScreenManager screenManager, Scanner scanner) {
        System.out.printf("%s%s%n%s%n",
                Notifications.getMessage("PROMPT_SIGN_UP_SCREEN"),
                Notifications.getMessage("MSG_LIBRARY_NAME"),
                Notifications.getMessage("SEPARATOR"));

        boolean retry = true;
        do {
            try {
                fillDetails(scanner);
                System.out.println(Notifications.getMessage("MSG_SIGN_UP_SUCCESSFUL"));
                retry = false;
            } catch(InterruptedException iEx) {
                System.out.printf("%s %s%n", Notifications.getMessage("ERR_SIGN_UP_ABORTED"),
                        Notifications.getMessage("MSG_EXIT_WITHOUT_SAVING"));
                retry = false;
            } catch(IllegalDateFormatException idfEx) {
                System.out.println(Notifications.getMessage("ERR_NOT_OF_AGE"));
            } catch(UserAlreadyPresentException uapEx) {
                retry = userAlreadyPresent(scanner);
            }
        } while(retry);

        try {
            screenManager.launch("start");
        } catch(ScreenNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    //to insert sign-up details
    private void fillDetails(Scanner scanner)
            throws InterruptedException, IllegalDateFormatException, UserAlreadyPresentException {
        System.out.print(Notifications.getMessage("PROMPT_FIRST_NAME"));
        String firstName = insertName(scanner);

        System.out.print(Notifications.getMessage("PROMPT_LAST_NAME"));
        String lastName = insertName(scanner);

        System.out.print(Notifications.getMessage("PROMPT_USERNAME"));
        String username = scanner.nextLine();

        System.out.print(Notifications.getMessage("PROMPT_PASSWORD"));
        String password = scanner.nextLine();

        System.out.print(Notifications.getMessage("PROMPT_BIRTH_DATE"));
        String birthDate = insertDate(scanner);

        System.out.printf("%s%n%s%n%s%n", Notifications.getMessage("SEPARATOR"),
                Notifications.getMessage("PROMPT_SIGN_UP_CONFIRMATION"),
                Notifications.getMessage("SEPARATOR")
        );

        if(insertString(YN_REGEX, scanner).equalsIgnoreCase(NO))
            throw new InterruptedException();

        signUp(firstName, lastName, username, password, birthDate);
    }

    //routine that gets executed if the user is trying to sign up but is already present in the database
    private boolean userAlreadyPresent(Scanner scanner) {
        System.out.printf("%s%n%s%n%s%n%s%n",
                Notifications.getMessage("ERR_USER_ALREADY_PRESENT"),
                Notifications.getMessage("SEPARATOR"),
                Notifications.getMessage("PROMPT_PRESENT_USER_MULTIPLE_CHOICE"),
                Notifications.getMessage("SEPARATOR"));

        switch(insertInteger(1, 3, scanner)) {
            case 1:
                System.out.printf("%s %s%n",
                        Notifications.getMessage("MSG_EXIT_WITHOUT_SAVING"),
                        Notifications.getMessage("MSG_MOVE_TO_LOGIN"));
                return false;
            case 2:
                System.out.println(Notifications.getMessage("PROMPT_MODIFY_FIELDS"));
                return true;
            default:
                return false;
        }
    }

    public void addListener(SignUpScreenListener listener) {
        this.listenersList.add(listener);
    }

    private void signUp(String firstName, String lastName, String username, String password, String birthDate)
            throws IllegalDateFormatException, UserAlreadyPresentException {
        for(SignUpScreenListener signUpScreenListener : listenersList)
            signUpScreenListener.onSignUp(firstName, lastName, username, password, birthDate);
    }
}
