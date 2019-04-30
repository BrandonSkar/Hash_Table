package helpers;

/**
 * Stores a key/value pair together in an object.
 *
 * DO NOT ALTER THIS FILE!
 *
 * @author Josh Archer
 * @version 1.0
 */
public class KeyValuePair<K, V>
{
    private K key;
    private V value;

    /**
     * Creates a new key/value pair.
     * @param key the new key
     * @param value the new value
     */
    public KeyValuePair(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    /**
     * Retrieves the key from the pair.
     * @return the key
     */
    public K getKey()
    {
        return key;
    }

    /**
     * Retrieves the value from the pair.
     * @return the value
     */
    public V getValue()
    {
        return value;
    }

    /**
     * Determines if two key/value pairs are equal, based
     * on their key alone.
     * @param other the other pair
     * @return true if the keys from two different pairs are
     * the same
     */
    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        else if (other == null || getClass() != other.getClass())
        {
            return false;
        }

        KeyValuePair<K, V> otherPair = (KeyValuePair<K, V>) other;
        return key.equals(otherPair.key);
    }

    /**
     * Returns the hash code for this pair based on
     * the key alone.
     * @return a new hash code
     */
    @Override
    public int hashCode()
    {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return key + " --> " + value;
    }
}
