package com.ruoyi.common.utils.tree;

import com.ruoyi.common.utils.tree.vo.IBaseTree;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TreeUtil {

    public static <T extends IBaseTree> List<T> build(List<T> list) {
        List<T> collect = list.stream().filter(p -> p.getParentId() == 0).collect(Collectors.toList());
        for (T t : collect) {
            buildChildren(t, list);
        }
        return collect;
    }

    private static <T extends IBaseTree> void buildChildren(T t, List<T> list) {
        for (T l : list) {
            if (Objects.equals(l.getParentId(), t.getId())) {
                t.getChildren().add(l);
                buildChildren(l, list);
            }
        }
    }
}

