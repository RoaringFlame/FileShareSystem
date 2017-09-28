package com.fss.util.alibaba.message;

import com.fss.util.alibaba.message.smsBean.TextMessage;

import java.io.IOException;

public interface SmsInterface {

	/**
	 * 发送短信
	 * @param message 发送的短信类
	 * @return 返回码 成功0，失败1，未找到模版2
	 */
	int sendMessage(TextMessage message) throws IOException;
}
