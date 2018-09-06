package main.view.screens;

import main.utility.exceptions.ScreenNotFoundException;
import main.utility.exceptions.SubscriptionExpiryImminentException;
import main.utility.exceptions.UserNotFoundException;
import main.utility.exceptions.WrongPasswordException;
import main.utility.notifications.Notifications;
import main.view.ScreenManager;
import main.view.listeners.LoginScreenListener;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Manuel Gallina
 * @since Version 1
 */
public class LoginScreen extends Screen {
    private ArrayList<LoginScreenListener> listenersList;

    public LoginScreen() {
        super("Login");
        listenersList = new ArrayList<>();
    }

    @Override
    public void initScreen(ScreenManager screenManager, Scanner scanner) {
        System.out.printf("%s%s%n%s%n",
                Notifications.getMessage("PROMPT_LOGIN_SCREEN"),
                Notifications.getMessage("MSG_LIBRARY_NAME"),
                Notifications.getMessage("SEPARATOR"));

        while(true) {
            String username;
            String password;

            System.out.printf("[%s %s]%n", ESCAPE_STRING, Notifications.getMessage("MSG_ESCAPE_STRING_MESSAGE"));

            try {
                username = inputRequest(Notifications.getMessage("PROMPT_USERNAME"), scanner);
                password = inputRequest(Notifications.getMessage("PROMPT_PASSWORD"), scanner);

                if(login(username, password).equals("operator")) {
                    screenManager.launch("operator");
                    break;
                }
                else {
                    screenManager.launch("customer");
                    break;
                }
            } catch(InterruptedException iEx) {
                System.out.printf("%s%n%n", Notifications.getMessage("MSG_EXIT_LOGIN"));
                break; //returns a null value in this case.
            } catch(UserNotFoundException unfEx) {
                System.out.println(Notifications.getMessage("ERR_USER_NOT_PRESENT"));
            } catch(WrongPasswordException wpEx) {
                System.out.println(Notifications.getMessage("ERR_WRONG_PASSWORD"));
            } catch(SubscriptionExpiryImminentException seiEx) {
                System.out.printf("%s%s %s %s.%n",
                        Notifications.getMessage("PROMPT_EXPIRY_IMMINENT"),
                        Notifications.getMessage("MSG_REMINDER_DAYS_LEFT"), seiEx.getDaysLeft(),
                        Notifications.getMessage("MSG_DAYS"));
                try {
                    screenManager.launch("customer");
                } catch(ScreenNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } catch(ScreenNotFoundException snfEx) {
                System.out.println(snfEx.getMessage());
            }

            System.out.printf("%s%n%s%n",
                    Notifications.getMessage("PROMPT_RETRY_LOGGING_IN"),
                    Notifications.getMessage("SEPARATOR"));
        }

        try {
            screenManager.launch("start");
        } catch(ScreenNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Asks for an input and acquires it. If the user types in {@code "!quit"}, the login section closes.
     */
    private String inputRequest(String prompt, Scanner scanner) throws InterruptedException {
        String input;

        System.out.print(prompt);
        input = scanner.next();
        scanner.nextLine();

        if(input.equals(ESCAPE_STRING))
            throw new InterruptedException();

        return input;
    }

    public void addListener(LoginScreenListener listenersList) {
        this.listenersList.add(listenersList);
    }

    private String login(String username, String password)
            throws UserNotFoundException, WrongPasswordException, SubscriptionExpiryImminentException {
        for(LoginScreenListener loginScreenListener : listenersList)
            if(loginScreenListener.onLogin(username, password) != null)
                return loginScreenListener.onLogin(username, password);
        return null;
    }
}
