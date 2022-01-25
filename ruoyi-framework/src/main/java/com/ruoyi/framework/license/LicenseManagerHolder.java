package com.ruoyi.framework.license;

import com.ruoyi.common.license.CustomLicenseManager;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 * @author huangchen@deepglint.com
 * @desc
 * @date 2019/3/14 13:26
 */
public class LicenseManagerHolder {
    private static volatile LicenseManager LICENSE_MANAGER;

    public static LicenseManager getInstance(LicenseParam param) {
        if (LICENSE_MANAGER == null) {
            synchronized (LicenseManagerHolder.class) {
                if (LICENSE_MANAGER == null) {
                    LICENSE_MANAGER = new CustomLicenseManager(param);
                }
            }
        }
        return LICENSE_MANAGER;
    }
}
