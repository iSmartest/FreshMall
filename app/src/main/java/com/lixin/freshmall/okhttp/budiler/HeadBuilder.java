package com.lixin.freshmall.okhttp.budiler;

import com.lixin.freshmall.okhttp.OkHttpUtils;
import com.lixin.freshmall.okhttp.budiler.budiler.GetBuilder;
import com.lixin.freshmall.okhttp.budiler.budiler.OtherRequest;
import com.lixin.freshmall.okhttp.budiler.budiler.RequestCall;

/**
 * Created by 小火
 * Create time on  2017/3/22
 * My mailbox is 1403241630@qq.com
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
