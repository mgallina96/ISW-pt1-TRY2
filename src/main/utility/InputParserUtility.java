package main.utility;

import main.utility.exceptions.IllegalDateFormatException;
import main.utility.notifications.Notifications;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class that contains useful methods for parsing strings and checking whether a given input is valid or not.
 *
 * @author Alessandro Polcini
 * Created on 08/03/18
 */
public class InputParserUtility {

    private static Logger logger = Logger.getLogger("InputParserUtility");

    //Constructor has been made private to prevent instantiation.
    private InputParserUtility() {}

    /**
     * Checks whether the input is an integer or not.
     *
     * @param input The input to be parsed and checked.
     * @return a boolean value: {@code true} if the input is an integer,
     *                          {@code false} otherwise.
     */
    public static boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
        }
        catch(NumberFormatException nfEx) {
            logger.log(Level.SEVERE, Notifications.getMessage("ERR_MSG_INVALID_INPUT"));
            return false;
        }

        return true;
    }

    /**
     * Checks whether the input is an integer. If so, the integer must have a value between {@code lowerBound}
     * (inclusive) and {@code upperBound} (exclusive), otherwise it's considered invalid.
     *
     * @param input The input to be parsed and checked.
     * @param lowerBound The lower bound for the input, inclusive.
     * @param upperBound The upper bound for the input, exclusive.
     * @return a boolean value: {@code true} if the input is both an integer and a value between {@code lowerBound}
     *                                       and {@code upperBound},
     *                          {@code false} otherwise.
     */
    public static boolean isValidInteger(String input, int lowerBound, int upperBound) {
        if(isValidInteger(input)) {
            int intInput = Integer.parseInt(input);
            return (intInput >= lowerBound && intInput < upperBound);
        }

        return false;
    }

    /**
     * Checks whether the input is a valid first name/last name/city name: a {@code String} composed of letters,
     * spaces, apostrophes, and dashes only.
     * <p>
     * Examples: "De Loria", "New York City", "O'Sullivan", "Stratford-upon-Avon", etc.
     *
     * @param input The {@code String} to be checked.
     * @return a boolean value: {@code true} if the input only contains letters, spaces, apostrophes and dashes.
     *                          {@code false} otherwise.
     */
    public static boolean isValidName(String input) {
        return input.matches("(([A-Z](('[A-Z])|(\\. ?[A-Z])+)?[a-zèéçòàùäåêëïîìöü]+)( |-)?)*([A-Z]('[A-Z])?[a-zèéçòàùäåêëïîìöü]+)*");
    }

    /**
     * Checks whether the input is a {@code double} or not.
     *
     * @param input The input to be parsed and checked.
     * @return a boolean value: {@code true} if the input is a {@code double},
     *                          {@code false} otherwise.
     */
    public static boolean isValidDouble(String input) {
        try {
            Double.parseDouble(input);
        }
        catch(Exception e) {
            logger.log(Level.SEVERE, Notifications.getMessage("ERR_MSG_INVALID_INPUT"));
            return false;
        }

        return true;
    }

    /**
     * Checks whether the input is a {@code double}. If so, the floating-point number must have a value between
     * {@code lowerBound} (inclusive) and {@code upperBound} (exclusive), otherwise it's considered invalid.
     *
     * @param input The input to be parsed and checked.
     * @param lowerBound The lower bound for the input, inclusive.
     * @param upperBound The upper bound for the input, exclusive.
     * @return a boolean value: {@code true} if the input is both a {@code double} and a value between
     *                                       {@code lowerBound} and {@code upperBound},
     *                          {@code false} otherwise.
     */
    public static boolean isValidDouble(String input, double lowerBound, double upperBound) {
        if(isValidDouble(input)) {
            double doubleInput = Double.parseDouble(input);
            return (doubleInput >= lowerBound && doubleInput < upperBound);
        }

        return false;
    }

    /**
     * Checks whether the input is a {@code long} or not.
     *
     * @param input The input to be parsed and checked.
     * @return a boolean value: {@code true} if the input is a {@code long},
     *                          {@code false} otherwise.
     */
    public static boolean isValidLong(String input) {
        try {
            Long.parseLong(input);
        }
        catch(NumberFormatException nfEx) {
            logger.log(Level.SEVERE, Notifications.getMessage("ERR_MSG_INVALID_INPUT"));
            return false;
        }

        return true;
    }

    /**
     * Checks whether the input is a {@code long}. If so, the long integer must have a value between {@code lowerBound}
     * (inclusive) and {@code upperBound} (exclusive), otherwise it's considered invalid.
     *
     * @param input The input to be parsed and checked.
     * @param lowerBound The lower bound for the input, inclusive.
     * @param upperBound The upper bound for the input, exclusive.
     * @return a boolean value: {@code true} if the input is both a {@code long} and a value between {@code lowerBound}
     *                                       and {@code upperBound},
     *                          {@code false} otherwise.
     */
    public static boolean isValidLong(String input, long lowerBound, long upperBound) {
        if(isValidLong(input)) {
            long longInput = Long.parseLong(input);
            return (longInput >= lowerBound && longInput < upperBound);
        }

        return false;
    }

    /**
     * Converts a date in DD/MM/YYYY format to a valid {@link GregorianCalendar} date.
     * <p> Accepted formats: "DD/MM/YYYY", "DD-MM-YYYY", "DD,MM,YYYY", "DD.MM.YYYY", "DD|MM|YYYY", etc. <p>Basically,
     * any single non-digit separator is accepted.
     * <p> Any invalid date is automatically corrected by the {@link GregorianCalendar} class. For instance: the input
     * "32/10/2015" is transformed into "1/11/2015" because it gets parsed as "(31/10/2015 + one day) = 1/11/2015".
     *
     * @param date The string value of a date in DD/MM/YYYY format.
     * @return the corresponding {@code GregorianCalendar} date.
     */
    public static GregorianCalendar toGregorianDate(String date) {
        GregorianCalendar finalDate = new GregorianCalendar();
        int[] fields = separateDate(date);

        finalDate.set(fields[2], fields[1] - 1, fields[0]);

        return finalDate;
    }

    /**
     * Checks whether the input is a valid date or not.
     * <p>To be considered valid, the date has to conform to the DD/MM/YYYY format and must follow the basic rules of
     * the Gregorian calendar.
     *
     * @param date The date to be parsed and checked.
     * @return a boolean value: {@code true} if the input is a valid date,
     *                          {@code false} otherwise.
     */
    public static boolean isValidDate(String date) {
        if(!date.matches("\\d?\\d[^0-9]\\d?\\d[^0-9]\\d\\d\\d\\d"))
            return false;

        int[] fields = separateDate(date);
        int day = fields[0];
        int month = fields[1];
        int year = fields[2];

        boolean leapYear = ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
        boolean monthDayBounds = month > 0 && month < 13 && day > 0;
        boolean february = month == 2 && ((leapYear && (day < 30)) || (!leapYear && (day < 29)));
        boolean thirty = (day < 31) && (month == 4 || month == 6 || month == 9 || month == 11);
        boolean thirtyOne = (day < 32) && !(month == 2 || month == 4 || month == 6 || month == 9 || month == 11);

        return monthDayBounds && (thirtyOne || thirty || february);
    }

    /**
     * Splits a DD/MM/YYYY date into three parts: DD, MM and YYYY and stores those values as integers in an array.
     *
     * @param date The date to separate.
     * @return the integer {@code array} composed of the extracted fields day, month and year.
     */
    private static int[] separateDate(String date) {
        date = date + "/";
        int len = date.length();
        int[] fields = new int[3];
        int count = 0;
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < len; i++) {
            String currentChar = date.charAt(i) + "";

            if(currentChar.matches("[^0-9]")) {
                fields[count++] = Integer.parseInt(sb.toString());
                sb = new StringBuilder();
                date = date.substring(i);
                len = date.length();
                i = 0;
            }
            else
                sb.append(currentChar);

        }

        return fields;
    }

    /**
     * Checks whether the given {@code String} represents a valid year or not.
     * To be valid, the year has to be between 0 and the current year.
     *
     * @param year the year to be checked.
     * @return a boolean value, {@code true} if the year is valid, {@code false} otherwise.
     */
    public static boolean isValidYear(String year) {
        int y;

        try {
            y = Integer.parseInt(year);
        }
        catch(Exception e) {
            return false;
        }

        return y >= 0 && y <= new GregorianCalendar().get(Calendar.YEAR);
    }

    /**
     * Checks whether the given {@code String} represents a valid path or not.
     * The path has to conform to the format "folder\subfolder\subfolder....".
     *
     * @param path the path to be checked.
     * @return a boolean value, {@code true} if the path is valid, {@code false} otherwise.
     */
    public static boolean isValidPathFormat(String path) {
        return path.matches("(\\w*\\\\\\w.*)+");
    }

    /**
     * Checks whether a hypothetical person with the given birth date would be of age or not.
     *
     * @param birthday The birth date.
     * @param legalAgeInYears The legal age (its value changes from country to country).
     * @return {@code true} if the date is "legal", {@code false} otherwise.
     * @throws IllegalDateFormatException If the inserted date has an invalid format.
     */
    public static boolean isOfAge(String birthday, int legalAgeInYears) throws IllegalDateFormatException {
        if(isValidDate(birthday)) {
            GregorianCalendar gregorianBirthday = toGregorianDate(birthday);
            gregorianBirthday.add(Calendar.YEAR, legalAgeInYears);

            return gregorianBirthday.before(new GregorianCalendar());
        }
        else
            throw new IllegalDateFormatException();
    }

    /**
     * Loops a scanner until the inserted {@code String} is a valid first name (or last name).
     * <p>The parameters and the logic for name validity are defined in the {@code isValidName()} method of the
     * {@link InputParserUtility} class.
     *
     * @return a valid name in the form of a {@code String}.
     */
    public static String insertName(Scanner scanner) {
        String name = scanner.nextLine();

        while(!InputParserUtility.isValidName(name)) {
            System.out.println(Notifications.getMessage("ERR_INVALID_NAME"));
            name = scanner.nextLine();
        }

        return name;
    }

    /**
     * Loops a scanner until the inserted {@code String} is a valid year.
     * <p>The parameters and the logic for year validity are defined in the {@code isValidYear()} method of the
     * {@link InputParserUtility} class.
     *
     * @return a valid year in the form of an {@code int}.
     */
    public static int insertYear(Scanner scanner) {
        String year = scanner.nextLine();

        while(!InputParserUtility.isValidYear(year)) {
            System.out.println(Notifications.getMessage("ERR_INVALID_YEAR"));
            year = scanner.nextLine();
        }

        return Integer.parseInt(year);
    }

    /**
     * Loops a scanner until the inserted {@code String} represents a valid integer.
     * <p>The parameters and the logic for integer validity are defined in the {@code isValidInteger()} method of the
     * {@link InputParserUtility} class.
     *
     * @return A valid int.
     */
    public static int insertInteger(Scanner scanner) {
        String integer = scanner.nextLine();

        while(!InputParserUtility.isValidInteger(integer)) {
            System.out.println(Notifications.getMessage("ERR_MSG_INVALID_INPUT"));
            integer = scanner.nextLine();
        }

        return Integer.parseInt(integer);
    }

    /**
     * Loops a scanner until the inserted {@code String} represents a valid integer between {@code lowerBound} and
     * {@code upperBound}.
     * <p>The parameters and the logic for integer validity are defined in the {@code isValidInteger()} method of the
     * {@link InputParserUtility} class.
     *
     * @param lowerBound The lower bound, inclusive.
     * @param upperBound The upper bound, exclusive.
     * @return A valid int.
     */
    @SuppressWarnings("all")
    public static int insertInteger(int lowerBound, int upperBound, Scanner scanner) {
        String integer = scanner.nextLine();

        while(!InputParserUtility.isValidInteger(integer, lowerBound, upperBound)) {
            System.out.println(Notifications.getMessage("ERR_MSG_INVALID_INPUT"));
            integer = scanner.nextLine();
        }

        return Integer.parseInt(integer);
    }

    /**
     * Loops a scanner until the inserted {@code String} is a valid birth date.
     * <p>The parameters and the logic for date validity are defined in the {@code isValidDate()} method of the
     * {@link InputParserUtility} class.
     *
     * @return a valid birth date in the form of a {@code String}.
     */
    public static String insertDate(Scanner scanner) {
        String date = scanner.nextLine();

        while(!InputParserUtility.isValidDate(date)) {
            System.out.println(Notifications.getMessage("ERR_INVALID_DATE"));
            date = scanner.nextLine();
        }

        return date;
    }

    /**
     * Loops a scanner until the inserted {@code String} matches the passed parameter.
     *
     * @param toMatch The {@code String} on which the matching algorithm is based.
     * @return The matching {@code String}.
     */
    public static String insertString(String toMatch, Scanner scanner) {
        String out;

        do {
            out = scanner.nextLine();
        } while(!out.matches(toMatch));

        return out;
    }

    /**
     * Converts the {@code GregorianCalendar} date into a {@code String} representation.
     *
     * @param date The date to convert.
     * @return The {@code String} representation of the given date.
     */
    public static String gregorianDateToString(GregorianCalendar date) {
        return date.get(Calendar.DAY_OF_MONTH) + "/" +
                date.get(Calendar.MONTH) + "/" +
                date.get(Calendar.YEAR);
    }

}
