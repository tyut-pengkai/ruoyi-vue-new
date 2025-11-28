package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.web.controller.xkt.vo.StoreProductStatistics.StoreProdAppViewRankResVO;
import com.ruoyi.web.controller.xkt.vo.notice.NoticeResVO;
import com.ruoyi.xkt.service.IStoreProductStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 档口商品统计
 *
 * @author ruoyi
 */
@Api(tags = "档口商品统计")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/store-prod-statistics")
public class StoreProductStatisticsController extends BaseController {

    final IStoreProductStatisticsService prodStatisticsService;

    @Anonymous
    @ApiOperation(value = "APP商品访问榜", httpMethod = "GET", response = R.class)
    @GetMapping("/app/view-rank")
    public R<StoreProdAppViewRankResVO> getAppViewRank() {
        return R.ok(BeanUtil.toBean(prodStatisticsService.getAppViewRank(), StoreProdAppViewRankResVO.class));
    }


}
