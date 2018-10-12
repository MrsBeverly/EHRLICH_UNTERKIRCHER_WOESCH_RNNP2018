package Blatt2.Aufgabe2;

import java.util.Arrays;

public class IntegerList {
    protected int[] data;
    protected int size = 0;
    private static final int DEFAULT_SIZE = 10;


    //Constructor for producing an empty IntegerList
    public IntegerList() {
        data = new int[DEFAULT_SIZE];
    }


    //Constructor for producing a list and migrating an existing Array
    public IntegerList(IntegerList toCopy) {
        /** * copy the original */
        data = toCopy.toArray();
        size = toCopy.size;
    }


    //Returns Value on index it is given, throws exception if index doesn't exist.
    public synchronized int get(int index) {
        if (data == null) {
            throw new NullPointerException();
        }
        if (index > size - 1) {
            throw new NullPointerException();
        }

        return data[index];
    }


    //Adds new value to the List on the first empty spot. Doubles length of array if it is full.
    public synchronized void add(int value) {
        //if fill-status (size) == data.length then make a new data-array with doubled size and migrate the values.
        if (size >= data.length) {
            int[] tmp = new int[data.length * 2];
            for (int i = 0; i < data.length; i++) {
                tmp[i] = data[i];
            }
            data = tmp;
        }

        //insert new value on next empty index
        data[size] = value;
        size++;

    }

    //resets List and fill-status
    public synchronized void clear() {
        data = new int[DEFAULT_SIZE];
        size = 0;
    }


    //Returns complete List as an array (Copy, not Reference)
    public synchronized int[] toArray() {
        return Arrays.copyOf(data, data.length);
    }

}
