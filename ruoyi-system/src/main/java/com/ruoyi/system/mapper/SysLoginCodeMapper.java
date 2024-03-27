package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.vo.BatchNoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 单码Mapper接口
 *
 * @author zwgu
 * @date 2021-12-03
 */
@Repository
public interface SysLoginCodeMapper {
    /**
     * 查询单码
     *
     * @param cardId 单码主键
     * @return 单码
     */
    public SysLoginCode selectSysLoginCodeByCardId(Long cardId);

    /**
     * 查询单码
     *
     * @param cardIds 单码主键
     * @return 单码
     */
    public List<SysLoginCode> selectSysLoginCodeByCardIds(Long[] cardIds);

    /**
     * 查询单码列表
     *
     * @param SysLoginCode 单码
     * @return 单码集合
     */
    public List<SysLoginCode> selectSysLoginCodeList(SysLoginCode SysLoginCode);

    public int countSysLoginCode(SysLoginCode SysLoginCode);

    /**
     * 新增单码
     *
     * @param SysLoginCode 单码
     * @return 结果
     */
    public int insertSysLoginCode(SysLoginCode SysLoginCode);

    /**
     * 修改单码
     *
     * @param SysLoginCode 单码
     * @return 结果
     */
    public int updateSysLoginCode(SysLoginCode SysLoginCode);

    /**
     * 删除单码
     *
     * @param cardId 单码主键
     * @return 结果
     */
    public int deleteSysLoginCodeByCardId(Long cardId);

    /**
     * 批量删除单码
     *
     * @param cardIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysLoginCodeByCardIds(Long[] cardIds);

    /**
     * 查询单码
     *
     * @param cardNo
     * @return
     */
    public SysLoginCode selectSysLoginCodeByCardNo(String cardNo);

    /**
     * 查询单码
     */
    public SysLoginCode selectSysLoginCodeByAppIdAndCardNo(@Param("appId") Long appId, @Param("cardNo") String cardNo);

    /**
     * 新增单码
     *
     * @param SysLoginCodeList
     */
    public int insertSysLoginCodeBatch(List<SysLoginCode> SysLoginCodeList);

    /**
     * 获取批次号列表
     * @return
     */
    public List<BatchNoVo> selectBatchNoList(@Param("agentId") Long agentId);

    public List<Map<String, Long>> countSysLoginCodeAll(SysLoginCode card);
}
