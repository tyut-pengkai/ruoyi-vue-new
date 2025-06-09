/**
 * @Copyright: lyj  All rights reserved.
 */
package com.ruoyi.rs.jcloud.mybatis.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.ruoyi.rs.jcloud.mybatis.domain.TIotDragonBowl;
import com.ruoyi.rs.jcloud.mybatis.domain.TIotDragonBowlExample;

@Mapper
public interface TIotDragonBowlMapper {

    TIotDragonBowl selectByPrimaryKey(Integer id);

    List<TIotDragonBowl> selectByExample(TIotDragonBowlExample example);

    int countByExample(TIotDragonBowlExample example);

    int insert(TIotDragonBowl record);

    int insertSelective(TIotDragonBowl record);

    int insertBatch(List<TIotDragonBowl> records);

    int deleteByPrimaryKey(Integer id);

    int deleteByExample(TIotDragonBowlExample example);

    int updateByPrimaryKey(TIotDragonBowl record);

    int updateByPrimaryKeySelective(TIotDragonBowl record);

    int updateByExampleSelective(@Param("record") TIotDragonBowl record, @Param("example") TIotDragonBowlExample example);

    int updateByExample(@Param("record") TIotDragonBowl record, @Param("example") TIotDragonBowlExample example);

}