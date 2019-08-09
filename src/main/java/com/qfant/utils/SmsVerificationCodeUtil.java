package com.qfant.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 手机验证码类
 * 
 * @author Liar
 * 
 */
public class SmsVerificationCodeUtil {
	public static final boolean DEBUG = false;
	public static Map<String,SmsVerificationCodeVo> smsVerificationCodes = new HashMap<>();
//	private final static ReadWriteLock lock = new ReentrantReadWriteLock();
//	private final static Lock readLock = lock.readLock(); // 读锁
//    private final static Lock writeLock = lock.writeLock(); // 写锁
    
    /**
     * 读取验证码
     * @param mobilePhone
     * @return
     */
    public synchronized static String getVerificationCode (String mobilePhone){
    	SmsVerificationCodeVo verificationCodeVo;
//    	readLock.lock();
    	verificationCodeVo=smsVerificationCodes.get(mobilePhone);
//    	 readLock.unlock();
    	 if (verificationCodeVo != null) {
    		 return verificationCodeVo.getVerificationCode();
    	 }
    	return null;
    }
    /**
     * 存入验证码
     * @return
     */
    public synchronized static void setVerificationCode (String mobilePhone,SmsVerificationCodeVo smsVerificationCodeVo){
    	Date now=new Date();
//    	writeLock.lock();
    	if (!smsVerificationCodes.isEmpty()) {
//    		writeLock.unlock();
//    		readLock.lock();
    		List<String> deleteList = new ArrayList<String>(3);
    		Iterator<Map.Entry<String, SmsVerificationCodeVo>> iter = smsVerificationCodes.entrySet().iterator(); 
    		while (iter.hasNext()) {
    		    Map.Entry<String, SmsVerificationCodeVo> entry = iter.next(); 
    		    String key = entry.getKey(); 
    		    SmsVerificationCodeVo val = entry.getValue(); 
    		    long between = (now.getTime()-val.getCreateTime().getTime()) / 1000;//除以1000是为了转换成秒
    		    long minute = between / 60;
    		    if (minute > 5) {//验证码保存十五分钟大于十五分钟的就remove
//    		    	readLock.unlock();
//    		    	writeLock.lock();
    		    	deleteList.add(key);
//    		    	writeLock.unlock();
    		    }
    		}
    		
    		for (String key : deleteList) {
    			smsVerificationCodes.remove(key);
			}
    		
//    		readLock.unlock();
//    		writeLock.lock();
    	}
    	smsVerificationCodes.put(mobilePhone, smsVerificationCodeVo);
//    	writeLock.unlock();
    }
    
    public static void main(String[] args) {
//    	SmsVerificationCodeVo smsVerificationCodeVo=new SmsVerificationCodeVo("13212345678","123456",new Date());
//    	SmsVerificationCodeVo smsVerificationCodeVo2=new SmsVerificationCodeVo("13212345671","123451",new Date());
//    	SmsVerificationCodeVo smsVerificationCodeVo3=new SmsVerificationCodeVo("13212345672","123452",new Date());
//    	SmsVerificationCodeVo smsVerificationCodeVo4=new SmsVerificationCodeVo("13212345673","123453",new Date());
//    	SmsVerificationCodeUtil.setVerificationCode("13212345678", smsVerificationCodeVo);
//    	SmsVerificationCodeUtil.setVerificationCode("13212345671", smsVerificationCodeVo2);
//    	SmsVerificationCodeUtil.setVerificationCode("13212345672", smsVerificationCodeVo3);
//    	SmsVerificationCodeUtil.setVerificationCode("13212345673", smsVerificationCodeVo4);
    	System.out.println(SmsVerificationCodeUtil.randomCode());
    	Calendar.getInstance();
	}
    /**
     * 生成手机验证码
     * @return
     */
    public static String randomCode () {
    	String sRand="";
    	Random random = new Random(); // 生成随机类  
        for (int i = 0; i < 6; i++) {  
            String rand = String.valueOf(random.nextInt(10));  
            sRand += rand;  
        }
        if(DEBUG) {
        	return "123456";
        }
        return sRand;
    }
}
