package strings;

/**
 * 该算法的基本思想是当出现不匹配时，就能知晓一部分文本的内容，我们可以利用这些信息避免将指针回退到所有这些已知的字符之前。
 * 在匹配失败时，如果模式字符串中的某处可以和匹配失败处的正文相匹配，那么就不应该完全跳过所有已经匹配的所有字符。
 */

public class KMP {

    private final int R;
    private final int m;
    private int[][] dfa;

    /**
     * Preprocess the pattern string.
     * @param pat the pattern string
     */
    public KMP(String pat) {
        this.R = 256;
        this.m = pat.length();

        dfa = new int[R][m];
        dfa[pat.charAt(0)][0] = 1;
    }

    public static void main(String[] args) {

    }
}
