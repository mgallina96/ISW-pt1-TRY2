package main.view.screens;

import main.utility.notifications.Notifications;
import main.utility.exceptions.ScreenNotFoundException;
import main.view.ScreenManager;

import java.util.Scanner;

import static main.utility.InputParserUtility.insertInteger;

/**
 * The first screen of the application.
 *
 * @author Manuel Gallina
 * @since Version 1
 */
public class StartScreen extends Screen {
    /** Constructor. */
    public StartScreen() {
        super("Start");
    }

    @Override
    public void initScreen(ScreenManager screenManager, Scanner scanner) {
        System.out.printf("%s%n%s%n%s%n%s%n> ",
                Notifications.getMessage("MSG_LIBRARY_NAME"),
                Notifications.getMessage("SEPARATOR"),
                Notifications.getMessage("PROMPT_LIBRARY_INITIAL_CHOICES"),
                Notifications.getMessage("SEPARATOR"));

        try {
            switch(insertInteger(1, 5, scanner)) {
                case 1: {
                    screenManager.launch("Login");
                    break;
                }
                case 2: {
                    screenManager.launch("SignUp");
                    break;
                }
                case 3: {
                    screenManager.launch("Settings");
                    break;
                }
                case 4:
                    break;
                default:
                    break;
            }
        } catch(ScreenNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
