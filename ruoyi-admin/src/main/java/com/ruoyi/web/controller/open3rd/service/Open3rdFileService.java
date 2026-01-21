package com.ruoyi.web.controller.open3rd.service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.ruoyi.common.config.Open3rdConfig;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.StringUtils;

@Service
public class Open3rdFileService
{
    private final Open3rdConfig open3rdConfig;

    public Open3rdFileService(Open3rdConfig open3rdConfig)
    {
        this.open3rdConfig = open3rdConfig;
    }

    public Optional<Open3rdFileDescriptor> findFile(String fileId)
    {
        for (Open3rdConfig.Open3rdFileConfig fileConfig : open3rdConfig.getFiles())
        {
            if (fileId.equals(fileConfig.getId()))
            {
                return resolveFileDescriptor(fileId, fileConfig);
            }
        }

        String root = resolveRoot();
        if (StringUtils.isNotEmpty(root))
        {
            Path candidate = Paths.get(root, fileId);
            File file = candidate.toFile();
            if (file.exists() && file.isFile())
            {
                return Optional.of(buildDescriptor(fileId, file.getName(), file, null, null, null, null));
            }
        }

        return Optional.empty();
    }

    private Optional<Open3rdFileDescriptor> resolveFileDescriptor(String fileId, Open3rdConfig.Open3rdFileConfig fileConfig)
    {
        String fileName = fileConfig.getName();
        if (StringUtils.isEmpty(fileName) && StringUtils.isNotEmpty(fileConfig.getPath()))
        {
            fileName = new File(fileConfig.getPath()).getName();
        }

        String path = fileConfig.getPath();
        if (StringUtils.isEmpty(path))
        {
            String root = resolveRoot();
            if (StringUtils.isNotEmpty(root) && StringUtils.isNotEmpty(fileName))
            {
                path = Paths.get(root, fileName).toString();
            }
        }

        if (StringUtils.isEmpty(path))
        {
            return Optional.empty();
        }

        File file = new File(path);
        if (!file.exists() || !file.isFile())
        {
            return Optional.empty();
        }

        return Optional.of(buildDescriptor(fileId, fileName, file, fileConfig.getCreatorId(), fileConfig.getModifierId(),
                fileConfig.getVersion(), fileConfig.getDisableWatermark()));
    }

    private Open3rdFileDescriptor buildDescriptor(String fileId, String fileName, File file, String creatorId, String modifierId,
            Long version, Boolean disableWatermark)
    {
        String resolvedCreatorId = StringUtils.isEmpty(creatorId) ? "system" : creatorId;
        String resolvedModifierId = StringUtils.isEmpty(modifierId) ? resolvedCreatorId : modifierId;
        Long resolvedVersion = Optional.ofNullable(version).orElse(1L);
        return new Open3rdFileDescriptor(fileId, fileName, file, resolvedCreatorId, resolvedModifierId, resolvedVersion,
                disableWatermark);
    }

    private String resolveRoot()
    {
        if (StringUtils.isNotEmpty(open3rdConfig.getFileRoot()))
        {
            return open3rdConfig.getFileRoot();
        }

        return RuoYiConfig.getUploadPath();
    }
}
