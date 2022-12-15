package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;

import java.util.Date;

public class TimeDiff extends Function {

    @Override
    public void init() {
        this.setApi(new Api("timeDiff.ng", "获取时间差", false, Constants.API_TAG_GENERAL,
                "获取时间差，格式yyyy-MM-dd HH:mm:ss", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL,
                new Param[]{
                        new Param("time1", false, "时间1，默认为当前时间"),
                        new Param("time2", false, "时间2，默认为当前时间"),
                        new Param("formatType", false, "结果格式，0.毫秒数 1.天数 2.友好文本，默认为0"),
                },
                new Resp(Resp.DataType.string, "时间2-时间1相差的时间")));
    }

    @Override
    public Object handle() {
        String time1 = getParams().get("time1");
        String time2 = getParams().get("time2");
        int formatType = Convert.toInt(getParams().get("formatType"), 0);
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        if (StringUtils.isBlank(time1)) {
            time1 = currentTimeMillis;
        }
        if (StringUtils.isBlank(time2)) {
            time2 = currentTimeMillis;
        }
        Date date1 = parseDate(time1);
        Date date2 = parseDate(time2);
        assert date1 != null;
        assert date2 != null;
        switch (formatType) {
            case 1:
                return DateUtils.differentDaysByMillisecond(date1, date2);
            case 2:
                return DateUtils.getDatePoor2(date2, date1);
            case 0:
            default:
                return DateUtils.differentSecondsByMillisecond(date1, date2);
        }
    }

    private Date parseDate(String time) {
        if (StringUtils.isBlank(time)) {
            return null;
        }
        if (StringUtils.isNumeric(time) && time.length() == 13) {
            return new Date(Long.parseLong(time));
        }
        return DateUtils.parseDate(time);
    }

}
