package Blatt2.Aufgabe3;

import java.util.Stack;

public class Producer extends Thread {
    private Stack<Integer> stack;
    private Stack<Boolean> flag;

    public Producer(Stack<Integer> stack, Stack<Boolean> flag) {
        this.stack = stack;
        this.flag = flag;
    }

    public void run() {
        // TODO: implement!

        for(int i=0; i<100; i++)
        {
            synchronized (this) {
                stack.push(i);
            }
            if(stack.size()==10) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        flag.push(true);
        System.out.println("Producer has thrown the flag");
    }
}

