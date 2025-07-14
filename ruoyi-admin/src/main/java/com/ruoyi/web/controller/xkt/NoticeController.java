package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.controller.xkt.vo.notice.*;
import com.ruoyi.xkt.dto.notice.*;
import com.ruoyi.xkt.service.INoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告 信息操作处理
 *
 * @author ruoyi
 */
@Api(tags = "通知公告（档口公告、系统公告）")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/notice")
public class NoticeController extends BaseController {

    final INoticeService noticeService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "新增公告（档口公告、系统公告）", httpMethod = "POST", response = R.class)
    @Log(title = "新增公告（档口公告、系统公告）", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody NoticeCreateVO createVO) {
        return R.ok(noticeService.create(BeanUtil.toBean(createVO, NoticeCreateDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "编辑公告（档口公告、系统公告）", httpMethod = "PUT", response = R.class)
    @Log(title = "编辑公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody NoticeEditVO editVO) {
        return R.ok(noticeService.edit(BeanUtil.toBean(editVO, NoticeEditDTO.class)));
    }

    @ApiOperation(value = "公告详情（档口公告、系统公告）", httpMethod = "PUT", response = R.class)
    @GetMapping("/{noticeId}")
    public R<NoticeResVO> getInfo(@PathVariable Long noticeId) {
        return R.ok(BeanUtil.toBean(noticeService.getInfo(noticeId), NoticeResVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "删除公告（档口公告、系统公告）", httpMethod = "DELETE", response = R.class)
    @Log(title = "删除公告", businessType = BusinessType.DELETE)
    @DeleteMapping("")
    public R<Integer> delete(@Validated @RequestBody NoticeDeleteVO deleteVO) {
        return R.ok(noticeService.delete(BeanUtil.toBean(deleteVO, NoticeDeleteDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "档口公告列表、系统公告列表 ", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<NoticeResDTO>> page(@Validated @RequestBody NoticePageVO pageVO) {
        return R.ok(noticeService.page(BeanUtil.toBean(pageVO, NoticePageDTO.class)));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')||@ss.hasSupplierSubRole()")
    @ApiOperation(value = "档口首页、电商卖家首页获取公告列表", httpMethod = "GET", response = R.class)
    @GetMapping("/list")
    public R<List<NoticeLatest10ResVO>> latest10() {
        return R.ok(BeanUtil.copyToList(noticeService.latest10(), NoticeLatest10ResVO.class));
    }


}
