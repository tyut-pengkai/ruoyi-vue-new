package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.xkt.service.IUserSearchHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户搜索历史Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Api(tags = "用户搜索历史")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/user-search-his")
public class UserSearchHistoryController extends XktBaseController {

    final IUserSearchHistoryService userSearchHisService;

    @ApiOperation(value = "获取用户搜索历史", httpMethod = "GET", response = R.class)
    @GetMapping(value = "")
    public R<List<String>> recordList() {
        return R.ok(userSearchHisService.recordList());
    }


    @ApiOperation(value = "删除用户搜索历史", httpMethod = "DELETE", response = R.class)
    @DeleteMapping("")
    public R<Integer> clearSearchHisRecord() {
        return R.ok(userSearchHisService.clearSearchHisRecord());
    }

}
