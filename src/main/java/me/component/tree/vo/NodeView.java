package me.component.tree.vo;

import me.component.tree.repository.*;

@lombok.Getter
@lombok.Setter
public class NodeView implements Comparable<NodeView> {

    private long id;
    private String name;

    @Override
    public int compareTo(NodeView obj) {
        return name.compareTo(obj.getName());
    }

    public static NodeView map(Node node) {
        NodeView view = new NodeView();
        if (node != null) {
            view.setId(node.getId());
            view.setName(node.getName());
        }
        return view;
    }
}
