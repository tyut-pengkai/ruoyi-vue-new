package com.ruoyi.common.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/8/1 16:25
 */
public class HtmlValidator {

    // 定义允许的 HTML 标签和属性（基于 UniApp rich-text 组件规则）
    private static final Safelist ALLOWED_HTML = Safelist.relaxed()
            .addTags(
                    "a", "abbr", "address", "article", "aside", "b", "bdi", "bdo", "big", "blockquote",
                    "br", "caption", "center", "cite", "code", "col", "colgroup", "dd", "del", "div",
                    "dl", "dt", "em", "fieldset", "font", "footer", "h1", "h2", "h3", "h4", "h5", "h6",
                    "header", "hr", "i", "img", "ins", "label", "legend", "li", "mark", "nav", "ol", "p",
                    "pre", "q", "rt", "ruby", "s", "section", "small", "span", "strong", "sub", "sup",
                    "table", "tbody", "td", "tfoot", "th", "thead", "tr", "tt", "u", "ul"
            )
            .addAttributes("a", "href", "title", "target")
            .addAttributes("img", "src", "alt", "height", "width")
            .addAttributes("table", "width", "height", "colspan", "rowspan", "align", "valign")
            .addAttributes("td", "colspan", "rowspan", "align", "valign")
            .addAttributes("th", "colspan", "rowspan", "align", "valign")
            .addAttributes("font", "color", "face", "size")
            .addAttributes(":all", "class", "id", "style"); // 允许全局属性

    /**
     * 检查 HTML 是否合法（仅包含允许的标签和属性）
     *
     * @param html 输入的富文本
     * @return true=合法，false=包含非法标签或属性
     */
    public static boolean isValidHtml(String html) {
        return Jsoup.isValid(html, ALLOWED_HTML);
    }

    /**
     * 清理 HTML，移除非法标签和属性
     *
     * @param html 输入的富文本
     * @return 清理后的安全 HTML
     */
    public static String sanitizeHtml(String html) {
        return Jsoup.clean(html, ALLOWED_HTML);
    }

}
