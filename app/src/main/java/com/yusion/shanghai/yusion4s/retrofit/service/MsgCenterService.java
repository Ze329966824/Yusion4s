package com.yusion.shanghai.yusion4s.retrofit.service;

import com.yusion.shanghai.yusion4s.base.BaseResult;
import com.yusion.shanghai.yusion4s.bean.msg_center.GetMsgList;
import com.yusion.shanghai.yusion4s.bean.msg_center.GetMsgStatus;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by LX on 2017/11/21.
 */

public interface MsgCenterService {
    @GET("api/message/new_message_status/")
    Call<BaseResult<GetMsgStatus>> getMessageStatus();

    @POST("api/message/read_new_msg/")
    Call<BaseResult> clearRedPoint();

    @GET("api/message/msg_list/")
    Call<BaseResult<GetMsgList>> getMessageList();
}
