package com.zzyl.nursing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NursingProjectDto {
    private Long id;
    private String name;
    private Integer orderNo;
    private String unit;
    private BigDecimal price;
    private String image;
    private String nursingRequirement;
    private Integer status;
    private String createdBy;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
