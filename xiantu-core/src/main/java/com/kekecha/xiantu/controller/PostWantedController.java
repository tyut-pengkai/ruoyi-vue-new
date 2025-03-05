package com.kekecha.xiantu.controller;

import com.kekecha.xiantu.domain.PostWanted;
import com.kekecha.xiantu.service.IPostWantedService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/postwanted")
public class PostWantedController {
    @Autowired
    private IPostWantedService postWantedService;

    @Anonymous
    @GetMapping("")
    public AjaxResult getPostWantedList(
                                        @RequestParam(name="pageNum", defaultValue = "1")int pageNum,
                                        @RequestParam(name="pageSize", defaultValue = "10")int pageSize,
                                        @RequestParam(name="filter", required = false) String filter,
                                        @RequestParam(name="city", required = false) String city)
    {
        boolean select_all = false;

        if (pageNum <= 0) {
            return AjaxResult.error("参数不合法");
        }

        if (pageSize <= 0) {
            select_all = true;
        }

        AjaxResult ajaxResult = AjaxResult.success("查询成功");
        List<PostWanted> list = postWantedService.select(filter, city);

        int total = list.size();
        ajaxResult.put("total", total);

        if (select_all) {
            ajaxResult.put("data", list);
        } else {
            int search_start = (pageNum - 1) * pageSize;
            int search_end = Math.min((pageNum * pageSize), total);
            // 如果 start 超过列表大小，返回空列表
            if (search_start >= total) {
                List<PostWanted> result_list = new ArrayList<>();
                ajaxResult.put("data", result_list);
            } else {
                List<PostWanted> result_list = list.subList(search_start, search_end);
                ajaxResult.put("data", result_list);
            }
        }
        return ajaxResult;
    }

    @Anonymous
//    @PreAuthorize("@ss.hasPermi('data:postwanted:list')")
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody PostWanted postWanted) {
        if (postWanted.getId() == 0) {
            postWantedService.insert(postWanted);
            return AjaxResult.success("创建成功");
        } else {
            postWantedService.update(postWanted);
            return AjaxResult.success("更新成功");
        }
    }

    @Anonymous
//    @PreAuthorize("@ss.hasPermi('data:postwanted:list')")
    @DeleteMapping("")
    public AjaxResult delete(int id) {
        int res = postWantedService.delete(id);
        AjaxResult ajaxResult = AjaxResult.success();
        if (res > 0) {
            return AjaxResult.success("删除成功");
        } else {
            return AjaxResult.error("删除的目标岗位不存在");
        }
    }
}
