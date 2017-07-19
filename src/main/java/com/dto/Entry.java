package com.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by JoeHuang on 2017/7/19.
 */
public class Entry<K,V> implements Map.Entry<K,V>{
    int hash;
    K key;
    V value;
    Entry<K,V> next;

    Entry(int hash, K key, V value, Entry<K,V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public Entry() {
    }

    public final K getKey()        { return key; }
    public final V getValue()      { return value; }


    public final String toString() { return key + "=" + value; }

    public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    public final V setValue(V newValue) {
        V oldValue = value;
        value = newValue;
        return oldValue;
    }

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public final boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof Map.Entry) {
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
            if (Objects.equals(key, e.getKey()) &&
                    Objects.equals(value, e.getValue()))
                return true;
        }
        return false;
    }
}
