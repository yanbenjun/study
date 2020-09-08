package com.yanbenjun.util;

import java.util.Map;

public class Pair<K, V> implements Map.Entry<K, V>
{
	private K key;
	private V value;
	
	public Pair(K key, V value)
	{
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey() {
		return this.key;
	}

	@Override
	public V getValue() {
		return this.value;
	}

	@Override
	public V setValue(V value) {
		V old = getValue();
		this.value = value;
		return old;
	}

}
