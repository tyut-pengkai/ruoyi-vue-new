package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.controller.xkt.vo.express.*;
import com.ruoyi.xkt.dto.express.ExpressFeeDTO;
import com.ruoyi.xkt.dto.express.ExpressRegionTreeNodeDTO;
import com.ruoyi.xkt.dto.express.ExpressStructAddressDTO;
import com.ruoyi.xkt.service.IExpressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-03 14:18
 */
@Api(tags = "物流")
@RestController
@RequestMapping("/rest/v1/express")
public class ExpressController extends XktBaseController {

    @Autowired
    private IExpressService expressService;

    @ApiOperation("全部物流")
    @GetMapping("allExpress")
    public R<List<ExpressVO>> allExpress() {
        return success(BeanUtil.copyToList(expressService.allExpress(), ExpressVO.class));
    }

    @ApiOperation("下单时物流选择列表 - 含快递费")
    @PostMapping("listExpressFee")
    public R<List<ExpressFeeVO>> listExpressFee(@Valid @RequestBody ExpressFeeReqVO vo) {
        List<ExpressFeeDTO> dtoList = expressService.listExpressFee(vo.getGoodsQuantity(), vo.getProvinceCode(),
                vo.getCityCode(), vo.getCountyCode());
        return success(BeanUtil.copyToList(dtoList, ExpressFeeVO.class));
    }

    @ApiOperation("获取行政规划树")
    @GetMapping("getRegionTree")
    public R<List<ExpressRegionTreeNodeVO>> getRegionTree() {
        List<ExpressRegionTreeNodeDTO> dtoList = expressService.getRegionTreeCache();
        return success(BeanUtil.copyToList(dtoList, ExpressRegionTreeNodeVO.class));
    }

    @ApiOperation("智能解析 - 对地址、姓名、电话等，进行智能识别")
    @PostMapping("parseNamePhoneAddress")
    public R<ExpressStructAddressVO> parseNamePhoneAddress(@Valid @RequestBody ExpressAddressParseReqVO vo) {
        ExpressStructAddressDTO dto = expressService.parseNamePhoneAddress(vo.getAddress());
        return success(BeanUtil.toBean(dto, ExpressStructAddressVO.class));
    }

}
