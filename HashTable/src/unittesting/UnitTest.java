package unittesting;

import hashtable.HashTable;
import org.junit.*;

import java.util.*;

/**
 * Provides a series of tests that verifies the functionality of
 * the HashTable
 *
 * @author Brandon Skar
 * @version 1.0
 */
public class UnitTest
{
    private HashTable table;

    /**
     * Create a table before each test
     */
    @Before
    public void createTable()
    {
        table = new HashTable();
    }

    /**
     * Test that the add method properly adds elements to the table and that
     * the size of the table increments accordingly
     * and also that it adds resizes correctly when adding elements to the same index
     */
    @Test
    public void testAdd()
    {
        table.add(1);
        //test if size increments by 1 if an element is added
        Assert.assertEquals("The size of the table is incorrect after adding " +
                "one element", 1, table.size(), 0);

        //check that the table contains 1
        Assert.assertTrue("The element is not contained in the table", table.contains(1));

        //clear the table
        table.clear();

        //add 5 elements with null indexes in between
        for(int i = 0; i < 5; i++) {
            table.add(i * 2);
        }

        //test if the size is 5
        Assert.assertEquals("The size of the table is incorrect after adding " +
                "5 elements", 5, table.size(), 0);

        //clear the table
        table.clear();

        //add 50 elements to the same index in the middle of the table until resize
        for(int i = 5; i <= 500; i += 10) {
            table.add(i);
        }
        for(int i = 5; i <= 500; i += 10) {
            table.add(i);
        }
        for(Object element : table) {
            System.out.println(element);
        }

        //test to see if the size is 50, resize should also be working
        Assert.assertEquals("The size of the table is incorrect after adding 50 " +
                "elements", 50, table.size(), 0);

        table.clear();

        //add duplicates to the table to make sure they are not counted
        table.add(2);
        table.add(2);
        table.add(2);

        //size should be 1
        Assert.assertEquals("Size of the table is incorrect after adding an element and " +
                "2 more duplicates", 1, table.size(), 0);
    }

    /**
     * Test that the remove method removes the specified elements from the table and
     * throws a NoSuchElementException when the element is not in the table
     * also the size is decremented as elements are removed
     */
    @Test
    public void testRemove()
    {
        //remove an element that doesnt exist
        try {
            table.remove(1);

            //should not get here
            Assert.fail("Did not throw an exception when removing an element that does not exist");
        }
        catch(NoSuchElementException ex) {}

        //add an element and remove it
        table.add(1);
        table.remove(1);

        //test that 1 is not in the table
        Assert.assertFalse("The table still contains 1 after removing it from the table", table.contains(1));

        Assert.assertEquals("Table size should be 0 after adding and removing 1 element",
                0, table.size(), 0);

        //remove an element that was already removed from the table
        try {
            table.remove(1);

            //should not get here
            Assert.fail("Did not throw an exception when removing an element that was already removed");
        }
        catch(NoSuchElementException ex) {}
    }

    /**
     * Test that the contains method finds the given element in the table
     * and it should not find an element that is not in the table
     */
    @Test
    public void testContains()
    {
        //test for an element that does not exist
        Assert.assertFalse("Element " + 1 + " should not be found in an empty table", table.contains(1));

        //test for an element that does exist
        table.add(1);
        Assert.assertTrue("Element " + 1 + " should be found after adding it to the table", table.contains(1));
    }

    /**
     * Test that the size of the table is incremented as elements are added and removed
     * and that the size is 0 when the table is cleared
     */
    @Test
    public void testSize()
    {
        //test size of an empty table
        Assert.assertEquals("Empty table size should be 0",
                0, table.size(), 0);

        //add 10 elements to the table and check the size
        for(int i = 0; i < 10; i++) {
            table.add(i);
        }

        Assert.assertEquals("Table size should be 10 after adding 10 elements",
                10, table.size(), 0);

        //test table size after removing 5 elements from the table
        for(int i = 0; i < 5; i++) {
            table.remove(i);
        }

        Assert.assertEquals("Table size should be 5 after removing 5 elements " +
                "from a table of 10 elements", 5, table.size(), 0);

        //test table size after clearing the table
        table.clear();

        Assert.assertEquals("Table size should be 0 after clearing the table",
                0, table.size(), 0);

        //add 500 elements to the table and check to make sure resizing
        //adjusts the size correctly
        for(int i = 0; i < 500; i++) {
            table.add(i);
        }

        Assert.assertEquals("Table size should be 500 after adding 500 elements to the table",
                500, table.size(), 0);
    }

    /**
     * Test that the isEmpty method returns that the table is empty on a newly created table
     * and that it is not empty on a table with an element added and that it is empty after
     * the table is cleared
     */
    @Test
    public void testIsEmpty()
    {
        //test is empty table is empty
        Assert.assertTrue("isEmpty method should return true on an empty table", table.isEmpty());

        //add an element to the table and test isEmpty()
        table.add(1);
        Assert.assertFalse("isEmpty method should return false on a table with 1 element", table.isEmpty());

        //test if table is empty after table is cleared
        table.clear();
        Assert.assertTrue("isEmpty method should return true on a table that was just cleared", table.isEmpty());
    }

    /**
     * Test that the clear method removes all of the elements from the table
     */
    @Test
    public void testClear()
    {
        //add elements and then clear and make sure size is 0
        for(int i = 0; i < 10; i++) {
            table.add(i);
        }

        table.clear();

        //the foreach loop should not be entered if the table is empty
        for(Object element : table) {
            //should not get here
            Assert.fail("The iterator detected that there was an element in the table after clearing " +
                    "the table");
        }

        //isEmpty should report a size of 0 elements
        Assert.assertTrue("Table should be empty after clearing the table", table.isEmpty());
    }

    /**
     * Test that the get method returns the correct element given by the user
     */
    @Test
    public void testGet()
    {
        //add elements to the table
        for(int i = 0; i < 10; i++) {
            table.add(i);
        }

        //check if the get method returns the correct element
        Assert.assertEquals("Get method should return 1 when get(1) is called", 1, (int)table.get(1), 0);
    }

    /**
     * Test that the iterator created is not null and that it properly iterates through the table
     * after adding elements to it.
     */
    @Test
    public void testIterator()
    {
        Iterator<Integer> iter = table.iterator();
        //test if we get a valid iterator
        Assert.assertNotEquals("Iterator is of a null value", null, iter);
        for(int i = 0; i < 25; i++) {
            table.add(i);
        }
        List<Integer> list = new ArrayList<>();
        for(Object element : table) {
            list.add((int)element);
        }
        Collections.sort(list);

        for(int i = 0; i < 25; i++) {
            //test to make sure the iterator got all the elements added
            Assert.assertEquals("The list provided the wrong element", i, list.get(i), 0);
        }
    }
}
