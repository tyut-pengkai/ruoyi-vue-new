package com.kekecha.xiantu.service.impl;

import com.kekecha.xiantu.domain.Article;
import com.kekecha.xiantu.mapper.ArticleMapper;
import com.kekecha.xiantu.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements IArticleService {
    @Autowired
    ArticleMapper articleMapper;

    public List<Article> selectAll(String tag) {
        if (tag.equals("news")) {
            return articleMapper.selectAllNews();
        } else if (tag.equals("knowledge")) {
            return articleMapper.selectAllKnowledge();
        } else {
            return new ArrayList<Article>();
        }
    }

    public List<Article> selectPin(String tag) {
        if (tag.equals("news")) {
            return articleMapper.selectPinNews();
        } else if (tag.equals("knowledge")) {
            return articleMapper.selectPinKnowledge();
        } else {
            return new ArrayList<Article>();
        }
    }

    public int insert(Article article) {
        return articleMapper.insertArticle(article);
    }

    public int update(Article article) {
        return articleMapper.updateArticle(article);
    }

    public int delete(int id) {
        return articleMapper.deleteArticle(id);
    }
}
