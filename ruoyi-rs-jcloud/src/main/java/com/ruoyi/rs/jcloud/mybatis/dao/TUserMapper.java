/**
 * @Copyright: lyj  All rights reserved.
 */
package com.ruoyi.rs.jcloud.mybatis.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.ruoyi.rs.jcloud.mybatis.domain.TUser;
import com.ruoyi.rs.jcloud.mybatis.domain.TUserExample;

@Mapper
public interface TUserMapper {

    TUser selectByPrimaryKey(Integer id);

    List<TUser> selectByExample(TUserExample example);

    int countByExample(TUserExample example);

    int insert(TUser record);

    int insertSelective(TUser record);

    int insertBatch(List<TUser> records);

    int deleteByPrimaryKey(Integer id);

    int deleteByExample(TUserExample example);

    int updateByPrimaryKey(TUser record);

    int updateByPrimaryKeySelective(TUser record);

    int updateByExampleSelective(@Param("record") TUser record, @Param("example") TUserExample example);

    int updateByExample(@Param("record") TUser record, @Param("example") TUserExample example);

}