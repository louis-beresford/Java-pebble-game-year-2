package com.company;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;

public class BagTest {

    /**
     * getName should return the name entered
     */
    @Test
    public void testGetName() {
        Bag bag1 = new Bag("T", new ArrayList<>());
        assertEquals(bag1.getName(), "T");
    }

    /**
     * getPebbles should return the pebbles entered
     */
    @Test
    public void testGetPebbles() {
        ArrayList<Integer> pebbles = new ArrayList<>();
        pebbles.add(1);
        pebbles.add(2);
        pebbles.add(3);
        pebbles.add(4);
        pebbles.add(5);
        pebbles.add(6);
        pebbles.add(7);
        pebbles.add(8);
        pebbles.add(9);

        Bag bag1 = new Bag("T", pebbles);
        assertEquals(bag1.getPebbles(), pebbles);

        Bag bag2 = new Bag("T", new ArrayList<>());
        assertEquals(bag2.getPebbles(), new ArrayList<>());
    }

    /**
     * removeRandomPebbles should remove a pebble from bag.pebbles
     */
    @Test
    public void testRemoveRandomPebble() {
        ArrayList<Integer> pebbles = new ArrayList<>();
        pebbles.add(1);
        pebbles.add(2);
        pebbles.add(3);
        pebbles.add(4);
        pebbles.add(5);
        pebbles.add(6);
        pebbles.add(7);
        pebbles.add(8);
        pebbles.add(9);

        Bag bag = new Bag("T", pebbles);
        bag.removeRandomPebble();
        assertEquals(bag.getPebbles().size(), 8);
    }

    /**
     * addPebble should add a pebble to bag.pebbles
     */
    @Test
    public void testAddPebble() {
        ArrayList<Integer> pebbles = new ArrayList<>();
        Bag bag = new Bag("T", pebbles);
        bag.addPebble(6);
        bag.addPebble(6);
        bag.addPebble(7);
        bag.addPebble(1000000000);

        assertEquals(bag.getPebbles().size(), 4);
    }
}
