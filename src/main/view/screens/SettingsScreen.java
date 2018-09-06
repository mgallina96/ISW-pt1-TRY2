package main.view.screens;

import main.utility.notifications.Notifications;
import main.utility.exceptions.ScreenNotFoundException;
import main.view.ScreenManager;

import java.util.Scanner;

/**
 * @author Manuel Gallina
 * @since Version 1
 */
public class SettingsScreen extends Screen {
    /** Constructor. */
    public SettingsScreen() {
        super("Settings");
    }

    @Override
    public void initScreen(ScreenManager screenManager, Scanner scanner) {
        System.out.println(Notifications.getMessage("PROMPT_CHOOSE_LANGUAGE"));

        String lang = scanner.nextLine();

        if(lang.equalsIgnoreCase("ita"))
            Notifications.setLanguage(Notifications.LANGUAGE_ITA);
        else if(lang.equalsIgnoreCase("eng"))
            Notifications.setLanguage(Notifications.LANGUAGE_ENG);
        else
            System.out.println(Notifications.getMessage("ERR_LANGUAGE_UNAVAILABLE"));


        try {
            screenManager.launch("Start");
        } catch(ScreenNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
