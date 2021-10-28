package com.company;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class PebbleGameTest {
    private final String locationOfBag2 = "example_file_2.txt";
    private final String locationOfBag3 = "example_file_3.txt";
    private final String locationOfBag4 = "example_file_4.txt";
    private final String locationOfBag5 = "example_file_5.txt";
    private final String locationOfBag6 = "example_file_6.txt";
    private final String locationOfBag7 = "example_file_7.txt";
    private final String locationOfBag8 = "example_file_8.txt";

    /**
     * Test whether the winners final hand is valid (10 pebbles that add up to 100)
     */
    @Test
    public void testWinnerHandValid() throws Exception {
        for (int i : new int[]{1, 2, 3, 20}) { // 20 is the max for locationOfBag2 since there are 220 items in locationOfBag2 so 220/11 = 22
            PebbleGame game = new PebbleGame(i, locationOfBag2, locationOfBag2, locationOfBag2, false);
            game.run();

            ArrayList<Integer> pebbles = game.getWinner().getPebbles();
            assertEquals(pebbles.size(), 10);
            int total = 0;
            for (int pebble : pebbles) {
                total += pebble;
            }

            assertEquals(total, 100);

        }

    }

    /**
     * Entering zero players should fail
     */
    @Test(expected = IllegalArgumentException.class)
    public void testZeroNumOfPlayers() throws Exception {
        PebbleGame game = new PebbleGame(0, locationOfBag2, locationOfBag2, locationOfBag2, false);
        game.run();
    }

    /**
     * Entering a negative number of players should fail
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumOfPlayers() throws Exception {
        PebbleGame game = new PebbleGame(-7, locationOfBag2, locationOfBag2, locationOfBag2, false);
        game.run();
    }

    /**
     * Entering an nonexistent file should fail
     */
    @Test(expected = FileNotFoundException.class)
    public void testIncorrectLocation() throws Exception {
        PebbleGame game = new PebbleGame(1, "blahblahblah.txt", locationOfBag2, locationOfBag2, false);
        game.run();
    }

    /**
     * A file with anything other than numbers or commas should fail
     */
    @Test(expected = Exception.class)
    public void testIncorrectFormatOfFile() throws Exception {
        PebbleGame game = new PebbleGame(1, locationOfBag5, locationOfBag5, locationOfBag5, false);
        game.run();
    }

    /**
     * Each black bag should contain at least 11 times as many pebbles as players
     */
    @Test(expected = Exception.class)
    public void testEachBagContainsAtLeast11TimesNumPebblesAsPlayers() throws Exception {
        PebbleGame game = new PebbleGame(100, locationOfBag7, locationOfBag7, locationOfBag7, false);
        game.run();
    }

    /**
     * null locationBag's should fail
     */
    @Test(expected = Exception.class)
    public void testNullBagLocation() throws Exception {
        PebbleGame game = new PebbleGame(1, locationOfBag2, null, locationOfBag2, false);
        game.run();
    }

    /**
     * Empty bag's should fail
     */
    @Test(expected = Exception.class)
    public void testEmptyBag() throws Exception {
        PebbleGame game = new PebbleGame(1, locationOfBag6, locationOfBag6, locationOfBag6, false);
        game.run();
    }

    /**
     * Zero weight pebbles weights should fail
     */
    @Test(expected = Exception.class)
    public void testZeroPebbleInBag() throws Exception {
        PebbleGame game = new PebbleGame(1, locationOfBag8, locationOfBag8, locationOfBag8, false);
        game.run();
    }

    /**
     * Negative weight pebbles weights should fail
     */
    @Test(expected = Exception.class)
    public void testNegativePebbleInBag() throws Exception {
        PebbleGame game = new PebbleGame(1, locationOfBag4, locationOfBag4, locationOfBag4, false);
        game.run();
    }

    /**
     * 10 largest pebbles in every black bag should be greater than 100, if not it should fail
     */
    @Test(expected = Exception.class)
    public void testTenLargestPebbles() throws Exception {
        PebbleGame game = new PebbleGame(1, locationOfBag3, locationOfBag3, locationOfBag3, false);
        game.run();
    }
}
