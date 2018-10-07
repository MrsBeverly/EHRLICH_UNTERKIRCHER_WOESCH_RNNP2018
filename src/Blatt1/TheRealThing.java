/*WRITTEN BY EHRLICH BEVERLY, UNTERKIRCHER CHRISTOPH AND WÖSCH TIMON*/
package Blatt1;
import java.io.*;

public class TheRealThing extends Thread {
    private static float result = -1;
    private String filename;
    private int start;
    private int end;

    /**
     * Creates a new TheRealThing thread which operates * on the indexes start to end.
     */
    public TheRealThing(String filename, int start, int end) {
        this.filename = filename;
        this.start = start;
        this.end = end;
    }


    /**
     * Performs "eine komplizierte Berechnung" on array and * returns the result
     */
    public float eine_komplizierte_Berechnung(float[] array) {
        float tmp = 0;
        for (int i = 0; i < array.length; i++) {
            tmp += array[i];
        }
        return tmp;
    }

    public void run() {
        // TODO ...
        synchronized (this) {
            try {
                //reading file
                BufferedReader br;
                //Lock critical region

                FileReader fr = new FileReader(filename);
                br = new BufferedReader(fr);

                br.readLine(); //name
                br.readLine(); //id
                int count = Integer.parseInt(String.valueOf(br.readLine())); //count
                String str = br.readLine(); //actual array
                String[] myArray = str.split(",");

                //Casting to Float
                float[] myFloatArray = new float[end - start + 1];
                float buffer;
                if (start < count) {
                    for (int i = start; i <= end; i++) {
                        if (i < count) {
                            buffer = Float.parseFloat(myArray[i]);
                            myFloatArray[i - start] = buffer;
                        }

                    }
                    result += eine_komplizierte_Berechnung(myFloatArray);
                    fr.close();
                    br.close();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        String pathToFile = "./myArrayData.dat";
        int numThreads = 12;
        int arraySize = 70;
        TheRealThing[] threads = new TheRealThing[numThreads];
        // TODO ...
        int amount = (int) Math.ceil(arraySize / numThreads); //5
        int start = 0;

        result = 0;

        //12 Threads created..
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new TheRealThing(pathToFile, start, start + amount);
            threads[i].start();
            start += amount + 1;
        }

        //Join Threads
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(result);
    }
}

