package com.ledi.util;
public interface PayCallBack {
	public void paySuccess(String uid, int money);
	public void payFail(String msg);
}