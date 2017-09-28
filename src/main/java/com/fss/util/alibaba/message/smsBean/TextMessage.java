package com.fss.util.alibaba.message.smsBean;

public class TextMessage {

	/**
	 * 短信内容
	 */
	private String content;
	
	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 短信模板
	 */
	private String template;
	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}

