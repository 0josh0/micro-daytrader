package org.iscas.util;

import java.util.AbstractSequentialList;
import java.util.ListIterator;

/**
 * Created by andyren on 2016/6/28.
 */
public class KeyBlock extends AbstractSequentialList {

    // min and max provide range of valid primary keys for this KeyBlock
    private int min = 0;
    private int max = 0;
    private int index = 0;

    public KeyBlock() {
        super();
        min = 0;
        max = 0;
        index = min;
    }
    public KeyBlock(int min, int max) {
        super();
        this.min = min;
        this.max = max;
        index = min;
    }

    public int size() {
        return (max - min) + 1;
    }

    public ListIterator listIterator(int arg0) {
        return new KeyBlockIterator();
    }

    class KeyBlockIterator implements ListIterator {


        public boolean hasNext() {
            return index <= max;
        }


        public synchronized Object next() {
            if (index > max)
                throw new RuntimeException("KeyBlock:next() -- Error KeyBlock depleted");
            return new Integer(index++);
        }


        public boolean hasPrevious() {
            return index > min;
        }


        public Object previous() {
            return new Integer(--index);
        }


        public int nextIndex() {
            return index - min;
        }


        public int previousIndex() {
            throw new UnsupportedOperationException("KeyBlock: previousIndex() not supported");
        }

        public void add(Object o) {
            throw new UnsupportedOperationException("KeyBlock: add() not supported");
        }


        public void remove() {
            throw new UnsupportedOperationException("KeyBlock: remove() not supported");
        }


        public void set(Object arg0) {
        }
    }
}