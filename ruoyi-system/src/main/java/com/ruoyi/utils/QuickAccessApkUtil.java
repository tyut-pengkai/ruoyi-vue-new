package com.ruoyi.utils;

import com.google.common.collect.Lists;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.PathUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.decode.AXMLDoc;
import com.ruoyi.common.utils.apk.apkeditor.axmleditor.editor.PermissionEditor;
import com.ruoyi.common.utils.apk.xml.decode.AXmlDecoder;
import com.ruoyi.common.utils.apk.xml.decode.AXmlResourceParser;
import com.ruoyi.common.utils.apk.xml.decode.XmlPullParser;
import com.ruoyi.common.utils.apk.zip.ZipEntry;
import com.ruoyi.common.utils.apk.zip.ZipFile;
import com.ruoyi.common.utils.apk.zip.ZipOutputStream;
import com.ruoyi.system.domain.vo.ActivityMethodVo;
import org.apache.commons.io.FileUtils;
import org.jf.baksmali.Adaptors.ClassDefinition;
import org.jf.baksmali.BaksmaliOptions;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.analysis.ClassPath;
import org.jf.dexlib2.analysis.DexClassProvider;
import org.jf.dexlib2.dexbacked.DexBackedClassDef;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexBackedMethod;
import org.jf.dexlib2.writer.builder.DexBuilder;
import org.jf.dexlib2.writer.io.MemoryDataStore;
import org.jf.smali.Smali;
import org.jf.smali.SmaliOptions;
import org.jf.util.IndentingWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QuickAccessApkUtil {

    //    private static final String injectConfigActivity = "Lcom/c/qat/GlobalConfig;";
//    private static final String injectApplication = "Lcom/App;";
//    private static final String injectApplicationPath = "com.App";
    private static final String injectConfigActivity = "Lcom/c/qat/InjectConfig;";
    private static final String injectApplicationPath = "com.App";
    private static final String injectApplication = "L" + injectApplicationPath.replaceAll("\\.", "/") + ";";
    private static String packageName;
    private static String applicationName;
    private static String dexFinalName;
    private static String mainDex; //入口所在dex

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
        list.add(injectApplicationPath);
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
        Smali.assembleSmaliFile(code, builder, new SmaliOptions());
    }

    private static String getClassDefString(DexBackedClassDef def) throws IOException {
        StringWriter stringWriter = new StringWriter();
        IndentingWriter writer = new IndentingWriter(stringWriter);
        ClassDefinition classDefinition = new ClassDefinition(new BaksmaliOptions(), def);
        classDefinition.writeTo(writer);
        writer.close();
        return stringWriter.toString();
    }

    public static byte[] doProcess(String oriPath, String apv, String template) throws Exception {
        ZipFile oriFile = new ZipFile(oriPath);
        Set<String> classNameSet = new HashSet<>();
        // 输出文件
//            String outputPath = oriPath.replace(".apk", "_injected_not_sign.apk");
        String outputPath = oriPath.replace(".tmp", "_injected_not_sign.tmp");
        // 原程序dex编译器
        DexBuilder oriBuilder = new DexBuilder(Opcodes.getDefault());
//        oriBuilder.setIgnoreMethodAndFieldError(true);
        DexBuilder oriFinalBuilder = new DexBuilder(Opcodes.getDefault());
        // 读入注入文件
//            String injectPath = PathUtils.getUserPath() + File.separator + "src/test/resources" + File.separator + "data.dat";
        String injectPath = PathUtils.getUserPath() + File.separator + "template" + File.separator + template + ".apk.tpl";
        File injectFile = new File(injectPath);
        // 抽取的代码编译器
        DexBuilder injectBuider = new DexBuilder(Opcodes.getDefault());
//            injectBuider.setIgnoreMethodAndFieldError(true);
        // 获取dex列表
        List<String> dexNameList = new ArrayList<>();
        Enumeration<ZipEntry> e = oriFile.getEntries();
        while (e.hasMoreElements()) {
            ZipEntry ze = e.nextElement();
            if (ze.getName().startsWith("classes") && ze.getName().endsWith("dex")) {
                dexNameList.add(ze.getName());
            }
        }
        // 判断dex数量
        if (dexNameList.size() == 0) {
            System.out.println("未找到dex文件");
            throw new ServiceException("未找到dex文件");
        } else {
            String targetDexName;
            if (dexNameList.size() == 1) {
                targetDexName = "classes.dex";
            } else {
                targetDexName = "classes" + dexNameList.size() + ".dex";
                // 多DEX，可能存在类重复问题
                for (String dexName : dexNameList) {
                    if(!dexName.equals(targetDexName)) {
                        ZipEntry entry = oriFile.getEntry(dexName);
                        DexBackedDexFile classes = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(oriFile.getInputStream(entry)));
                        for (DexBackedClassDef cl : classes.getClasses()) {
                            classNameSet.add(cl.getType());
                        }
                    }
                }
            }
            // 分析dex
            ZipEntry dexEntry = oriFile.getEntry(targetDexName);
            DexBackedDexFile oriClasses = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(oriFile.getInputStream(dexEntry)));
            DexBackedDexFile injectClasses = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(new FileInputStream(injectFile)));
            Set<DexBackedClassDef> classesSet = new HashSet<>();
//                Set<String> classesNameSet = new HashSet<>();
//                for (DexBackedClassDef item : oriClasses.getClasses()) {
////                    System.out.println(item.getType());
//                    classesNameSet.add(item.getType());
//                }
//                System.out.println(classesNameSet.size());
//                for (DexBackedClassDef item : injectClasses.getClasses()) {
////                    System.out.println(item.getType());
//                    classesNameSet.add(item.getType());
//                }
//                System.out.println(classesNameSet.size());

            // 检查原包是否已被红叶注入过
            for (DexBackedClassDef aClass : oriClasses.getClasses()) {
                if (aClass.getType().equals(injectConfigActivity)) {
                    throw new ServiceException("该APK已经被红叶注入过，无法二次注入");
                }
            }

            classesSet.addAll(oriClasses.getClasses());
            classesSet.addAll(injectClasses.getClasses().stream().filter(o -> !classNameSet.contains(o.getType())).collect(Collectors.toList()));
//                System.out.println(classesSet.size());
            List<DexBackedClassDef> classesList = new ArrayList<>(classesSet);

            // 分析Axml及编辑
            ZipEntry XmlEntry = oriFile.getEntry("AndroidManifest.xml");
            byte[] xml = parseManifest(oriFile.getInputStream(XmlEntry));

            // 注入配置
            ClassPath injectClasspath = new ClassPath(Lists.newArrayList(new DexClassProvider(injectClasses)), false, injectClasses.getClasses().size());
            DexBackedClassDef injectConfigActivityClassDef = (DexBackedClassDef) injectClasspath.getClassDef(injectConfigActivity);
            editConfig(injectConfigActivityClassDef, oriBuilder, apv);

            // 处理自定义applicationName
            if (StringUtils.isNotBlank(applicationName)) {
                DexBackedClassDef injectApplicationClassDef = (DexBackedClassDef) injectClasspath.getClassDef(injectApplication);
                String code = getClassDefString(injectApplicationClassDef);
                if (applicationName.startsWith(".")) {
                    if (packageName == null)
                        throw new NullPointerException("Package name is null.");
                    applicationName = packageName + applicationName;
                }
                applicationName = "L" + applicationName.replace('.', '/') + ";";
                code = code.replace("Landroid/app/Application;", applicationName);
                Smali.assembleSmaliFile(code, oriBuilder, new SmaliOptions());

                // 检查被继承的类是否有final
                if(!"Landroid/app/Application;".equals(applicationName)) {
                    out: for (String dexName : dexNameList) {
                        ZipEntry entry = oriFile.getEntry(dexName);
                        DexBackedDexFile classes = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(oriFile.getInputStream(entry)));
                        oriFinalBuilder = new DexBuilder(Opcodes.getDefault());
                        for (DexBackedClassDef cl : classes.getClasses()) {
                            if (cl.getType().equals(applicationName)) {
                                dexFinalName = dexName;
                                System.out.println("applicationName:" + applicationName);
                                System.out.println("AccessFlags: " + cl.getAccessFlags());
                                // 0x10代表final
                                if ((cl.getAccessFlags() & 0x10) != 0) {
                                    System.out.println("isFinal: true");
                                    String classDefString = getClassDefString(cl);
                                    classDefString = classDefString.replace("final " + applicationName, applicationName);
                                    Smali.assembleSmaliFile(classDefString, oriFinalBuilder, new SmaliOptions());
                                } else {
                                    System.out.println("isFinal: false");
                                    dexFinalName = null;
                                    break out;
                                }
                            } else {
                                oriFinalBuilder.internClassDef(cl);
                            }
                        }
                        if (StringUtils.isNotBlank(dexFinalName)) {
                            break;
                        }
                    }
                }
            }

            // 合并dex
            for (int i = 0; i < classesList.size(); i++) {
                DexBackedClassDef cl = classesList.get(i);
                if (!cl.getType().equals(injectConfigActivity)) {
                    if (StringUtils.isNotBlank(applicationName) && cl.getType().equals(injectApplication)) {
                        continue;
                    } else {
                        oriBuilder.internClassDef(cl);
                    }
                }
                // 更新进度
            }

            // 重组APK
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            boolean insertFlag = true;
            try {
                AXMLDoc doc = new AXMLDoc();
                doc.parse(new ByteArrayInputStream(xml));
                PermissionEditor permissionEditor = new PermissionEditor(doc);
                permissionEditor.setEditorInfo(new PermissionEditor.EditorInfo()
                        .with(new PermissionEditor.PermissionOpera("android.permission.本验证由红叶网络验证提供").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.QQ群.947144396").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.COORDSOFT").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.INTERNET").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.ACCESS_NETWORK_STATE").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.ACCESS_WIFI_STATE").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.READ_PHONE_STATE").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.WRITE_EXTERNAL_STORAGE").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.REQUEST_INSTALL_PACKAGES").add())
                );
                permissionEditor.commit();
                doc.build(byteArrayOutputStream);
                doc.release();
            } catch (Exception e1) {
                insertFlag = false;
                e1.printStackTrace();
            }
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputPath));
//            zos.setLevel(1);
            zos.putNextEntry("AndroidManifest.xml");
            if (insertFlag) {
                zos.write(byteArrayOutputStream.toByteArray());
            } else {
                zos.write(xml);
            }
            zos.closeEntry();

            MemoryDataStore store = new MemoryDataStore();
            oriBuilder.writeTo(store);
            zos.putNextEntry(targetDexName);
            zos.write(Arrays.copyOf(store.getBufferData(), store.getSize()));
            zos.closeEntry();

            if(StringUtils.isNotBlank(dexFinalName)) {
                MemoryDataStore store2 = new MemoryDataStore();
                oriFinalBuilder.writeTo(store2);
                zos.putNextEntry(dexFinalName);
                zos.write(Arrays.copyOf(store2.getBufferData(), store2.getSize()));
                zos.closeEntry();
            }

            Enumeration<ZipEntry> enumeration = oriFile.getEntries();
            while (enumeration.hasMoreElements()) {
                ZipEntry ze = enumeration.nextElement();
                if (ze.getName().equals("AndroidManifest.xml") || ze.getName().equals(targetDexName) || ze.getName().equals(dexFinalName))
                    continue;
                zos.copyZipEntry(ze, oriFile);
                // 更新进度
            }
            zos.close();
            oriFile.close();
        }
        return FileUtils.readFileToByteArray(new File(outputPath));
    }

    public static byte[] doProcess2(String oriPath, String apv, String template, ActivityMethodVo vo) {
        try {
            ZipFile oriFile = new ZipFile(oriPath);
            Set<String> classNameSet = new HashSet<>();
            // 输出文件
//            String outputPath = oriPath.replace(".apk", "_injected_not_sign.apk");
            String outputPath = oriPath.replace(".tmp", "_injected_not_sign.tmp");
            // 原程序dex编译器
            DexBuilder oriBuilder = new DexBuilder(Opcodes.getDefault());
            oriBuilder.setIgnoreMethodAndFieldError(true);
            DexBuilder oriBuilder2 = new DexBuilder(Opcodes.getDefault());
            oriBuilder.setIgnoreMethodAndFieldError(true);
            // 读入注入文件
//            String injectPath = PathUtils.getUserPath() + File.separator + "src/test/resources" + File.separator + "data.dat";
            String injectPath = PathUtils.getUserPath() + File.separator + "template" + File.separator + template + ".apk.tpl";
            File injectFile = new File(injectPath);
            // 抽取的代码编译器
//            DexBuilder injectBuider = new DexBuilder(Opcodes.getDefault());
//            injectBuider.setIgnoreMethodAndFieldError(true);

            // 获取dex列表
            List<String> dexNameList = new ArrayList<>();
            Enumeration<ZipEntry> e = oriFile.getEntries();
            while (e.hasMoreElements()) {
                ZipEntry ze = e.nextElement();
                if (ze.getName().startsWith("classes") && ze.getName().endsWith("dex")) {
                    dexNameList.add(ze.getName());
                }
            }
            // 判断dex数量
            String targetDexName;
            if (dexNameList.size() == 0) {
                System.out.println("未找到dex文件");
                throw new ServiceException("未找到dex文件");
            } else {
                if (dexNameList.size() == 1) {
                    targetDexName = "classes.dex";
                } else {
                    targetDexName = "classes" + dexNameList.size() + ".dex";
                    // 多DEX，可能存在类重复问题
                    for (String dexName : dexNameList) {
                        if(!dexName.equals(targetDexName)) {
                            ZipEntry entry = oriFile.getEntry(dexName);
                            DexBackedDexFile classes = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(oriFile.getInputStream(entry)));
                            for (DexBackedClassDef cl : classes.getClasses()) {
                                classNameSet.add(cl.getType());
                            }
                        }
                    }
                }
            }

            // 分析dex
            DexBackedDexFile oriClasses = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(new BufferedInputStream(oriFile.getInputStream(oriFile.getEntry(mainDex)))));
            DexBackedDexFile injectClasses = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(new FileInputStream(injectFile)));
            Set<DexBackedClassDef> classesSet = new HashSet<>();

            // 检查原包是否已被红叶注入过
            DexBackedDexFile oriClasses2 = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(new BufferedInputStream(oriFile.getInputStream(oriFile.getEntry(targetDexName)))));
            for (DexBackedClassDef aClass : oriClasses2.getClasses()) {
                if (aClass.getType().equals(injectConfigActivity)) {
                    throw new ServiceException("该APK已经被红叶注入过，无法二次注入");
                }
            }

            classesSet.addAll(oriClasses.getClasses());
            if (targetDexName.equals(mainDex)) {
                classesSet.addAll(injectClasses.getClasses().stream().filter(o -> !classNameSet.contains(o.getType())).collect(Collectors.toList()));
            }
            List<DexBackedClassDef> classesList = new ArrayList<>(classesSet);

            // 插入代码
            ClassPath oriClasspath = new ClassPath(Lists.newArrayList(new DexClassProvider(oriClasses)), false, oriClasses.getClasses().size());
            DexBackedClassDef oriActivityClassDef = (DexBackedClassDef) oriClasspath.getClassDef("L" + vo.getActivity().replace(".", "/") + ";");
            insertCode(oriActivityClassDef, oriBuilder, vo.getMethod());

            // 注入配置
            ClassPath injectClasspath = new ClassPath(Lists.newArrayList(new DexClassProvider(injectClasses)), false, injectClasses.getClasses().size());
            DexBackedClassDef injectConfigActivityClassDef = (DexBackedClassDef) injectClasspath.getClassDef(injectConfigActivity);
            editConfig(injectConfigActivityClassDef, oriBuilder, apv);

            // 合并dex
            for (int i = 0; i < classesList.size(); i++) {
                DexBackedClassDef cl = classesList.get(i);
                if (!cl.getType().equals("L" + vo.getActivity().replace(".", "/") + ";")
                        && !cl.getType().equals(injectConfigActivity)) {
                    oriBuilder.internClassDef(cl);
                }
                // 更新进度
            }
            System.out.println("getMethodReferences.size(): " + oriBuilder.getMethodReferences().size());

            // 重组APK
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            boolean insertFlag = false;
            boolean insertFlag = true;
            try {
                AXMLDoc doc = new AXMLDoc();
                doc.parse(new ByteArrayInputStream(toByteArray(oriFile.getInputStream(oriFile.getEntry("AndroidManifest.xml")))));
//            doc.parse(oriFile.getInputStream(oriFile.getEntry("AndroidManifest.xml")));
                PermissionEditor permissionEditor = new PermissionEditor(doc);
                permissionEditor.setEditorInfo(new PermissionEditor.EditorInfo()
                        .with(new PermissionEditor.PermissionOpera("android.permission.本验证由红叶网络验证提供").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.QQ群.947144396").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.COORDSOFT").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.INTERNET").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.ACCESS_NETWORK_STATE").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.ACCESS_WIFI_STATE").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.READ_PHONE_STATE").add())
                        .with(new PermissionEditor.PermissionOpera("android.permission.WRITE_EXTERNAL_STORAGE").add())
                );
                permissionEditor.commit();
                doc.build(byteArrayOutputStream);
                doc.release();
            } catch (Exception e1) {
                insertFlag = false;
                e1.printStackTrace();
            }
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputPath));
            zos.setLevel(1);

            if (insertFlag) {
                zos.putNextEntry("AndroidManifest.xml");
                zos.write(byteArrayOutputStream.toByteArray());
                zos.closeEntry();
            }

            MemoryDataStore store = new MemoryDataStore();
            oriBuilder.writeTo(store);
            zos.putNextEntry(mainDex);
            zos.write(Arrays.copyOf(store.getBufferData(), store.getSize()));
            zos.closeEntry();

            if (!targetDexName.equals(mainDex)) {
//                DexBackedDexFile oriClasses2 = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(new BufferedInputStream(oriFile.getInputStream(oriFile.getEntry(targetDexName)))));
                Set<DexBackedClassDef> classesSet2 = new HashSet<>();
                classesSet2.addAll(oriClasses2.getClasses());
                classesSet2.addAll(injectClasses.getClasses().stream().filter(o -> !classNameSet.contains(o.getType())).collect(Collectors.toList()));
                List<DexBackedClassDef> classesList2 = new ArrayList<>(classesSet2);
                for (int i = 0; i < classesList2.size(); i++) {
                    DexBackedClassDef cl = classesList2.get(i);
                    oriBuilder2.internClassDef(cl);
                    // 更新进度
                }
                MemoryDataStore store2 = new MemoryDataStore();
                oriBuilder2.writeTo(store2);
                zos.putNextEntry(targetDexName);
                zos.write(Arrays.copyOf(store2.getBufferData(), store2.getSize()));
                zos.closeEntry();
            }

            Enumeration<ZipEntry> enumeration = oriFile.getEntries();
            while (enumeration.hasMoreElements()) {
                ZipEntry ze = enumeration.nextElement();
                if (ze.getName().equals("AndroidManifest.xml") && insertFlag || ze.getName().equals(mainDex) || ze.getName().equals(targetDexName))
                    continue;
                zos.copyZipEntry(ze, oriFile);
                // 更新进度
            }
            zos.close();
            oriFile.close();
            return FileUtils.readFileToByteArray(new File(outputPath));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
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
        StringWriter stringWriter = new StringWriter();
        IndentingWriter writer = new IndentingWriter(stringWriter);
        ClassDefinition classDefinition = new ClassDefinition(new BaksmaliOptions(), def);
        classDefinition.writeTo(writer);
        writer.close();
        String code = stringWriter.toString();
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
        assert insertCodeFile != null;
        String insertCode = FileUtils.readFileToString(insertCodeFile, StandardCharsets.UTF_8);
        String buff2 = r.replace("return", insertCode + "\nreturn");
        code = code.replace(r, buff2);
        Smali.assembleSmaliFile(editRegister(code), builder, new SmaliOptions());
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
                        list.add(name);
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
//                if (mainDex != null) {
//                    break;
//                }
                cl = DexBackedDexFile.fromInputStream(Opcodes.getDefault(), new BufferedInputStream(new BufferedInputStream(oriFile.getInputStream(oriFile.getEntry(dexFile)))));
                for (DexBackedClassDef h : cl.getClasses()) {
                    if (h.getType().equals("L" + activity.replace(".", "/") + ";")) {
                        mainDex = dexFile;
                        for (DexBackedMethod m : h.getMethods()) {
                            methodList.add(m.getName());
                        }
                        break;
                    }
                }
            }
            return methodList.stream().distinct().collect(Collectors.toList());
        }
    }

}
