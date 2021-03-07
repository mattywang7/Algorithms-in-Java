package graphics;


import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;

/**
 * 图处理的一个经典问题是，找到一个社交网络中两个人之间间隔的度数
 * 用Kevin Bacon这个游戏来说明：
 * Kevin Bacon是一个活跃的演员，曾出演过许多电影。我们为图中的每个演员赋一个Bacon数：
 * Bacon本人为0，所有和Bacon出演过同一部电影的人的值为1，所有和Bacon（Bacon本人除外，不然Bacon就要变成2了）数为1的演员出演过同一部电影的人的值为2，
 * 以此类推。
 * 给定一个演员的名字，游戏最简单的玩法就是找出一系列的电影和演员来回溯到Kevin Bacon.
 * Kevin Bacon必须定义为最短电影链的长度
 */

public class DegreesOfSeparation {

    private DegreesOfSeparation() {

    }

    public static void main(String[] args) {
        String filename = args[0];
        String delimiter = args[1];
        String source = args[2];

        SymbolGraphic sg = new SymbolGraphic(filename, delimiter);
        Graphic g = sg.graphic();
        if (!sg.contains(source)) {
            System.out.println(source + " not in database.");
            return;
        }

        int s = sg.indexOf(source);
        BreadFirstPaths bfs = new BreadFirstPaths(g, s);

        while (!StdIn.isEmpty()) {
            String sink = StdIn.readLine();
            if (sg.contains(sink)) {
                int t = sg.indexOf(sink);
                if (bfs.hasPathTo(t)) {
                    for (int w : bfs.pathTo(t)) {
                        System.out.println("    " + sg.nameOf(w));
                    }
                }
                else {
                    System.out.println("not connected.");
                }
            }
            else {
                System.out.println(sink + " not in database.");
            }
        }
    }
}
