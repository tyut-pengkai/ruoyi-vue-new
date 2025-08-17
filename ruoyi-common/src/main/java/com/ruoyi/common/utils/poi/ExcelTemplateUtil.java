package com.ruoyi.common.utils.poi;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LH
 * @date 2019/3/20 13:27
 **/
@SuppressWarnings("all")
public class ExcelTemplateUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    private static final String HIDE = "$hide$";

    private static final String COMMON_REGEX = "^(.*)\\$\\{(.+)\\}(.*)$";
    /**
     * for 循环表达式
     */
    private static final String FOREACH_REGEX = "^\\$\\{(.+\\[.+\\])\\}$";
    /**
     * for 循环表达式中的字段
     */
    private static final String FOREACH_REGEX_FIELD_NAME = "^\\$\\{(.+)\\[.+\\]\\}$";
    private static final String ARRAY_REGEX = "^.+\\[(.+)\\]$";

    /**
     * 导出excel
     *
     * @param data     数据源
     * @param template 模板输入流
     * @param out      excel 输出
     * @throws Exception
     */
    public static <T> void export(T data, InputStream template, OutputStream out) throws Exception {
        export(data, template, out, null);
    }

    /**
     * 使用4个参数进行导出
     *
     * @param data        数据源
     * @param template    模板输入流
     * @param out         输出流
     * @param datePattern 数据格式
     * @throws Exception
     */
    public static <T> void export(T data, InputStream template, OutputStream out, String datePattern) throws Exception {
        export(data, template, out, datePattern, 0, null);
    }


    /**
     * 导出excel (用于导出不规则数据)
     * 支持表达式: ${name}， ${list[name]}, 表达式全部在模板中配置
     * ${name} ： 数据源中属性名存在name
     * ${list[name]} : 数据源对象中存在属性list（List），并且列表对象中存在属性name
     *
     * @param data         数据源
     * @param template     模板输入流
     * @param out          输出
     * @param datePattern  日期格式化样式
     * @param sheetNumber  sheet编号
     * @param mergeRegions 合并单元格
     * @throws Exception
     */
    public static <T> void export(T data, InputStream template, OutputStream out, String datePattern, Integer sheetNumber, List<ExcelMergeRegion> mergeRegions) throws Exception {
        Workbook workbook = WorkbookFactory.create(template);
        Sheet sheet = workbook.getSheetAt(sheetNumber);

        Row row;
        Cell cell;
        boolean foreach;
        String curListFieldName = null;
        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            foreach = false;

            List<String> expressions = new ArrayList<>();
            Map<String, CellStyle> styleMap = new ConcurrentHashMap<>(20);
            for (int clumnINdex = 0; clumnINdex <= row.getLastCellNum(); clumnINdex++) {
                cell = row.getCell(clumnINdex);
                if (cell == null) {
                    if (foreach) {
                        expressions.add("");
                    } else {
                        continue;
                    }
                }
                String cellValue = getCellText(cell);
                if (cellValue.matches(FOREACH_REGEX)) {
                    if (!foreach) {
                        foreach = true;
                        clumnINdex = -1;
                        continue;
                    }
                    String expression = cellValue.replaceAll(FOREACH_REGEX, "$1");
                    curListFieldName = cellValue.replaceAll(FOREACH_REGEX_FIELD_NAME, "$1");
                    expressions.add(expression);
                    styleMap.put(expression, cell.getCellStyle());
                } else if (cellValue.matches(COMMON_REGEX)) {
                    String fieldName = cellValue.replaceAll(COMMON_REGEX, "$2");
                    String fieldValue = getFieldStringValue(data, fieldName);
                    String newValue = cellValue.replaceAll(COMMON_REGEX, "$1" + fieldValue + "$3");
                    setCellValue(cell, newValue, datePattern);
                    if (foreach) {
                        expressions.add(newValue);
                    }
                } else {
                    if (foreach) {
                        expressions.add(cellValue);
                    }
                }
            }
            // 处理for循环表达式
            if (expressions != null && !expressions.isEmpty()) {
                int index = 0;
                List list = getFieldListValue(data, curListFieldName);
                if (list == null || list.isEmpty()) {
                    sheet.removeRow(row);
                    continue;
                }
                // 把表达式行到最后一行的内容往后移动list 大小的行数
                if (list.size() - 1 != 0 && rowIndex + 1 <= sheet.getLastRowNum()) {
                    sheet.shiftRows(rowIndex + 1, sheet.getLastRowNum(), list.size() - 1);
                }
                String expression;
                Object item;
                Cell cur;
                for (int k = 0; k < list.size(); k++) {
                    item = list.get(k);
                    index++;
                    row = sheet.getRow(rowIndex++);
                    if (row == null) {
                        row = sheet.createRow(rowIndex - 1);
                    }
                    for (int i = 0; i < expressions.size(); i++) {
                        cur = row.getCell(i);
                        if (cur == null) {
                            cur = row.createCell(i);
                        }
                        expression = expressions.get(i);
                        if (expression.matches(ARRAY_REGEX)) {
                            // items[waybillNo] -> wayBillNo -> item.waybillNo
                            cur.setCellStyle(styleMap.get(expression));
                            expression = expression.replaceAll(ARRAY_REGEX, "$1");
                            if (expression.equals("INDEX")) {
                                setCellValue(cur, index, datePattern);
                            } else {
                                setCellValue(cur, getFieldValue(item, expression), datePattern);
                            }
                        } else {
                            setCellValue(cur, expression, datePattern);
                        }
                    }
                }
                rowIndex--;
            }
        }
        if (CollUtil.isNotEmpty(mergeRegions)) {
            for (ExcelMergeRegion mergeRegion : mergeRegions) {
                CellRangeAddress cellAddresses = new CellRangeAddress(mergeRegion.getFirstRow(),
                        mergeRegion.getLastRow(), mergeRegion.getFirstCol(), mergeRegion.getLastCol());
                sheet.addMergedRegion(cellAddresses);
            }
        }
        workbook.write(out);
    }


    /**
     * Excel 导出
     * 目标源对象必须使用 @ExcelColumn注解标记属性  例如 @ExcelColumn(index = 0, head = "运单号码")
     * index: 显示列的顺序， head: excle表头
     *
     * @param data 数据源列表
     * @param out  目标流
     * @param <T>
     */
    public static <T> void export(List<T> data, OutputStream out) {
        export(data, out, null, false, null);
    }

    public static <T> void export(List<T> data, OutputStream out, String datePattern) {
        export(data, out, datePattern, false, null);
    }

    public static <T> void export(List<T> data, OutputStream out, boolean xlsx) {
        export(data, out, null, true, null);
    }

    public static <T> void export(List<T> data, OutputStream out, List<String> excludes) {
        export(data, out, null, false, excludes);
    }

    /**
     * Excel 导出
     *
     * @param data        数据源
     * @param out         输入目标
     * @param datePattern 日期
     * @param xlsx        文件格式是否为xlsx
     * @param excludes    不需要导出的列
     * @param <T>         <pre>
     *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            {@code
     */
    public static <T> void export(List<T> data, OutputStream out, String datePattern, boolean xlsx,
                                  List<String> excludes) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("数据不能为空");
        }
        if (datePattern == null) {
            datePattern = "yyyy-MM-dd HH:mm:ss";
        }
        Class<?> dataCls = data.get(0).getClass();
        Field[] fields = dataCls.getDeclaredFields();
        List<Column> columns = new ArrayList<>();

        for (Field field : fields) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null) {
                field.setAccessible(true);
                // 排除列
                if (excludes != null && excludes.contains(field.getName())) {
                    continue;
                }
                Object value = getFieldValue(field, data.get(0));
                columns.add(new Column(field, annotation.index(), annotation.head()));
            }
        }
        columns.sort((o1, o2) -> Integer.compare(o1.getIndex(), o2.getIndex()));

        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(xlsx);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        Sheet sheet = workbook.createSheet();
        int rowIndex = 0;
        int cellIndex = 0;
        Row headRow = sheet.createRow(rowIndex++);
        for (Column column : columns) {
            Cell cell = headRow.createCell(cellIndex++);
            setCellValue(cell, column.getHead());
        }
        Row curRow;
        for (T cur : data) {
            curRow = sheet.createRow(rowIndex++);
            Cell cell;
            cellIndex = 0;
            for (Column column : columns) {
                cell = curRow.createCell(cellIndex++);
                setCellValue(cell, getFieldValue(column.getField(), cur), datePattern);
            }
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static InputStream getTemplate(String templateName) {
        String path = "META-INF/excel/" + templateName;
        return new ClassPathResource(path).getStream();
    }

    static class Column {
        private Field field;
        private int index;
        private String head;

        public Column(Field field, int index, String head) {
            this.field = field;
            this.index = index;
            this.head = head;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }
    }

    private static Object getFieldValue(Field field, Object data) {
        field.setAccessible(true);
        try {
            return field.get(data);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static <T> Object getFieldValue(T data, String fieldName) {
        try {
            if (fieldName.equals("list") && data instanceof List) {
                return data;
            }
            if (data instanceof Map) {
                return ((Map<?, ?>) data).get(fieldName);
            }
            Field field = data.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(data);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("无字段[" + fieldName + "]");
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static <T> String getFieldStringValue(T data, String fieldName) {
        Object value = getFieldValue(data, fieldName);
        return value == null ? "" : value.toString();
    }

    private static <T> List getFieldListValue(T data, String fieldName) {
        Object value = getFieldValue(data, fieldName);
        if (value == null) {
            return null;
        }
        if (!(value instanceof List)) {
            throw new IllegalArgumentException("字段[" + fieldName + "]非List类型");
        }
        return (List) value;
    }

    private static String getCellText(Cell cell) {
        if (cell == null) {
            return "";
        }
        CellType cellType = cell.getCellType();

        if (CellType.STRING.equals(cellType)) {
            return cell.getStringCellValue();
        } else if (CellType.NUMERIC.equals(cellType)) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return "";
        }
    }


    private static void setCellValue(Cell cell, Object value) {
        setCellValue(cell, value, null);
    }

    private static void setCellValue(Cell cell, Object value, String datePattern) {
        if (value == null) {
            value = "";
        }
        if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof BigDecimal) {
            BigDecimal val = (BigDecimal) value;
            cell.setCellValue(val.doubleValue());
        } else if (value instanceof Date) {
            if (datePattern == null) {
                datePattern = "yyyy-MM-dd HH:mm:ss";
            }
            Date date = (Date) value;
            String formatValue = new SimpleDateFormat(datePattern).format(date);
            cell.setCellValue(formatValue);
        } else if (value instanceof Number) {
            cell.setCellValue(Double.valueOf(value.toString()));
        }
    }

}


