package com.common.mapping;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.common.extractor.HybaseExtractor;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
@Data
@Component
public class HybaseMapping {

    private Map<String, HybaseClassMapping> classMappings = new HashMap<>();

    /**
     * 读取hybase映射文件，解析映射关系到 classMappings
     */
    @PostConstruct
    public void init() {
        // 获取类路径下的xml文件
        ClassPathResource trshybase_mapping = new ClassPathResource(StrUtil.join(File.separator, "hybase", "trshybase-mapping.list"));
        List<String> files = new LinkedList<>();
        BufferedReader reader = trshybase_mapping.getReader(Charset.defaultCharset());
        IoUtil.readLines(reader, files);
        log.debug("读取hybase实体映射文件路径：{}，映射关系数量：{}", trshybase_mapping.getPath(), files.size());
        for (String fileName : files) {
            // 获取结尾都是.thm.xml的文件
            if (fileName.endsWith(".thm.xml")) {
                ClassPathResource templateResource = new ClassPathResource(StrUtil.join(File.separator, "hybase", fileName));
                log.debug("映射文件 {} 处理", fileName);
                InputStream resourceAsStream = templateResource.getStream();
                Document document = XmlUtil.readXML(resourceAsStream);
                // 获取根节点 trshybase-mapping
                NodeList roots = document.getElementsByTagName("trshybase-mapping");
                for (int i = 0; i < roots.getLength(); i++) {
                    Node root = roots.item(i);
                    // 获取根节点下的class节点
                    NodeList classNodeList = root.getChildNodes();
                    for (int j = 0; j < classNodeList.getLength(); j++) {
                        Node classNode = classNodeList.item(j);
                        if (classNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element classElement = (Element) classNode;
                            HybaseClassMapping hybaseClassMapping = new HybaseClassMapping();
                            String id = classElement.getAttribute("id");
                            hybaseClassMapping.setId(id);
                            hybaseClassMapping.setClassName(classElement.getAttribute("name"));
                            hybaseClassMapping.setTable(classElement.getAttribute("table"));
                            // 获取class节点下的property节点
                            Map<String, String> columnPropertyMap = new LinkedHashMap<>();
                            Map<String, HybasePropertyMapping> propertyMappings = new LinkedHashMap<>();
                            // 获取class节点下的property节点
                            NodeList classChildNodeList = classNode.getChildNodes();
                            // 有高亮的属性名
                            Set<String> highlighterPropertyNames = new HashSet<>();
                            for (int k = 0; k < classChildNodeList.getLength(); k++) {
                                Node propertyNode = classChildNodeList.item(k);
                                if (propertyNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element propertyElement = (Element) propertyNode;
                                    String uuid = "uuid";
                                    if (uuid.equals(propertyElement.getAttribute("name"))) {
                                        if (Boolean.parseBoolean(propertyElement.getAttribute(uuid))) {
                                            hybaseClassMapping.setUuidPropertyName(uuid);
                                        }
                                    } else {
                                        columnPropertyMap.put(propertyElement.getAttribute("name"), propertyElement.getAttribute("column"));
                                        if (Boolean.parseBoolean(propertyElement.getAttribute("highlighter"))) {
                                            highlighterPropertyNames.add(propertyElement.getAttribute("column"));
                                        }
                                        // 获取property节点下的extractor节点
                                        NodeList propertyChildNodes = propertyNode.getChildNodes();
                                        if (propertyChildNodes.getLength() > 0) {
                                            HybasePropertyMapping hybasePropertyMapping = new HybasePropertyMapping();
                                            hybasePropertyMapping.setPropertyName(propertyElement.getAttribute("name"));
                                            hybasePropertyMapping.setColumnName(propertyElement.getAttribute("column"));
                                            List<HybaseExtractor> hybaseExtractorList = new ArrayList<>();
                                            for (int l = 0; l < propertyChildNodes.getLength(); l++) {
                                                Node extractor = propertyChildNodes.item(l);
                                                if (extractor.getNodeType() == Node.ELEMENT_NODE) {
                                                    Element extractorElement = (Element) extractor;
                                                    hybaseExtractorList.add(ReflectUtil.newInstance(extractorElement.getAttribute("type")));
                                                }
                                            }
                                            hybasePropertyMapping.setHybaseExtractorList(hybaseExtractorList);
                                            propertyMappings.put(propertyElement.getAttribute("name"), hybasePropertyMapping);
                                        }
                                    }
                                }
                            }
                            hybaseClassMapping.setColumnPropertyMap(columnPropertyMap);
                            hybaseClassMapping.setColumnNames(columnPropertyMap.values());
                            hybaseClassMapping.setPropertyNames(columnPropertyMap.keySet());
                            hybaseClassMapping.setHighlighterPropertyNames(highlighterPropertyNames);
                            hybaseClassMapping.setPropertyMappings(propertyMappings);
                            classMappings.put(id, hybaseClassMapping);
                        }
                    }
                }
            }
        }
    }

    public HybaseClassMapping getClassMapping(String id) {
        return classMappings.get(id);
    }
}
