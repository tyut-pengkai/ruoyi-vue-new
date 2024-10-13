package com.ruoyi.utils;

import com.google.common.collect.Lists;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.decode.AXMLDoc;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.editor.ApplicationInfoEditor;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.editor.PermissionEditor;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.utils.TypedValue;
import com.ruoyi.common.utils.apk.xml.decode.AXmlDecoder;
import com.ruoyi.common.utils.apk.xml.decode.AXmlResourceParser;
import com.ruoyi.common.utils.apk.xml.decode.XmlPullParser;
import com.ruoyi.common.utils.apk.zip.ZipEntry;
import com.ruoyi.common.utils.apk.zip.ZipFile;
import com.ruoyi.common.utils.apk.zip.ZipOutputStream;
import com.ruoyi.system.domain.vo.ActivityMethodVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jf.baksmali.Adaptors.ClassDefinition;
import org.jf.baksmali.BaksmaliOptions;
import org.jf.baksmali.formatter.BaksmaliWriter;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.analysis.ClassPath;
import org.jf.dexlib2.analysis.DexClassProvider;
import org.jf.dexlib2.dexbacked.DexBackedClassDef;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexBackedMethod;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.writer.builder.DexBuilder;
import org.jf.smali.Smali;
import org.jf.smali.SmaliOptions;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class QuickAccessApkUtil {

    private static final String injectConfigClass = "Lcom/c/qat/InjectConfig;";
    private static final String injectEntranceClass = "com.App";
    private static final String injectEntranceType = classNameToTypeName(injectEntranceClass);
    private static String packageName;
    private static String applicationName;

    private static byte[] parseManifest(InputStream is) throws IOException {
        AXmlDecoder axml = AXmlDecoder.decode(is);
        AXmlResourceParser parser = new AXmlResourceParser();
        parser.open(new ByteArrayInputStream(axml.getData()), axml.mTableStrings);
        boolean success = false;
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
            if (type != XmlPullParser.START_TAG)
                continue;
            if (parser.getName().equals("manifest")) {
                int size = parser.getAttributeCount();
                for (int i = 0; i < size; ++i) {
                    if (parser.getAttributeName(i).equals("package")) {
                        packageName = parser.getAttributeValue(i);
                    }
                }
            } else if (parser.getName().equals("application")) {
                int size = parser.getAttributeCount();
                for (int i = 0; i < size; ++i) {
                    if (parser.getAttributeNameResource(i) == 0x01010003) {
                        applicationName = parser.getAttributeValue(i);
                        int index = axml.mTableStrings.getSize();
                        byte[] data = axml.getData();
                        int off = parser.currentAttributeStart + 20 * i;
                        off += 8;
                        writeInt(data, off, index);
                        off += 8;
                        writeInt(data, off, index);
                    }
                }
                if (StringUtils.isBlank(applicationName)) {
                    int off = parser.currentAttributeStart;
                    byte[] data = axml.getData();
                    byte[] newData = new byte[data.length + 20];
                    System.arraycopy(data, 0, newData, 0, off);
                    System.arraycopy(data, off, newData, off + 20, data.length - off);

                    // chunkSize
                    int chunkSize = readInt(newData, off - 32);
                    writeInt(newData, off - 32, chunkSize + 20);
                    // attributeCount
                    writeInt(newData, off - 8, size + 1);

                    int idIndex = parser.findResourceID(0x01010003);
                    if (idIndex == -1)
                        throw new IOException("idIndex == -1");
                    boolean isMax = true;
                    for (int i = 0; i < size; ++i) {
                        int id = parser.getAttributeNameResource(i);
                        if (id > 0x01010003) {
                            isMax = false;
                            if (i != 0) {
                                System.arraycopy(newData, off + 20, newData, off, 20 * i);
                                off += 20 * i;
                            }
                            break;
                        }
                    }
                    if (isMax) {
                        System.arraycopy(newData, off + 20, newData, off, 20 * size);
                        off += 20 * size;
                    }
                    writeInt(newData, off, axml.mTableStrings.find("http://schemas.android.com/apk/res/android"));
                    writeInt(newData, off + 4, idIndex);
                    writeInt(newData, off + 8, axml.mTableStrings.getSize());
                    writeInt(newData, off + 12, 0x03000008);
                    writeInt(newData, off + 16, axml.mTableStrings.getSize());
                    axml.setData(newData);
                }
                success = true;
                break;
            }
        }
        if (!success)
            throw new IOException();
        ArrayList<String> list = new ArrayList<>(axml.mTableStrings.getSize());
        axml.mTableStrings.getStrings(list);
        list.add(injectEntranceClass);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        axml.write(list, baos);
        return baos.toByteArray();
    }

    private static void writeInt(byte[] data, int off, int value) {
        data[off++] = (byte) (value & 0xFF);
        data[off++] = (byte) ((value >>> 8) & 0xFF);
        data[off++] = (byte) ((value >>> 16) & 0xFF);
        data[off] = (byte) ((value >>> 24) & 0xFF);
    }

    private static int readInt(byte[] data, int off) {
        return data[off + 3] << 24 | (data[off + 2] & 0xFF) << 16 | (data[off + 1] & 0xFF) << 8
                | data[off] & 0xFF;
    }

    private static void editConfig(DexBackedClassDef def, DexBuilder builder, String apv) throws Exception {
        String code = getClassDefString(def);
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        Properties conf = new Properties();
//        conf.setProperty("appid", "appid");
//        conf.setProperty("ver", "appVer");
//        conf.store(out, "");
//
//        String d = new String(Base64.encodeBase64(out.toByteArray()));
//        out.close();
//        code = code.replace("\"" + "Config" + "\"", "\"" + d + "\"");
        code = code.replace("\"" + "Config" + "\"", "\"" + apv + "\"");
        assembleSmaliFile(code, builder, new SmaliOptions());
    }

    private static String getClassDefString(DexBackedClassDef def) throws IOException {
        StringWriter stringWriter = new StringWriter();
        BaksmaliWriter writer = new BaksmaliWriter(stringWriter);
        ClassDefinition classDefinition = new ClassDefinition(new BaksmaliOptions(), def);
        classDefinition.writeTo(writer);
        writer.close();
        return stringWriter.toString();
    }

    private static Set<? extends DexBackedClassDef> getClassDefFromString(String string) {
        try {
            File tempFileO = File.createTempFile("smali_o" + System.currentTimeMillis(), "");
            File tempFileI = File.createTempFile("smali_i" + System.currentTimeMillis(), "");
            FileUtils.writeStringToFile(tempFileI, string, StandardCharsets.UTF_8);
            SmaliOptions so = new SmaliOptions();
            so.outputDexFile = tempFileO.getCanonicalPath();
            Smali.assemble(so, tempFileI.getCanonicalPath());
            DexBackedDexFile dexFile = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(new FileInputStream(tempFileO)));
            return dexFile.getClasses();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    public static byte[] doProcess(String oriPath, String apv, String template, boolean enhancedMode) throws Exception {
        log.info("[注入]启动全局注入");
        String dexFinalName = null;
        ZipFile oriFile = new ZipFile(oriPath);
        Set<String> classNameSet = new HashSet<>();
        Map<String, DexBuilder> dexBuilderMap = new HashMap<>();
        // 输出文件
//        String outputPath = oriPath.replace(".apk", "_injected_not_sign.apk");
        String outputPath = oriPath.replace(".tmp", "_injected_not_sign.tmp");
        // 原程序dex编译器
        DexBuilder oriTargetBuilder = new DexBuilder(Opcodes.getDefault());
//        oriTargetBuilder.setIgnoreMethodAndFieldError(true);
        DexBuilder oriFinalBuilder = new DexBuilder(Opcodes.getDefault());

        // 读入注入文件
        log.info("[注入]正在读入注入文件");
        String injectPath = PathUtils.getUserPath() + File.separator + "template" + File.separator + template + ".apk.tpl";
        File injectFile = new File(injectPath);
        // 抽取的代码编译器
//        DexBuilder injectBuider = new DexBuilder(Opcodes.getDefault());
//        injectBuider.setIgnoreMethodAndFieldError(true);

        // 分析Axml及编辑 获取activity列表
        log.info("[注入]正在分析Axml");
        ZipEntry xmlEntry = oriFile.getEntry("AndroidManifest.xml");
        byte[] xml = parseManifest(oriFile.getInputStream(xmlEntry));

        Set<String> activityTypeSet = new HashSet<>();
        log.info("[注入]是否开启增强模式：{}", enhancedMode);
        if(enhancedMode) { // 增强模式，将在每一个activity中注入daemon检测代码
            List<String> activityList = parseManifestActivity(oriFile.getInputStream(xmlEntry));
            log.info("[注入][增强模式]获取到activity数量：{}个", activityList.size());
            activityTypeSet.addAll(activityList.stream().map(QuickAccessApkUtil::classNameToTypeName).collect(Collectors.toSet()));
        }

        // 获取dex列表
        log.info("[注入]正在获取dex列表");
        List<String> dexNameList = new ArrayList<>();
        Enumeration<ZipEntry> e = oriFile.getEntries();
        while (e.hasMoreElements()) {
            ZipEntry ze = e.nextElement();
            if (ze.getName().startsWith("classes") && ze.getName().endsWith("dex")) {
                dexNameList.add(ze.getName());
            }
        }
        log.info("[注入]获取到dex数量：{}个", dexNameList.size());
        // 判断dex数量
        if (dexNameList.isEmpty()) {
            log.info("[注入]未找到dex文件");
            throw new ServiceException("未找到dex文件");
        } else {
            String targetDexName;
            if (dexNameList.size() == 1) {
                targetDexName = "classes.dex";
            } else {
                targetDexName = "classes" + dexNameList.size() + ".dex";
            }
            log.info("[注入]targetDex：{}", targetDexName);
            // 多DEX，可能存在类重复问题
            for (String dexName : dexNameList) {
                ZipEntry entry = oriFile.getEntry(dexName);
                DexBackedDexFile classes = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(oriFile.getInputStream(entry)));
                log.info("[注入]dex：{}，classes：{}", dexName, classes.getClasses().size());
                for (DexBackedClassDef cl : classes.getClasses()) {
                    classNameSet.add(cl.getType());
                    if(!dexName.equals(targetDexName)) {
                        dexBuilderMap.computeIfAbsent(dexName, k->new DexBuilder(Opcodes.getDefault()));
                        if(activityTypeSet.contains(cl.getType())) {
                            try {
                                insertCodeActivity(cl, dexBuilderMap.get(dexName), "onCreate");
                                log.info("[注入][增强模式]注入activity成功：{}", cl.getType());
                            } catch (Exception ignored) {
                                internClassDef(dexBuilderMap.get(dexName), cl);
                                log.warn("[注入][增强模式]注入activity失败：{}", cl.getType());
                            }
                        } else {
                            internClassDef(dexBuilderMap.get(dexName), cl);
                        }
                    }
                }
            }

            // 检查原包是否已被红叶注入过
            log.info("[注入]正在检查原包是否已被红叶注入过");
            for (String className : classNameSet) {
                if (className.equals(injectConfigClass)) {
                    log.info("[注入]该APK已经被红叶注入过，无法二次注入");
                    throw new ServiceException("该APK已经被红叶注入过，无法二次注入");
                }
            }

            // 分析dex
            log.info("[注入]正在分析dex");
            ZipEntry dexEntry = oriFile.getEntry(targetDexName);
            DexBackedDexFile oriTargetDex = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(oriFile.getInputStream(dexEntry)));
            DexBackedDexFile injectDex = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(Files.newInputStream(injectFile.toPath())));

            List<DexBackedClassDef> classesList = new ArrayList<>();
            classesList.addAll(oriTargetDex.getClasses()); // 不能过滤activity，会造成类丢失。.stream().filter(o -> !activityTypeSet.contains(o.getType())).collect(Collectors.toList())
            classesList.addAll(injectDex.getClasses().stream().filter(o -> !classNameSet.contains(o.getType())).collect(Collectors.toList()));

            // 注入配置
            log.info("[注入]正在注入配置");
            ClassPath injectClasspath = new ClassPath(Lists.newArrayList(new DexClassProvider(injectDex)), false, injectDex.getClasses().size());
            DexBackedClassDef injectConfigActivityClassDef = (DexBackedClassDef) injectClasspath.getClassDef(injectConfigClass);
            editConfig(injectConfigActivityClassDef, oriTargetBuilder, apv);

            // 处理自定义applicationName
            log.info("[注入]正在处理自定义applicationName");
            if (StringUtils.isNotBlank(applicationName)) {
                log.info("[注入]处理自定义applicationName：{}", applicationName);
                DexBackedClassDef injectApplicationClassDef = (DexBackedClassDef) injectClasspath.getClassDef(injectEntranceType);
                String code = getClassDefString(injectApplicationClassDef);
                if (applicationName.startsWith(".")) {
                    if (packageName == null)
                        throw new NullPointerException("Package name is null.");
                    applicationName = packageName + applicationName;
                }
                applicationName = "L" + applicationName.replaceAll("\\.", "/") + ";";
                code = code.replace("Landroid/app/Application;", applicationName);
                assembleSmaliFile(code, oriTargetBuilder, new SmaliOptions());

                // 检查被继承的类是否有final
                if(!"Landroid/app/Application;".equals(applicationName)) {
                    out: for (String dexName : dexNameList) {
                        ZipEntry entry = oriFile.getEntry(dexName);
                        DexBackedDexFile classes = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(oriFile.getInputStream(entry)));
                        oriFinalBuilder = new DexBuilder(Opcodes.getDefault());
                        for (DexBackedClassDef cl : classes.getClasses()) {
                            if (cl.getType().equals(applicationName)) {
                                dexFinalName = dexName;
                                // 0x10代表final
                                if ((cl.getAccessFlags() & 0x10) != 0) {
                                    log.info("[注入]处理isFinal：{}", true);
                                    String classDefString = getClassDefString(cl);
                                    classDefString = classDefString.replace("final " + applicationName, applicationName);
                                    if(dexFinalName.equals(targetDexName)) {
                                        assembleSmaliFile(classDefString, oriTargetBuilder, new SmaliOptions());
                                    } else {
                                        DexBuilder dexBuilder = dexBuilderMap.getOrDefault(dexFinalName, oriFinalBuilder);
                                        map.computeIfPresent(dexBuilder, (k, v) -> v).remove(cl);
                                        assembleSmaliFile(classDefString, dexBuilder, new SmaliOptions());
                                    }
                                } else {
                                    log.info("[注入]处理isFinal：{}", false);
                                    dexFinalName = null;
                                    break out;
                                }
                            } else {
                                internClassDef(oriFinalBuilder, cl);
                            }
                        }
                        if (StringUtils.isNotBlank(dexFinalName)) {
                            break;
                        }
                    }
                }
            }

            // 合并target dex
            log.info("[注入]正在合并targetDex");
            for (int i = 0; i < classesList.size(); i++) {
                DexBackedClassDef cl = classesList.get(i);
                if (!cl.getType().equals(injectConfigClass)) {
                    if (StringUtils.isNotBlank(applicationName) && cl.getType().equals(injectEntranceType)) {
                        continue; // 上方已经添加过
                    } else {
                        if(activityTypeSet.contains(cl.getType())) {
                            try {
                                insertCodeActivity(cl, oriTargetBuilder, "onCreate");
                                log.info("[注入][增强模式]注入activity成功：{}", cl.getType());
                            } catch (Exception ignored) {
                                internClassDef(oriTargetBuilder, cl);
                                log.warn("[注入][增强模式]注入activity失败：{}", cl.getType());
                            }
                        } else {
                            internClassDef(oriTargetBuilder, cl);
                        }
                    }
                }
                // 更新进度
            }

            // 重组APK
            log.info("[注入]正在重组APK");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            boolean insertFlag = true;
            try {
                addNecessaryPermission(byteArrayOutputStream, xml);
            } catch (Exception e1) {
                insertFlag = false;
                e1.printStackTrace();
            }

            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputPath));
            zos.setLevel(1);
            zos.putNextEntry("AndroidManifest.xml");
            if (insertFlag) {
                log.info("[注入]添加标记信息成功");
                zos.write(byteArrayOutputStream.toByteArray());
            } else {
                log.warn("[注入]添加标记信息失败");
                zos.write(xml);
            }
            zos.closeEntry();

            log.info("[注入]编译targetDex：{}，classes：{}", targetDexName, map.get(oriTargetBuilder).size());
//            MemoryDataStore store = new MemoryDataStore();
//            oriTargetBuilder.writeTo(store);
            zos.putNextEntry(targetDexName);
//            zos.write(store.getData());
            zos.write(toByteArray(oriTargetBuilder));
            zos.closeEntry();

            for (Map.Entry<String, DexBuilder> entry : dexBuilderMap.entrySet()) {
                log.info("[注入]编译dex：{}，classes：{}", entry.getKey(), map.get(entry.getValue()).size());
                zos.putNextEntry(entry.getKey());
                zos.write(toByteArray(entry.getValue()));
                zos.closeEntry();
            }

            if(StringUtils.isNotBlank(dexFinalName) && !dexFinalName.equals(targetDexName) && !dexBuilderMap.containsKey(dexFinalName)) {
                log.info("[注入]编译finalDex：{}，classes：{}", dexFinalName, map.get(oriFinalBuilder).size());
//                MemoryDataStore store2 = new MemoryDataStore();
//                oriFinalBuilder.writeTo(store2);
                zos.putNextEntry(dexFinalName);
//                zos.write(store2.getData());
                zos.write(toByteArray(oriFinalBuilder));
                zos.closeEntry();
            }

            log.info("[注入]正在编译最终apk");
            Enumeration<ZipEntry> enumeration = oriFile.getEntries();
            while (enumeration.hasMoreElements()) {
                ZipEntry ze = enumeration.nextElement();
                if (ze.getName().equals("AndroidManifest.xml") || ze.getName().equals(targetDexName) || ze.getName().equals(dexFinalName)) {
                    continue;
                }
                if(dexBuilderMap.containsKey(ze.getName())) {
                    continue;
                }
                zos.copyZipEntry(ze, oriFile);
                // 更新进度
            }
            zos.close();
            oriFile.close();
        }
        return FileUtils.readFileToByteArray(new File(outputPath));
    }

    public static byte[] doProcess2(String oriPath, String apv, String template, ActivityMethodVo vo, boolean enhancedMode) throws Exception {
        log.info("[注入]启动单例注入");
        String userSelectedDexName = null; //入口所在dex
        ZipFile oriFile = new ZipFile(oriPath);
        Set<String> classNameSet = new HashSet<>();
        Map<String, DexBuilder> dexBuilderMap = new HashMap<>();
        // 输出文件
//        String outputPath = oriPath.replace(".apk", "_injected_not_sign.apk");
        String outputPath = oriPath.replace(".tmp", "_injected_not_sign.tmp");
        // 原程序dex编译器
        DexBuilder oriUserSelectedBuilder = new DexBuilder(Opcodes.getDefault());
//         oriUserSelectedBuilder.setIgnoreMethodAndFieldError(true);
        DexBuilder oriTargetBuilder = new DexBuilder(Opcodes.getDefault());
//         oriTargetBuilder.setIgnoreMethodAndFieldError(true);

        // 读入注入文件
        log.info("[注入]正在读入注入文件");
        String injectPath = PathUtils.getUserPath() + File.separator + "template" + File.separator + template + ".apk.tpl";
        File injectFile = new File(injectPath);
        // 抽取的代码编译器
//         DexBuilder injectBuider = new DexBuilder(Opcodes.getDefault());
//         injectBuider.setIgnoreMethodAndFieldError(true);

        // 分析Axml及编辑 获取activity列表
        log.info("[注入]正在分析Axml");
        ZipEntry xmlEntry = oriFile.getEntry("AndroidManifest.xml");
        byte[] xml = toByteArray(oriFile.getInputStream(xmlEntry));

        Set<String> activityTypeSet = new HashSet<>();
        log.info("[注入]是否开启增强模式：{}", enhancedMode);
        if(enhancedMode) { // 增强模式，将在每一个activity中注入daemon检测代码
            List<String> activityList = parseManifestActivity(oriFile.getInputStream(xmlEntry));
            log.info("[注入][增强模式]获取到activity数量：{}个", activityList.size());
            activityTypeSet.addAll(activityList.stream().map(QuickAccessApkUtil::classNameToTypeName).collect(Collectors.toSet()));
        }

        // 获取dex列表
        log.info("[注入]正在获取dex列表");
        List<String> dexNameList = new ArrayList<>();
        Enumeration<ZipEntry> e = oriFile.getEntries();
        while (e.hasMoreElements()) {
            ZipEntry ze = e.nextElement();
            if (ze.getName().startsWith("classes") && ze.getName().endsWith("dex")) {
                dexNameList.add(ze.getName());
            }
        }
        log.info("[注入]获取到dex数量：{}个", dexNameList.size());
        // 判断dex数量
        if (dexNameList.isEmpty()) {
            log.info("[注入]未找到dex文件");
            throw new ServiceException("未找到dex文件");
        } else {
            String targetDexName;
            if (dexNameList.size() == 1) {
                targetDexName = "classes.dex";
            } else {
                targetDexName = "classes" + dexNameList.size() + ".dex";
            }
            log.info("[注入]targetDex：{}", targetDexName);
            // 计算用户选择的是哪个dex
            for (String dexName : dexNameList) {
                ZipEntry entry = oriFile.getEntry(dexName);
                DexBackedDexFile classes = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(oriFile.getInputStream(entry)));
                for (DexBackedClassDef cl : classes.getClasses()) {
                    if (cl.getType().equals(classNameToTypeName(vo.getActivity()))) {
                        userSelectedDexName = dexName;
                        log.info("[注入]userSelectedDexName：{}", userSelectedDexName);
                    }
                }
            }
            // 多DEX，可能存在类重复问题
            for (String dexName : dexNameList) {
                ZipEntry entry = oriFile.getEntry(dexName);
                DexBackedDexFile classes = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(oriFile.getInputStream(entry)));
                log.info("[注入]dex：{}，classes：{}", dexName, classes.getClasses().size());
                for (DexBackedClassDef cl : classes.getClasses()) {
                    classNameSet.add(cl.getType());
                    if(!dexName.equals(targetDexName) && !dexName.equals((userSelectedDexName))) {
                        dexBuilderMap.computeIfAbsent(dexName, k->new DexBuilder(Opcodes.getDefault()));
                        if(activityTypeSet.contains(cl.getType())) {
                            try {
                                insertCodeActivity(cl, dexBuilderMap.get(dexName), "onCreate");
                                log.info("[注入][增强模式]注入activity成功：{}", cl.getType());
                            } catch (Exception ignored) {
                                internClassDef(dexBuilderMap.get(dexName), cl);
                                log.warn("[注入][增强模式]注入activity失败：{}", cl.getType());
                            }
                        } else {
                            internClassDef(dexBuilderMap.get(dexName), cl);
                        }
                    }
                }
            }

            // 检查原包是否已被红叶注入过
            log.info("[注入]正在检查原包是否已被红叶注入过");
            for (String className : classNameSet) {
                if (className.equals(injectConfigClass)) {
                    log.info("[注入]该APK已经被红叶注入过，无法二次注入");
                    throw new ServiceException("该APK已经被红叶注入过，无法二次注入");
                }
            }

            // 分析dex
            log.info("[注入]正在分析dex");
            ZipEntry dexEntry = oriFile.getEntry(targetDexName);
            DexBackedDexFile oriTargetDex = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(oriFile.getInputStream(dexEntry)));
            DexBackedDexFile injectDex = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(Files.newInputStream(injectFile.toPath())));

            List<DexBackedClassDef> classesList = new ArrayList<>();
            classesList.addAll(oriTargetDex.getClasses());
            classesList.addAll(injectDex.getClasses().stream().filter(o -> !classNameSet.contains(o.getType())).collect(Collectors.toList()));

            // 注入配置
            log.info("[注入]正在注入配置");
            ClassPath injectClasspath = new ClassPath(Lists.newArrayList(new DexClassProvider(injectDex)), false, injectDex.getClasses().size());
            DexBackedClassDef injectConfigActivityClassDef = (DexBackedClassDef) injectClasspath.getClassDef(injectConfigClass);
            editConfig(injectConfigActivityClassDef, oriTargetBuilder, apv);

            // 合并target dex
            log.info("[注入]正在合并targetDex");
            for (int i = 0; i < classesList.size(); i++) {
                DexBackedClassDef cl = classesList.get(i);
                if (!cl.getType().equals(injectConfigClass)) {
                    if(cl.getType().equals(classNameToTypeName(vo.getActivity()))) { // 假设用户所选dex为target dex
                        insertCode(cl, oriTargetBuilder, vo.getMethod());
                    } else {
                        if(activityTypeSet.contains(cl.getType())) {
                            try {
                                insertCodeActivity(cl, oriTargetBuilder, "onCreate");
                                log.info("[注入][增强模式]注入activity成功：{}", cl.getType());
                            } catch (Exception ignored) {
                                internClassDef(oriTargetBuilder, cl);
                                log.warn("[注入][增强模式]注入activity失败：{}", cl.getType());
                            }
                        } else {
                            internClassDef(oriTargetBuilder, cl);
                        }
                    }
                }
                // 更新进度
            }

            // 重组APK
            log.info("[注入]正在重组APK");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            boolean insertFlag = true;
            try {
                addNecessaryPermission(byteArrayOutputStream, xml);
            } catch (Exception e1) {
                insertFlag = false;
                e1.printStackTrace();
            }

            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputPath));
            zos.setLevel(1);
            zos.putNextEntry("AndroidManifest.xml");
            if (insertFlag) {
                log.info("[注入]添加标记信息成功");
                zos.write(byteArrayOutputStream.toByteArray());
            } else {
                log.warn("[注入]添加标记信息失败");
                zos.write(xml);
            }
            zos.closeEntry();

            log.info("[注入]编译targetDex：{}，classes：{}", targetDexName, map.get(oriTargetBuilder).size());
    //        MemoryDataStore store = new MemoryDataStore();
    //        oriMainBuilder.writeTo(store);
            zos.putNextEntry(targetDexName);
    //        zos.write(store.getData());
            zos.write(toByteArray(oriTargetBuilder));
            zos.closeEntry();

            for (Map.Entry<String, DexBuilder> entry : dexBuilderMap.entrySet()) {
                log.info("[注入]编译dex：{}，classes：{}", entry.getKey(), map.get(entry.getValue()).size());
                zos.putNextEntry(entry.getKey());
                zos.write(toByteArray(entry.getValue()));
                zos.closeEntry();
            }

            if (!targetDexName.equals(userSelectedDexName) && !dexBuilderMap.containsKey(userSelectedDexName)) {
                // 合并dex
                DexBackedDexFile userSelectedDex = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(new BufferedInputStream(oriFile.getInputStream(oriFile.getEntry(userSelectedDexName)))));
                List<DexBackedClassDef> classesSelectedList = new ArrayList<>(userSelectedDex.getClasses());
                for (int i = 0; i < classesSelectedList.size(); i++) {
                    DexBackedClassDef cl = classesSelectedList.get(i);
                    if (!cl.getType().equals(injectConfigClass)) {
                        if(cl.getType().equals(classNameToTypeName(vo.getActivity()))) {
                            insertCode(cl, oriUserSelectedBuilder, vo.getMethod());
                        } else {
                            if(activityTypeSet.contains(cl.getType())) {
                                try {
                                    insertCodeActivity(cl, oriUserSelectedBuilder, "onCreate");
                                    log.info("[注入][增强模式]注入activity成功：{}", cl.getType());
                                } catch (Exception ignored) {
                                    internClassDef(oriUserSelectedBuilder, cl);
                                    log.warn("[注入][增强模式]注入activity失败：{}", cl.getType());
                                }
                            } else {
                                internClassDef(oriUserSelectedBuilder, cl);
                            }
                        }
                    }
                    // 更新进度
                }

                log.info("[注入]编译userSelectedDex：{}，classes：{}", userSelectedDexName, map.get(oriUserSelectedBuilder).size());
//                MemoryDataStore store2 = new MemoryDataStore();
//                oriTargetBuilder.writeTo(store2);
                zos.putNextEntry(userSelectedDexName);
//                zos.write(store2.getData());
                zos.write(toByteArray(oriUserSelectedBuilder));
                zos.closeEntry();
            }

            log.info("[注入]正在编译最终apk");
            Enumeration<ZipEntry> enumeration = oriFile.getEntries();
            while (enumeration.hasMoreElements()) {
                ZipEntry ze = enumeration.nextElement();
                if (ze.getName().equals("AndroidManifest.xml") || ze.getName().equals(targetDexName) || ze.getName().equals(userSelectedDexName)) {
                    continue;
                }
                if(dexBuilderMap.containsKey(ze.getName())) {
                    continue;
                }
                zos.copyZipEntry(ze, oriFile);
                // 更新进度
            }
            zos.close();
            oriFile.close();
        }
        return FileUtils.readFileToByteArray(new File(outputPath));
    }

    private static void addNecessaryPermission(ByteArrayOutputStream byteArrayOutputStream, byte[] xml) throws Exception {
        AXMLDoc doc = new AXMLDoc();
        doc.parse(new ByteArrayInputStream(xml));

        try {
            PermissionEditor permissionEditor = new PermissionEditor(doc);
            permissionEditor.setEditorInfo(new PermissionEditor.EditorInfo()
                    .with(new PermissionEditor.PermissionOpera("android.permission.本验证由红叶网络验证提供").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.QQ群.947144396").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.COORDSOFT").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.HY_COORDSOFT_COM").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.INTERNET").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.ACCESS_NETWORK_STATE").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.ACCESS_WIFI_STATE").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.READ_PHONE_STATE").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.READ_EXTERNAL_STORAGE").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.WRITE_EXTERNAL_STORAGE").add())
                    .with(new PermissionEditor.PermissionOpera("android.permission.REQUEST_INSTALL_PACKAGES").add())
            );
            permissionEditor.commit();
        } catch (Exception e) {
            log.warn("[注入]添加权限失败", e);
        }

        try {
            ApplicationInfoEditor applicationInfoEditor1 = new ApplicationInfoEditor(doc);
            applicationInfoEditor1.setEditorInfo(new ApplicationInfoEditor.EditorInfo(TypedValue.TYPE_INT_BOOLEAN, "requestLegacyExternalStorage", "true"));
            applicationInfoEditor1.commit();

            ApplicationInfoEditor applicationInfoEditor2 = new ApplicationInfoEditor(doc);
            applicationInfoEditor2.setEditorInfo(new ApplicationInfoEditor.EditorInfo(TypedValue.TYPE_INT_BOOLEAN, "usesCleartextTraffic", "true", 1));
            applicationInfoEditor2.commit();
        } catch (Exception ignored) {
            log.warn("[注入]添加application属性失败");
        }

        doc.build(byteArrayOutputStream);
        doc.release();
//        doc.print();
    }

    private static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    //插入代码
    private static void insertCode(DexBackedClassDef def, DexBuilder builder, String method) throws Exception {
        String code = getClassDefString(def);
        String rgex = ".method(.*)" + method + "\\(([\\s\\S]*?)end method{0,1}";
        Pattern p = Pattern.compile(rgex);
        Matcher m = p.matcher(code);
        String r = null;
        while (m.find()) {
            r = m.group();
        }
        if (r == null) {
            throw new Exception("没有找到函数头");
        }
        File insertCodeFile = PathUtils.getResourceFile("insertCode.txt");
        String insertCode = FileUtils.readFileToString(insertCodeFile, StandardCharsets.UTF_8);
        String buff2 = r.replace("return", insertCode + "\nreturn");
        code = code.replace(r, buff2);
        assembleSmaliFile(editRegister(code), builder, new SmaliOptions());
    }

    private static void insertCodeActivity(DexBackedClassDef def, DexBuilder builder, String method) throws Exception {
        String code = getClassDefString(def);
        String rgex = ".method(.*)" + method + "\\(([\\s\\S]*?)end method{0,1}";
        Pattern p = Pattern.compile(rgex);
        Matcher m = p.matcher(code);
        String r = null;
        while (m.find()) {
            r = m.group();
        }
        if (r == null) {
            throw new Exception("没有找到函数头");
        }
        File insertCodeFile = PathUtils.getResourceFile("insertCodeActivity.txt");
        String insertCode = FileUtils.readFileToString(insertCodeFile, StandardCharsets.UTF_8);
        String buff2 = r.replace("return", insertCode + "\nreturn");
        code = code.replace(r, buff2);
        assembleSmaliFile(editRegister(code), builder, new SmaliOptions());
    }

    //修改reg
    private static String editRegister(String buff) throws Exception {
        String rgex = ".registers [0-9]{1,2}";
        Pattern p = Pattern.compile(rgex);
        Matcher m = p.matcher(buff);
        String r = null;
        while (m.find()) {
            r = m.group();
        }
        if (r == null) {
            throw new Exception("没有找到registers");
        }
        int reg = Integer.parseInt(r.replace(".registers ", ""));
        while (true) {
            if (reg < 12) {
                reg++;
            } else {
                break;
            }
        }
        buff = buff.replace(r, ".registers " + reg);
        return buff;
    }

    private static List<String> parseManifestActivity(InputStream is) throws IOException {
        String packageName = null;
        List<String> list = new ArrayList<>();
        AXmlDecoder axml = AXmlDecoder.decode(is);
        AXmlResourceParser parser = new AXmlResourceParser();
        parser.open(new ByteArrayInputStream(axml.getData()), axml.mTableStrings);
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
            if (type != XmlPullParser.START_TAG)
                continue;
            if (parser.getName().equals("manifest")) {
                int size = parser.getAttributeCount();
                for (int i = 0; i < size; ++i) {
                    if (parser.getAttributeName(i).equals("package")) {
                        packageName = parser.getAttributeValue(i);
                    }
                }
            } else if (parser.getName().equals("activity")) {
                int size = parser.getAttributeCount();
                for (int i = 0; i < size; ++i) {
                    if (parser.getAttributeNameResource(i) == 0x01010003) {
                        String name = parser.getAttributeValue(i);
                        if (name.startsWith(".")) {
                            name = packageName + name;
                        }
                        if(!list.contains(name)) {
                            list.add(name);
                        }
                    }
                }
            }
        }
        return list.stream().distinct().collect(Collectors.toList());
    }

    public static List<String> parseManifestActivity(String oriPath) throws IOException {
        ZipFile oriFile = new ZipFile(oriPath);
        ZipEntry XmlEntry = oriFile.getEntry("AndroidManifest.xml");
        return parseManifestActivity(oriFile.getInputStream(XmlEntry));
    }

    public static List<String> parserMethod(String oriPath, String activity) throws IOException {
        ZipFile oriFile = new ZipFile(oriPath);
        // 获取dex列表
        List<String> dexNameList = new ArrayList<>();
        Enumeration<ZipEntry> e = oriFile.getEntries();
        while (e.hasMoreElements()) {
            ZipEntry ze = e.nextElement();
            if (ze.isDirectory()) {
                continue;
            } else if (ze.getName().startsWith("classes") && ze.getName().endsWith("dex")) {
                dexNameList.add(ze.getName());
            }
        }
        // 判断dex数量
        if (dexNameList.size() == 0) {
            System.out.println("未找到dex文件");
            throw new ServiceException("未找到dex文件");
        } else {
            DexBackedDexFile cl;
            List<String> methodList = new ArrayList<>();
            for (final String dexFile : dexNameList) {
                cl = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(new BufferedInputStream(oriFile.getInputStream(oriFile.getEntry(dexFile)))));
                for (DexBackedClassDef h : cl.getClasses()) {
                    if (h.getType().equals(classNameToTypeName(activity))) {
                        for (DexBackedMethod m : h.getMethods()) {
                            if(!methodList.contains(m.getName())) {
                                methodList.add(m.getName());
                            }
                        }
                        break;
                    }
                }
            }
            return methodList.stream().distinct().collect(Collectors.toList());
        }
    }

    private static void writeToDexFile(String filePath, List<ClassDef> classes, int opCode) throws IOException {
        DexFileFactory.writeDexFile(filePath, new DexFile() {
            @Nonnull
            @Override
            public Set<? extends ClassDef> getClasses() {
                return new AbstractSet<ClassDef>() {
                    @Nonnull
                    @Override
                    public Iterator<ClassDef> iterator() {
                        return classes.iterator();
                    }

                    @Override
                    public int size() {
                        return classes.size();
                    }
                };
            }

            @Nonnull
            @Override
            public Opcodes getOpcodes() {
                return Opcodes.forApi(opCode);
            }
        });
    }

    private static final Map<DexBuilder, Set<ClassDef>> map = new HashMap<>();
    private static void assembleSmaliFile(String code, DexBuilder builder, SmaliOptions options) {
        Set<? extends DexBackedClassDef> classDefSet = getClassDefFromString(code);
        map.computeIfAbsent(builder, k->new HashSet<>()).addAll(classDefSet);
    }

    private static void internClassDef(DexBuilder builder, ClassDef classDef) {
        map.computeIfAbsent(builder, k->new HashSet<>()).add(classDef);
    }

    private static byte[] toByteArray(DexBuilder builder) throws IOException {
        File tempFile = File.createTempFile("dex" + System.currentTimeMillis(), null);
        QuickAccessApkUtil.writeToDexFile(tempFile.getCanonicalPath(), new ArrayList<>(map.get(builder)), Opcodes.getDefault().api);
        return toByteArray(new BufferedInputStream(new FileInputStream(tempFile)));
    }

    public static byte[] doProcess3(String apv, String template) {
        try {
            DexBuilder targetBuilder = new DexBuilder(Opcodes.getDefault());
            // 读入注入文件
            String injectPath = PathUtils.getUserPath() + File.separator + "template" + File.separator + template + ".apk.tpl";
            File injectFile = new File(injectPath);

            // 分析dex
            DexBackedDexFile injectDex = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(new FileInputStream(injectFile)));

            // 注入配置
            ClassPath injectClasspath = new ClassPath(Lists.newArrayList(new DexClassProvider(injectDex)), false, injectDex.getClasses().size());
            DexBackedClassDef injectConfigActivityClassDef = (DexBackedClassDef) injectClasspath.getClassDef(injectConfigClass);
            editConfig(injectConfigActivityClassDef, targetBuilder, apv);

            Set<DexBackedClassDef> classesTargetSet = new HashSet<>(injectDex.getClasses());
            List<DexBackedClassDef> classesTargetList = new ArrayList<>(classesTargetSet);
            for (int i = 0; i < classesTargetList.size(); i++) {
                DexBackedClassDef cl = classesTargetList.get(i);
                internClassDef(targetBuilder, cl);
                // 更新进度
            }
            return toByteArray(targetBuilder);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    private static String classNameToTypeName(String className) {
        return "L" + className.replaceAll("\\.", "/") + ";";
    }
}
