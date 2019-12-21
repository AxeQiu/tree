package me.component.tree.vo;

import java.util.*;
import java.util.stream.Collectors;

import me.component.tree.repository.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@lombok.Getter
@lombok.Setter
public class TreeView implements Comparable<TreeView> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeView.class);

    private long id;
    private String name;
    private List<TreeView> children;

    @Override
    public int compareTo(TreeView obj) {
        return name.compareTo(obj.getName());
    }

    /**
     * 创建view
     */
    public static TreeView map(Node node) {
        TreeView view = new TreeView();
        view.setId(node.getId());
        view.setName(node.getName());
        view.setChildren(new ArrayList<TreeView>());
        return view;
    }

    /**
     * 构造树
     * O(n)
     * ArrayList 结构下如:
     * |------------------------------------|
     * |      name      | lft | rgt | depth |
     * |------------------------------------|
     * | 图书           | 1   | 34  |  1    |
     * | 文史类         | 2   | 13  |  2    |
     * | 文学           | 3   | 4   |  3    |
     * | 历史           | 5   | 10  |  3    |
     * | 明朝那些事儿   | 6   | 7   |  4    |
     * | 英国工业革命   | 8   | 9   |  4    |
     * | 社会学         | 11  | 12  |  3    |
     * | 财经类         | 14  | 21  |  2    |
     * | 经济学         | 15  | 16  |  3    |
     * | 会计           | 17  | 18  |  3    |
     * | 金融           | 19  | 20  |  3    |
     * | 科技类         | 22  | 29  |  2    |
     * | 计算机科学     | 23  | 24  |  3    |
     * | 数学           | 25  | 26  |  3    |
     * | 物理           | 27  | 28  |  3    |
     * | 少儿类         | 30  | 31  |  2    |
     * | 教育类         | 32  | 33  |  2    |
     * |________________|_____|_____|_______|
     */
    public static TreeView map(ArrayList<Node> listIncludingRoot) {

        Node root = listIncludingRoot.get(0);
        TreeView treeView = TreeView.map(root);

        List<Node> list = listIncludingRoot.subList(1, listIncludingRoot.size());

        if (list.isEmpty()) {
            return treeView;
        }

        //注意压栈时的装箱问题, 是否对性能造成显著影响
        //如果多租户场景下, 可能产生大量的装箱对象(int -> Integer),从而导致频繁gc
        //Map<Integer, TreeView> helperMap = new HashMap<>();

        //为避免装箱问题, 使用array代替map
        final int absoluteRootDepth = root.getDepth();
        int absoluteMaxDepth = absoluteRootDepth;
        for (Node n : list) {
            if (n.getDepth() > absoluteMaxDepth) {
                absoluteMaxDepth = n.getDepth();
            }
        }
        TreeView[] helperArray = new TreeView[absoluteMaxDepth - absoluteRootDepth];

        ArrayList<TreeView> resultList = new ArrayList<>();
        final int topLevelDepth = list.get(0).getDepth();

        //处理第1个数据
        int currentDepth = topLevelDepth;
        TreeView parentView = TreeView.map(list.get(0));
        resultList.add(parentView);

        //从第2个数据开始
        for (int i=1; i<list.size(); i++) {
            Node node = list.get(i);
            if (node.getDepth() == topLevelDepth) {
                //遇到根节点
                parentView = TreeView.map(node);
                currentDepth = topLevelDepth;
                //helperMap.clear();
                resultList.add(parentView);
            } else if (node.getDepth() == currentDepth + 1) {
                //遇到直接下级
                parentView.getChildren().add(TreeView.map(node));
            } else if (node.getDepth() == currentDepth + 2) {
                //遇到跨级下级
                //helperMap.put(currentDepth, parentView); //装箱产生多余对象
                helperArray[currentDepth - absoluteRootDepth] = parentView;
                currentDepth += 1;
                parentView = parentView.getChildren().get( parentView.getChildren().size() - 1 );
                parentView.getChildren().add(TreeView.map(node));
            } else if (node.getDepth() <= currentDepth) {
                //遇到上级(直接或跨级)
                currentDepth = node.getDepth() - 1;
                //parentView = helperMap.get(currentDepth); //装箱产生多余对象
                parentView = helperArray[currentDepth - absoluteRootDepth];
                parentView.getChildren().add(TreeView.map(node));
            }
        }
        treeView.getChildren().addAll(resultList);
        return treeView;
    }
}
