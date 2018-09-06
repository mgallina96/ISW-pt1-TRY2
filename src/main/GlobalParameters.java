package main;

/**
 * Utility class that holds various multi-purpose parameters for general use.
 *
 * @author Manuel Gallina
 */
public class GlobalParameters {

    //integers
    public static final int RENEWAL_BOUNDARY_IN_DAYS = 10;
    public static final int LEGAL_AGE_IN_YEARS = 18;
    public static final int EXPIRY_TIME_IN_YEARS = 5;
    public static final int SHORT_SEPARATOR_BOUND = 46;
    public static final int MEDIUM_SEPARATOR_BOUND = 106;
    public static final int LONG_SEPARATOR_BOUND = 166;

    //strings
    public static final String MEDIA_DATABASE_FILE_PATH = "resources\\data\\Biblioteca SMART - Media Database.ser";
    public static final String LOAN_DATABASE_FILE_PATH = "resources\\data\\Biblioteca SMART - Loan Database.ser";
    public static final String USER_DATABASE_FILE_PATH = "resources\\data\\Biblioteca SMART - User Database.ser";
    public static final String FILESYSTEM_FILE_PATH = "resources\\data\\Biblioteca SMART - File System.ser";

    private GlobalParameters() {}

}
