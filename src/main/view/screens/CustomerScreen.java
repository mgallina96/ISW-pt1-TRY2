package main.view.screens;

import main.utility.exceptions.CannotRenewException;
import main.utility.notifications.Notifications;
import main.view.ScreenManager;
import main.view.listeners.CustomerScreenListener;

import java.util.ArrayList;
import java.util.Scanner;

import static main.GlobalParameters.RENEWAL_BOUNDARY_IN_DAYS;
import static main.utility.InputParserUtility.insertInteger;

/**
 * @author Manuel Gallina
 * @since Version 1
 */
public class CustomerScreen extends Screen {
    private ArrayList<CustomerScreenListener> listenersList;

    public CustomerScreen() {
        super("customer");
        listenersList = new ArrayList<>();
    }

    @Override
    public void initScreen(ScreenManager screenManager, Scanner scanner) {
        boolean exitFromCustomerSection = false;

        while(!exitFromCustomerSection) {
            System.out.printf("%s%n%s\t\t\t\t\t\t%s%s%n%s%n",
                    Notifications.getMessage("SEPARATOR"),
                    Notifications.getMessage("PROMPT_CUSTOMER_CHOICES"),
                    Notifications.getMessage("MSG_LOGGED_IN_AS"), getCurrentUserName(),
                    Notifications.getMessage("SEPARATOR"));

            switch(insertInteger(1, 6, scanner)) {
                case 1:
                    try {
                        renewSubscription();
                        System.out.println(Notifications.getMessage("MSG_SUBSCRIPTION_RENEWAL_SUCCESSFUL"));
                    } catch(CannotRenewException crEx) {
                        System.out.printf("%s%n", Notifications.getMessage("ERR_CANNOT_RENEW"));
                        System.out.printf("%s%s%s%s%s%s%s%d %s %s%n",
                                Notifications.getMessage("MSG_SUBSCRIPTION_REMINDER"), crEx.getSupscriptionDate(),
                                Notifications.getMessage("MSG_LATEST_RENEWAL_REMINDER"), crEx.getLastRenewalDate(),
                                Notifications.getMessage("MSG_EXPIRY_REMINDER"), crEx.getExpiryDate(),
                                Notifications.getMessage("MSG_RENEWAL_INFO"), RENEWAL_BOUNDARY_IN_DAYS,
                                Notifications.getMessage("MSG_DAYS"),
                                Notifications.getMessage("MSG_RENEWAL_INFO_END_OF_MESSAGE")
                        );
                    }
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    System.out.printf("%s%n", Notifications.getMessage("MSG_LOG_OUT"));
                    exitFromCustomerSection = true;
                    break;
                default:
                    break;
            }
        }
    }

    public void addListener(CustomerScreenListener listener) {
        listenersList.add(listener);
    }

    private void renewSubscription() throws CannotRenewException {
        for(CustomerScreenListener customerScreenListener : listenersList)
            customerScreenListener.renewSubscription();
    }

    private String getCurrentUserName() {
        for(CustomerScreenListener customerScreenListener : listenersList) {
            if(customerScreenListener.getCurrentUserName() != null)
                return customerScreenListener.getCurrentUserName();
        }
        return null;
    }
}
