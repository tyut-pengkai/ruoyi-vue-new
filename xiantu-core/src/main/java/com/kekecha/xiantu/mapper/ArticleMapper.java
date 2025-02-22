package com.kekecha.xiantu.mapper;

import com.kekecha.xiantu.domain.Article;

import java.util.List;

public interface ArticleMapper {
    List<Article> selectAllArticle();
    List<Article> selectAllNews();
    List<Article> selectAllKnowledge();

    List<Article> selectPinArticle();
    List<Article> selectPinNews();
    List<Article> selectPinKnowledge();

    int insertArticle(Article article);
    int updateArticle(Article article);
    int deleteArticle(int id);
}
