package com.ruoyi.web.controller.sale;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysCardTemplateService;
import com.ruoyi.web.domain.SaleAppVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 销售Controller
 *
 * @author zwgu
 * @date 2022-02-21
 */
@RestController
@RequestMapping("/sale")
public class SysSaleController extends BaseController {
    @Autowired
    private ISysAppService sysAppService;
    @Autowired
    private ISysCardTemplateService sysCardTemplateService;

    /**
     * 查询软件列表
     */
    @GetMapping("/appList")
    public TableDataInfo appList() {
        List<SaleAppVo> saleAppVoList = new ArrayList<>();
        List<SysApp> appList = sysAppService.selectSysAppList(new SysApp());
        for (SysApp app : appList) {
            SysCardTemplate ct = new SysCardTemplate();
            ct.setAppId(app.getAppId());
            ct.setOnSale(UserConstants.YES);
            saleAppVoList.add(new SaleAppVo(app.getAppId(), app.getAppName(), sysCardTemplateService.selectSysCardTemplateList(ct).size()));
        }
        return getDataTable(saleAppVoList);
    }

}
