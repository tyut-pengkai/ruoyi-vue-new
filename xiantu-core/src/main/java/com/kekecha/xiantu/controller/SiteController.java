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
    @GetMapping("/net")
    public AjaxResult getNetList(String city, String filter, int pageNum, int pageSize)
    {
        int type = 1;

        boolean select_all = false;

        if (pageNum <= 0) {
            return AjaxResult.error("参数不合法");
        }

        if (pageSize <= 0) {
            select_all = true;
        }

        AjaxResult ajaxResult = AjaxResult.success("查询成功");

        List<Site> siteList = siteService.selectSite(type, city, filter);

        int total = siteList.size();
        ajaxResult.put("total", total);

        if (select_all) {
            ajaxResult.put("data", siteList);
        } else {
            int search_start = (pageNum - 1) * pageSize;
            int search_end = Math.min((pageNum * pageSize), total);
            // 如果 start 超过列表大小，返回空列表
            search_end = Math.min(search_end, total);
            if (search_start >= total) {
                List<Site> sub_list = new ArrayList<>();
                ajaxResult.put("data", sub_list);
            } else {
                List<Site> sub_list = siteList.subList(search_start, search_end);
                ajaxResult.put("data", sub_list);
            }
        }

        return ajaxResult;
    }

    @Anonymous
    @GetMapping("/parking")
    public AjaxResult getParkingList(String city, String filter, int pageNum, int pageSize)
    {
        int type = 2;
        boolean select_all = false;

        if (pageNum <= 0) {
            return AjaxResult.error("参数不合法");
        }

        if (pageSize <= 0) {
            select_all = true;
        }

        AjaxResult ajaxResult = AjaxResult.success("查询成功");

        List<Site> siteList = siteService.selectSite(type, city, filter);

        int total = siteList.size();
        ajaxResult.put("total", total);

        if (select_all) {
            ajaxResult.put("data", siteList);
        } else {
            int search_start = (pageNum - 1) * pageSize;
            int search_end = Math.min((pageNum * pageSize), total);
            // 如果 start 超过列表大小，返回空列表
            search_end = Math.min(search_end, total);
            if (search_start >= total) {
                List<Site> sub_list = new ArrayList<>();
                ajaxResult.put("data", sub_list);
            } else {
                List<Site> sub_list = siteList.subList(search_start, search_end);
                ajaxResult.put("data", sub_list);
            }
        }

        return ajaxResult;
    }

    @Anonymous
    @PostMapping("/net/edit")
    public AjaxResult netEdit(@RequestBody Site site)
    {
        site.setType(1);

        Site old_site = siteService.selectOne(1, site.getName());
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
    @PostMapping("/parking/edit")
    public AjaxResult parkingEdit(@RequestBody Site site)
    {
        site.setType(2);

        Site old_site = siteService.selectOne(2, site.getName());
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
