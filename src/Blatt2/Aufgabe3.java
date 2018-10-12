package Blatt2;

import java.util.Stack;

/*WRITTEN BY EHRLICH BEVERLY, UNTERKIRCHER CHRISTOPH AND WÖSCH TIMON*/
public class Aufgabe3 {

    public class Consumer extends Thread {
        private Stack<Integer> stack;

        public Consumer(Stack<Integer> stack) {
            this.stack = stack;
        }

        public void run() {
            // TODO: implement!
        }
    }

    public class Producer extends Thread {
        private Stack<Integer> stack;

        public Producer(Stack<Integer> stack) {
            this.stack = stack;
        }

        public void run() {
            // TODO: implement!
        }
    }

    public static class Main {
        public static void main(String[] args) {
            Stack<Integer> sharedStack = new Stack<>();
            // TODO: implement
        }
    }
}
