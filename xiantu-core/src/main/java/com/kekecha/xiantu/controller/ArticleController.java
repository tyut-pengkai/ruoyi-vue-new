package com.kekecha.xiantu.controller;

import com.kekecha.xiantu.domain.Article;
import com.kekecha.xiantu.service.IArticleService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController {
    @Autowired
    private IArticleService articleService;

    @Anonymous
    @GetMapping("")
    public AjaxResult list(String tag, int start, int end)
    {
        if (start < 0 || start > end) {
            return AjaxResult.error("参数不合法");
        }

        AjaxResult ajaxResult = AjaxResult.success("查询成功");

        List<Article> pin_list = articleService.selectPin(tag);
        ajaxResult.put("pin", pin_list);

        List<Article> normal_list = articleService.selectAll(tag);
        int total = normal_list.size();
        ajaxResult.put("total", total);
        int search_end = Math.min(end, total);
        // 如果 start 超过列表大小，返回空列表
        if (start >= total) {
            List<Article> normal_result = new ArrayList<>();
            ajaxResult.put("data", normal_result);
        } else {
            List<Article> normal_result = normal_list.subList(start, search_end);
            ajaxResult.put("data", normal_result);
        }

        return ajaxResult;
    }

    @Anonymous
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody Article article)
    {
        if (article.getId() == 0) {
            /* 新建 */
            try {
                System.out.println("article is pin: " + article.getIsPin());
                articleService.insert(article);
                return AjaxResult.success("创建成功");
            } catch (Exception e) {
                return AjaxResult.error("创建失败" + e.getMessage());
            }
        } else {
            /* 修订 */
            if (articleService.update(article) <= 0) {
                System.out.println("Article " + article.getId() + " not exist");
            }
            return AjaxResult.success("更新成功");
        }
    }

    @Anonymous
    @DeleteMapping("")
    public AjaxResult delete(int id)
    {
        if (articleService.delete(id) <= 0) {
            System.out.println("Article " + id + " not exist");
        }
        return AjaxResult.success("删除成功");
    }
}
