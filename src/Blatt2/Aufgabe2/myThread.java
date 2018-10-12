package Blatt2.Aufgabe2;
/*WRITTEN BY EHRLICH BEVERLY, UNTERKIRCHER CHRISTOPH AND WÖSCH TIMON*/


import java.util.concurrent.ThreadLocalRandom;

public class myThread extends Thread {
    static IntegerList myList = new IntegerList();

    public myThread(IntegerList myList) {
        this.myList = myList;
    }


    public void run() {

        //Debug boolean enables print-blocks for debugging, to see in which status the Program is at a sepcific moment
        //These Blocks are irrelevant to the functionality of the code.
        boolean debug = false;

        //This will hold the values to insert into the List later.
        int tmp;


        if (debug) {
            System.out.println("Hallo i am Thread " + Thread.currentThread().getId());
        }

        //Calculates how many values this thread will add --> random 1 to 100
        int amountOfValues = ThreadLocalRandom.current().nextInt(1, 100 + 1);

        if (debug) {
            System.out.println(amountOfValues + " = Amount of integers to add");
        }

        /*Adds as many random integers as amountOfValues says
         *With odds of 2% clears the List (tmp= 30 to 50)
         *With odds of 5% gets integer on index 5
         *With odds of 5% returns array and drops it
         */

        for (int i = 0; i < amountOfValues; i++) {
            //tmp=(int)Math.random()*1000;
            tmp = ThreadLocalRandom.current().nextInt(1, 1000);
            int tmpGet;
            myList.add(tmp);

            if (30 < tmp && tmp < 50) {
                myList.clear();
            }


            if (50 < tmp && tmp < 100) {
                tmpGet = myList.get(5);
                if (debug) {
                    System.out.println("Thread " + Thread.currentThread().getId() + " got " + tmpGet);
                }
            } //evtl exception


            if (100 < tmp && tmp < 150) {
                myList.toArray();
            }

            if (debug) {
                System.out.println("I do something, I am Thread " + Thread.currentThread().getId());
            }
        }

    }


    public static void main(String[] args) {
        //Threads created..
        int numThreads = 10;
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new myThread(myList);
            threads[i].start();
        }

        //Join Threads
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Printing what they have done
        int[] arrToPrint = myList.toArray();
        for (int i = 0; i < arrToPrint.length; i++) {
            System.out.println(arrToPrint[i]);
        }


    }
}


