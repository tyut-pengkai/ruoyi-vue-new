/**
 * @Copyright: lyj  All rights reserved.
 */
package com.ruoyi.rs.jcloud.mybatis.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.ruoyi.rs.jcloud.mybatis.domain.TIotDragonBowlLog;
import com.ruoyi.rs.jcloud.mybatis.domain.TIotDragonBowlLogExample;

@Mapper
public interface TIotDragonBowlLogMapper {

    TIotDragonBowlLog selectByPrimaryKey(Integer id);

    List<TIotDragonBowlLog> selectByExample(TIotDragonBowlLogExample example);

    int countByExample(TIotDragonBowlLogExample example);

    int insert(TIotDragonBowlLog record);

    int insertSelective(TIotDragonBowlLog record);

    int insertBatch(List<TIotDragonBowlLog> records);

    int deleteByPrimaryKey(Integer id);

    int deleteByExample(TIotDragonBowlLogExample example);

    int updateByPrimaryKey(TIotDragonBowlLog record);

    int updateByPrimaryKeySelective(TIotDragonBowlLog record);

    int updateByExampleSelective(@Param("record") TIotDragonBowlLog record, @Param("example") TIotDragonBowlLogExample example);

    int updateByExample(@Param("record") TIotDragonBowlLog record, @Param("example") TIotDragonBowlLogExample example);

}