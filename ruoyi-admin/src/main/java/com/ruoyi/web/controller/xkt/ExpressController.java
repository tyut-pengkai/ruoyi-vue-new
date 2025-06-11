package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.PageVO;
import com.ruoyi.web.controller.xkt.vo.IdVO;
import com.ruoyi.web.controller.xkt.vo.express.*;
import com.ruoyi.xkt.dto.express.*;
import com.ruoyi.xkt.service.IExpressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

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
        List<ExpressFeeDTO> dtoList = expressService.listExpressFee(
                Optional.ofNullable(vo.getGoodsQuantity()).orElse(1),
                vo.getProvinceCode(),
                vo.getCityCode(), vo.getCountyCode()
        );
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

    @ApiOperation("查询物流轨迹")
    @PostMapping("queryTrackRecord")
    public R<Map<String, List<TrackRecordVO>>> queryTrackRecord(@Valid @RequestBody TrackRecordQueryVO vo) {
        List<ExpressTrackRecordDTO> list = expressService.listTrackRecord(vo.getExpressWaybillNos());
        Map<String, List<TrackRecordVO>> voMap = list.stream().map(o -> BeanUtil.toBean(o, TrackRecordVO.class))
                .collect(Collectors.groupingBy(TrackRecordVO::getExpressWaybillNo));
        return success(voMap);
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation("创建快递费配置 - 管理员")
    @PostMapping("/fee-config/create")
    public R<Long> createFeeConfig(@Valid @RequestBody ExpressFeeConfigEditVO vo) {
        ExpressFeeConfigEditDTO dto = BeanUtil.toBean(vo, ExpressFeeConfigEditDTO.class);
        Long id = expressService.createExpressFeeConfig(dto);
        return success(id);
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation("修改快递费配置 - 管理员")
    @PostMapping("/fee-config/edit")
    public R<Long> editExpressFeeConfig(@Valid @RequestBody ExpressFeeConfigEditVO vo) {
        ExpressFeeConfigEditDTO dto = BeanUtil.toBean(vo, ExpressFeeConfigEditDTO.class);
        expressService.modifyExpressFeeConfig(dto);
        return success(dto.getId());
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation("删除快递费配置 - 管理员")
    @PostMapping("/fee-config/remove")
    public R removeExpressFeeConfigInfo(@Validated @RequestBody IdVO vo) {
        expressService.deleteExpressFeeConfig(vo.getId());
        return R.ok();
    }

    @ApiOperation(value = "快递费配置详情")
    @GetMapping(value = "/fee-config/{id}")
    public R<ExpressFeeConfigVO> getExpressFeeConfigInfo(@PathVariable("id") Long id) {
        ExpressFeeConfigDTO infoDTO = expressService.getExpressFeeConfigById(id);
        return success(BeanUtil.toBean(infoDTO, ExpressFeeConfigVO.class));
    }

    @ApiOperation(value = "快递费配置分页查询")
    @PostMapping("/fee-config/page")
    public R<PageVO<ExpressFeeConfigListItemVO>> pageExpressFeeConfig(@Validated @RequestBody
                                                                              ExpressFeeConfigQueryVO vo) {
        ExpressFeeConfigQueryDTO queryDTO = BeanUtil.toBean(vo, ExpressFeeConfigQueryDTO.class);
        Page<ExpressFeeConfigListItemDTO> pageDTO = PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        expressService.listFeeConfig(queryDTO);
        return success(PageVO.of(pageDTO, ExpressFeeConfigListItemVO.class));
    }

    @ApiOperation(value = "快递费展示")
    @GetMapping(value = "/fee/show")
    public R<List<ExpressFeeShowVO>> showExpressFee() {
        List<ExpressFeeConfigListItemDTO> configItems = expressService.listFeeConfig(new ExpressFeeConfigQueryDTO());
        Map<String, Map<String, ExpressFeeShowVO.Item>> map = new HashMap<>();
        for (ExpressFeeConfigListItemDTO configItem : configItems) {
            Map<String, ExpressFeeShowVO.Item> itemMap = map.computeIfAbsent(configItem.getExpressName(),
                    k -> new HashMap<>());
            String ik = configItem.getFirstItemAmount() + "-" + configItem.getNextItemAmount();
            ExpressFeeShowVO.Item item = itemMap.computeIfAbsent(ik,
                    k -> new ExpressFeeShowVO.Item(configItem.getRegionName(), 1,
                            configItem.getFirstItemAmount(), 1, configItem.getNextItemAmount()));
            if (!item.getAres().equals(configItem.getRegionName())) {
                item.setAres(item.getAres() + "," + configItem.getRegionName());
            }
        }
        List<ExpressFeeShowVO> vos = new ArrayList<>(map.size());
        for (Map.Entry<String, Map<String, ExpressFeeShowVO.Item>> entry : map.entrySet()) {
            ExpressFeeShowVO vo = new ExpressFeeShowVO();
            vo.setExpressName(entry.getKey());
            vo.setItems(new ArrayList<>(entry.getValue().values()));
        }
        return R.ok(vos);
    }

}
