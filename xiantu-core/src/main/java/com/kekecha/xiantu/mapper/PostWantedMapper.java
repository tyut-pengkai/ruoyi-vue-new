package com.kekecha.xiantu.mapper;

import com.kekecha.xiantu.domain.PostWanted;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostWantedMapper {
    List<PostWanted> select(@Param("filter") String filter, @Param("city") String city);
    int insert(PostWanted postWanted);
    int update(PostWanted postWanted);
    int delete(int id);
}
