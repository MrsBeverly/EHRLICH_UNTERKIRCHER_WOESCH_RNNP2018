package Blatt2.Aufgabe3;

import java.util.Random;
import java.util.Stack;

/*WRITTEN BY EHRLICH BEVERLY, UNTERKIRCHER CHRISTOPH AND WÖSCH TIMON*/
//test chris
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

        //while the flag has not been thrown
        while(flag.empty())
        {
            //checking if stack is not empty
            if(!stack.empty())
            {
                //summing up the values
                synchronized (this) {
                    //Stack ist selbst schon synchronized - man braucht es also nicht extra synchronizen
                    //condition wait wenn stack leer (condition auf stackgröße)
                    result += stack.pop();
                }
                System.out.println(result);
            }
            //thread is tired it goes to sleep
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //finishing up after the flag has been thrown
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




