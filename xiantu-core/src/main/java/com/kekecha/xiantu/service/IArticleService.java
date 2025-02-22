package com.kekecha.xiantu.service;
import com.kekecha.xiantu.domain.Article;

import java.util.List;

public interface IArticleService {
    List<Article> selectAll(String tag) ;
    List<Article> selectPin(String tag) ;
    int insert(Article article);
    int update(Article article);
    int delete(int id);
}
