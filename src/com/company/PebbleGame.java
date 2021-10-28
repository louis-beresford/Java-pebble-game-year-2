package com.company;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for Game.
 *
 * @version 20/11/2019
 */

class PebbleGame {
    private Player[] players;
    private HashMap<String, Bag> whiteBags;
    private HashMap<String, Bag> blackBags;

    private Bag lastBlackBag;
    private ArrayList<Player> winners;
    private boolean verbose;
    private volatile boolean winner;

    /**
     * @param numOfPlayers   the number of player to be added to the game
     * @param locationOfBagX the file location of the black bag X
     * @param locationOfBagY the file location of the black bag Y
     * @param locationOfBagZ the file location of the black bag Z
     * @param verbose        whether you want the program to output current game information
     * @throws IllegalArgumentException numOfPlayers should be greater than 0
     * @throws Exception                bag should not contain negatives
     */
    PebbleGame(int numOfPlayers, String locationOfBagX, String locationOfBagY, String locationOfBagZ, boolean verbose) throws Exception {
        if (numOfPlayers < 1) throw new IllegalArgumentException("numOfPlayers should be greater than 0");

        whiteBags = new HashMap<>();
        whiteBags.put("A", new Bag("A", new ArrayList<>()));
        whiteBags.put("B", new Bag("B", new ArrayList<>()));
        whiteBags.put("C", new Bag("C", new ArrayList<>()));

        blackBags = new HashMap<>();
        blackBags.put("X", new Bag("X", getBagContents(locationOfBagX)));
        blackBags.put("Y", new Bag("Y", getBagContents(locationOfBagY)));
        blackBags.put("Z", new Bag("Z", getBagContents(locationOfBagZ)));

        // check that each black bag contains at least 11 times as many pebbles as players
        for (Bag blackBag : blackBags.values()) {
            if (blackBag.getPebbles().size() < 11 * numOfPlayers) {
                throw new Exception("Each black bag should contain at least 11 times as many pebbles as players");
            }
        }

        // 10 largest pebbles in every black bag should be greater equal 100
        for (Bag bag : blackBags.values()) {
            ArrayList<Integer> pebbles = new ArrayList<>(bag.getPebbles());

            Collections.sort(pebbles);
            int topTenSum = 0;
            for (int i = 0; i != 10; i += 1) {
                topTenSum += pebbles.get(pebbles.size() - i - 1);
            }

            if (topTenSum < 100) {
                throw new Exception("10 largest pebbles in every black bag should be greater than 100");
            }
        }

        // create players
        players = new Player[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i = i + 1) {
            players[i] = new Player(i, new ArrayList<>());
        }

        winners = new ArrayList<>();
        this.verbose = verbose;
    }

    /**
     * Start player threads
     */
    void run() {
        if (verbose) System.out.println("Game started");

        ArrayList<Thread> threads = new ArrayList<>();
        for (Player player : players) {
            Thread t = new Thread(player);
            t.start();
            threads.add(t);
        }

        // wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (verbose) System.out.println("The winner is " + getWinner());
        if (verbose) System.out.println("Game end");
    }

    /**
     * Get the current successful pebble lists
     *
     * @return successful pebble lists
     */
    public Player getWinner() {
        return winners.get(0);
    }

    /**
     * Get an array of the contents of a bag file
     *
     * @param locationOfBag the location of the bag
     * @return the array of pebbles
     * @throws Exception bag contains negatives
     */
    private ArrayList<Integer> getBagContents(String locationOfBag) throws Exception {
        Scanner bagScanner = new Scanner(new File(locationOfBag));
        ArrayList<Integer> bag = new ArrayList<>();

        while (bagScanner.hasNextLine()) {
            String line = bagScanner.nextLine();

            // file should not contain negatives
            if (line.contains("-")) {
                throw new Exception(locationOfBag + " should not contain negatives or zero");
            }

            Scanner scanner = new Scanner(line);
            scanner.useDelimiter(",");
            while (scanner.hasNextInt()) {
                int nextInt = scanner.nextInt();
                if (nextInt == 0) {
                    throw new Exception(locationOfBag + " should not contain zero");
                }
                bag.add(nextInt);
            }
            scanner.close();
        }

        bagScanner.close();

        return bag;
    }

    /**
     * A player, plays as a thread to the game
     */
    class Player implements Runnable {
        private final int id;
        private ArrayList<Integer> pebbles;

        Player(int id, ArrayList<Integer> pebbles) {
            this.id = id;
            this.pebbles = pebbles;

            create_log_file();
        }

        ArrayList<Integer> getPebbles() {
            return pebbles;
        }

        /**
         * Does a player have winning pebbles
         *
         * @return whether a player has won
         */
        private boolean hasWon() {
            int sum = 0;
            for (Integer pebble : pebbles) {
                sum += pebble;
            }
            return sum == 100 && pebbles.size() == 10;
        }

        /**
         * Create new log file for player
         *
         * @return the File of the created log file or null if unsuccessful
         */
        private String create_log_file() {
            String path = "player" + id + "_output.txt";
            File log_file = new File(path);

            try {
                if (log_file.exists()) {
                    log_file.delete();
                }

                log_file.createNewFile();
                return path;
            } catch (IOException e) {
                System.out.println("Warning: could not create " + path);
                return null;
            }
        }

        /**
         * Discard pebble to linked white bag and draw a pebble from a random black bag
         */
        private void takeTurn() {
            // discard random pebble into linked white bag
            synchronized (Player.class) {
                if (winner) {
                    return;
                }
                int oldPebble = pebbles.remove(ThreadLocalRandom.current().nextInt(pebbles.size()));
                Bag whiteBag = getWhiteBagLinkedToBlackBag(lastBlackBag);
                whiteBag.addPebble(oldPebble);
                log("player" + id + " has discarded a " + oldPebble + " to bag " + whiteBag.getName());
                log("player" + id + " hand is " + pebbles);

//            // draw a random pebble from a random black bag
                Bag randomBlackBag = chooseRandomBlackBag();
                int randomPebble = randomBlackBag.removeRandomPebble();
                pebbles.add(randomPebble);
                log("player" + id + " has drawn a " + randomPebble + " from bag " + randomBlackBag.getName());
                log("player" + id + " hand is " + pebbles);

                if (hasWon()) {
                    winner = true;
                    winners.add(this);
                    return;
                }

                // if black bag is now empty, empty contents of linked white bag into black bag before other player
                // tries to take from the bag
                if (randomBlackBag.getPebbles().size() == 0) {
                    Bag linkedWhiteBag = getWhiteBagLinkedToBlackBag(randomBlackBag);
                    int whiteSize = linkedWhiteBag.getPebbles().size();
                    for (int i = 0; i < whiteSize; i++) {
                        int pebble = linkedWhiteBag.removeRandomPebble();
                        randomBlackBag.addPebble(pebble);
                    }
                }
            }

        }

        /**
         * Return a random black bag
         *
         * @return a random black bag object
         */
        private Bag chooseRandomBlackBag() {
            int ran = 1 + (int) (Math.random() * (3));
            switch (ran) {
                case 1:
                    lastBlackBag = blackBags.get("X");
                    return blackBags.get("X");
                case 2:
                    lastBlackBag = blackBags.get("Y");
                    return blackBags.get("Y");
                default:
                    lastBlackBag = blackBags.get("Z");
                    return blackBags.get("Z");
            }
        }

        /**
         * Returns linked white bag to black bag
         *
         * @param blackBag the black bag that's linked
         * @return the linked white bag
         */
        private Bag getWhiteBagLinkedToBlackBag(Bag blackBag) {
            if (blackBag.getName().equals("X")) {
                return whiteBags.get("A");
            } else if (blackBag.getName().equals("Y")) {
                return whiteBags.get("B");
            } else {
                return whiteBags.get("C");
            }
        }

        /**
         * Log the moves and hands of a player in a file. named player*_output.txt
         *
         * @param text the new line of text in the player file
         */
        private void log(String text) {
            if (verbose) System.out.println(text);

            Writer output = null;
            try {
                output = new BufferedWriter(new FileWriter("player" + id + "_output.txt", true));
                output.append(text).append("\n");
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Start player game loop
         */
        @Override
        public void run() {
            // initial choosing of 10 pebbles from a random black bag
            synchronized (Player.class) {
                Bag bag = chooseRandomBlackBag();

                // pick 10 pebbles from random black bag
                for (int i = 0; i < 10; i++) {
                    int pebble = bag.removeRandomPebble();
                    pebbles.add(pebble);
                }

                // if all the players have been given 10 pebbles un-pause them and start main loop
                int count = 0;
                for (Player player : players) {
                    if (player.getPebbles().size() == 10) {
                        count += 1;
                    }
                }

                if (count == players.length) {
                    Player.class.notifyAll();
                } else {
                    try {
                        Player.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

            // main player game loop
            while (!winner) {
                takeTurn();
                Thread.yield(); // equal priority for each player
            }

        }

        @Override
        public String toString() {
            return "player" + id + ": " + pebbles.toString();
        }
    }
}
