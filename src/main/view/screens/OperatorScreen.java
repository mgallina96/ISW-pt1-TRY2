package main.view.screens;

import main.utility.data.Field;
import main.utility.exceptions.MediaNotFoundException;
import main.utility.exceptions.MediaTypeNotFoundException;
import main.utility.exceptions.PathNotFoundException;
import main.utility.notifications.Notifications;
import main.view.ScreenManager;
import main.view.listeners.OperatorScreenListener;

import java.util.ArrayList;
import java.util.Scanner;

import static main.utility.InputParserUtility.*;

/**
 * @author Manuel Gallina
 * @since Version 1
 */
public class OperatorScreen extends Screen {
    private ArrayList<OperatorScreenListener> listenersList;

    /** Constructor. */
    public OperatorScreen() {
        super("operator");
        listenersList = new ArrayList<>();
    }

    @Override
    public void initScreen(ScreenManager screenManager, Scanner scanner) {
        boolean exitFromOperatorSection = false;

        while(!exitFromOperatorSection) {
            System.out.printf("%s%n%s\t\t\t\t\t\t%s%s%n%s%n",
                    Notifications.getMessage("SEPARATOR"),
                    Notifications.getMessage("PROMPT_OPERATOR_CHOICES"),
                    Notifications.getMessage("MSG_LOGGED_IN_AS"), getCurrentUserName(),
                    Notifications.getMessage("SEPARATOR"));

            switch(insertInteger(1, 8, scanner)) {
                case 1: {
                    addMediaRoutine(scanner);
                    break;
                }
                case 2: {
                    removeMediaRoutine(scanner);
                    break;
                }
                case 3: {
                    visualizeCategoryContentRoutine(scanner);
                    break;
                }
                case 4:
                    break;
                case 5:
                    System.out.println(getUsersList());
                    break;
                case 6:
                    System.out.printf("%s%n", Notifications.getMessage("MSG_LOG_OUT"));
                    exitFromOperatorSection = true;
                    break;
                default:
                    break;
            }
        }
    }

    private void visualizeCategoryContentRoutine(Scanner scanner) {
        System.out.println(Notifications.getMessage("PROMPT_SEARCH_FOR_MEDIA"));
        System.out.println(getFolderTree());

        boolean done = false;
        String output = null;
        while(!done) {
            try {
                String category = scanner.nextLine();
                if(category.equals(ESCAPE_STRING))
                    System.out.println(Notifications.getMessage("MSG_ABORT"));
                output = getMediaList(category);
                done = true;
            } catch(PathNotFoundException pnfEx) {
                System.out.println("Invealid path. Retry.\n");
            }
        }
        if(output.length() > 0)
            System.out.printf("%s%n%s%n", Notifications.getMessage("MSG_FILTERED_MEDIA_LIST"), output);
        else
            System.out.println(Notifications.getMessage("ERR_FILTERED_MEDIA_LIST_EMPTY"));

    }

    private void removeMediaRoutine(Scanner scanner) {
        System.out.println(Notifications.getMessage("PROMPT_REMOVE_MEDIA_ID"));
        boolean done = false;

        while(!done) {
            int id = insertInteger(scanner);
            System.out.println(Notifications.getMessage("PROMPT_REMOVE_CONFIRMATION"));
            if(insertString(YN_REGEX, scanner).equalsIgnoreCase(YES)) {
                try {
                    this.removeMedia(id);
                } catch(MediaNotFoundException mnfEx) {
                    System.out.println(Notifications.getMessage("ERR_MEDIA_NOT_PRESENT"));
                    id = insertInteger(scanner);
                }
                System.out.println(Notifications.getMessage("MSG_REMOVE_SUCCESSFUL"));
                done = true;
            } else
                System.out.println(Notifications.getMessage("MSG_ABORT"));
        }
    }

    private void addMediaRoutine(Scanner scanner) {
        boolean done = false;

        while(!done) {
            System.out.println(Notifications.getMessage("PROMPT_CHOOSE_MEDIA_TYPE"));
            String mediaType = scanner.next();

            ArrayList<String> values = new ArrayList<>();
            try {
                ArrayList<Field> fields = this.getTypeValues(mediaType);
                assert fields != null;
                for(Field field : fields) {
                    System.out.println(field.getName() + ": ");
                    switch(field.getType()) {
                        case STRING: {
                            values.add(insertString("[A-Z0-9].+", scanner));
                            break;
                        }
                        case INTEGER: {
                            values.add(Integer.toString(insertInteger(scanner)));
                            break;
                        }
                        case DATE: {
                            values.add(insertDate(scanner));
                            break;
                        }
                        case YEAR: {
                            values.add(Integer.toString(insertYear(scanner)));
                            break;
                        }
                        default:
                            break;
                    }
                }

                System.out.printf("%s%n%s%n",
                        Notifications.getMessage("SEPARATOR"),
                        Notifications.getMessage("PROMPT_SELECT_PATH")
                );
                System.out.println(this.getFolderTree());
                boolean validPath = false;
                String path = null;
                while(!validPath) {
                    path = scanner.nextLine();
                    try {
                        isValidPath(path);
                        validPath = true;
                    } catch(PathNotFoundException e) {
                        System.out.println("Invalid Path. Retry\n");
                    }
                }
                this.addMedia(mediaType, values, path);
                done = true;
            } catch(MediaTypeNotFoundException e) {
                System.out.println(Notifications.getMessage("ERR_MSG_INVALID_INPUT"));
            }
        }
    }

    public void addListener(OperatorScreenListener listener) {
        listenersList.add(listener);
    }

    private String getCurrentUserName() {
        for(OperatorScreenListener operatorScreenListener : listenersList) {
            if(operatorScreenListener.getCurrentUserName() != null)
                return operatorScreenListener.getCurrentUserName();
        }
        return null;
    }

    private String getUsersList() {
        for(OperatorScreenListener operatorScreenListener : listenersList) {
            if(operatorScreenListener.getUserList() != null)
                return operatorScreenListener.getUserList();
        }
        return null;
    }

    private void addMedia(String mediaType, ArrayList<String> values, String category) {
        for(OperatorScreenListener operatorScreenListener : listenersList)
            operatorScreenListener.addMedia(mediaType, values, category);
    }

    private void isValidPath(String path) throws PathNotFoundException {
        for(OperatorScreenListener operatorScreenListener : listenersList)
            operatorScreenListener.isValidPath(path);
    }

    private ArrayList<Field> getTypeValues(String mediaType) throws MediaTypeNotFoundException {
        for(OperatorScreenListener operatorScreenListener : listenersList) {
            if(operatorScreenListener.getTypeValues(mediaType) != null)
                return operatorScreenListener.getTypeValues(mediaType);
        }
        return null;
    }

    private String getFolderTree() {
        for(OperatorScreenListener operatorScreenListener : listenersList) {
            if(operatorScreenListener.getFolderTree() != null)
                return operatorScreenListener.getFolderTree();
        }
        return null;
    }

    private void removeMedia(int mediaId) throws MediaNotFoundException {
        for(OperatorScreenListener operatorScreenListener : listenersList)
            operatorScreenListener.removeMedia(mediaId);
    }

    private String getMediaList(String category) throws PathNotFoundException {
        for(OperatorScreenListener operatorScreenListener : listenersList) {
            if(operatorScreenListener.getMediaList(category) != null)
                return operatorScreenListener.getMediaList(category);
        }
        return null;
    }
}
