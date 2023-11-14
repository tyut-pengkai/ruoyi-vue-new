package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.SysAppUserCount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户信息统计Mapper接口
 *
 * @author ruoyi
 */
public interface SysAppUserCountMapper extends BaseMapper<SysAppUserCount> {

    @Select("select * from sys_app_user_count auc where auc.app_id = #{appId} and date(auc.create_time) = date(#{createTime})")
    public SysAppUserCount selectAppUserCountByAppIdAndCreateTime(@Param("appId") Long appId, @Param("createTime") String createTime);

}
