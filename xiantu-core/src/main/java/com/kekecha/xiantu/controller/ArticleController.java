package com.kekecha.xiantu.controller;

import com.kekecha.xiantu.domain.Article;
import com.kekecha.xiantu.service.IArticleService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController {
    @Autowired
    private IArticleService articleService;

    @Anonymous
    @GetMapping("/news")
    public AjaxResult getNewsList(int pageNum, int pageSize)
    {
        String tag = "news";
        boolean select_all = false;
        try {
            if (pageNum <= 0) {
                return AjaxResult.error("参数不合法");
            }
            if (pageSize <= 0) {
                select_all = true;
            }
            AjaxResult ajaxResult = AjaxResult.success("查询成功");

            List<Article> pin_list = articleService.selectPin(tag);
            ajaxResult.put("pin", pin_list);

            List<Article> normal_list = articleService.selectAll(tag);
            int total = normal_list.size();
            ajaxResult.put("total", total);

            if (select_all) {
                ajaxResult.put("data", normal_list);
            } else {
                int search_start = (pageNum - 1) * pageSize;
                int search_end = Math.min((pageNum * pageSize), total);
                if (search_start >= total) {
                    List<Article> normal_result = new ArrayList<>();
                    ajaxResult.put("data", normal_result);
                } else {
                    List<Article> normal_result = normal_list.subList(search_start, search_end);
                    ajaxResult.put("data", normal_result);
                }
            }
            return ajaxResult;
        } catch (Exception e) {
            return AjaxResult.error("系统异常，获取数据失败");
        }
    }

    @Anonymous
    @GetMapping("/knowledge")
    public AjaxResult getKnowledgeList(int pageNum, int pageSize)
    {
        String tag = "knowledge";
        boolean select_all = false;
        try {
            if (pageNum <= 0) {
                return AjaxResult.error("参数不合法");
            }
            if (pageSize <= 0) {
                select_all = true;
            }
            AjaxResult ajaxResult = AjaxResult.success("查询成功");

            List<Article> pin_list = articleService.selectPin(tag);
            ajaxResult.put("pin", pin_list);

            List<Article> normal_list = articleService.selectAll(tag);
            int total = normal_list.size();
            ajaxResult.put("total", total);

            if (select_all) {
                ajaxResult.put("data", normal_list);
            } else {
                int search_start = (pageNum - 1) * pageSize;
                int search_end = Math.min((pageNum * pageSize), total);
                if (search_start >= total) {
                    List<Article> normal_result = new ArrayList<>();
                    ajaxResult.put("data", normal_result);
                } else {
                    List<Article> normal_result = normal_list.subList(search_start, search_end);
                    ajaxResult.put("data", normal_result);
                }
            }
            return ajaxResult;
        } catch (Exception e) {
            return AjaxResult.error("系统异常，获取数据失败");
        }
    }

    @PreAuthorize("@ss.hasPermi('data:new:list')")
    @PostMapping("/news/edit")
    public AjaxResult newEdit(@RequestBody Article article)
    {
        article.setTag("news");
        if (article.getId() == 0) {
            try {
                long time = Instant.now().getEpochSecond();
                article.setCreateTime(time);
                article.setUpdateTime(time);
                articleService.insert(article);
                return AjaxResult.success("创建成功");
            } catch (Exception e) {
                return AjaxResult.error("创建失败, 内部参数错误");
            }
        } else {
            long time = Instant.now().getEpochSecond();
            article.setUpdateTime(time);
            articleService.update(article);
            return AjaxResult.success("更新成功");
        }
    }

    @PreAuthorize("@ss.hasPermi('data:knowledge:list')")
    @PostMapping("/knowledge/edit")
    public AjaxResult knowledgeEdit(@RequestBody Article article)
    {
        article.setTag("knowledge");
        if (article.getId() == 0) {
            try {
                long time = Instant.now().getEpochSecond();
                article.setCreateTime(time);
                article.setUpdateTime(time);
                articleService.insert(article);
                return AjaxResult.success("创建成功");
            } catch (Exception e) {
                return AjaxResult.error("创建失败, 内部参数错误");
            }
        } else {
            long time = Instant.now().getEpochSecond();
            article.setUpdateTime(time);
            articleService.update(article);
            return AjaxResult.success("更新成功");
        }
    }

    @PreAuthorize("@ss.hasAnyPermi({'data:new:list', 'data:knowledge:list'})")
    @DeleteMapping("")
    public AjaxResult delete(@RequestParam("id") int id)
    {
        try {
            articleService.delete(id);
            return AjaxResult.success("删除成功");
        } catch (Exception e) {
            return AjaxResult.error("删除失败");
        }
    }
}
