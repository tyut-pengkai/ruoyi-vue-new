package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.SysAppTrialUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 试用信息Mapper接口
 *
 * @author zwgu
 * @date 2022-08-01
 */
@Repository
public interface SysAppTrialUserMapper extends BaseMapper<SysAppTrialUser> {
    /**
     * 查询试用信息
     *
     * @param appTrialUserId 试用信息主键
     * @return 试用信息
     */
    public SysAppTrialUser selectSysAppTrialUserByAppTrialUserId(Long appTrialUserId);

    /**
     * 查询试用信息列表
     *
     * @param sysAppTrialUser 试用信息
     * @return 试用信息集合
     */
    public List<SysAppTrialUser> selectSysAppTrialUserList(SysAppTrialUser sysAppTrialUser);

    public List<SysAppTrialUser> selectSysAppTrialUserListByAppIds(Long[] appIds);

    public List<SysAppTrialUser> selectSysAppTrialUserListByAppIdsAndNextEnableTimeBeforeNow(Long[] appIds);

    /**
     * 新增试用信息
     *
     * @param sysAppTrialUser 试用信息
     * @return 结果
     */
    public int insertSysAppTrialUser(SysAppTrialUser sysAppTrialUser);

    /**
     * 修改试用信息
     *
     * @param sysAppTrialUser 试用信息
     * @return 结果
     */
    public int updateSysAppTrialUser(SysAppTrialUser sysAppTrialUser);

    /**
     * 删除试用信息
     *
     * @param appTrialUserId 试用信息主键
     * @return 结果
     */
    public int deleteSysAppTrialUserByAppTrialUserId(Long appTrialUserId);

    /**
     * 批量删除试用信息
     *
     * @param appTrialUserIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAppTrialUserByAppTrialUserIds(Long[] appTrialUserIds);

    /**
     * 查询试用信息
     *
     * @param appId APP主键
     * @return 试用信息
     */
    public SysAppTrialUser selectSysAppTrialUserByAppIdAndLoginIpAndDeviceCode(@Param("appId") Long appId, @Param("loginIp") String loginIp, @Param("deviceCode") String deviceCode);

    public List<SysAppTrialUser> selectSysAppTrialUserByLoginIp(String loginIp);
}
