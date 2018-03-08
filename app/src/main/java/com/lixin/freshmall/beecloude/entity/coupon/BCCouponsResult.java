package com.lixin.freshmall.beecloude.entity.coupon;

import com.lixin.freshmall.beecloude.entity.BCRestfulCommonResult;

import java.util.List;

/**
 * Created by xuanzhui on 2017/8/17.
 * 根据条件查询
 */

public class BCCouponsResult extends BCRestfulCommonResult {
    private List<BCCoupon> coupons;

    public List<BCCoupon> getCoupons() {
        return coupons;
    }

    public BCCouponsResult(Integer resultCode, String resultMsg, String errDetail) {
        super(resultCode, resultMsg, errDetail);
    }
}
