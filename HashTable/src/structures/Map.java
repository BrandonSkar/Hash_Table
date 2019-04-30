package structures;

import hashtable.HashTable;
import helpers.KeyValuePair;
import interfaces.ICollection;
import interfaces.IMap;
import interfaces.ISet;

import java.util.Iterator;

public class Map<K, V> implements IMap<K, V>
{
    private HashTable<KeyValuePair<K, V>> table = new HashTable<>();
    private KeyValuePair<K, V> hashMap;

    @Override
    public void add(K key, V value)
    {
        //create new KeyValue Object and store it in the table
        hashMap = new KeyValuePair<>(key, value);
        table.add(hashMap);
    }

    @Override
    public void remove(K key)
    {
        //remove the KeyValue that matches the key
        table.remove(new KeyValuePair<>(key, null));
    }

    @Override
    public V get(K key)
    {
        V value = null;

        //loop through each KeyValue in the table
        for(KeyValuePair<K, V> element : table) {
            //if the keys match, get the value
            if(element.getKey().equals(key)) {
                value = element.getValue();
                break;

            }
        }
        return value;
    }

    @Override
    public boolean keyExists(K key)
    {
        //find if table contains a KeyValue with given key
        return table.contains(new KeyValuePair<>(key, null));
    }

    @Override
    public boolean valueExists(V value)
    {
        //loop through each KeyValue in the table and find if the value
        //is in the table
        for(KeyValuePair<K, V> element : table) {
            if(element.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size()
    {
        //table size
        return table.size();
    }

    @Override
    public boolean isEmpty()
    {
        //empty or not
        return table.isEmpty();
    }

    @Override
    public void clear()
    {
        //clear the table
        table.clear();
    }

    @Override
    public Iterator<KeyValuePair<K, V>> iterator()
    {
        //return table iterator
        return table.iterator();
    }

    @Override
    public ISet<K> keyset()
    {
        ISet<K> temp = new Set<>();
        //loop through the KeyValues in table and get each key from the table
        for(KeyValuePair<K, V> element : table) {
            temp.add(element.getKey());
        }
        return temp;
    }

    @Override
    public ICollection<V> values()
    {
        ICollection<V> temp = new Set<>();
        //loop through the KeyValues in the table and get each value from the table
        for(KeyValuePair<K, V> element : table) {
            temp.add(element.getValue());
        }
        return temp;
    }
}
