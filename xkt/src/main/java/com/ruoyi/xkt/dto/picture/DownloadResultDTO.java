package com.ruoyi.xkt.dto.picture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-03-26 15:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DownloadResultDTO {

    private String fileName;

    private String filePath;

    private String tempDirName;

    private String tempDirPath;
}
