package Blatt2.Aufgabe3;

import java.util.Random;
import java.util.Stack;

/*WRITTEN BY EHRLICH BEVERLY, UNTERKIRCHER CHRISTOPH AND WÖSCH TIMON*/

public class Consumer extends Thread {
    private Stack<Integer> stack;
    private Stack<Boolean> flag;

    public Consumer(Stack<Integer> stack, Stack<Boolean> flag)
    {
        this.stack = stack;
        this.flag = flag;
    }

    public void run() {
        // TODO: implement
        int result = 0;

        while(flag.empty())
        {
            if(!stack.empty())
            {
                synchronized (this) {
                    result += stack.pop();
                }
                System.out.println(result);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (!stack.empty())
        {
            synchronized (this) {
                result += stack.pop();
            }
            System.out.println(result);
        }
        System.out.println("Consumer has reached the end");
    }
}




