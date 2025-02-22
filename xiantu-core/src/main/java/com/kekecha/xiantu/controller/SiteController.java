package com.kekecha.xiantu.controller;

import com.kekecha.xiantu.domain.Camera;
import com.kekecha.xiantu.domain.Site;
import com.kekecha.xiantu.service.ICameraService;
import com.kekecha.xiantu.service.ISiteService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/site")
public class SiteController extends BaseController {

    @Autowired
    private ISiteService siteService;
    @Autowired
    private ICameraService cameraService;

    @Anonymous
    @GetMapping("")
    public AjaxResult list(int type, String city, String filter, int start, int end)
    {
        if (start < 0 || start > end) {
            return AjaxResult.error("参数不合法");
        }

        AjaxResult ajaxResult = AjaxResult.success("查询成功");

        List<Site> siteList = siteService.selectSite(type, city, filter);

        int total = siteList.size();
        ajaxResult.put("total", total);

        int search_end = Math.min(end, total);
        if (start >= total) {
            List<Site> sub_list = new ArrayList<>();
            ajaxResult.put("data", sub_list);
        } else {
            List<Site> sub_list = siteList.subList(start, search_end);
            ajaxResult.put("data", sub_list);
        }

        return ajaxResult;
    }

    @Anonymous
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody Site site)
    {
        int site_type = site.getType();

        if (site_type <= 0 || site_type > 2) {
            return AjaxResult.error("参数错误");
        }

        Site old_site = siteService.selectOne(site_type, site.getName());
        if (old_site != null) {
            System.out.println(old_site.toString());
            siteService.updateSite(site);
            return AjaxResult.success("更新成功");
        } else {
            System.out.println("old site " + site.getName() + " not found");
            siteService.insertSite(site);
            return AjaxResult.success("创建成功");
        }
    }

    @Anonymous
    @DeleteMapping("")
    public AjaxResult delete(@RequestParam("name") String name)
    {
        if (siteService.deleteSite(name) <= 0) {
            System.out.println("Site " + name + " not exist");
        }
        return AjaxResult.success("删除成功");
    }

    @Anonymous
    @GetMapping("/camera")
    public AjaxResult selectRefCamera(@RequestParam("name") String name)
    {
        AjaxResult ajaxResult = AjaxResult.success("查询成功");
        try {
            List<Camera> list = cameraService.selectByRef(name);
            ajaxResult.put("total", list.size());
            ajaxResult.put("data", list);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ajaxResult.put("total", 0);
            ajaxResult.put("data", new ArrayList<>());
        }

        return ajaxResult;
    }
}
