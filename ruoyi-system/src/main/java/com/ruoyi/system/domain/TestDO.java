package com.ruoyi.system.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

/**
 * @author liangyq
 * @date 2025-03-18 15:26
 */
@Data
@TableName("test")
public class TestDO {

    @TableId
    private Long id;

    private String description;

    @Version
    private Long version;
}
