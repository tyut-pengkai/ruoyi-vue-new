package com.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.common.mapping.HybaseClassMapping;
import com.common.query.HybaseSort;
import com.trs.hybase.client.params.SearchParams;
import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * 	search.range.filter：用于对视图检索的时候，对分裂库进行过滤，缩小检索范围。例如按照日期分库以后，可以在这儿制定要检索的日期范围（splitColumn:[start TO end]）。注：为了提高检索速度，可以根据分区字段或分库字段进行过滤，缩小过滤范围；该参数仅过滤在哪些子库中查找，不做精确检索时使用；分库字段的过滤仅支持单个词，不支持范围查找，比如，id:[123 TO 456]。支持多个过滤语句，以分号分割，多个过滤语句需要同时满足。例如：id:5 OR id:6; date:[2012.12.16 TO 2013.02.16]。单个过滤语句可以使用OR连接多个过滤条件，其中满足1条即可。仅支持分区字段或者分库字段，如果出现其他字段，不进行过滤。<br/><br/>
 * 	search.ideo.single：服务器端设置冗余按字索引的情况下，通过这个选项选择检索的类型。当该选项设置成true的时候，选择按字检索；设置成false的时候，选择按词检索；默认值为false。<br/><br/>
 * 	search.syntax.name：服务器端表达式解析规则，可值trs/hyabse。trs表示兼容TRS Server表达式，hybase表示HYBASE专用语法（默认值）。<br/><br/>
 * 	virtualrule.express.prior：对虚拟逻辑字段检索有效；false表示“字段逻辑优先”，即虚拟逻辑在每个检索原子（检索键值）上进行扩展。true表示“表达式逻辑优先”，即虚拟逻辑在虚拟逻辑字段所限定的最大子表达式上进行扩展。默认false。<br/><br/>
 * 	search.highlighter.analyzer：取值“trs/hybase”检索结果的高亮方式，默认trs。<br/><br/>
 * 	search.highlighter.keepsingle：取值“true/false”，true表示高亮关键字时，保持单个字符的高亮，否则至少高亮两个字符，默认false。<br/><br/>
 * 	search.highlighter.expand：取值“true/false”，true表示支持标红扩展，例如ab*，可以对abc进行标红，但对于文本的范围检索，可能会存在全文标红的情况；false表示不进行标红扩展，默认false。<br/><br/>
 * 	search.match.max：当单个节点检索记录数达到满足值时，便可以将结果返回客户端；当单个节点包含多个子库且超过最大检索线程数时，该参数使用效果方可见。<br/><br/>
 * 	search.match.rate：当返回结果的节点数达到检索的节点的一定比例时，便可以返回结果，可以配合search.match.max使用。<br/><br/>
 * 	search.include.uncache：分时归档视图检索时，是否检索全部子库，设置为false，表示仅检索位于本地的子库，即子库开/关为open，仅HDFS为false的子库；设置为true，表示检索全部子库，默认false。<br/><br/>
 * 	hybase.search.nosort：设置字段是否排序检索，设置为true，表示不排序；设置为false，表示排序，默认为false。<br/><br/>
 * 	search.syntax.trs.restrict：检索参数，表示严格使用trs语法，其中，TRS语法中，检索COL=北京拓尔思信息技术股份有限公司，默认是短语检索。<br/><br/>
 * 	search.parse.style：其参数为“normal”、“word”和“nlp”。对于按字分词的全文字段（phrase或者document）检索，先使用IKAnalyzer按词分词，再使用TRSAnalyzer按字分词，两次分词结果中，取最长的分词方式，作为“短语”检索。目的是中和查全率和查准率的问题。IKAnalyzer进行分词的分词词典，依据entity数据库中的实体词词典，定时更新。参数“normal”表示不使用混合分词方式；参数“word”表示采用混合分词方式；参数“nlp”，等同于“原表达式短语检索”^70 OR 原表达式word检索^20 OR 原表达式。 此参数区分#WORD检索和#NLP检索，#WORD只能添加到单字字段的检索属性上，此参数可以针对整个查询串涉及的全文字段；#WORD的实体词识别停留在模式匹配上，此参数是从分词的角度处理，效果会更优。<br/><br/>
 * 	search.phrase.wildcard：短语模糊检索。在短语检索中，“abc,d*f,aab,X*z,abc,m$n,pqr,stav”,默认通配符被看做查询串一部分，增加参数之后，既可以保证模糊检索，又能保证短语检索的位置关系。True将星号，问号，dollar符视为普通字符，false将其视为通配符，默认false，即将其视为通配符。<br/><br/>
 * 	result.timestamp.read：获取timestamp值。<br/><br/>
 * 	facet.having 分类统计，多维统计时，分别过滤。 用法： [字段名][比较符号][数值], 比较符号支持 >、<、 =、 <=、 >=<br/><br/>
 * 	having.level 分类统计，多维统计时，配合facet.having参数使用，取值：all, local, global, 默认all;global表示再最后全局汇总时进行过滤， local表示仅在单个节点汇总时过滤<br/><br/>
 * 	category.merge.values 分类统计类别合并参数，格式: 合并类别1=原始类别1,原始类别2;合并类别2=原始类别3,原始类别4支持原始类别同时归类多个大类别中。<br/><br/>
 * 	category.merge.original 合并后是否保留原始类别，默认false<br/><br/>
 * 	category.result.frequence 统计结果按词频统计，默认false，即按记录数<br/><br/>
 * 	search.relevance.shrink true/false,相关度归一化<br/><br/>
 * 	hybase.columns.cutsize 字段名1:数值1;字段名2:数值2....优先级高于setCutsize参数，对不同全文字段指定不同的裁剪长度<br/><br/>
 * 	pinyin.analyzer.columns 指定拼音检索字段，对勾选了“拼音索引”的字段，在检索时采用拼音分词器， 示例： 字段1,字段2,字段3...<br/><br/>
 * 	search.pinyin.first 拼音首字母检索，对勾选了“允许拼音首字母检索”的字段，在检索时可根据拼音的首字母检索。默认是：false<br/><br/>
 * 	search.date.format 指定日期字段的返回格式，如：“yyyy-MM-dd”<br/><br/>
 * 	search.similar.boost 文本相似度检索时相似程度，值越大相似度越高，命中记录数越少。其取值范围为[1-100]，默认值为20。<br/><br/>
 * 	search.synonym.extend 同义词扩展检索,属性值可选“disable”、“phrase”、“term”,默认是：disable<br/><br/>
 * 	disable:禁用同义词检索<br/><br/>
 * 	phrase:字段的查询串作为一个词项，再依据同义词库找出对应的同义词，将这一系列的同义词进行OR查询。<br/><br/>
 * 	term:在索引层由TRS分词器（按双字或者按词）分词后作为一个词项，由这些词项找出对应的同义词。<br/><br/>
 */
@Data
public class HybaseSearchParams {

    private SearchParams searchParams;

    public HybaseSearchParams() {
        searchParams = new SearchParams();
    }

    public static HybaseSearchParams builder() {
        return new HybaseSearchParams();
    }

    /**
     * 缺省检索字段
     *
     * @param columns 检索字段
     * @return 缺省检索字段
     */
    private static String getColumns(Collection<String> columns) {
        return StrUtil.nullToEmpty(CollUtil.join(columns, ";"));
    }

    public SearchParams build() {
        return searchParams;
    }

    /**
     * 转换排序参数
     *
     * @param hybaseSorts 排序参数
     * @return 排序参数字符串
     */
    public HybaseSearchParams setSortMethod(List<HybaseSort> hybaseSorts, HybaseClassMapping classMapping) {
        String sort = StrUtil.nullToEmpty(CollUtil.join(hybaseSorts, ";", (x) -> {
            x.setHybaseField(classMapping.getColumnPropertyMap().get(x.getField()));
            return x.getExpression();
        }));
        searchParams.setSortMethod(sort);
        return this;
    }

    /**
     * 设置子库过滤条件
     *
     * @param query 检索条件
     */
    public HybaseSearchParams setRangeFilter(String query) {
        if (query.contains(HybaseUtils.range)) {
            searchParams.setProperty("search.range.filter", HybaseUtils.getSearchRangeFilter(query));
        }
        return this;
    }

    /**
     * 设置是否排序，查询1W条以上数据时，不排序
     */
    public HybaseSearchParams setNosort(Boolean nosort) {
        if (nosort) {
            searchParams.setBoolProperty("hybase.search.nosort", true);
        }
        return this;
    }

    /**
     * 设置要检索的字段
     *
     * @param columnNames 字段名
     * @return 检索字段
     */
    public HybaseSearchParams setReadColumns(Collection<String> columnNames) {
        searchParams.setReadColumns(getColumns(columnNames));
        return this;
    }

    /**
     * 设置要高亮的字段
     *
     * @param highlighterPropertyNames 高亮字段名
     * @return 高亮字段
     */
    public HybaseSearchParams setColorColumns(Collection<String> highlighterPropertyNames) {
        searchParams.setColorColumns(getColumns(highlighterPropertyNames));
        return this;
    }

    /**
     * 设置要检索的字段
     *
     * @param quickSearch 是否快速检索
     * @return 检索字段
     */
    public HybaseSearchParams setQuickSearch(Boolean quickSearch) {
        if (quickSearch) {
            searchParams.setQuickSearch(true);
            searchParams.setEstimateUnit(30000);
        }
        return this;
    }
}
