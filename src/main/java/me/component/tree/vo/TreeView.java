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
}
