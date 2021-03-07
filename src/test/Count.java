package test;

import edu.princeton.cs.algs4.StdIn;
import strings.Alphabet;

public class Count {

    private Count() {}

    public static void main(String[] args) {
        Alphabet alphabet = new Alphabet("0123456789");
        // R表示了这个字母表共有多少个不同的字符
        final int R = alphabet.radix();
        int[] count = new int[R];
        while (StdIn.hasNextChar()) {
            char c = StdIn.readChar();
            if (alphabet.contains(c)) {
                count[alphabet.toIndex(c)]++;
            }
        }

        for (int c = 0; c < R; c++) {
            System.out.println(alphabet.toChar(c) + ": " + count[c]);
        }
    }
}
