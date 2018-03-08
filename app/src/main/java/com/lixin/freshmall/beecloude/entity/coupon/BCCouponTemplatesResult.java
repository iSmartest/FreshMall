package com.lixin.freshmall.beecloude.entity.coupon;

import com.lixin.freshmall.beecloude.entity.BCRestfulCommonResult;

import java.util.List;

/**
 * Created by xuanzhui on 2017/8/17.
 * 根据条件查询卡券模板结果
 */

public class BCCouponTemplatesResult extends BCRestfulCommonResult {
    private List<BCCouponTemplate> coupon_templates;

    public BCCouponTemplatesResult(Integer resultCode, String resultMsg, String errDetail) {
        super(resultCode, resultMsg, errDetail);
    }

    /**
     * @return 卡券模板列表
     */
    public List<BCCouponTemplate> getCouponTemplates() {
        return coupon_templates;
    }
}
