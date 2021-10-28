package com.company;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to the pebble game");

        System.out.println(
                "You will be asked to enter the number of players.\n" +
                "And then for the location of the three files in turn containing comma separated integer values for the pebble weights.\n" +
                "The integer values must be strictly positive." +
                "The game will then be simulated, and output written to files in this directory.\n");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the number of players");
        int numOfPlayers = scanner.nextInt();
        scanner.nextLine();  // Consume newline left-over

        System.out.println("Please enter location of black bag number 0 to load");
        String locationOfBagX = scanner.nextLine();

        System.out.println("Please enter location of bag number 1 to load");
        String locationOfBagY = scanner.nextLine();

        System.out.println("Please enter location of black bag number 2 to load");
        String locationOfBagZ = scanner.nextLine();

        PebbleGame game;
        try {
            game = new PebbleGame(numOfPlayers, locationOfBagX, locationOfBagY, locationOfBagZ, true);
        } catch (FileNotFoundException e) {
            System.out.println("One of the file locations are not valid");
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        game.run();

    }
}
