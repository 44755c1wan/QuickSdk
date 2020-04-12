package com.ledi.bean;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import android.util.Log;

public class MsgManager {
	 private static Timer timer = new Timer();
	 private static Map<com.ledi.bean.MsgKey, com.ledi.bean.MsgTask> msgTasks = Collections
	   .synchronizedMap(new HashMap<com.ledi.bean.MsgKey, com.ledi.bean.MsgTask>());

	 public static void putMsgTask(com.ledi.bean.MsgKey msgKey,
                                   com.ledi.bean.MsgTask msgTask) {
	  synchronized (msgTasks) {
	   msgTasks.put(msgKey, msgTask);
	  }
	 }

	 public static void startMsgTask(com.ledi.bean.MsgKey msgKey,
                                     com.ledi.bean.MsgTask msgTask) {
	  putMsgTask(msgKey, msgTask);
	  timer.schedule(msgTask, msgTask.getDeltaTime());
	  Log.d("zyr",""+msgKey.getIndex());
	 }

	 public static com.ledi.bean.MsgTask removeMsgTask(com.ledi.bean.MsgKey msgKey) {
	  com.ledi.bean.MsgTask msgTask = null;
	  synchronized (msgTasks) {
	   msgTask = msgTasks.remove(msgKey);
	  }
	  return msgTask;
	 }

	 public static boolean stopMsgTask(MsgKey msgKey) {
	  MsgTask msgTask = removeMsgTask(msgKey);
	  if(msgTask != null)
	  {
	   msgTask.cancel();
	   return true;
	  }
	  return false;
	 }

	}
