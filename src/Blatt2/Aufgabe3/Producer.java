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

        //generating values
        for(int i=0; i<100; i++)
        {
            //pushing them to the stack
            synchronized (this) {
                stack.push(i);
            }
            //thread is tired it goes to sleep
            if(stack.size()==10) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //thread has finished and throws the flag
        flag.push(true);
        System.out.println("Producer has thrown the flag");
    }
}

