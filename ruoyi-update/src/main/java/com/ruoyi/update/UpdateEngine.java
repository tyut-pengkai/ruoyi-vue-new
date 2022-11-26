package com.ruoyi.update;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.update.support.EnumValue;
import com.ruoyi.update.support.FileFilter;
import com.ruoyi.update.support.FileInfo;
import com.ruoyi.update.support.UpdateInfo;
import com.ruoyi.update.utils.Utils;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class UpdateEngine {

    private static final String version = "1.0.0";
    private EnumValue.HandleStrategy defaultHandleStrategy = EnumValue.HandleStrategy.HANDLE;
    private EnumValue.DeleteStrategy defaultDeleteStrategy = EnumValue.DeleteStrategy.HANDLE;
    private EnumValue.OverwriteStrategy defaultOverwriteStrategy = EnumValue.OverwriteStrategy.HANDLE;
    private List<FileFilter> dirFilterList = null;
    private List<FileFilter> fileFilterList = null;
    private Boolean showIgnore = false;
    private Boolean showSame = false;
    private String baseDownloadUrl = null;

    public void addDirFilter(FileFilter dirFilter) {
        if (dirFilterList == null) {
            dirFilterList = new ArrayList<>();
        }
        dirFilterList.add(dirFilter);
    }

    public void addFileFilter(FileFilter fileFilter) {
        if (fileFilterList == null) {
            fileFilterList = new ArrayList<>();
        }
        fileFilterList.add(fileFilter);
    }

    /**
     * 获取两个文件列表差异信息公用部分
     *
     * @param remoteFileInfoList
     * @param localFileInfoList
     * @return
     */
    private ArrayList<FileInfo> getDiff(List<FileInfo> localFileInfoList, List<FileInfo> remoteFileInfoList) {
        ArrayList<FileInfo> diffList = new ArrayList<>(0);
        ArrayList<FileInfo> result = getFileAggregation(localFileInfoList, remoteFileInfoList, Operator.ADD);
        for (FileInfo fileInfo : result) {// 求增
            if (checkFilter(fileInfo)) {
                fileInfo.setDiffType(EnumValue.DiffType.ADD);
                diffList.add(fileInfo);
            }
        }
        result = getFileAggregation(localFileInfoList, remoteFileInfoList, Operator.SUB);
        for (FileInfo fileInfo : result) {// 求删
            if (checkFilter(fileInfo)) {
                fileInfo.setDiffType(EnumValue.DiffType.DEL);
                boolean flagFileDelete = defaultDeleteStrategy == EnumValue.DeleteStrategy.HANDLE;
                if (fileFilterList != null) {
                    for (FileFilter filter : fileFilterList) {
                        if (filter.getDeleteStrategy() != null && filter.getDeleteStrategy() != defaultDeleteStrategy) {
                            String filterShortPath = Utils.makePath(filter.getShortPath());
                            if (fileInfo.getShortPath().endsWith(filterShortPath)) {
                                flagFileDelete = !flagFileDelete;
                                break;
                            }
                        }
                    }
                }
                if (flagFileDelete) {
                    diffList.add(fileInfo);
                }
            }
        }
        result = getFileAggregation(localFileInfoList, remoteFileInfoList, Operator.EQ);
        for (FileInfo fileInfo : result) {// 求同
            if (checkFilter(fileInfo)) {
                String rMd5 = null;
                String lMd5 = null;
                for (FileInfo rFileInfo : remoteFileInfoList) {
                    if (rFileInfo.equals(fileInfo)) {
                        rMd5 = rFileInfo.getMd5();
                        break;
                    }
                }
                for (FileInfo lFileInfo : localFileInfoList) {
                    if (lFileInfo.equals(fileInfo)) {
                        lMd5 = lFileInfo.getMd5();
                        break;
                    }
                }
                if (Objects.equals(rMd5, lMd5) && lMd5 != null) {
                    fileInfo.setDiffType(EnumValue.DiffType.NONE);
                    if (showSame) {
                        diffList.add(fileInfo);
                    }
                } else {
                    fileInfo.setDiffType(EnumValue.DiffType.UPDATE);
                    boolean flagFileOverwrite = defaultOverwriteStrategy == EnumValue.OverwriteStrategy.HANDLE;
                    if (fileFilterList != null) {
                        for (FileFilter filter : fileFilterList) {
                            if (filter.getOverwriteStrategy() != null && filter.getOverwriteStrategy() != defaultOverwriteStrategy) {
                                String filterShortPath = Utils.makePath(filter.getShortPath());
                                if (fileInfo.getShortPath().endsWith(filterShortPath)) {
                                    flagFileOverwrite = !flagFileOverwrite;
                                    break;
                                }
                            }
                        }
                    }
                    if (flagFileOverwrite) {
                        diffList.add(fileInfo);
                    }
                }
            }
        }
        return diffList;
    }

    /**
     * 获取两个文件列表差集、交集
     *
     * @param remoteFileInfoList
     * @param localFileInfoList
     * @param operator
     * @return
     */
    private ArrayList<FileInfo> getFileAggregation(List<FileInfo> localFileInfoList, List<FileInfo> remoteFileInfoList, Operator operator) {
        ArrayList<FileInfo> result = new ArrayList<>(0);
        if (operator == Operator.ADD) {
            result.addAll(remoteFileInfoList);
            result.removeAll(localFileInfoList);
        } else if (operator == Operator.SUB) {
            result.addAll(localFileInfoList);
            result.removeAll(remoteFileInfoList);
        } else if (operator == Operator.EQ) {
            result.addAll(remoteFileInfoList);
            result.retainAll(localFileInfoList);
        }
        return result;
    }

    /**
     * 根据配置策略过滤
     *
     * @param fileInfo
     * @return
     */
    private boolean checkFilter(FileInfo fileInfo) {
        boolean flagDirIgnore = defaultHandleStrategy == EnumValue.HandleStrategy.IGNORE;
        if (dirFilterList != null) {
            for (FileFilter filter : dirFilterList) {
                if (filter.getHandleStrategy() != defaultHandleStrategy) {
                    String filterShortPath = Utils.makePath(filter.getShortPath());
                    if (fileInfo.getShortPath().startsWith(filterShortPath)) {
                        flagDirIgnore = !flagDirIgnore;
                        break;
                    }
                }
            }
        }
        if (!flagDirIgnore) {
            boolean flagFileIgnore = defaultHandleStrategy == EnumValue.HandleStrategy.IGNORE;
            if (fileFilterList != null) {
                for (FileFilter filter : fileFilterList) {
                    if (filter.getHandleStrategy() != defaultHandleStrategy) {
                        String filterShortPath = Utils.makeDirPath(filter.getShortPath());
                        if (fileInfo.getShortPath().endsWith(filterShortPath)) {
                            flagFileIgnore = !flagFileIgnore;
                            break;
                        }
                    }
                }
            }
            if (!flagFileIgnore) {
                fileInfo.setHandleStrategy(EnumValue.HandleStrategy.HANDLE);
                return true;
            } else {
                if (showIgnore) {
                    fileInfo.setHandleStrategy(EnumValue.HandleStrategy.IGNORE);
                    return true;
                }
            }
        } else {
            if (showIgnore) {
                fileInfo.setHandleStrategy(EnumValue.HandleStrategy.IGNORE);
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定目录下的文件信息数据
     *
     * @param dirPath 顶级目录路径
     * @return
     */
    public ArrayList<FileInfo> getFileInfoList(String dirPath) {
        dirPath = Utils.makeDirPath(dirPath);
        File dir = new File(dirPath);
        ArrayList<File> fileList = (ArrayList<File>) FileUtils.listFiles(dir, null, true);
        ArrayList<FileInfo> result = new ArrayList<>(0);
        for (File file : fileList) {
            try {
                String shortPath = file.getCanonicalPath().replace(dirPath, "");
                String md5 = DigestUtils.md5Hex(new FileInputStream(file));
                FileInfo fileInfo = new FileInfo();
//                fileInfo.setFile(file);
                fileInfo.setShortPath(shortPath);
                fileInfo.setSize(file.length());
                fileInfo.setMd5(md5);
                result.add(fileInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 与指定目录内容比较差异信息
     *
     * @param remoteDirPath
     * @param localDirPath
     * @return
     */
    public ArrayList<FileInfo> getDiffFromDir(String localDirPath, String remoteDirPath) {
        localDirPath = Utils.makeDirPath(localDirPath);
        remoteDirPath = Utils.makeDirPath(remoteDirPath);
        ArrayList<FileInfo> localFileInfoList = getFileInfoList(localDirPath);
        ArrayList<FileInfo> remoteFileInfoList = getFileInfoList(remoteDirPath);
        return getDiff(localFileInfoList, remoteFileInfoList);
    }

    /**
     * 与指定JSON内容比较差异信息
     *
     * @param localDirPath
     * @param remoteJson
     * @return
     */
    public ArrayList<FileInfo> getDiffFromJson(String localDirPath, String remoteJson) {
        localDirPath = Utils.makeDirPath(localDirPath);
        List<FileInfo> remoteFileInfoList = JSON.parseArray(remoteJson, FileInfo.class);
        ArrayList<FileInfo> localFileInfoList = getFileInfoList(localDirPath);
        return getDiff(localFileInfoList, remoteFileInfoList);
    }

    /**
     * 与指定URL内容比较差异信息
     *
     * @param localDirPath
     * @param updateUrl
     * @return
     */
    public ArrayList<FileInfo> getDiffFromUrl(String localDirPath, String updateUrl) {
        localDirPath = Utils.makeDirPath(localDirPath);
        String remoteJson = Utils.readFromUrl(updateUrl);
        return getDiffFromJson(localDirPath, remoteJson);
    }

    /**
     * 与指定JSON内容比较升级信息
     *
     * @param remoteJson
     * @param localDirPath
     * @return
     */
    public UpdateInfo getUpdateInfoFromJson(String localDirPath, String remoteJson) {
        localDirPath = Utils.makeDirPath(localDirPath);
        UpdateInfo update = JSON.parseObject(remoteJson, UpdateInfo.class);
        List<FileFilter> fileFilterList = update.getFileFilterList();
        if (fileFilterList != null && fileFilterList.size() > 0) {
            for (FileFilter filter : fileFilterList) {
                if (filter.getFilterType() == EnumValue.FilterType.DIR) {
                    addDirFilter(filter);
                } else if (filter.getFilterType() == EnumValue.FilterType.FILE) {
                    addFileFilter(filter);
                }
            }
        }
        List<FileInfo> remoteFileInfoList = update.getFileInfoList();
        ArrayList<FileInfo> localFileInfoList = getFileInfoList(localDirPath);
        ArrayList<FileInfo> fileInfo = getDiff(localFileInfoList, remoteFileInfoList);
        update.setFileInfoList(fileInfo);
        return update;
    }

    /**
     * 与指定URL内容比较升级信息
     *
     * @param localDirPath
     * @param updateUrl
     * @return
     */
    public UpdateInfo getUpdateInfoFromUrl(String localDirPath, String updateUrl) {
        localDirPath = Utils.makeDirPath(localDirPath);
        String remoteJson = Utils.readFromUrl(updateUrl);
        return getUpdateInfoFromJson(localDirPath, remoteJson);
    }

    private enum Operator {
        EQ, ADD, SUB
    }

}
