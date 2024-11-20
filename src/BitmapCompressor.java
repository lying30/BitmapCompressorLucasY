/******************************************************************************
 *  Compilation:  javac BitmapCompressor.java
 *  Execution:    java BitmapCompressor - < input.bin   (compress)
 *  Execution:    java BitmapCompressor + < input.bin   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   q32x48.bin
 *                q64x96.bin
 *                mystery.bin
 *
 *  Compress or expand binary input from standard input.
 *
 *  % java DumpBinary 0 < mystery.bin
 *  8000 bits
 *
 *  % java BitmapCompressor - < mystery.bin | java DumpBinary 0
 *  1240 bits
 ******************************************************************************/

/**
 *  The {@code BitmapCompressor} class provides static methods for compressing
 *  and expanding a binary bitmap input.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Zach Blick
 *  @author YOUR NAME HERE
 */
public class BitmapCompressor {

    /**
     * Reads a sequence of bits from standard input, compresses them,
     * and writes the results to standard output.
     */
    public static void compress() {

        String s = BinaryStdIn.readString();
        int n = s.length();
        char currentBit = s.charAt(0);
        int count = 1;


        // Look through the binary test file
        // keep track of the consecutive 0 or 1 and write it as
        // write the consecutive finds as 7 bits (first bit represents the
        // bits represents whether the consecutive string of ints is full of 0s or 1s
        // the other 6 bits represent the consecutive run size ranging from 0-63

        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == currentBit) {
                count++;
                if (count == 127) {
                    BinaryStdOut.write(count, 8);
                    BinaryStdOut.write(currentBit == '1');
                }

            } else {
                BinaryStdOut.write(count, 8);
                BinaryStdOut.write(currentBit == '1');
                currentBit = s.charAt(i);
                count = 1;
            }
        }
        if (count > 0) {
            BinaryStdOut.write(count, 8);
            BinaryStdOut.write(currentBit == '1');
        }
        BinaryStdOut.close();
    }

    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand() {

        while (!BinaryStdIn.isEmpty()) {
            int count = BinaryStdIn.readInt(8);
            boolean bit = BinaryStdIn.readBoolean();

            for (int i = 0; i < count; i++) {
                BinaryStdOut.write(bit ? '1' : '0');
            }
        }
        BinaryStdOut.close();
    }

    /**
     * When executed at the command-line, run {@code compress()} if the command-line
     * argument is "-" and {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}