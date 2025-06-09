/**
 * @Copyright: lyj  All rights reserved.
 */
package com.ruoyi.rs.jcloud.mybatis.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import com.ruoyi.rs.jcloud.mybatis.domain.TAiEventContent;
import com.ruoyi.rs.jcloud.mybatis.domain.TAiEventContentExample;

@Mapper
public interface TAiEventContentMapper {

    TAiEventContent selectByPrimaryKey(Integer id);

    List<TAiEventContent> selectByExample(TAiEventContentExample example);

    int countByExample(TAiEventContentExample example);

    int insert(TAiEventContent record);

    int insertSelective(TAiEventContent record);

    int insertBatch(List<TAiEventContent> records);

    int deleteByPrimaryKey(Integer id);

    int deleteByExample(TAiEventContentExample example);

    int updateByPrimaryKey(TAiEventContent record);

    int updateByPrimaryKeySelective(TAiEventContent record);

    int updateByExampleSelective(@Param("record") TAiEventContent record, @Param("example") TAiEventContentExample example);

    int updateByExample(@Param("record") TAiEventContent record, @Param("example") TAiEventContentExample example);

}