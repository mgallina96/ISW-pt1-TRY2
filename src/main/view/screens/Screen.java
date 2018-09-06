package main.view.screens;

import main.view.ScreenManager;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * A generic textual screen.
 *
 * @author Manuel Gallina
 * @since Version 1
 */
public class Screen {
    static final String ESCAPE_STRING = "!quit";
    static final String YES = "y";
    static final String NO = "n";
    static final String YN_REGEX = "[yYnN]";

    private String name;

    /**
     * Constructor.
     *
     * @param name The name of this screen.
     */
    public Screen(String name) {
        this.name = name;
    }

    /**
     * Initializes this screen.
     *
     * @param screenManager The screen manager.
     * @param scanner The input scanner.
     */
    public void initScreen(ScreenManager screenManager, Scanner scanner) {
        System.out.println(this.name + " screen");
    }

    /**
     * Returns the name of this screen.
     *
     * @return The name of this screen.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this screen.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}
