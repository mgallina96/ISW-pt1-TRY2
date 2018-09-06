package main;

import java.util.Scanner;

/**
 * Access point of the application.
 *
 * @author Manuel Gallina
 * @since Version 1
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SystemController.getInstance().start(scanner);
        scanner.close();
    }
}
