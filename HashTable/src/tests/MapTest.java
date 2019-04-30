package tests;

import helpers.KeyValuePair;
import interfaces.IMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import structures.Map;

import java.util.Arrays;

/**
 * Tests the Map<T> class to determine if it follows
 * the contract of the IMap<T> interface.
 *
 * DO NOT ALTER THIS FILE!
 *
 * @author Josh Archer
 * @version 1.0
 */
public class MapTest
{
    private static final int JOAN_AGE = 67;
    private static final int JEREMY_AGE = 70;
    private static final int JIM_AGE = 73;
    private static final int JILL_AGE = 60;
    private static final int JONAS_AGE = 64;
    public static final int SALLY_AGE = 45;
    public static final int MISSING_AGE = 50;

    private String[] names = {"Joan", "Jeremy", "Jim", "Jill", "Jonas"};
    private int[] heightInInches = {JOAN_AGE, JEREMY_AGE, JIM_AGE, JILL_AGE, JONAS_AGE};
    private IMap<String, Integer> map;

    /**
     * Prepares each test before execution.
     */
    @Before
    public void setup()
    {
        map = new Map<>();
    }

    //adds some initial elements to the set for tests
    private void addTestableElements()
    {
        for (int i = 0; i < names.length; i++)
        {
            map.add(names[i], heightInInches[i]);
        }
    }

    /**
     * Verifies that pairs of elements are located in the structure
     * after calling Map.add(key, value).
     */
    @Test
    public void pairsExistAfterAdding()
    {
        addTestableElements();

        //check if elements are present
        for (String name : names)
        {
            Assert.assertTrue("Name (" + name + ") not found after adding to map as key", map.keyExists(name));
        }

        for (int i = 0; i < names.length; i++)
        {
            int height = map.get(names[i]);
            Assert.assertEquals("Not able to retrieve correct height (" + height + ") for key (" + names[i] + ")",
                    heightInInches[i], height);
        }
    }

    /**
     * Verifies that associated values can be found using a key and
     * the Map.get(key) method.
     */
    @Test
    public void getValueWithKey()
    {
        addTestableElements();

        //check that we can retrieve an existing value from a key in the map
        Assert.assertEquals("Cannot retrieve a value (" + heightInInches[2] +") associated with a key (" +
                map.get(names[2]) + ")", heightInInches[2], map.get(names[2]).intValue());

        //check that we get null back for a missing key in the map
        Assert.assertNull("Calling Map.get(x) for a missing element does not return null",
                map.get("Sally"));
    }

    /**
     * Verifies that the number of elements reported in the structure
     * are consistent when calling size(), clear(), add() or remove().
     */
    @Test
    public void numberOfElementsConsistent()
    {
        //size should initially be empty
        Assert.assertEquals("Map.size() does not return zero for an empty map", 0, map.size());
        Assert.assertTrue("Map.isEmpty() does not return true for an empty map", map.isEmpty());

        //size should represent the number of pairs in the map
        addTestableElements();
        Assert.assertEquals("Number of elements in the map does not match the number of pairs added" +
                " to the map", names.length, map.size());
        Assert.assertFalse("Map.isEmpty() does not return false for a non-empty map", map.isEmpty());

        //size should change after more additions or removals
        map.add("Sally", SALLY_AGE);
        Assert.assertEquals("Map.size() has not incremented after adding a new element",
                names.length + 1, map.size());
        map.remove("Jeremy");
        Assert.assertEquals("Map.size() has not decremented after adding a new element",
                names.length, map.size());

        map.clear();
        Assert.assertEquals("Map.size() does not return zero after calling Map.clear()",
                0, map.size());
    }

    /**
     * Verifies that Map.keyExists(key) correctly identifies keys in the map.
     */
    @Test
    public void keysExist()
    {
        addTestableElements();

        //find existing key
        Assert.assertTrue("Existing key is not found in map (Jonas)", map.keyExists("Jonas"));

        //look for missing key
        Assert.assertFalse("Map is reporting a key that has not been added to map (Sam)",
                map.keyExists("Sam"));
    }

    /**
     * Verifies that Map.valueExists(key) correctly identifies values in the map.
     */
    @Test
    public void valuesExist()
    {
        addTestableElements();

        //find existing value
        Assert.assertTrue("Existing value is not found in map (" + JILL_AGE +
                ")", map.valueExists(JILL_AGE));

        //look for missing value
        Assert.assertFalse("Map is reporting a value that has not been added to map (50)",
                map.valueExists(MISSING_AGE));
    }

    /**
     * Verifies that the iterator in the Map class returns all pairs in the map.
     */
    @Test
    public void iteratingOverPairs()
    {
        addTestableElements();

        //see if we can identify all pairs added
        String[] namesSeen = new String[names.length];
        int[] heightsSeen = new int[heightInInches.length];
        int index = 0;
        for (KeyValuePair<String, Integer> pair : map)
        {
            namesSeen[index] = pair.getKey();
            heightsSeen[index] = pair.getValue();

            index++;
        }

        //sort and verify
        verifyNames(names, namesSeen);
        verifyHeights(heightInInches, heightsSeen);
    }

    /**
     * Verifies that the iterator from Map.keyset() returns all keys in the map.
     */
    @Test
    public void iteratingOverKeys()
    {
        addTestableElements();

        //see if we can identify all pairs added
        String[] namesSeen = new String[names.length];
        int index = 0;
        for (String name : map.keyset())
        {
            namesSeen[index] = name;

            index++;
        }

        //sort and verify
        verifyNames(names, namesSeen);
    }

    /**
     * Verifies that the iterator from Map.values() returns all values in the map.
     */
    @Test
    public void iteratingOverValues()
    {
        addTestableElements();

        //see if we can identify all pairs added
        int[] heightsSeen = new int[heightInInches.length];
        int index = 0;
        for (int height : map.values())
        {
            heightsSeen[index] = height;

            index++;
        }

        //sort and verify
        verifyHeights(heightInInches, heightsSeen);
    }

    private void verifyNames(String[] expected, String[] actual)
    {
        Arrays.sort(actual);
        Arrays.sort(expected);

        Assert.assertArrayEquals("All names retrieved from the map " + Arrays.toString(expected) +
                        " do not match the names added previously " + Arrays.toString(actual),
                expected, actual);
    }

    private void verifyHeights(int[] expected, int[] actual)
    {
        Arrays.sort(actual);
        Arrays.sort(expected);

        Assert.assertArrayEquals("All heights retrieved from the map " + Arrays.toString(expected) +
                        " do not match the heights added previously " + Arrays.toString(actual),
                expected, actual);
    }
}
