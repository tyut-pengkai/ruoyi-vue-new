package com.common.utils;


import com.trs.hybase.client.params.OperationParams;
import lombok.Data;

@Data
public class HybaseOperationParams {

    private OperationParams operationParams;

    public HybaseOperationParams() {
        operationParams = new OperationParams();
    }

    public static HybaseOperationParams builder() {
        return new HybaseOperationParams();
    }

    public OperationParams build() {
        return operationParams;
    }

    /**
     * insert.skip.error是否跳过错误的记录(true/false,默认false)
     * false:遇到错误记录抛出异常
     * true:跳过错误记录
     *
     * @param skipError 是否跳过错误的记录
     */
    public HybaseOperationParams setSkipError(boolean skipError) {
        operationParams.setBoolProperty("insert.skip.error", skipError);
        return this;
    }

    /**
     * insert.duplicate.override当设置了唯一值字段时，碰到相同的唯一值，是否覆盖 true/false，默认false）
     * false:遇到重复唯一值不覆盖，跳过
     * true:遇到重复唯一值覆盖
     *
     * @param duplicateOverride 是否覆盖
     */
    public HybaseOperationParams setDuplicateOverride(boolean duplicateOverride) {
        operationParams.setBoolProperty("insert.duplicate.override", duplicateOverride);
        return this;
    }

    /**
     * insert.duplicate.error当设置唯一值的情况下，碰到相同的唯一值，是否抛出异常
     * false:遇到重复唯一值不抛出异常
     * true:遇到重复唯一值抛出异常
     *
     * @param duplicateError 是否抛出异常
     */
    public HybaseOperationParams setDuplicateError(boolean duplicateError) {
        operationParams.setBoolProperty("insert.duplicate.error", duplicateError);
        return this;
    }

    /**
     * skip.file.error 是否跳过异常附件(true/false，默认false)，异常附件多指附件不存在于指定路径下。
     * false:不跳过异常附件，遇到异常附件抛出异常
     * true：跳过异常附件，不抛出异常
     *
     * @param skipFileError 是否跳过异常附件
     */
    public HybaseOperationParams setSkipFileError(boolean skipFileError) {
        operationParams.setBoolProperty("skip.file.error", skipFileError);
        return this;
    }

    /**
     * server.doc.extract 是否进行附件抽取（服务器端）(true/false，默认false)
     * false:不对附件中文本进行抽取
     * true：对附件中文本进行抽取
     *
     * @param serverDocExtract 是否进行附件抽取
     */
    public HybaseOperationParams setServerDocExtract(boolean serverDocExtract) {
        operationParams.setBoolProperty("server.doc.extract", serverDocExtract);
        return this;
    }

    /**
     * index.whitespace.reserve 是否保留首尾出现的空格，对DATE和NUMBER类型字段设置无效((true/false，默认false)
     * true:全部保留首尾空格；
     * false时，对PHRASE和DOCUMNE类型字段不保留尾空格，CHAR类型不保留首尾空格。
     *
     * @param indexWhitespaceReserve 是否保留首尾出现的空格
     */

    public HybaseOperationParams setIndexWhitespaceReserve(boolean indexWhitespaceReserve) {
        operationParams.setBoolProperty("index.whitespace.reserve", indexWhitespaceReserve);
        return this;
    }

    /**
     * insert.localhost.prior 自动分裂视图或分时归档视图，当未设置一级分区时，分区号在当前在线的节点上随机选择。设置此参数，本地节点优先。
     *
     * @param insertLocalhostPrior 是否本地节点优先
     */
    public HybaseOperationParams setInsertLocalhostPrior(boolean insertLocalhostPrior) {
        operationParams.setBoolProperty("insert.localhost.prior", insertLocalhostPrior);
        return this;
    }
}
