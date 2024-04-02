package src;

import java.util.Scanner;

public class Pomocnik {

    private static final Scanner scanner = new Scanner(System.in);

    public static int readIntFromConsole(String msg, int min, int max) {
        System.out.print(msg);
        return readIntFromConsole(min, max);
    }

    public static int readIntFromConsole(int min, int max) {
        int input;
        do {
            System.out.printf("Zadajte číslo od %d do %d: ", min, max);
            while (!scanner.hasNextInt()) {
                System.out.println("Neplatný vstup. Skúste to znova.");
                scanner.next();
                System.out.printf("Zadajte číslo od %d do %d: ", min, max);
            }
            input = scanner.nextInt();
            if (input < min || input > max) {
                System.out.println("Neplatný vstup. Skúste to znova.");
            }
        } while (input < min || input > max);

        return input;
    }

    public static int readIntFromConsole(String msg, int min) {
        System.out.print(msg);
        return readIntFromConsole(min);
    }

    public static int readIntFromConsole(int min) {
        int input;
        do {
            System.out.printf("Zadajte číslo väčšie ako %d: ", min);
            while (!scanner.hasNextInt()) {
                System.out.println("Neplatný vstup. Skúste to znova.");
                scanner.next();
                System.out.printf("Zadajte číslo väčšie ako %d: ", min);
            }
            input = scanner.nextInt();
            if (input < min) {
                System.out.println("Neplatný vstup. Skúste to znova.");
            }
        } while (input < min);

        return input;
    }

    public static double readDoubleFromConsole(String msg, double min, double max) {
        System.out.print(msg);
        return readDoubleFromConsole(min, max);
    }

    public static double readDoubleFromConsole(double min, double max) {
        double input;
        do {
            System.out.printf("Zadajte číslo od %.2f do %.2f: ", min, max);
            while (!scanner.hasNextDouble()) {
                System.out.println("Neplatný vstup. Skúste to znova.");
                scanner.next();
                System.out.printf("Zadajte číslo od %.2f do %.2f: ", min, max);
            }
            input = scanner.nextDouble();
            if (input < min || input > max) {
                System.out.println("Neplatný vstup. Skúste to znova.");
            }
        } while (input < min || input > max);

        return input;
    }

    public static double readDoubleFromConsole(String msg, double min) {
        System.out.print(msg);
        return readDoubleFromConsole(min);
    }

    public static double readDoubleFromConsole(double min) {
        double input;
        do {
            System.out.printf("Zadajte číslo väčšie ako %.2f: ", min);
            while (!scanner.hasNextDouble()) {
                System.out.println("Neplatný vstup. Skúste to znova.");
                scanner.next();
                System.out.printf("Zadajte číslo väčšie ako %.2f: ", min);
            }
            input = scanner.nextDouble();
            if (input < min) {
                System.out.println("Neplatný vstup. Skúste to znova.");
            }
        } while (input < min);

        return input;
    }
}

