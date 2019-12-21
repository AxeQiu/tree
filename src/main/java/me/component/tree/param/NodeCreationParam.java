package me.component.tree.param;

import javax.validation.constraints.*;

@lombok.Getter
@lombok.Setter
public class NodeCreationParam {

    @NotNull
    private Long parentId;

    @NotBlank
    String name;
}
