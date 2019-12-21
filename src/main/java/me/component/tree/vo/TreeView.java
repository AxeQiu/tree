package me.component.tree.vo;

import java.util.*;
import java.util.stream.Collectors;

import me.component.tree.repository.*;

@lombok.Getter
@lombok.Setter
public class TreeView implements Comparable<TreeView> {

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
     */
    public static List<TreeView> map(List<Node> list) {
        //list是根据lft排序的结果集, 根据其depth来构造树
        List<TreeView> resultList = new ArrayList<>();
        final int topLevelDepth = list.get(0).getDepth();
        TreeView currentView = TreeView.map(list.get(0));
        recurisiveHelper(list.subList(1, list.size()), topLevelDepth, topLevelDepth, currentView, resultList);
    }

    private static recurisiveHelper(final List<Node> list, final int currentDepth, final int topLevelDepth, final TreeView currentView, final List<TreeView> resultList) {
        if (currentDepth == topLevelDepth) {
            resultList.add(currentView);
        }
        for (int i=0; i<list.size(); i++) {
            Node node = list.get(i);
            if (node.getDepth() == currentDepth+1) {
                //遇到了下级节点
                currentView.getChildren().add(node);
            } else if (node.getDepth() == currentDepth+2) {
                //遇到了下级节点的下级节点
            } else if (node.getDepth() == currentDepth-1) {
                //遇到了上级节点
                return;
            }
        }
    }
}
