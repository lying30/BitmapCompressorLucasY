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
 *  @author Lucas Ying
 */
public class BitmapCompressor {

    /**
     * Reads a sequence of bits from standard input, compresses them,
     * and writes the results to standard output.
     */
    public static void compress() {

        String s = BinaryStdIn.readString();
        int n = s.length();
        char currentBit = '0';
        int count = 0;

        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == currentBit) {
                count++;
                // If maximum of 255 is hit in run
                if (count == 255) {
                    BinaryStdOut.write(count, 8);
                    count = 0;
                }

            } else {
                // Write the current run length
                BinaryStdOut.write(count, 8);
                // Switch to the next bit
                currentBit = s.charAt(i);
                count = 1;
            }
        }
        // Write the final run length
        if (count > 0) {
            BinaryStdOut.write(count, 8);
        }
        BinaryStdOut.close();
    }

    /**
     * Reads a sequence of bits from standard input, decodes it,
     * and writes the results to standard output.
     */
    public static void expand() {
        // Start with 0 by default
        char currentbit = '0';

        while (!BinaryStdIn.isEmpty()) {
            int count = BinaryStdIn.readInt(8);

            // Write the current bit 'count' times
            for (int i = 0; i < count; i++) {
                BinaryStdOut.write(currentbit);
            }
            // Flip the bit for the next run (used operator to evaluate if currentBit was a 0 and switch based off that boolean value of true or false)
            currentbit = (currentbit == '0') ? '1' : '0';
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