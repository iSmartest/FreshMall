package com.lixin.freshmall.beecloude;

import com.lixin.freshmall.beecloude.entity.coupon.BCCouponCriteria;
import com.lixin.freshmall.beecloude.entity.coupon.BCCouponResult;
import com.lixin.freshmall.beecloude.entity.coupon.BCCouponTemplateCriteria;
import com.lixin.freshmall.beecloude.entity.coupon.BCCouponTemplateResult;
import com.lixin.freshmall.beecloude.entity.coupon.BCCouponTemplatesResult;
import com.lixin.freshmall.beecloude.entity.coupon.BCCouponsResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuanzhui on 2017/8/17.
 * 用于营销卡券
 */

public class BCSalesService {

    public BCCouponResult createCoupon(String templateId, String userId) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("template_id", templateId);
        reqParams.put("user_id", userId);

        String url = BCHttpClientUtil.BEECLOUD_HOST + "/2/rest/coupon";

        return (BCCouponResult) BCHttpClientUtil.addRestObject(url, reqParams, BCCouponResult.class, true);
    }

    public BCCouponResult queryCoupon(String id) {
        String url = BCHttpClientUtil.BEECLOUD_HOST + "/2/rest/coupon";

        return (BCCouponResult) BCHttpClientUtil.queryRestObjectById(url, id, BCCouponResult.class,
                true, true);
    }

    public BCCouponsResult queryCoupons(BCCouponCriteria couponCriteria) {
        String url = BCHttpClientUtil.BEECLOUD_HOST + "/2/rest/coupon";

        return (BCCouponsResult) BCHttpClientUtil.queryRestObjects(url,
                BCHttpClientUtil.objectToMap(couponCriteria), BCCouponsResult.class,
                true, true);
    }

    public BCCouponTemplateResult queryCouponTemplate(String id) {
        String url = BCHttpClientUtil.BEECLOUD_HOST + "/2/rest/coupon/template";

        return (BCCouponTemplateResult) BCHttpClientUtil.queryRestObjectById(url, id,
                BCCouponTemplateResult.class, true, true);
    }

    public BCCouponTemplatesResult queryCouponTemplates(BCCouponTemplateCriteria templateCriteria) {
        String url = BCHttpClientUtil.BEECLOUD_HOST + "/2/rest/coupon/template";

        return (BCCouponTemplatesResult) BCHttpClientUtil.queryRestObjects(url,
                BCHttpClientUtil.objectToMap(templateCriteria), BCCouponTemplatesResult.class,
                true, true);
    }
}
