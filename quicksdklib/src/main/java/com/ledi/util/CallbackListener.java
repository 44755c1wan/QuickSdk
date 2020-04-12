package com.ledi.util;

public interface CallbackListener {
	public void loginReback(String session_id, String uid);
	public void loginBackKey(boolean back);
	public boolean init(boolean init);
}
