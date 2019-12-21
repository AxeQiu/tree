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
     * 构造深度为2的树
     */
    public static TreeView map(Node parent, List<Node> children) {
        TreeView view = new TreeView();
        view.setId(parent.getId());
        view.setName(parent.getName());
        if (children != null) {
            List<TreeView> list =
                children
                .stream()
                .map(node -> TreeView.map(node, null))
                .collect(Collectors.toList());
            view.setChildren(list);
        }
        return view;
    }

    /**
     * 构造树(不包括根节点)
     * O(n)
     * ArrayList 结构下如:
     * |------------------------------------|
     * |      name      | lft | rgt | depth |
     * |------------------------------------|
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
    public static List<TreeView> map(ArrayList<Node> list) {
        //for (int i=0; i<list.size(); i++) {
        //    LOGGER.info(list.get(i).getName() + "\t" + list.get(i).getDepth());
        //}

        if (list.isEmpty()) {
            return Collections.<TreeView>emptyList();
        }
        //注意压栈时的装箱问题, 是否对性能造成显著影响

        ArrayList<TreeView> resultList = new ArrayList<>();
        Stack<TreeView> viewStack = new Stack<>();
        Stack<Integer> depthStack = new Stack<>();
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
                viewStack.clear();
                depthStack.clear();
                resultList.add(parentView);
            } else if (node.getDepth() == currentDepth + 1) {
                //遇到直接下级
                parentView.getChildren().add(TreeView.map(node));
            } else if (node.getDepth() == currentDepth + 2) {
                //遇到跨级下级
                viewStack.push(parentView);
                depthStack.push(currentDepth); //装箱
                currentDepth += 1;
                parentView = parentView.getChildren().get( parentView.getChildren().size() - 1 );
                parentView.getChildren().add(TreeView.map(node));
            } else if (node.getDepth() <= currentDepth) {
                //遇到上级(直接或跨级)
                int lastDepth = depthStack.pop();
                TreeView lastView = viewStack.pop();
                while (node.getDepth() != lastDepth + 1) {
                    //if (viewStack.isEmpty()) {
                    //    //到达结尾
                    //    break;
                    //}
                    lastDepth = depthStack.pop();
                    lastView = viewStack.pop();
                }
                lastView.getChildren().add(TreeView.map(node));
                parentView = lastView;
                currentDepth = lastDepth;
            }
        }
        return resultList;
    }
}
