package strings;

import java.util.Arrays;

/**
 * 字符串的连接：
 * 我们会避免将字符一个一个地追加到字符串中，因为在Java里这个过程所需的时间将会是平方级别的
 * 为此Java提供了一个StringBuilder类
 *
 * 在一般的Java实现中，a[i]很可能比s.charAt(i)要快很多
 *
 * inverse[] 中存储的是alphabet[]中每个字符的index，因为知道了一个字符，无法直接知道它在alphabet中的index
 *
 * 当char作为数组的index时，会被自动转换为对应的整数
 */

public class Alphabet {

    public static final Alphabet BINARY = new Alphabet("01");
    public static final Alphabet OCTAL = new Alphabet("01234567");
    public static final Alphabet DECIMAL = new Alphabet("0123456789");
    public static final Alphabet HEXADECIMAL = new Alphabet("0123456789ABCDEF");
    public static final Alphabet DNA = new Alphabet("ACGT");
    public static final Alphabet PROTEIN = new Alphabet("ACDEFGHIKLMNPQRSTVWY");
    public static final Alphabet LOWERCASE = new Alphabet("abcdefghijklmnopqrstuvwxyz");
    public static final Alphabet UPPERCASE = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    public static final Alphabet BASE64 = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");
    public static final Alphabet ASCII = new Alphabet(128);
    public static final Alphabet EXTENDED_ASCII = new Alphabet(256);
    public static final Alphabet UNICODE16 = new Alphabet(65536);



    private char[] alphabet;            // the characters in the alphabet
    private int[] inverse;              // indices
    private final int R;                // the radix of the alphabet

    /**
     * Initializes a new alphabet from the given set of characters.
     * @param alpha String
     */
    public Alphabet(String alpha) {
        // check that the String alpha has no duplicate characters
        boolean[] unicode = new boolean[Character.MAX_VALUE];
        for (int i = 0; i < alpha.length(); i++) {
            char c = alpha.charAt(i);
            if (unicode[c]) {
                throw new IllegalArgumentException(alpha + " has duplicate characters.");
            }
            unicode[c] = true;
        }

        alphabet = alpha.toCharArray();
        R = alpha.length();
        inverse = new int[Character.MAX_VALUE];
        Arrays.fill(inverse, -1);

        // alphabet[c] - each char in the char array of String alpha
        // inverse[char] - transfer the char to index of array inverse
        for (int c = 0; c < R; c++) {
            inverse[alphabet[c]] = c;
        }
    }

    /**
     * Initializes a new alphabet using characters 0 through R-1.
     * @param radix int
     */
    private Alphabet(int radix) {
        this.R = radix;
        alphabet = new char[R];
        inverse = new int[R];

        for (int i = 0; i < R; i++) {
            alphabet[i] = (char) i;
        }

        for (int i = 0; i < R; i++) {
            inverse[i] = i;
        }
    }

    public Alphabet() {
        this(256);
    }

    public boolean contains(char c) {
        return inverse[c] != -1;
    }

    public int radix() {
        return R;
    }

    public int toIndex(char c) {
        if (c >= inverse.length || inverse[c] == -1) {
            throw new IllegalArgumentException("Character " + c + " is not in the alphabet.");
        }
        return inverse[c];
    }

    /**
     *
     * @param s String
     * @return Returns the indices corresponding to the argument characters.
     */
    public int[] toIndices(String s) {
        char[] source = s.toCharArray();
        int[] target = new int[s.length()];
        for (int i = 0; i < source.length; i++) {
            target[i] = toIndex(source[i]);
        }
        return target;
    }

    public char toChar(int index) {
        if (index < 0 || index >= R) {
            throw new IllegalArgumentException("Index must between 0 and " + R + ": " + index);
        }
        return alphabet[index];
    }

    public String toChars(int[] indices) {
        StringBuilder sb = new StringBuilder(indices.length);
        for (int i = 0; i < indices.length; i++) {
            sb.append(toChar(indices[i]));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Alphabet alphabet = new Alphabet("ABCDE");
    }
}
