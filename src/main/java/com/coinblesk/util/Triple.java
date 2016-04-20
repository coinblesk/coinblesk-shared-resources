/*
 * Copyright 2016 The Coinblesk team and the CSG Group at University of Zurich
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.coinblesk.util;

/**
 *
 * @author Thomas Bocek
 */
public class Triple<K, V, L> {

    private final K element0;
    private final V element1;
    private final L element2;

    public static <K, V, L> Triple<K, V, L> create(K element0, V element1, L element2) {
        return new Triple<K, V, L>(element0, element1, element2);
    }

    public Triple(K element0, V element1, L element2) {
        this.element0 = element0;
        this.element1 = element1;
        this.element2 = element2;
    }

    public K element0() {
        return element0;
    }

    public V element1() {
        return element1;
    }
    
    public L element2() {
        return element2;
    }

    public Triple<K, V, L> element0(K element0) {
        return new Triple<K, V, L>(element0, element1, element2);
    }

    public Triple<K, V, L> element1(V element1) {
        return new Triple<K, V, L>(element0, element1, element2);
    }

    public Triple<K, V, L> element2(L element2) {
        return new Triple<K, V, L>(element0, element1, element2);
    }

    public boolean isEmpty() {
        return element0 == null && element1 == null;
    }

    public static <K, V, L> Triple<K, V, L> empty() {
        return new Triple<K, V, L>(null, null, null);
    }

    /**
     * Checks the two objects for equality by delegating to their respective {@link Object#equals(Object)}
     * methods.
     *
     * @param o the {@link Pair} to which this one is to be checked for equality
     * @return true if the underlying objects of the Pair are both considered equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Triple)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Triple<?, ?, ?> p = (Triple<?, ?, ?>) o;
        return equals(p.element0, element0)
                && equals(p.element1, element1)
                && equals(p.element2, element2);
    }

    /**
     * Compute a hash code using the hash codes of the underlying objects
     *
     * @return a hashcode of the Pair
     */
    @Override
    public int hashCode() {
        return (element0 == null ? 0 : element0.hashCode())
                ^ (element1 == null ? 0 : element1.hashCode())
                ^ (element2 == null ? 0 : element2.hashCode());
    }

    private static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
}
