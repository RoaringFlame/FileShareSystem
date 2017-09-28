package com.fss.util.alibaba.message;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fss.util.alibaba.message.smsBean.ResponseBody;
import com.fss.util.alibaba.message.smsBean.TextMessage;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@Component
public class AliSend implements SmsInterface{

	public int sendMessage(TextMessage message) throws IOException {
		ResourceBundle bundle=PropertyResourceBundle.getBundle("sms");
		TaobaoClient client = new DefaultTaobaoClient(bundle.getString("taobaoUrl"),
				bundle.getString("taobaoAppkey"), bundle.getString("taobaoSecret"));
    	AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
    	req.setExtend("123456");
    	req.setSmsType(bundle.getString("smsType"));
    	req.setSmsFreeSignName(bundle.getString("smsFreeSignName"));
    	req.setSmsParamString(message.getContent());
    	req.setRecNum(message.getPhone());

		//后期在判断
    	if(message.getTemplate().equals("phoneBind")){
    		req.setSmsTemplateCode(bundle.getString("smsTemplateCodePhoneBind"));
    	}else if(message.getTemplate().equals("changePassword")){
			req.setSmsTemplateCode(bundle.getString("smsTemplateChangePassword"));
		}else{
			return 2;
		}
    	
    	AlibabaAliqinFcSmsNumSendResponse rsp = null;
		try {
			rsp = client.execute(req);
		} catch (ApiException e) {
			e.printStackTrace();
		}

		assert rsp != null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		ResponseBody responseBody = mapper.readValue(rsp.getBody(), ResponseBody.class);
		return responseBody.getAlibabaAliqinFcSmsNumSendResponse().getResult().getErrCode();
	}
}
