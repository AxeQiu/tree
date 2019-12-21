package me.component.tree.param;

import javax.validation.constraints.NotNull;

import me.component.tree.vo.*;

@lombok.Getter
@lombok.Setter
public class NodeSearchParam {

    @NotNull
    private Long id;

    public static NodeSearchParam copyFromView(NodeView nodeView) {
        NodeSearchParam param = new NodeSearchParam();
        param.setId(nodeView.getId());
        return param;
    }
}
