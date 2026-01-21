package com.ruoyi.web.controller.open3rd;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.config.Open3rdConfig;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.web.controller.open3rd.domain.Open3rdDownloadInfo;
import com.ruoyi.web.controller.open3rd.domain.Open3rdFileInfo;
import com.ruoyi.web.controller.open3rd.domain.Open3rdPermission;
import com.ruoyi.web.controller.open3rd.domain.Open3rdResponse;
import com.ruoyi.web.controller.open3rd.domain.Open3rdSignaturePayload;
import com.ruoyi.web.controller.open3rd.service.Open3rdError;
import com.ruoyi.web.controller.open3rd.service.Open3rdFileDescriptor;
import com.ruoyi.web.controller.open3rd.service.Open3rdFileService;
import com.ruoyi.web.controller.open3rd.service.Open3rdSignatureService;
import com.ruoyi.web.controller.open3rd.service.Open3rdSignatureService.Open3rdTokenUser;

@Anonymous
@RestController
@RequestMapping("/open3rd")
public class Open3rdController
{
    private static final String FILE_ID_PATTERN = "^[A-Za-z0-9][A-Za-z0-9_-]{0,127}$";

    private final Open3rdFileService fileService;
    private final Open3rdSignatureService signatureService;
    private final Open3rdConfig open3rdConfig;
    private final ServerConfig serverConfig;

    public Open3rdController(Open3rdFileService fileService, Open3rdSignatureService signatureService,
            Open3rdConfig open3rdConfig, ServerConfig serverConfig)
    {
        this.fileService = fileService;
        this.signatureService = signatureService;
        this.open3rdConfig = open3rdConfig;
        this.serverConfig = serverConfig;
    }

    @GetMapping("/signature")
    public Open3rdResponse<Open3rdSignaturePayload> signature()
    {
        Open3rdSignaturePayload payload = new Open3rdSignaturePayload(open3rdConfig.getAppId(),
                signatureService.generateSignature());
        return Open3rdResponse.success(payload);
    }

    @GetMapping("/files/{fileId}/permission")
    public Open3rdResponse<Open3rdPermission> permission(@PathVariable("fileId") String fileId,
            HttpServletRequest request)
    {
        Optional<Open3rdError> error = validateRequest(fileId, request);
        if (error.isPresent())
        {
            return Open3rdResponse.error(error.get().getCode(), error.get().getMessage());
        }

        Optional<Open3rdFileDescriptor> fileDescriptor = fileService.findFile(fileId);
        if (fileDescriptor.isEmpty())
        {
            return Open3rdResponse.error(Open3rdError.FILE_NOT_FOUND.getCode(), Open3rdError.FILE_NOT_FOUND.getMessage());
        }

        Open3rdPermission permission = buildDefaultPermission();
        Open3rdTokenUser tokenUser = resolveTokenUser(request);
        if (tokenUser == null && StringUtils.isNotEmpty(request.getHeader(Open3rdSignatureService.HEADER_TOKEN)))
        {
            return Open3rdResponse.error(Open3rdError.INVALID_TOKEN.getCode(), Open3rdError.INVALID_TOKEN.getMessage());
        }
        if (tokenUser != null && StringUtils.isNotEmpty(tokenUser.getUserId()))
        {
            permission.setUserId(tokenUser.getUserId());
        }

        return Open3rdResponse.success(permission);
    }

    @GetMapping("/files/{fileId}")
    public Open3rdResponse<Open3rdFileInfo> fileInfo(@PathVariable("fileId") String fileId, HttpServletRequest request)
    {
        Optional<Open3rdError> error = validateRequest(fileId, request);
        if (error.isPresent())
        {
            return Open3rdResponse.error(error.get().getCode(), error.get().getMessage());
        }

        Optional<Open3rdFileDescriptor> fileDescriptor = fileService.findFile(fileId);
        if (fileDescriptor.isEmpty())
        {
            return Open3rdResponse.error(Open3rdError.FILE_NOT_FOUND.getCode(), Open3rdError.FILE_NOT_FOUND.getMessage());
        }

        Open3rdFileDescriptor descriptor = fileDescriptor.get();
        File file = descriptor.getFile();
        Open3rdFileInfo info = new Open3rdFileInfo();
        info.setId(descriptor.getFileId());
        info.setName(descriptor.getName());
        info.setVersion(descriptor.getVersion());
        info.setCreatorId(descriptor.getCreatorId());
        info.setModifierId(descriptor.getModifierId());
        info.setSize(file.length());
        info.setDisableWatermark(descriptor.getDisableWatermark());
        info.setUpdateTime(file.lastModified() / 1000);
        info.setCreateTime(getCreateTime(file));
        return Open3rdResponse.success(info);
    }

    @GetMapping("/files/{fileId}/download")
    public Open3rdResponse<Open3rdDownloadInfo> download(@PathVariable("fileId") String fileId,
            HttpServletRequest request)
    {
        Optional<Open3rdError> error = validateRequest(fileId, request);
        if (error.isPresent())
        {
            return Open3rdResponse.error(error.get().getCode(), error.get().getMessage());
        }

        Optional<Open3rdFileDescriptor> fileDescriptor = fileService.findFile(fileId);
        if (fileDescriptor.isEmpty())
        {
            return Open3rdResponse.error(Open3rdError.FILE_NOT_FOUND.getCode(), Open3rdError.FILE_NOT_FOUND.getMessage());
        }

        String url = serverConfig.getUrl() + "/open3rd/files/" + fileId + "/content";
        return Open3rdResponse.success(new Open3rdDownloadInfo(url));
    }

    @GetMapping("/files/{fileId}/content")
    public void downloadContent(@PathVariable("fileId") String fileId, HttpServletRequest request,
            HttpServletResponse response) throws IOException
    {
        Optional<Open3rdFileDescriptor> descriptor = fileService.findFile(fileId);
        if (descriptor.isEmpty())
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File file = descriptor.get().getFile();
        long fileLength = file.length();
        String rangeHeader = request.getHeader("Range");
        response.setHeader("Accept-Ranges", "bytes");
        response.setContentType(resolveContentType(file));

        if (StringUtils.isNotEmpty(rangeHeader) && rangeHeader.startsWith("bytes="))
        {
            String[] ranges = rangeHeader.replace("bytes=", "").split("-", 2);
            long start = parseRangeValue(ranges[0], 0);
            long end = ranges.length > 1 && StringUtils.isNotEmpty(ranges[1]) ? parseRangeValue(ranges[1], fileLength - 1)
                    : fileLength - 1;

            if (start > end || end >= fileLength)
            {
                response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return;
            }

            long contentLength = end - start + 1;
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Content-Range", String.format("bytes %d-%d/%d", start, end, fileLength));
            response.setHeader("Content-Length", String.valueOf(contentLength));
            writeFileRange(file, start, contentLength, response);
            return;
        }

        response.setHeader("Content-Length", String.valueOf(fileLength));
        writeFileRange(file, 0, fileLength, response);
    }

    private Optional<Open3rdError> validateRequest(String fileId, HttpServletRequest request)
    {
        if (!isValidFileId(fileId))
        {
            return Optional.of(Open3rdError.INVALID_PARAM);
        }

        Optional<Open3rdError> signatureError = signatureService.validateHeaders(request);
        if (signatureError.isPresent())
        {
            return signatureError;
        }

        return Optional.empty();
    }

    private Open3rdPermission buildDefaultPermission()
    {
        Open3rdPermission permission = new Open3rdPermission();
        Open3rdConfig.Open3rdPermissionConfig config = open3rdConfig.getPermission();
        permission.setRead(config.isRead());
        permission.setUpdate(config.isUpdate());
        permission.setCopy(config.isCopy());
        permission.setComment(config.isComment());
        permission.setPrint(config.isPrint());
        permission.setDownload(config.isDownload());
        permission.setRename(config.isRename());
        permission.setHistory(config.isHistory());
        permission.setManage(config.isManage());
        return permission;
    }

    private Open3rdTokenUser resolveTokenUser(HttpServletRequest request)
    {
        String token = request.getHeader(Open3rdSignatureService.HEADER_TOKEN);
        return signatureService.resolveToken(token).orElse(null);
    }

    private boolean isValidFileId(String fileId)
    {
        return StringUtils.isNotEmpty(fileId) && fileId.matches(FILE_ID_PATTERN);
    }

    private long parseRangeValue(String value, long defaultValue)
    {
        if (StringUtils.isEmpty(value))
        {
            return defaultValue;
        }
        try
        {
            return Long.parseLong(value);
        }
        catch (NumberFormatException ex)
        {
            return defaultValue;
        }
    }

    private void writeFileRange(File file, long start, long contentLength, HttpServletResponse response) throws IOException
    {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r"))
        {
            randomAccessFile.seek(start);
            byte[] buffer = new byte[8192];
            long remaining = contentLength;
            while (remaining > 0)
            {
                int read = randomAccessFile.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                if (read == -1)
                {
                    break;
                }
                response.getOutputStream().write(buffer, 0, read);
                remaining -= read;
            }
        }
    }

    private long getCreateTime(File file)
    {
        try
        {
            return Files.readAttributes(file.toPath(), java.nio.file.attribute.BasicFileAttributes.class).creationTime()
                    .toInstant().getEpochSecond();
        }
        catch (IOException ex)
        {
            return Instant.ofEpochMilli(file.lastModified()).getEpochSecond();
        }
    }

    private String resolveContentType(File file)
    {
        try
        {
            String contentType = Files.probeContentType(file.toPath());
            return StringUtils.isEmpty(contentType) ? MediaType.APPLICATION_OCTET_STREAM_VALUE : contentType;
        }
        catch (IOException ex)
        {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
}
