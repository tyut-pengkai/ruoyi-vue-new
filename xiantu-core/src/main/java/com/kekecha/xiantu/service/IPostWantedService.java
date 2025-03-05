package com.kekecha.xiantu.service;

import com.kekecha.xiantu.domain.PostWanted;

import java.util.List;

public interface IPostWantedService {
    List<PostWanted> select(String filter, String city);
    int insert(PostWanted postWanted);
    int update(PostWanted postWanted);
    int delete(int id);
}
