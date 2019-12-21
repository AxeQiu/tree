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
     */
    public static List<TreeView> map(List<Node> list) {
        for (int i=0; i<list.size(); i++) {
            LOGGER.info(list.get(i).getName() + "\t" + list.get(i).getDepth());
        }
        //list是根据lft排序的结果集, 根据其depth来构造树
        List<TreeView> resultList = new ArrayList<>();
        final int topLevelDepth = list.get(0).getDepth();
        TreeView currentView = TreeView.map(list.get(0));
        recurisiveHelper(list, topLevelDepth, topLevelDepth, currentView, resultList);
        return resultList;
    }

    private static void recurisiveHelper(final List<Node> list, final int currentDepth, final int topLevelDepth, final TreeView currentView, final List<TreeView> resultList) {
        LOGGER.info("----recrusive-----");
        LOGGER.info("currentDepth: " + currentDepth + " " + currentView.getName());
        if (currentDepth == topLevelDepth) {
            resultList.add(currentView);
        }
        for (int i=1; i<list.size(); i++) {
            Node node = list.get(i);
            TreeView view = TreeView.map(node);
            if (node.getDepth() == currentDepth+1) {
                //遇到了下级节点
                currentView.getChildren().add(view);
            } else if (node.getDepth() == currentDepth+2) {
                //遇到了下级节点的下级节点
                List<TreeView> childrenOfCurrentView = currentView.getChildren();
                TreeView lastView = childrenOfCurrentView.get(childrenOfCurrentView.size() - 1);
                recurisiveHelper(list.subList(i-1, list.size()), currentDepth+1, topLevelDepth, lastView, resultList);
            } else if (node.getDepth() == currentDepth) {
                //遇到了上级节点
                recurisiveHelper(list.subList(i, list.size()), currentDepth, topLevelDepth, view, resultList);
                return;
            }
        }
    }
}
