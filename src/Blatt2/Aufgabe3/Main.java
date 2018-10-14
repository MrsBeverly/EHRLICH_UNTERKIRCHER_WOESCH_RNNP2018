package Blatt2.Aufgabe3;

import java.util.Stack;

public class Main extends Thread{
    public static void main(String[] args) {
        Stack<Integer> sharedStack = new Stack<>();
        // TODO: implement

        //Creating a flag to stop the threads
        Stack<Boolean> sharedflag = new Stack<>();

        //create threads and start them
        Producer pr = new Producer(sharedStack,sharedflag);
        Consumer cs = new Consumer(sharedStack,sharedflag);
        cs.start();
        pr.start();

        //threads join
        try {
            pr.join();
            cs.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}