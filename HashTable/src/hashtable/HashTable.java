package hashtable;
import interfaces.ICollection;

import java.util.*;

/**
 * HashTable Collection Object
 * @param <T> Accepts any Object
 * @author Brandon Skar
 * @version 1.0
 */
public class HashTable<T> implements ICollection<T>, Iterable<T>
{
    private static final int INITIAL_TABLE_SIZE = 10;
    private static final double LOAD_FACTOR = 2.5;
    private static final double RESIZE_RATIO = 1.5;
    private Node[] elements;
    private int size;
    private int modCount;

    /**
     * Default constructor for HashTable class
     * instantiates a Node array
     */
    public HashTable()
    {
        elements = new Node[INITIAL_TABLE_SIZE];
    }

    @Override
    public void add(T element)
    {
        //check for duplicate
        if(contains(element)) {
            return;
        }

        //if the amount of elements reaches over %250 of the array length, then
        //resize the array
        if((double)((size) / elements.length) > LOAD_FACTOR) {
            resize();
        }

        //Get the hash code of the element and mod it by the array length
        //to get the array index to put it in
        int hashCode = Math.abs(element.hashCode());
        int index = hashCode % elements.length;

        //if empty index of the array
        if(elements[index] == null) {
            elements[index] = new Node<>(element, null);
        }
        //begin traversing the nodes of the arrays index
        else {
            Node<T> pointer = elements[index];

            //begin traversing the nodes
            while(pointer.next != null) {

                //go to the next node
                pointer = pointer.next;
            }
            //if it reaches the end of the list. add the new node
            //pointing to a null node
            pointer.next = new Node<>(element, null);
        }

        //increment size and modCount
        size++;
        modCount++;
    }

    //resize the table when there are more than %250 of elements in the table
    private void resize()
    {
        //make size 0 to begin adding elements to the new array
        size = 0;

        //add elements from the table to a list
        List<T> list = new ArrayList<>();
        for(Node<T> element : elements) {
            if(element != null) {
                while(element.next != null) {
                    list.add(element.data);
                    element = element.next;
                }
                list.add(element.data);
            }
        }

        //create a new elements table with a new size of %150 of the old table
        elements = new Node[(int)(elements.length * RESIZE_RATIO)];

        //begin adding each element to the table from the list
        for(T element : list) {
            add(element);
        }
    }

    @Override
    public void remove(T element)
    {
        //Get the hash code of the element and mod it by the array length
        //to get the array index to put it in
        int hashCode = Math.abs(element.hashCode());
        int index = hashCode % elements.length;
        Node<T> pointer = elements[index];

        //throw an exception when trying to remove an element that does not exist
        if(pointer == null) {
            throw new NoSuchElementException("No element " + element + " found");
        }
        else {
            //if the element is the first one found
            if(pointer.data.equals(element)) {
                elements[index] = pointer.next;
                size--;
                modCount++;
            }
            //scan for the element
            else {
                while (pointer.next != null) {
                    if (pointer.next.data.equals(element)) {
                        pointer.next = pointer.next.next;
                        size--;
                        modCount++;
                        return;
                    }
                    pointer = pointer.next;
                }
            }
        }

    }

    @Override
    public boolean contains(T element)
    {
        //Get the hash code of the element and mod it by the array length
        //to get the array index to put it in
        int hashCode = Math.abs(element.hashCode());
        int index = hashCode % elements.length;
        Node<T> pointer = elements[index];

        //if first node is null then element is not in the table
        if(pointer == null) {
            return false;
        }
        //scan the elements
        else {
            //if the element is the first one found
            if(pointer.data.equals(element)) {
                return true;
            }
            else {
                //scan for the element
                while (pointer.next != null) {
                    if (pointer.next.data.equals(element)) {
                        return true;
                    }
                    pointer = pointer.next;
                }
            }
        }
        return false;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear()
    {
        elements = new Node[INITIAL_TABLE_SIZE];
        size = 0;
        modCount++;
    }

    @Override
    public T get(T element)
    {
        //if the table contains the element, return it, otherwise return null
        //contains method uses .equals() method to compare elements
        if(contains(element)) {
            return element;
        }
        return null;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new TableIterator();
    }

    @Override
    public String toString()
    {
        return "HashTable{" +
                "elements=" + Arrays.toString(elements) +
                ", size=" + size +
                '}';
    }

    private class Node<T>
    {
        private T data;
        private Node next;

        public Node(T data, Node next)
        {
            this.data = data;
            this.next = next;
        }

        @Override
        public String toString()
        {
            return "Node{" +
                    "data=" + data +
                    ", next=" + next +
                    '}';
        }
    }

    private class TableIterator implements Iterator<T>
    {
        private int currentModCount;
        private int currentIndex = -1;
        private Node<T> currentNode;

        public TableIterator()
        {
            //find the next index
            currentModCount = modCount;
            findNextIndex();
        }

        //get the next index of the node array
        private void findNextIndex()
        {
            if(currentModCount != modCount) {
                throw new ConcurrentModificationException("Cannot modify table while iterating");
            }

            //get next index while currentIndex is less than the length of the array
            //and the index is not null
            do {
                currentIndex++;
            }
            while(currentIndex < elements.length && elements[currentIndex] == null);

            //if the currentIndex is out of bounds make currentIndex -1 so does not search for next
            if(currentIndex == elements.length) {
                currentIndex = -1;
                return;
            }

            //if the index is not null make currentNode equal the node at the currentIndex
            if(elements[currentIndex] != null) {
                currentNode = elements[currentIndex];
            }
        }

        //when it is in the index, keep searching for the next node until next is null
        private void findNextNode()
        {
            if(currentNode.next != null) {
                currentNode = currentNode.next;
            }
        }

        @Override
        public boolean hasNext()
        {
            if(currentModCount != modCount) {
                throw new ConcurrentModificationException("Cannot modify table while iterating");
            }

            return currentIndex != -1;
        }

        @Override
        public T next()
        {
            if(currentModCount != modCount) {
                throw new ConcurrentModificationException("Cannot modify table while iterating");
            }

            T element = currentNode.data;
            if(currentNode.next == null) {
                findNextIndex();
            }
            else {
                findNextNode();
            }

            return element;
        }

        @Override
        public String toString()
        {
            return "TableIterator{" +
                    "currentIndex=" + currentIndex +
                    ", node=" + currentNode +
                    '}';
        }
    }
}
