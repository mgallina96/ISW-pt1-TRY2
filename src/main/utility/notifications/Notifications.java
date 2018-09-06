package main.utility.notifications;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class that contains useful textual messages, warnings and notifications for this particular program.
 * <p>
 * All messages are loaded from .txt files that have to follow a specific set of rules in order to be read properly.
 * These messages then get loaded into a HashMap which binds every message to its identifier (e.g.: "Welcome!" gets
 * bound to "MSG_WELCOME"). This identifier can then be used to fetch the actual message which can then be printed.
 * <p>
 * All languages are potentially supported.
 *
 * @author Alessandro Polcini
 */
public class Notifications {

    public static final String LANGUAGE_ITA = "lang_ita.txt";
    public static final String LANGUAGE_ENG = "lang_eng.txt";

    private static final String DELIMITER = " -@ ";
    private static HashMap<String, String> messages;
    private static String currentLanguage;

    private Notifications() {
        //sets english as the default language
        messages = new HashMap<>();
        currentLanguage = LANGUAGE_ENG;
    }

    /**
     * Sets the language in which the notifications will be shown.
     *
     * @param language The language to be set.
     */
    public static void setLanguage(String language) {
        new Notifications();

        if(!language.equals(currentLanguage)) {
            currentLanguage = language;
        }

        updateLanguage();
    }

    //changes the language
    private static void updateLanguage() {
        messages.clear();

        File file = new File("resources\\languages\\" + currentLanguage);

        try (
            Scanner scanner = new Scanner(file)
        ) {
            while(scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split(DELIMITER);

                messages.put(split[0], split[1].replaceAll("\\\\t", "\t").replaceAll("\\\\n", "\n"));
            }
        }
        catch(Exception e) {
            Logger.getLogger("Notifications").log(Level.SEVERE, "ERROR");
        }
    }

    /**
     * Getter for the desired message.
     *
     * @param messageName The message-specific ID.
     * @return The actual message bound to its {@code messageName}.
     */
    public static String getMessage(String messageName) {
        return messages.get(messageName);
    }
}

