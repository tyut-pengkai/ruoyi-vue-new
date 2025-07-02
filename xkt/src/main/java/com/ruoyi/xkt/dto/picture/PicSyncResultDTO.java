package com.ruoyi.xkt.dto.picture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-05-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PicSyncResultDTO {

    private String productPicKey;

    private Boolean success;

}
