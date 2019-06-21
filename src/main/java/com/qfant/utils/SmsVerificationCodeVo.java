package com.qfant.utils;

import java.util.Date;

/**
 * 手机验证码vo
 * 
 * @author Liar
 * 
 */
public class SmsVerificationCodeVo {

	private String mobilePhone;
	private String verificationCode;
	private Date createTime;

	public SmsVerificationCodeVo (String mobilePhone,String verificationCode,Date createTime) {
		this.mobilePhone=mobilePhone;
		this.verificationCode=verificationCode;
		this.createTime=createTime;
	}
	
	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
