package strings;

import java.util.Arrays;

/**
 * 要实现一个通用的字符串排序算法，我们应该考虑从左向右遍历所有字符串
 * 首先用键索引计数法将所有字符串按照首字母排序，然后（递归地）再将每个首字母所对应的子数组排序
 * （忽略首字母，因为每一类中的首字母都是一样的）
 *
 * 和快速排序一样，MSD的字符串排序会将数组切分为能够独立排序的子数组来完成排序任务
 *
 * 在MSD的字符串排序算法中，要特别注意到达字符串末尾的情况
 * 在排序中，合理的做法是将所有字符都已被检查过的字符串所在的子数组排在所有子数组的前面，这样就不需要递归的将该子数组排序
 */

public class MSD {

    private static final int R = 256;
    private static final int CUTOFF = 15;           // cutoff to insertion sort

    private MSD() {}

    public static void sort(String[] a) {
        int n = a.length;
        String[] aux = new String[n];
        sort(a, 0, n - 1, 0, aux);
    }

    private static void sort(String[] a, int lo, int hi, int d, String[] aux) {
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        int[] count = new int[R + 2];
        for (int i = lo; i <= hi; i++) {
            int c = charAt(a[i], d);
            count[c + 2]++;
        }

        for (int r = 0; r < R + 1; r++) {
            count[r + 1] += count[r];
        }

        for (int i = lo; i <= hi; i++) {
            int c = charAt(a[i], d);
            aux[count[c + 1]++] = a[i];
        }

        for (int i = lo; i <= hi; i++) {
            a[i] = aux[i - lo];
        }

        for (int r = 0; r < R; r++) {
            sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
        }
    }



    // return the dth character in String s, -1 if d == s.length()
    private static int charAt(String s, int d) {
        assert d >= 0 && d <= s.length();
        if (d == s.length()) {
            return -1;
        }
        return s.charAt(d);
    }

    private static void exchange(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

//     is v less than w, starting at character d
    private static boolean less(String v, String w, int d) {
        int vLength = v.length();
        int wLength = w.length();
        for (int i = d; i < Math.min(vLength, wLength); i++) {
            if (v.charAt(i) < w.charAt(i)) {
                return true;
            }
            if (v.charAt(i) > w.charAt(i)) {
                return false;
            }
        }
        return v.length() < w.length();
    }

    private static void insertion(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1], d); j--) {
                exchange(a, j - 1, j);
            }
        }
    }

    public static void main(String[] args) {
        String[] a = {"bed", "bug", "dad", "yes", "zoo", "now", "for", "tip", "ilk", "dim", "tag",
        "jot", "sob", "nob", "sky", "hut", "men", "egg", "few", "jay", "owl", "joy", "rap", "gig", "wee",
        "was", "wad", "fee", "tap", "tar", "dug", "jam", "all", "bad", "yet"};

        MSD.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
