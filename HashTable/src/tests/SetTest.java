package tests;

import interfaces.ISet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import structures.Set;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

/**
 * Tests the Set<T> class to determine if it follows
 * the contract of the ISet<T> interface.
 *
 * DO NOT ALTER THIS FILE!
 *
 * @author Josh Archer
 * @version 1.0
 */
public class SetTest
{
    private static final int LOW = 3;
    private static final int HIGH = 7;
    private static final int NUM_TEST_ELEMENTS = 5;
    private static final int ARRAY_TEST_ELEMENTS = 10000;

    private ISet<Integer> set;

    /**
     * Prepares each test before execution.
     */
    @Before
    public void setup()
    {
        set = new Set<>();
    }

    //adds some initial elements to the set for tests
    private void addTestableElements()
    {
        for (int i = 1; i <= NUM_TEST_ELEMENTS; i++)
        {
            set.add(i);
        }
    }

    /**
     * Verifies that elements that are added to set are actually
     * inside the structure.
     */
    @Test
    public void addedElementsExist()
    {
        addTestableElements();

        //can we find the elements added above?
        for (int i = 1; i <= NUM_TEST_ELEMENTS; i++)
        {
            Assert.assertTrue("Element " + i + " is missing after being added to the set",
                    set.contains(i));
        }

        //are there other elements?
        Assert.assertEquals("size() is incorrect after adding " + NUM_TEST_ELEMENTS + " elements",
               NUM_TEST_ELEMENTS, set.size());

        //double check no false positives
        int[] missingElements = {0, 6, 10};
        for (int i = 1; i < missingElements.length; i++)
        {
            Assert.assertFalse("Missing element " + missingElements[i] + " is reported as found.",
                    set.contains(missingElements[i]));
        }

        //make sure we can retrieve the element using get()
        Assert.assertEquals("Cannot retrieve element using Set.get()", 1, set.get(1).intValue());
    }

    /**
     * Verifies that duplicates can never be added to the set.
     */
    @Test
    public void noDuplicates()
    {
        int testElement = 1;

        addTestableElements();

        //record the size
        int size = set.size();

        //add a duplicate
        set.add(testElement);

        //make sure the size has not changed
        Assert.assertEquals("Size has changed after adding a duplicate.", size, set.size());

        //make sure we only find the new element a single time
        int timesSeen = 0;
        for (int element : set)
        {
            if (element == testElement)
            {
                timesSeen++;
            }
        }

        Assert.assertEquals("Element found more than once in the set.", 1, timesSeen);
    }

    /**
     * Performs a stress test by adding thousands of elements to the
     * set and verifying that all elements are accessible.
     */
    @Test
    public void addingMultipleElements()
    {
        //stress test the structure to look for errors
        int[] testElements = generateRandomArray();

        //add them all, expect no errors
        for (int element : testElements)
        {
            set.add(element);
        }

        //make sure our size is sensible (it is unlikely to be equal to
        //NUM_ELEMENTS due to duplicates)
        Assert.assertTrue("Number of elements in set (" + set.size() +") is larger " +
                "than the number of elements added as part of the test (" + ARRAY_TEST_ELEMENTS + ")",
                set.size() <= ARRAY_TEST_ELEMENTS);

        //make sure no duplicates
        int[] elements = new int[set.size()]; //move elements to an array
        int index = 0;
        for (int element : set)
        {
            elements[index] = element;
            index++;
        }

        Arrays.sort(elements); //sort the array

        for (int i = 0; i < elements.length - 1; i++) //make sure all adjacent elements are not similar
        {
            Assert.assertTrue("Duplicate elements found: " + elements[i] + ", " + elements[i + 1],
                    elements[i] != elements[i + 1]);
        }
    }

    /**
     * Tests the removal of elements from the set.
     */
    @Test
    public void removeElements()
    {
        int element = 3;

        addTestableElements();

        //remove an element and verify that we can still add the element again
        //i.e. it shouldn't get flagged as a duplicate
        set.remove(element);

        //verify removed
        for (int current : set)
        {
            Assert.assertNotEquals("Element (" + element +
                            ") was not removed from the set after calling Set.remove(3)",
                    current, element);
        }
        Assert.assertEquals("Size is not one less after calling Set.remove(3)",
                NUM_TEST_ELEMENTS - 1, set.size());

        //add the element again
        set.add(element);

        //verify added
        Assert.assertTrue("Set does not contain 3 after calling Set.remove(3) and then Set.add(3)", set.contains(3));
        Assert.assertEquals("Size is incorrect after calling Set.remove(3) and then Set.add(3)",
                NUM_TEST_ELEMENTS, set.size());

        //verify that clear is working
        set.clear();
        Assert.assertEquals("Set size is not zero after calling Set.clear()", 0, set.size());
        Assert.assertTrue("Set.isEmpty() does not return false for an empty set.", set.isEmpty());
    }

    /**
     * Verifies that the set iterator works as expected.
     */
    @Test
    public void iteratorUsable()
    {
        //iterator makes sense for an empty set
        Iterator<Integer> iter = set.iterator();
        Assert.assertFalse("Iterator.hasNext() is returning true with no elements", iter.hasNext());

        //verify that we can retrieve elements added
        addTestableElements();
        containsAll(set, generateRange(1, 5),
                "Elements returned from iterator do not match those added");
    }

    /**
     * Verifies that the union operation and empty sets
     * behave as expected.
     */
    @Test
    public void unionOfEmptySets()
    {
        //create another set
        ISet<Integer> otherSet = new Set<>();

        //verify union of empty sets is empty set
        ISet<Integer> result = set.union(otherSet);
        Assert.assertEquals("Result of union of two empty sets is not an empty set",
                result.size(), 0);
    }

    /**
     * Verifies that the union operation behaves correctly for
     * non-empty sets.
     */
    @Test
    public void unionOperationWorks()
    {
        addTestableElements();

        //create another set
        ISet<Integer> otherSet = new Set<>();
        for (int i = LOW; i <= HIGH; i++)
        {
            otherSet.add(i);
        }

        //perform the union, verify the results
        ISet<Integer> result = set.union(otherSet);
        for (int i = 1; i <= HIGH; i++)
        {
            Assert.assertTrue("Set does not contain (" + i + ") after union operation",
                    result.contains(i));
        }
    }

    /**
     * Verifies that the intersection operation and empty sets
     * behave as expected.
     */
    @Test
    public void intersectionWithEmptySet()
    {
        addTestableElements();

        //create another set
        ISet<Integer> otherSet = new Set<>();

        //verify intersection of non-empty set and an empty set is the empty set
        ISet<Integer> result = set.intersects(otherSet);
        Assert.assertTrue("Result of intersection of non-empty set and empty set is not an empty set",
                result.isEmpty());
    }

    /**
     * Verifies that the intersection operation behaves correctly for
     * non-empty sets.
     */
    @Test
    public void intersectionOperationWorks()
    {
        addTestableElements();

        //create another set
        ISet<Integer> otherSet = new Set<>();
        for (int i = LOW; i <= HIGH; i++)
        {
            otherSet.add(i);
        }

        //perform the union, verify the results
        ISet<Integer> result = set.intersects(otherSet);
        for (int i = LOW; i <= NUM_TEST_ELEMENTS; i++)
        {
            Assert.assertTrue("Set does not contain (" + i + ") after intersecting " +
                            "[1, 2, 3, 4, 5] and [3, 4, 5, 6, 7]",
                    result.contains(i));
        }

        for (int i = 1; i <= HIGH; i++) {
            if (i < LOW || i > NUM_TEST_ELEMENTS)
            {
                Assert.assertFalse("Set contains (" + i + ") after intersecting " +
                                "[1, 2, 3, 4, 5] and [3, 4, 5, 6, 7]", result.contains(i));
            }
        }
    }

    /**
     * Verifies that the difference operation and empty sets
     * behave as expected.
     */
    @Test
    public void differenceWithEmptySet()
    {
        addTestableElements();

        //create another set
        ISet<Integer> otherSet = new Set<>();

        //verify intersection of non-empty set and an empty set is the empty set
        ISet<Integer> result = otherSet.difference(set);
        Assert.assertTrue("Result of {} - {1, 2, 3, 4, 5} should be {}",
                result.isEmpty());

        result = set.difference(otherSet);
        Assert.assertEquals("Result of {1, 2, 3, 4, 5} - {} should be {1, 2, 3, 4, 5}",
                result.size(), 5);
        containsAll(set, generateRange(1, 5),
                "Result of {1, 2, 3, 4, 5} - {} should be {1, 2, 3, 4, 5}");
    }

    /**
     * Verifies that the difference operation behaves correctly for
     * non-empty sets.
     */
    @Test
    public void differenceOperationWorks()
    {
        addTestableElements();

        //create another set
        ISet<Integer> otherSet = new Set<>();
        for (int i = LOW; i <= HIGH; i++)
        {
            otherSet.add(i);
        }

        //verify A - B
        ISet<Integer> result = set.difference(otherSet);
        Assert.assertEquals("Result of {1, 2, 3, 4, 5} - {3, 4, 5, 6, 7} should be {1, 2}",
                result.size(), 2);
        containsAll(result, generateRange(1, 2),
                "Result of {1, 2, 3, 4, 5} - {3, 4, 5, 6, 7} should be {1, 2}");

        //verify B - A
        result = otherSet.difference(set);
        Assert.assertEquals("Result of {3, 4, 5, 6, 7} - {1, 2, 3, 4, 5} should be {6, 7}",
                result.size(), 2);
        containsAll(result, generateRange(6, 7),
                "Result of {3, 4, 5, 6, 7} - {1, 2, 3, 4, 5} should be {6, 7}");
    }

    /**
     * Verifies that the subset operation and empty sets
     * behave as expected.
     */
    @Test
    public void subsetWithEmptySet()
    {
        ISet<Integer> otherSet = new Set<>();

        //empty set is a subset of the empty set
        Assert.assertTrue("An empty set is not recognized as a subset of another empty set",
                set.isSubset(otherSet));

        //empty set is a subset of a non-empty set
        addTestableElements();

        Assert.assertTrue("An empty set is not recognized as a subset of a non-empty set",
                set.isSubset(otherSet));
        Assert.assertFalse("A non-empty set is recognized as a subset of an empty set",
                otherSet.isSubset(set));
    }

    /**
     * Verifies that the subset operation behaves correctly for
     * non-empty sets.
     */
    @Test
    public void subsetOperationWorks()
    {
        addTestableElements();

        //create another set
        ISet<Integer> otherSet = new Set<>();
        for (int i = LOW; i <= NUM_TEST_ELEMENTS; i++)
        {
            otherSet.add(i);
        }

        //verify subsets
        Assert.assertTrue("{3, 4, 5} is not recognized as a subset of {1, 2, 3, 4, 5}",
                set.isSubset(otherSet));
        Assert.assertFalse("{1, 2, 3, 4, 5} is recognized as a subset of {3, 4, 5}",
                otherSet.isSubset(set));
    }

    /**
     * Verifies that the disjoint operation and empty sets
     * behave as expected.
     */
    @Test
    public void disjointWithEmptySet()
    {
        ISet<Integer> otherSet = new Set<>();

        //empty set is disjoint with any other set
        Assert.assertTrue("An empty set is not recognized as disjoint with another set",
                otherSet.isDisjoint(set));

        //add elements and verify still disjoint
        addTestableElements();

        Assert.assertTrue("An empty set is not recognized as disjoint with another set",
                otherSet.isDisjoint(set));
    }

    /**
     * Verifies that the disjoint operation behaves correctly for
     * non-empty sets.
     */
    @Test
    public void disjointOperationWorks()
    {
        addTestableElements();

        //create another set
        ISet<Integer> otherSet = new Set<>();
        for (int i = LOW; i <= HIGH; i++)
        {
            otherSet.add(i);
        }

        //verify sets that are not disjoint
        Assert.assertFalse("{1, 2, 3, 4, 5} and {3, 4, 5, 6, 7} are incorrectly recognized as disjoint",
                set.isDisjoint(otherSet));

        //change elements
        otherSet = new Set<>();
        for (int i = NUM_TEST_ELEMENTS + 1; i <= HIGH; i++)
        {
            otherSet.add(i);
        }

        //verify sets that are disjoint
        Assert.assertTrue("{1, 2, 3, 4, 5} and {6, 7} are not recognized as disjoint",
                set.isDisjoint(otherSet));
    }

    /**
     * Verifies that the empty set operation behaves correctly.
     */
    @Test
    public void emptySetOperationWorks()
    {
        //before adding any elements a set is the empty set
        Assert.assertTrue("A set with no elements is not recognized as the empty set",
                set.isEmptySet());

        //sets with elements are not the empty set
        addTestableElements();
        Assert.assertFalse("A set with elements is recognized as the empty set", set.isEmptySet());

        //removing all elements should result in the empty set
        for (int i = 1; i <= NUM_TEST_ELEMENTS; i++)
        {
            set.remove(i);
        }
        Assert.assertTrue("After removing all elements a set is not recognized as the empty set",
                set.isEmptySet());
    }

    private void containsAll(ISet<Integer> set, int[] expectedElements, String message)
    {
        //remove all elements from the set and add them to a sorted array
        int index = 0;
        int[] retrieved = new int[set.size()];
        for (int element : set)
        {
            retrieved[index] = element;
            index++;
        }
        Arrays.sort(retrieved);

        //verify this matches our expected elements
        Assert.assertArrayEquals(message, expectedElements, retrieved);
    }

    private int[] generateRange(int lowValue, int highValue)
    {
        int[] array = new int[highValue - lowValue + 1];

        for (int i = 0; i < array.length; i++)
        {
            array[i] = lowValue + i;
        }

        return array;
    }

    private int[] generateRandomArray()
    {
        Random random = new Random();
        int[] array = new int[ARRAY_TEST_ELEMENTS];

        for (int i = 0; i < array.length; i++)
        {
            array[i] = random.nextInt(ARRAY_TEST_ELEMENTS + 1);
        }

        return array;
    }
}
