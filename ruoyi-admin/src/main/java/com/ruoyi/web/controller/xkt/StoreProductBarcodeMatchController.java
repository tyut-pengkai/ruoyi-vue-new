package com.ruoyi.web.controller.xkt;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.xkt.vo.storeProdBarcodeMatch.BarcodeMatchVO;
import com.ruoyi.xkt.domain.StoreProductBarcodeMatch;
import com.ruoyi.xkt.dto.storeProdBarcodeMatch.BarcodeMatchDTO;
import com.ruoyi.xkt.service.IStoreProductBarcodeMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口条形码和第三方系统条形码匹配结果Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/v1/barcode-matches")
public class StoreProductBarcodeMatchController extends XktBaseController {

}
