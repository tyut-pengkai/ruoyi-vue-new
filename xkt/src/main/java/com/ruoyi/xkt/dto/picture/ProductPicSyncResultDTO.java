package com.ruoyi.xkt.dto.picture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPicSyncResultDTO {

    private Boolean success;

    private List<PicSyncResultDTO> picSyncResults;

}
