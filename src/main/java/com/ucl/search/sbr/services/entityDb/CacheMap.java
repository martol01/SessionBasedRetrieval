package com.ucl.search.sbr.services.entityDb;

import java.util.TreeMap;

/**
 * Created by gabriel on 15/03/15.
 */
public class CacheMap<K,V> extends TreeMap<K,V> {

    private int cacheMaxSize;

    public CacheMap(int cacheMaxSize) {
        super();
        this.cacheMaxSize = cacheMaxSize;
    }

    @Override
    public V put(K key, V value) {
        V result = super.put(key, value);
        reduceCache();
        return result;
    }

    private void reduceCache() {
        int size = size();
        while (size > cacheMaxSize) {
            remove(firstKey());
        }
    }

}
