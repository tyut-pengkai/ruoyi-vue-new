package com.common.extractor;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 抽取带命中点的摘要.
 */
@Getter
public class HybaseHitAbstractExtractor implements HybaseExtractor {

    private static final Set<Character> SENTENCE_ENDS = new HashSet<>();

    static {
        SENTENCE_ENDS.add('.');
        SENTENCE_ENDS.add('。');
        SENTENCE_ENDS.add('!');
        SENTENCE_ENDS.add('！');
        SENTENCE_ENDS.add('?');
        SENTENCE_ENDS.add('？');
        SENTENCE_ENDS.add('\r');
        SENTENCE_ENDS.add('\n');
        SENTENCE_ENDS.add(';');
        SENTENCE_ENDS.add('；');
    }

    // fields ---------------------------------------------------------------
    private int maxTotalLength = 50;
    private int maxSpanCount = 50;
    private String suffix = "";

    // methods --------------------------------------------------------------

    private static String getHitPoints(String colorString, List<JavaHitPoint> hits) {
        StringBuilder contentBuilder = new StringBuilder();
        String regxExpr = "(<font color=red>)|(</font>)";
        Pattern pattern = Pattern.compile(regxExpr);
        Matcher matcher = pattern.matcher(colorString);
        int prevEnd = 0;
        JavaHitPoint hit = null;
        while (matcher.find()) {
            contentBuilder.append(colorString, prevEnd, matcher.start());
            if (matcher.group(1) != null) {
                hit = new JavaHitPoint();
                hit.iStart = contentBuilder.length();
            } else if (hit != null) {
                hit.iLength = contentBuilder.length() - hit.iStart;
                hits.add(hit);
                hit = null;
            }
            prevEnd = matcher.end();
        }
        if (prevEnd < colorString.length()) {
            contentBuilder.append(colorString.substring(prevEnd));
        }
        return contentBuilder.toString();
    }

    public static String getHitAbstract(String content, JavaHitPoint[] javaHitPoints, int maxTotalLength, int maxSpanCount, String suffix) {
        // 算法一：确保描红的结果包含最多的命中点
//		ArrayList<ArrayList<Integer>> spans=getBestSpans(javaHitPoints,0,javaHitPoints.length-1,maxTotalLength,maxSpanCount,suffix.length());
//		String abs="";
//		int totalSpansLength=getSpansLength(javaHitPoints,spans,0,spans.size()-1,suffix);
//		int restExtendLength=Math.max((maxTotalLength-totalSpansLength),0);
//		for(int i=0;i<spans.size();i++){
//			int perExtendLength=Math.max((restExtendLength/(spans.size()-i)),0);
//			RedString spanString=getHitStringWithRed(content,javaHitPoints,spans.get(i),perExtendLength);
//			restExtendLength-=(spanString.getHead()+spanString.getTail());
//			abs=abs+spanString.getRedString()+suffix;
//		}
//		return abs;
        // 算法二：确保描红的结果都是由完整的句子构成的
        int[] sentenceBound = new int[2];
        int prevEnd = 0;
        int prevPointEnd = 0;
        int restTotalLength = maxTotalLength;
        StringBuilder builder = new StringBuilder();
        String abstractPrefix = "";
        int nEnd = findSenenceStart(content, prevEnd, javaHitPoints[0].iStart);
        int nStart = nEnd <= 250 ? 0 : nEnd - 250;
        StringBuilder build1 = new StringBuilder(250);
        boolean inHtml = false;
        for (int i = nStart; i < nEnd; i++) {
            char c = content.charAt(i);
            if (c == '<') {
                inHtml = true;
                continue;
            }
            if (content.startsWith("&lt;", i)) {
                inHtml = true;
                i = i + "&lt;".length() - 1;
                continue;
            }
            if (c == '>') {
                inHtml = false;
                continue;
            }
            if (content.startsWith("&gt;", i)) {
                inHtml = false;
                i = i + "&gt;".length() - 1;
                continue;
            }
            if (inHtml) {
                continue;
            }
            build1.append(c);
        }
        abstractPrefix = build1.toString().trim();

        do {
            ArrayList<Integer> bestSpan = findBestSpan(javaHitPoints, prevPointEnd, javaHitPoints.length - 1, restTotalLength);
            if (builder.length() > 0) {
                builder.append(suffix);
            }
            builder.append(buildSpanString(content, prevEnd, javaHitPoints, bestSpan, sentenceBound));
            prevEnd = sentenceBound[1];
            prevPointEnd = bestSpan.get(bestSpan.size() - 1) + 1;
            restTotalLength -= sentenceBound[1] - sentenceBound[0] + suffix.length();
        } while (restTotalLength > 0 && prevPointEnd < javaHitPoints.length - 1);
        String abstractStr = StrUtil.trim(builder);
        if (abstractStr.length() < 250) {
            int needSupply = 250 - abstractStr.length();
            int nStartSupply = abstractPrefix.length() > needSupply ? abstractPrefix.length() - needSupply : 0;
            // 这里可能少个字符，对着看看前面放在abstractPrefix中，结合setenceBound
            abstractStr = nStartSupply > 0 ? "..." + StrUtil.nullToEmpty(StrUtil.subSuf(abstractPrefix, nStartSupply)) + abstractStr : StrUtil.nullToEmpty(StrUtil.subSuf(abstractPrefix, nStartSupply)) + abstractStr;
        }
        // StrUtil应该最好结合sentenceBound特殊字符来处理下
        return StrUtil.trim(abstractStr);
    }

    private static ArrayList<Integer> findBestSpan(JavaHitPoint[] javaHitPoints, int begin, int end, int length) {
        ArrayList<Integer> bestSpan = new ArrayList<>();
        for (int i = begin; i <= end; i++) {
            ArrayList<Integer> curSpan;
            curSpan = new ArrayList<>();
            for (int j = i; j <= end; j++) {
                if (curSpan.isEmpty()) {
                    curSpan.add(j);
                } else {
                    JavaHitPoint curHit = javaHitPoints[j];
                    JavaHitPoint spanHeadHit = javaHitPoints[curSpan.get(0)];
                    int spanLength = curHit.iStart - spanHeadHit.iStart + curHit.iLength;
                    if (spanLength > length) {
                        break;
                    } else {
                        curSpan.add(j);
                    }
                }
            }
            if (curSpan.size() > bestSpan.size()) {
                bestSpan = curSpan;
            } else if (curSpan.size() == bestSpan.size()) {
                int curSpanLength = getSpanLength(javaHitPoints, curSpan);
                int bestSpanLength = getSpanLength(javaHitPoints, bestSpan);
                if (curSpanLength < bestSpanLength) {
                    bestSpan = curSpan;
                }
            }

        }
        return bestSpan;
    }

    private static int getSpanLength(JavaHitPoint[] javaHitPoints, ArrayList<Integer> span) {
        JavaHitPoint first = javaHitPoints[span.get(0)];
        JavaHitPoint last = javaHitPoints[span.get(span.size() - 1)];
        return last.iStart - first.iStart + last.iLength;
    }

    private static String buildSpanString(String content, int prevEnd, JavaHitPoint[] javaHitPoints, ArrayList<Integer> span, int[] sentenceBound) {
        StringBuilder builder = new StringBuilder();
        JavaHitPoint firstHitPoint = javaHitPoints[span.get(0)];
        int sentenceStart = findSenenceStart(content, prevEnd, firstHitPoint.iStart);
        if (sentenceStart < firstHitPoint.iStart) {
            builder.append(content, sentenceStart, firstHitPoint.iStart);
        }
        int prePointEnd = firstHitPoint.iStart;
        for (Integer integer : span) {
            JavaHitPoint hitPoint = javaHitPoints[integer];
            if (prePointEnd < hitPoint.iStart) {
                builder.append(content, prePointEnd, hitPoint.iStart);
            }
            builder.append("<highlighter>").append(content, hitPoint.iStart, hitPoint.iStart + hitPoint.iLength).append("</highlighter>");
            prePointEnd = hitPoint.iStart + hitPoint.iLength;
        }
        JavaHitPoint lastHitPoint = javaHitPoints[span.get(span.size() - 1)];
        int sentenceEnd = findSenenceEnd(content, lastHitPoint.iStart + lastHitPoint.iLength, content.length());
        if (sentenceEnd > lastHitPoint.iStart + lastHitPoint.iLength) {
            builder.append(content, lastHitPoint.iStart + lastHitPoint.iLength, sentenceEnd);
        }
        sentenceBound[0] = sentenceStart;
        sentenceBound[1] = sentenceEnd;
        return builder.toString();
    }

    private static int findSenenceStart(String content, int start, int end) {
        // 查询end之前的句子开始的位置，不能超过start
        for (int i = end; i >= start; i--) {
            char ch = content.charAt(i);
            if (SENTENCE_ENDS.contains(ch)) {
                return i + 1;
            }
        }
        return start;
    }

    private static int findSenenceEnd(String content, int start, int end) {
        // 查询start之后的句子句子结束的位置，不能超过end
        for (int i = start; i < end; i++) {
            char ch = content.charAt(i);
            if (SENTENCE_ENDS.contains(ch)) {
                return i + 1;
            }
        }
        return end;
    }

    /**
     * 使用params中的关键词参数对字段值进行人工描红.
     * 关键词参数的名字由keywordName属性指定,默认是"name"。
     */
    @Override
    public String extractValue(String fieldName, String value) {
        List<JavaHitPoint> hits = new ArrayList<>();
        String content = getHitPoints(value, hits);
        if (hits.isEmpty()) {
            return null;
        }
        return getHitAbstract(content, hits.toArray(new JavaHitPoint[0]), maxTotalLength, maxSpanCount, suffix);
    }

    // accessors ------------------------------------------------------------

    public void setMaxTotalLength(int maxTotalLength) {
        this.maxTotalLength = maxTotalLength;
    }

    public void setMaxSpanCount(int maxSpanCount) {
        this.maxSpanCount = maxSpanCount;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}

