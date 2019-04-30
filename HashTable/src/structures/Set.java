package structures;

import hashtable.HashTable;
import interfaces.ISet;

import java.util.Iterator;

public class Set<T> implements ISet<T>
{
    private HashTable<T> table = new HashTable<>();

    @Override
    public void add(T element)
    {
        //add element to the table
        table.add(element);
    }

    @Override
    public void remove(T element)
    {
        //remove element from the table
        table.remove(element);
    }

    @Override
    public boolean contains(T element)
    {
        //check if the table contains the given element
        return table.contains(element);
    }

    @Override
    public int size()
    {
        //return table size
        return table.size();
    }

    @Override
    public boolean isEmpty()
    {
        //return if the table is empty or not
        return table.isEmpty();
    }

    @Override
    public void clear()
    {
        //clear the table
        table.clear();
    }

    @Override
    public T get(T element)
    {
        //get the given element from the table
        return table.get(element);
    }

    @Override
    public Iterator<T> iterator()
    {
        //return the table iterator
        return table.iterator();
    }

    @Override
    public ISet<T> union(ISet<T> other)
    {
        ISet<T> temp = new Set<>();
        //add each element from original table to the ISet object
        for(T element : table) {
            temp.add(element);
        }

        //add each element from the other table to the ISet object
        for(T element : other) {
            temp.add(element);
        }
        return temp;
    }

    @Override
    public ISet<T> intersects(ISet<T> other)
    {
        ISet<T> temp = new Set<>();
        //loop through the table and add each element that is contained in
        //both original and other table
        for(T element : table) {
            for(T otherElement : other) {
                if(element.equals(otherElement)) {
                    temp.add(element);
                }
            }
        }
        return temp;
    }

    @Override
    public ISet<T> difference(ISet<T> other)
    {
        ISet<T> temp = new Set<>();
        //add each element in original table to ISet object
        for(T element : table) {
            temp.add(element);
        }

        //remove each element from ISet that matches the element in other table
        for(T element : table) {
            for(T otherElement : other) {
                if(element.equals(otherElement)) {
                    temp.remove(element);
                }
            }
        }
        return temp;
    }

    @Override
    public boolean isSubset(ISet<T> other)
    {
        //find if there are any elements that are not contained in original table
        for(T element : other) {
            if(!table.contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDisjoint(ISet<T> other)
    {
        //check that both tables have unique elements
        for(T element : other) {
            if(table.contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmptySet()
    {
        //return if table is empty
        return table.isEmpty();
    }
}
