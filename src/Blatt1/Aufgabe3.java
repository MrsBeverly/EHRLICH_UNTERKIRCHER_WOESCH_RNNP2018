/*WRITTEN BY EHRLICH BEVERLY, UNTERKIRCHER CHRISTOPH AND WÖSCH TIMON*/
package Blatt1;

import java.util.BitSet;

public class Aufgabe3 {
    public static void main(String[] args) {
        int debug = 0;    // 0..runnableValues    1..ExceptionSeqNumTooHigh   2..SeqNumNegative  3..PayloadEmpty

        switch (debug) {
            case 0:

                byte[] mesg1 = createMsg(true, true, 1, new byte[]{(byte) 0x1});
                for (int i = 0; i < mesg1.length; i++) {
                    System.out.print(mesg1[i]);
                }

                System.out.println(" ");
                System.out.println(" ");

                mesg1 = createMsg(true, true, 5, new byte[]{(byte) 0x5});
                for (int i = 0; i < mesg1.length; i++) {
                    System.out.print(mesg1[i]);
                }

                System.out.println(" ");
                System.out.println(" ");


                mesg1 = createMsg(true, true, 5, new byte[]{(byte) 0x5d, 0x6a, 0x7c, 0x4b, 0x24, 0x42});
                for (int i = 0; i < mesg1.length; i++) {
                    System.out.print(mesg1[i]);
                }

                break;


            case 1:
                mesg1 = createMsg(true, true, 65536, new byte[]{(byte) 0x5d, 0x6a, 0x7c, 0x4b, 0x24, 0x42});
                for (int i = 0; i < mesg1.length; i++) {
                    System.out.print(mesg1[i]);
                }
                break;


            case 2:
                mesg1 = createMsg(true, true, -4, new byte[]{(byte) 0x5d, 0x6a, 0x7c, 0x4b, 0x24, 0x42});
                for (int i = 0; i < mesg1.length; i++) {
                    System.out.print(mesg1[i]);
                }
                break;


            case 3:
                mesg1 = createMsg(true, true, 65536, null);
                for (int i = 0; i < mesg1.length; i++) {
                    System.out.print(mesg1[i]);
                }
                break;
        }
    }

    static byte[] createMsg(boolean isData, boolean isUrgent, int sequenceNumber, byte[] payload) throws IllegalArgumentException {
        boolean debug = false;


        //Exception Handlig
        /**
         * IN ÜBUNG:
         * BEI SEQUENCENUMBER NEGATIVE WERTE BEACHTEN!!!!
         * IN JAVA IST ALLES SIGNED; DAHER 3 SHIFT
         **/
        if ((sequenceNumber > Math.pow(2, 16) - 1) || sequenceNumber < 0 || payload == null) {
            throw new IllegalArgumentException();
        }

        BitSet myBitSet = new BitSet();

        //Filling the first 32 bit

        // 5 Bit Version =2           --> 0001 0--- ---- ---- ---- ---- ---- ----
        myBitSet.set(1);

        if (debug) {
            for (int debugIdx = 0; debugIdx < myBitSet.length(); debugIdx++) {
                if (myBitSet.get(debugIdx)) {
                    System.out.print("1");
                } else {
                    System.out.print("0");
                }
            }
            System.out.println("   Version number set. Length: " + myBitSet.length());
        }


        // 9 Bit reserved =0          --> ---- -xxx xxxx xx-- ---- ---- ---- ----


        // 1 bit set if isData 0 || 1 --> ---- ---- ---- --x- ---- ---- ---- ----
        if (isData) {
            myBitSet.set(14);
        }
        ;

        if (debug) {
            for (int debugIdx = 0; debugIdx < myBitSet.length(); debugIdx++) {
                if (myBitSet.get(debugIdx)) {
                    System.out.print("1");
                } else {
                    System.out.print("0");
                }
            }
            System.out.println("   Data Bit set. Length: " + myBitSet.length());
        }


        //1bit set if isUrgent 0 || 1 --> ---- ---- ---- ---x ---- ---- ---- ----
        if (isUrgent) {
            myBitSet.set(15);
        }
        ;
        if (debug) {
            for (int debugIdx = 0; debugIdx < myBitSet.length(); debugIdx++) {
                if (myBitSet.get(debugIdx)) {
                    System.out.print("1");
                } else {
                    System.out.print("0");
                }
            }
            System.out.println("   Urgent Bit set. Length: " + myBitSet.length());
        }

        // 16 bit SequenceNumber       --> ---- ---- ---- ---- xxxx xxxx xxxx xxxx
        BitSet tmpBitSet = new BitSet().valueOf(new long[]{sequenceNumber});
        int runIdx = 16;
        for (int i = 0; i < tmpBitSet.length(); i++) {
            if (tmpBitSet.get(i)) {
                myBitSet.set(runIdx + i);
            }
        }
        if (debug) {
            for (int debugIdx = 0; debugIdx < myBitSet.length(); debugIdx++) {
                if (myBitSet.get(debugIdx)) {
                    System.out.print("1");
                } else {
                    System.out.print("0");
                }
            }
            System.out.println("   Sequence Number added. Length: " + myBitSet.length());
        }

        //adding 32 Bit payload length

        BitSet payloadBitSet = new BitSet().valueOf(payload);
        tmpBitSet = new BitSet().valueOf(new long[]{payloadBitSet.length()});
        runIdx = 32;
        for (int i = 0; i < tmpBitSet.length(); i++) {
            if (tmpBitSet.get(i)) {
                myBitSet.set(runIdx + i);
            }

        }
        if (debug) {
            for (int debugIdx = 0; debugIdx < myBitSet.length(); debugIdx++) {
                if (myBitSet.get(debugIdx)) {
                    System.out.print("1");
                } else {
                    System.out.print("0");
                }
            }
            System.out.println("   PL Length added. Length: " + myBitSet.length());
        }

        //adding payload to existing 64 bit
        for (int i = 0; i < payloadBitSet.length(); i++) {
            if (payloadBitSet.get(i)) {
                myBitSet.set(i + 64);
            }
        }
        if (debug) {
            for (int debugIdx = 0; debugIdx < myBitSet.length(); debugIdx++) {
                if (myBitSet.get(debugIdx)) {
                    System.out.print("1");
                } else {
                    System.out.print("0");
                }
            }
            System.out.println("   Payload added. Length: " + myBitSet.length());
        }

        return myBitSet.toByteArray();
    }
}
