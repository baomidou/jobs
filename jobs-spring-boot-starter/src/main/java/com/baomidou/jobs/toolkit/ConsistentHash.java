package com.baomidou.jobs.toolkit;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一次性 HASH
 *
 * @author jobob
 * @since 2019-07-20
 */
public class ConsistentHash<T> {
    private int virtualNodeCount = 1;
    private SortedMap<Integer, T> hashRing = new TreeMap<>();

    /**
     * Default constructor.
     */
    public ConsistentHash() {
        // to do nothing
    }

    /**
     * Constructor that can be set virtualNodeCount.
     * If you add new node, it will be created randomly any index as the number of virtualNodeCount.
     *
     * @param virtualNodeCount the number of virtual count per each node.
     */
    public ConsistentHash(int virtualNodeCount) {
        this.virtualNodeCount = virtualNodeCount;
    }

    /**
     * Add new node.
     *
     * @param node T node object.
     */
    public synchronized void add(T node) {
        for (int i = 0; i < virtualNodeCount; i++) {
            addNode(node, i);
        }
    }

    public synchronized void add(Collection<T> nodes) {
        int i = 0;
        for (T node : nodes) {
            addNode(node, ++i);
        }
    }

    private void addNode(T node, int i) {
        hashRing.put(getHashKey(node, i), node);
    }

    /**
     * Remove node from hash.
     *
     * @param node T node object.
     */
    public synchronized void remove(T node) {
        for (int i = 0; i < virtualNodeCount; i++) {
            hashRing.remove(getHashKey(node, i));
        }
    }

    public int getHashKey(T node, int i) {
        return hash(new StringBuffer("HASH-").append(node.toString())
                .append("-NODE-").append(i).toString());
    }

    /**
     * Get node from the given key.
     *
     * @param key key data.
     * @return Node node.
     */
    public T getNode(T key) {
        // 大于当前 hash 的所有 map
        SortedMap<Integer, T> subMap = hashRing.tailMap(hash(key.toString()));
        if (subMap.isEmpty()) {
            return hashRing.get(hashRing.firstKey());
        }
        return subMap.get(subMap.firstKey());
    }

    /**
     * FNV1_32_HASH 算法计算 Hash 值
     *
     * @param key 待计算 KEY
     * @return
     */
    private int hash(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash ^ key.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }
}