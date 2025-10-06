package com.smart.system.permission.vo;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.smart.framework.common.util.MenuTreeUtil;
import com.smart.system.permission.entity.SmartPermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Treeselect树结构实体类
 * @author smart
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeSelect implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private String id;

    /** 节点名称 */
    private String label;

    /** 节点禁用 */
    private boolean disabled = false;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

}
