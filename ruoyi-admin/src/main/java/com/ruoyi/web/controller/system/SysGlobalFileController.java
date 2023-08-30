package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.uuid.SnowflakeIdWorker;
import com.ruoyi.system.domain.SysGlobalFile;
import com.ruoyi.system.service.ISysGlobalFileService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 远程文件Controller
 *
 * @author zwgu
 * @date 2022-09-30
 */
@RestController
@RequestMapping("/system/globalFile")
public class SysGlobalFileController extends BaseController {
    @Autowired
    private ISysGlobalFileService sysGlobalFileService;
    @Resource
    private RedisCache redisCache;

    /**
     * 查询远程文件列表
     */
    @PreAuthorize("@ss.hasPermi('system:globalFile:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysGlobalFile sysGlobalFile) {
        startPage();
        List<SysGlobalFile> list = sysGlobalFileService.selectSysGlobalFileList(sysGlobalFile);
        return getDataTable(list);
    }

    /**
     * 导出远程文件列表
     */
    @PreAuthorize("@ss.hasPermi('system:globalFile:export')")
    @Log(title = "远程文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysGlobalFile sysGlobalFile) {
        List<SysGlobalFile> list = sysGlobalFileService.selectSysGlobalFileList(sysGlobalFile);
        ExcelUtil<SysGlobalFile> util = new ExcelUtil<SysGlobalFile>(SysGlobalFile.class);
        util.exportExcel(response, list, "远程文件数据");
    }

    /**
     * 获取远程文件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:globalFile:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysGlobalFileService.selectSysGlobalFileById(id));
    }

    /**
     * 新增远程文件
     */
    @PreAuthorize("@ss.hasPermi('system:globalFile:add')")
    @Log(title = "远程文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysGlobalFile sysGlobalFile) {
        return toAjax(sysGlobalFileService.insertSysGlobalFile(sysGlobalFile));
    }

    /**
     * 修改远程文件
     */
    @PreAuthorize("@ss.hasPermi('system:globalFile:edit')")
    @Log(title = "远程文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysGlobalFile sysGlobalFile) {
        return toAjax(sysGlobalFileService.updateSysGlobalFile(sysGlobalFile));
    }

    /**
     * 删除远程文件
     */
    @PreAuthorize("@ss.hasPermi('system:globalFile:remove')")
    @Log(title = "远程文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysGlobalFileService.deleteSysGlobalFileByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('system:globalFile:edit')")
    @Log(title = "远程文件", businessType = BusinessType.UPDATE)
    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile file, Long globalFileId) {
        SysGlobalFile globalFile = sysGlobalFileService.selectSysGlobalFileById(globalFileId);
        if (globalFile != null) {
            String filePath = RuoYiConfig.getGlobalFilePath() + File.separator + globalFileId;
            File desc = new File(filePath);
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
            try {
                FileUtils.writeByteArrayToFile(desc, file.getBytes());
                return AjaxResult.success();
            } catch (IOException e) {
                e.printStackTrace();
                return AjaxResult.error("文件上传出错：" + e.getMessage());
            }
        }
        return AjaxResult.error("远程文件不存在");
    }

    /**
     * 下载远程文件
     */
    @PreAuthorize("@ss.hasPermi('system:globalFile:query')")
    @GetMapping(value = "/download/{id}")
    public AjaxResult download(@PathVariable("id") Long id) {
        SysGlobalFile globalFile = sysGlobalFileService.selectSysGlobalFileById(id);
        if (globalFile != null) {
            String filePath = RuoYiConfig.getGlobalFilePath() + File.separator + globalFile.getId();
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    String randomPath = String.valueOf(new SnowflakeIdWorker().nextId());
                    FileUtils.copyFile(file, new File(RuoYiConfig.getGlobalFilePath() + File.separator + randomPath + File.separator + globalFile.getName()));
                    // 存入redis。默认有效期5分钟
                    redisCache.setCacheObject(CacheConstants.GLOBAL_FILE_DOWNLOAD_KEY + randomPath + File.separator + globalFile.getName(), null, 300, TimeUnit.SECONDS);
                    return AjaxResult.success(randomPath + File.separator + globalFile.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ApiException(ErrorCode.ERROR_OTHER_FAULTS, e.getMessage());
                }
            }
        }
        return AjaxResult.error("远程文件不存在");
    }
}
