package strings;

import edu.princeton.cs.algs4.StdIn;

/**
 * 低位优先的字符串排序：
 * 如果将字符串看成一个256进制的数字，那么从右向左检查字符串就相当于先检查数字的最低位。
 * 这种方法最适合用于键的长度都相同的字符串排序应用
 *
 * 命题A：键索引计数法排序 N 个键为 0 到 R - 1 之间的整数的元素需要访问数组 (11N + 4R + 1) 次
 * 键索引计数法是一种对于小整数排序非常有效却常常被忽略的方法。理解它的工作原理是理解字符串排序的第一步。
 * 命题A意味着键索引计数法突破了NlgN的排序算法运行时间下限
 * 键索引计数法不需要比较（它只通过key()方法访问数据）
 * 只要R在N的一个常数因子范围内
 *
 * 将定长字符串排序可以通过键索引计数法来完成，如LSD所示
 * 如果字符串的长度均为W，那就从右向左以每个位置的字符作为键，用键索引计数法将字符串排序N遍
 *
 * 命题B：LSD的字符串排序算法能够稳定地将定长字符串排序
 * 该命题完全依赖于键索引计数法的实现是稳定的
 *
 * Extended ASCII table 的标号是 0 - 255
 *
 * 命题B（续）：对于基于R个字符的字母表的N个以长为W的字符串为键的元素，LSD的字符串排序需要访问 ~(7WN + 3WR) 次数组，使用的空间和 N + R 成正比
 * 对于典型的应用，R远小于N，因此命题B说明算法的总运行时间与WN成正比
 * N个长度为W的字符串的输入总共含有WN个字符，因此LSD的字符串排序的运行时间与输入的规模成正比
 */

public class LSD {

    private static final int BITS_PER_BYTE = 8;

    private LSD() {}

    /**
     * Rearranges the array of w-character strings in ascending order.
     * @param a the array to be sorted
     * @param w the number of characters of per string
     */
    public static void sort(String[] a, int w) {
        int n = a.length;
        int R = 256;
        String[] aux = new String[n];

        for (int d = w - 1; d >= 0; d--) {
            int[] count = new int[R + 1];

            // count[i] - the number of keys which are equal to i - 1
            for (int i = 0; i < n; i++) {
                count[a[i].charAt(d) + 1]++;
            }

            // count[i] - the number of keys which are less than i
            for (int r = 0; r < R; r++) {
                count[r + 1] += count[r];
            }

            for (int i = 0; i < n; i++) {
                aux[count[a[i].charAt(d)]++] = a[i];
            }

            for (int i = 0; i < n; i++) {
                a[i] = aux[i];
            }
        }
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        int n = a.length;
        int w = a[0].length();

        for (int i = 0; i < n; i++) {
            assert a[i].length() == w : "String must have fixed length.";
        }

        sort(a, w);

        for (int i = 0; i < n; i++) {
            System.out.println(a[i]);
        }
    }
}
