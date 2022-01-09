package com.amdocs.oss.common;

public class Box<K,V> implements Pair<K, V> {

    private K key;
    private V value;

    public Box(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }
}
