package com.easycode.cloud.service.impl;

import com.easycode.cloud.domain.dto.OutSourceOrderBomDto;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.weifu.cloud.constant.EsbSendSapConstant;
import com.easycode.cloud.service.IOutsourceOrderBomSyncService;
import com.weifu.cloud.service.RemoteEsbSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 寄售调拨退货明细Service业务层处理
 *
 * @author bcp
 * @date 2023-03-17
 */
@Service
public class IOutsourceOrderBomSyncServiceImpl implements IOutsourceOrderBomSyncService
{
    @Autowired
    private RemoteEsbSendService remoteEsbSendService;

    private static final Logger logger = LoggerFactory.getLogger(IOutsourceOrderBomSyncServiceImpl.class);

    @Override
    public List<OutSourceOrderBomDto> getOutsourceBomList(String purchaseOrderNo) throws Exception
    {

        MsgObject reqMo = new MsgObject(IMsgObject.MOType.initSR);
        reqMo.setReqValue("PURCHASEORDER", purchaseOrderNo);
        // 正常情况不需要传
//            reqMo.setReqValue("RETRAN", "X");
        // feign 调用
        byte[] result = remoteEsbSendService.sendToEsb(EsbSendSapConstant.Z_MES_OUTSOURCE_BOM, reqMo.toString().getBytes());
        // 转化为标准结果
        MsgObject resMo = new MsgObject(result, IMsgObject.MOType.initSR);
        logger.info("esb返回结果：" + resMo.toString());
        // 头部信息
        List<GroupRecord> headerList = resMo.getResGroupRecord("IT_ITEM");
        // 返回多行数据
        List<OutSourceOrderBomDto> orderList = new ArrayList();
        for (int i = 0; i < headerList.size(); i++) {
            GroupRecord item = headerList.get(i);
            OutSourceOrderBomDto outSourceOrderBomDto = new OutSourceOrderBomDto();
            for (int j = 0; j < item.getFieldSize(); j++) {
                String key = item.getFieldKey(j);
                String value = item.getFieldValue(j);
                if(key.equals("PO_NUMBER")){
                    outSourceOrderBomDto.setPurchaseOrderNo(value);
                }else if (key.equals("PO_ITEM")){
                    outSourceOrderBomDto.setPoItemNo(value);
                }else if (key.equals("ITEM_NO")){
                    outSourceOrderBomDto.setItemNo(value);
                }else if (key.equals("MATERIAL")){
                    outSourceOrderBomDto.setMaterialNo(value);
                }else if (key.equals("ENTRY_QUANTITY")){
                    outSourceOrderBomDto.setQty(new BigDecimal(value));
                }else if (key.equals("ENTRY_UOM")){
                    outSourceOrderBomDto.setUnit(value);
                }else if (key.equals("PLANT")){
                    outSourceOrderBomDto.setPlantCode(value);
                }
            }
            orderList.add(outSourceOrderBomDto);
        }
        return orderList;
    }
}
