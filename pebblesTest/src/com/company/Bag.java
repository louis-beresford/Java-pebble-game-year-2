package com.company;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class for Bag.
 *
 * @version 20/11/2019
 */

class Bag {
    private final String name;
    private ArrayList<Integer> pebbles;

    /**
     * @param name the name of the bag i.e. A, B, C
     * @param pebbles the initial pebbles to put in the bag
     */
    Bag(String name, ArrayList<Integer> pebbles) {
        this.name = name;
        this.pebbles = pebbles;
    }

    /**
     * @return the name of the bag
     */
    String getName() {
        return name;
    }

    /**
     * @return the current pebbles in the bag as a list of ints
     */
    ArrayList<Integer> getPebbles() {
        return pebbles;
    }

    /**
     * Remove a random pebble
     *
     * @return the removed pebble
     */
    synchronized int removeRandomPebble() {
        int random = ThreadLocalRandom.current().nextInt(pebbles.size());
        int pebble = pebbles.get(random);
        pebbles.remove(random);
        return pebble;
    }

    /**
     * Add a pebble
     *
     * @param pebble the pebble to add
     */
    synchronized void addPebble(int pebble){
        pebbles.add(pebble);
    }
}
