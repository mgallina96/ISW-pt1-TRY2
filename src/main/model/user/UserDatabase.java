package main.model.user;

import main.model.Database;
import main.utility.data.Field;
import main.utility.exceptions.*;
import main.view.listeners.CustomerScreenListener;
import main.view.listeners.LoginScreenListener;
import main.view.listeners.OperatorScreenListener;
import main.view.listeners.SignUpScreenListener;

import java.io.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static main.GlobalParameters.*;
import static main.utility.InputParserUtility.gregorianDateToString;
import static main.utility.InputParserUtility.isOfAge;
import static main.utility.InputParserUtility.toGregorianDate;

/**
 * @author Manuel Gallina
 * @since Version 1
 */
public class UserDatabase
        implements Database, SignUpScreenListener, LoginScreenListener, OperatorScreenListener, CustomerScreenListener {
    private HashMap<String, User> userData;
    private User currentUser;

    private boolean saveEnabled;

    public UserDatabase(boolean saveEnabled) {
        userData = new HashMap<>();
        userData.put("admin", new User(true, "admin", "admin",
                "admin", "admin", new GregorianCalendar(1900, Calendar.JANUARY, 1)));
        this.saveEnabled = saveEnabled;
    }

    public UserDatabase() {
        this(true);
    }

    @Override
    public void initDatabase() {
        try {
            userData = this.load();
            sweep();
        } catch(IOException e) {
            this.save();
        }
    }

    @Override
    public void save() {
        if(this.isSaveEnabled()) {
            try (
                    //to increase serializing speed
                    RandomAccessFile raf = new RandomAccessFile(USER_DATABASE_FILE_PATH, "rw");
                    FileOutputStream fileOut = new FileOutputStream(raf.getFD());
                    ObjectOutputStream out = new ObjectOutputStream(fileOut)
            ) {
                out.writeObject(userData);
            }
            catch(IOException ioEx) {
                ioEx.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, User> load() throws IOException {
        try (
                FileInputStream fileIn = new FileInputStream(USER_DATABASE_FILE_PATH);
                ObjectInputStream in = new ObjectInputStream(fileIn)
        ) {
            return (HashMap<String, User>) in.readObject();
        }
        catch(ClassNotFoundException cnfEx) {
            cnfEx.printStackTrace();
        }

        return null;
    }

    @Override
    public void onSignUp(String firstName, String lastName, String username, String password, String birthDate)
            throws IllegalDateFormatException, UserAlreadyPresentException {
        if(userData.containsKey(username))
            throw new UserAlreadyPresentException();
        if(!isOfAge(birthDate, LEGAL_AGE_IN_YEARS))
            throw new IllegalDateFormatException();

        userData.put(username, new User(username, password, firstName, lastName, toGregorianDate(birthDate)));
        this.save();
    }

    @Override
    public String onLogin(String username, String password)
            throws UserNotFoundException, WrongPasswordException, SubscriptionExpiryImminentException {
        if(userData.containsKey(username))
            if(userData.get(username).getPassword().equals(password)) {
                this.currentUser = userData.get(username);
                if(this.currentUser.isOperator())
                    return "operator";
                else {
                    int days = (int)Math.abs(ChronoUnit.DAYS.between(
                            new GregorianCalendar().toInstant(),
                            currentUser.getSubscriptionExpiryDate().toInstant())
                    );
                    if(days <= RENEWAL_BOUNDARY_IN_DAYS)
                        throw new SubscriptionExpiryImminentException(days);
                    return "customer";
                }
            }
            else
                throw new WrongPasswordException();
        else
            throw new UserNotFoundException();
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public int getUserDataSize() {
        return userData.size();
    }

    @Override
    public String getUserList() {
        return this.toString();
    }

    @Override
    public void addMedia(String mediaType, ArrayList<String> values, String Category) {

    }

    @Override
    public void removeMedia(Integer mediaId) {

    }

    @Override
    public ArrayList<Field> getTypeValues(String mediaType) {
        return null;
    }

    @Override
    public String getMediaList(String category) {
        return null;
    }

    @Override
    public String getFolderTree() {
        return null;
    }

    @Override
    public void isValidPath(String path) throws PathNotFoundException {

    }

    void sweep() {
        ArrayList<String> toRemove = new ArrayList<>();
        for(User user : userData.values())
            if(user.getSubscriptionExpiryDate().before(new GregorianCalendar()) && !user.isOperator())
                toRemove.add(user.getUsername());
        for(String s : toRemove)
            userData.remove(s);
    }

    public void addUser(User user) {
        this.userData.put(user.getUsername(), user);
    }

    @Override
    public String getCurrentUserName() {
        return this.currentUser.getUsername();
    }

    @Override
    public void renewSubscription() throws CannotRenewException {
        int days = (int)Math.abs(ChronoUnit.DAYS.between(
                new GregorianCalendar().toInstant(),
                currentUser.getSubscriptionExpiryDate().toInstant())
        );
        if(days <= RENEWAL_BOUNDARY_IN_DAYS) {
            GregorianCalendar newExpiryDate = (GregorianCalendar) this.currentUser.getSubscriptionExpiryDate().clone();
            newExpiryDate.add(Calendar.YEAR, EXPIRY_TIME_IN_YEARS);
            this.getCurrentUser().setSubscriptionExpiryDate(newExpiryDate);
            this.save();
        } else
            throw new CannotRenewException(
                    gregorianDateToString(currentUser.getSubscriptionDate()),
                    currentUser.getRenewalDates().size() == 0 ?
                            "/" :
                            gregorianDateToString(currentUser.getRenewalDates().get(currentUser.getRenewalDates().size() - 1)),
                    gregorianDateToString(currentUser.getSubscriptionExpiryDate())
            );
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(User value : userData.values()) {
            stringBuilder.append(value.toString());
        }
        return stringBuilder.toString();
    }

    public boolean isSaveEnabled() {
        return saveEnabled;
    }

    public void setSaveEnabled(boolean saveEnabled) {
        this.saveEnabled = saveEnabled;
    }
}
