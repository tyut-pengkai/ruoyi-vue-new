package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.manager.EnumManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/enum")
public class EnumController extends BaseController {

    @GetMapping(value = "/name/{name}")
    public AjaxResult getEnums(@PathVariable String name){
        return success(EnumManager.getEnum(name));
    }
}
