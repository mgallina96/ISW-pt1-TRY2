package main.view;

import main.utility.exceptions.ScreenNotFoundException;
import main.view.screens.Screen;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class manages the interactions between the screens of the application. It works only with
 * textual interfaces, subclasses of the {@link Screen}.
 *
 * @author Manuel Gallina
 * @since Version 1
 */
public class ScreenManager {
    private Screen root;
    private ArrayList<Screen> screens;

    private Scanner scanner;

    /**
     * Constructor.
     *
     * @param root The first screen to be launched.
     * @param scanner The input scanner.
     */
    public ScreenManager(Screen root, Scanner scanner) {
        this.root = root;
        this.screens = new ArrayList<>();

        this.getScreens().add(root);
        this.scanner = scanner;
    }

    /**
     * Returns the list of available screens.
     *
     * @return The list of available screens.
     */
    public ArrayList<Screen> getScreens() {
        return screens;
    }

    /**
     * Launches the root screen of the application.
     */
    public void start() {
        this.root.initScreen(this, scanner);
    }

    /**
     * Launches the requested screen, if present.
     *
     * @param screenName The name of the requested screen.
     * @throws ScreenNotFoundException If the requested screen cannot be found in the list of the available screens.
     */
    public void launch(String screenName) throws ScreenNotFoundException {
        boolean found = false;
        for(Screen screen : screens) {
            if(screen.getName().equalsIgnoreCase(screenName)) {
                found = true;
                screen.initScreen(this, scanner);
                break;
            }
        }

        if(!found)
            throw new ScreenNotFoundException(screenName);
    }
}
