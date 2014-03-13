package ch.uzh.csg.mbps.util;

import java.io.Serializable;

public class Pair<T> implements Serializable {
	private static final long serialVersionUID = -4191425920852909121L;
	
	private T one;
	private T two;
	
	public Pair() {
	}
	
	public Pair(T one, T two) {
		this.one = one;
		this.two = two;
	}
	
	public T getFirst() {
		return one;
	}
	
	public T getSecond() {
		return two;
	}
	
	public void setFirst(T t) {
		this.one = t;
	}
	
	public void setSecond(T t) {
		this.two = t;
	}
}
