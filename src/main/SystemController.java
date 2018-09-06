package main;

import main.model.Database;
import main.model.media.filesystem.FileSystem;
import main.model.media.MediaDatabase;
import main.model.user.UserDatabase;
import main.utility.data.DataType;
import main.utility.data.Field;
import main.utility.notifications.Notifications;
import main.view.*;
import main.view.listeners.CustomerScreenListener;
import main.view.listeners.LoginScreenListener;
import main.view.listeners.OperatorScreenListener;
import main.view.listeners.SignUpScreenListener;
import main.view.screens.*;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Manuel Gallina
 * @since Version 1
 */
public class SystemController {
    private static SystemController instance;

    private Database userDatabase;
    private Database mediaDatabase;
    private Database fileSystem;

    private SystemController() {
    }

    public void start(Scanner scanner) {
        initDatabase();
        initGui(scanner);
    }

    private void initDatabase() {
        userDatabase = new UserDatabase();
        userDatabase.initDatabase();
        fileSystem = new FileSystem();
        fileSystem.initDatabase();
        mediaDatabase = new MediaDatabase((FileSystem) fileSystem);
        mediaDatabase.initDatabase();

        ArrayList<Field> fields = new ArrayList<>();
        fields.add(new Field("Title", DataType.STRING));
        fields.add(new Field("Author", DataType.STRING));
        fields.add(new Field("Page Number", DataType.INTEGER));
        fields.add(new Field("Year", DataType.YEAR));
        fields.add(new Field("Publisher", DataType.STRING));
        fields.add(new Field("Genre", DataType.STRING));
        ((MediaDatabase) mediaDatabase).addMediaType("Book", fields);
    }

    private void initGui(Scanner scanner) {
        Notifications.setLanguage(Notifications.LANGUAGE_ITA);

        LoginScreen loginScreen = new LoginScreen();
        loginScreen.addListener((LoginScreenListener) userDatabase);

        SignUpScreen signUpScreen = new SignUpScreen();
        signUpScreen.addListener((SignUpScreenListener) userDatabase);

        OperatorScreen operatorScreen = new OperatorScreen();
        operatorScreen.addListener((OperatorScreenListener) userDatabase);
        operatorScreen.addListener((OperatorScreenListener) mediaDatabase);
        operatorScreen.addListener((OperatorScreenListener) fileSystem);

        CustomerScreen customerScreen = new CustomerScreen();
        customerScreen.addListener((CustomerScreenListener) userDatabase);

        Screen startScreen = new StartScreen();
        Screen settingsScreen = new SettingsScreen();

        ScreenManager screenManager = new ScreenManager(startScreen, scanner);
        screenManager.getScreens().add(loginScreen);
        screenManager.getScreens().add(signUpScreen);
        screenManager.getScreens().add(settingsScreen);
        screenManager.getScreens().add(operatorScreen);
        screenManager.getScreens().add(customerScreen);

        screenManager.start();
    }

    public static SystemController getInstance() {
        if(instance == null)
            instance = new SystemController();
        return instance;
    }
}
