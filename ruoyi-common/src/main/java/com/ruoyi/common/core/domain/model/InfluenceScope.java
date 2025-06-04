package com.ruoyi.common.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author liangyq
 * @date 2025-05-30 17:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfluenceScope {

    private Integer count;

    private Set<Long> userIds;
}
