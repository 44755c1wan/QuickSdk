package com.ledi.util;


import org.apache.commons.httpclient.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.huawei.hms.pps.AdChannelInfoClient;
import com.ledi.floatwindow.net.HttpUtilq;


public class Operatee {
	private static Activity activity;
	static Application activity2;
    private static SharedPreferences sp;

	public static PayCallBack payCallBack;// 调用充值回调，会返回到多少钱
	public static Intent isReLandIntent = null;
	public static boolean issuccess = false;
	public static CallbackListener backListener;
	public static LoadPayCallBackLinstener loadPayBackLinstener;
	public static boolean huaweiflag = false;

	public static Intent startServiceIntent;////1111111111111111111111111111111111111111111

	public static void roleInfo1(Activity context,String serverID, 
			String serverName, 
			String gameRoleName, 
			String gameRoleID, 
			String gameUserLevel,
			String VipLevel, 
//			String GameBalance,
//			String GameUserLevel, 
//			String PartyName,
			String roleCreateTime 
//			String PartyId,
//			String GameRoleGender,
//			String GameRolePower,
//			String PartyRoleId, 
//			String PartyRoleName, 
//			String ProfessionId,
//			String Profession,
//			String Friendlist
			){
		activity = context;
		com.ledi.util.Conetq.ServerID = serverID;
		com.ledi.util.Conetq.ServerName = serverName;
		com.ledi.util.Conetq.GameRoleName = gameRoleName;
		com.ledi.util.Conetq.GameRoleID = gameRoleID;
		com.ledi.util.Conetq.GameUserLevel = gameUserLevel;
		com.ledi.util.Conetq.VipLevel = VipLevel;
//		Conetq.GameBalance = GameBalance;
//		Conetq.PartyName = PartyName;
		com.ledi.util.Conetq.RoleCreateTime = roleCreateTime;
//		Conetq.PartyId = PartyId;
//		Conetq.GameRoleGender = GameRoleGender;
//		Conetq.GameRolePower = GameRolePower;
//		Conetq.PartyRoleId = PartyRoleId;
//		Conetq.PartyRoleName = PartyRoleName;
//		Conetq.ProfessionId = ProfessionId;
//		Conetq.Profession = Profession;
//		Conetq.Friendlist = Friendlist;
		com.ledi.util.Conetq.phoneTel = sp_get("phonetell", "").toString();
		com.ledi.util.Conetq.productCode = sp_get("productCode", "").toString();
		com.ledi.util.Conetq.sdkTypes = sp_get("sdkTypes", "").toString();
		com.ledi.util.Conetq.imei2 = sp_get("imei", "").toString();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				NameValuePair[] roleInfo_pair = {						
						new NameValuePair("sid", com.ledi.util.Conetq.ServerID),
						new NameValuePair("sName", UtilOther.gbEncoding(com.ledi.util.Conetq.ServerName)),
						new NameValuePair("roleName", UtilOther.string2Unicode(com.ledi.util.Conetq.GameRoleName)),
						new NameValuePair("roleId", com.ledi.util.Conetq.GameRoleID),
						new NameValuePair("roleLevel", com.ledi.util.Conetq.GameUserLevel),
						new NameValuePair("vip", com.ledi.util.Conetq.VipLevel),
//						new NameValuePair("yuanbao", Conetq.GameBalance),
//						new NameValuePair("roleLevel", Conetq.GameUserLevel),
//						new NameValuePair("partyName", UtilOther.string2Unicode(Conetq.PartyName )),
						new NameValuePair("roleCreateTime", com.ledi.util.Conetq.RoleCreateTime),
//						new NameValuePair("partyId", Conetq.PartyId),
//						new NameValuePair("gameRoleGender", UtilOther.string2Unicode(Conetq.GameRoleGender )),
//						new NameValuePair("gameRolePower", Conetq.GameRolePower),
//						new NameValuePair("partyRoleId", Conetq.PartyRoleId),
//						new NameValuePair("partyRoleName", UtilOther.string2Unicode(Conetq.PartyRoleName)),
//						new NameValuePair("professionId", Conetq.ProfessionId),
//						new NameValuePair("profession", UtilOther.string2Unicode(Conetq.Profession)),
//						new NameValuePair("friendlist", Conetq.Friendlist),
						new NameValuePair("imei", com.ledi.util.Conetq.imei2),
						new NameValuePair("package", com.ledi.util.Conetq.sdkTypes),
						new NameValuePair("productCode", com.ledi.util.Conetq.productCode),
						new NameValuePair("ProductKey", com.ledi.util.Conetq.ProductKey),
						new NameValuePair("phonetel", com.ledi.util.Conetq.phoneTel),
						new NameValuePair("login_uid", com.ledi.util.Conetq.login_uid),//登录后服务器返回的UID，用户唯一标识 Conetq.login_uid
				};
				Log.i("roleInfo_pair", roleInfo_pair+"");

					
					String _roleInfo = HttpUtilq.getData(com.ledi.util.Conetq.roleInfo, roleInfo_pair);//发起请求获取返回数据
					try {
						JSONObject json = new JSONObject(_roleInfo);//将返回的数据转为json对象
						int status1 = json.getInt("status");	
						if(_roleInfo.equals("") || status1 != 1){//如果返回参数为空或者status1不等于1的，开始for循环请求
							for (int i = 0; i < 7; i++) {//7次请求开始
								
								_roleInfo = HttpUtilq.getData(com.ledi.util.Conetq.roleInfo, roleInfo_pair);//发起请求获取返回数据
								if(!_roleInfo.equals("")){//判断循环中的请求是否为空，如果不为空
									try {
										JSONObject jsonObject = new JSONObject(_roleInfo);//将返回的数据转为json对象
										int status = jsonObject.getInt("status");//解析status数据
										if(status == 1){//如果数据为1，将i赋值为7，结束循环
											i = 7;
										}
										
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Log.i("roleInfo", _roleInfo+"上报成功");
				
			}
		}).start();
	}
	public static void Payment(Activity context,String serverID, /////////////1111111111111111111111
			String serverName, 
			String gameRoleName, 
			String gameRoleID, 
//			String gameUserLevel,
//			String VipLevel, 
//			String GameBalance, 
//			String PartyName,
			String CpOrderID,
			String GoodsName,
			int Count,
			double Amount,
			String GoodsID,
			String ExtrasParams,
			final String orderStaus
			){
		activity = context;
		com.ledi.util.Conetq.ServerID = serverID;//服务器ID
		com.ledi.util.Conetq.ServerName = serverName;//服务器名称
		com.ledi.util.Conetq.GameRoleName = gameRoleName;//角色名称
		com.ledi.util.Conetq.GameRoleID = gameRoleID;//角色ID
//		Conetq.GameUserLevel = gameUserLevel;//等级
//		Conetq.VipLevel = VipLevel;//设置当前用户vip等级，必须为整型字符串
//		Conetq.GameBalance = GameBalance;//角色现有金额
//		Conetq.PartyName = PartyName;//设置帮派，公会名称
		com.ledi.util.Conetq.CpOrderID = CpOrderID;
		com.ledi.util.Conetq.GoodsName = GoodsName;
		com.ledi.util.Conetq.Count = Count;
		com.ledi.util.Conetq.Amount = Amount*100;
		com.ledi.util.Conetq.GoodsID = GoodsID;
		com.ledi.util.Conetq.ExtrasParams = ExtrasParams;
		com.ledi.util.Conetq.phoneTel = sp_get("phonetell", "").toString();
		com.ledi.util.Conetq.productCode = sp_get("productCode", "").toString();
		com.ledi.util.Conetq.sdkTypes = sp_get("sdkTypes", "").toString();
		com.ledi.util.Conetq.imei2 = sp_get("imei", "").toString();
		//
		new Thread(new Runnable() {
			@Override
			public void run() {
				final NameValuePair[] roleInfo_pair = {						
						new NameValuePair("sid", com.ledi.util.Conetq.ServerID),
						new NameValuePair("sName",UtilOther.gbEncoding(com.ledi.util.Conetq.ServerName)),
						new NameValuePair("roleName", UtilOther.string2Unicode(com.ledi.util.Conetq.GameRoleName)),
						new NameValuePair("roleId", com.ledi.util.Conetq.GameRoleID),
//						new NameValuePair("roleLevel", Conetq.GameUserLevel),
//						new NameValuePair("vip", Conetq.VipLevel),
//						new NameValuePair("yuanbao", Conetq.GameBalance),
//						new NameValuePair("roleLevel", Conetq.GameUserLevel),
//						new NameValuePair("partyName", UtilOther.string2Unicode(Conetq.PartyName )),
//						new NameValuePair("roleLevel", Conetq.GameUserLevel),
						new NameValuePair("cpOrderID", com.ledi.util.Conetq.CpOrderID),
						new NameValuePair("goodsName", UtilOther.string2Unicode(com.ledi.util.Conetq.GoodsName )),
						new NameValuePair("Count", com.ledi.util.Conetq.Count+""),
						new NameValuePair("Amount", com.ledi.util.Conetq.Amount+""),
						new NameValuePair("goodsID", com.ledi.util.Conetq.GoodsID),
						new NameValuePair("extrasParams", com.ledi.util.Conetq.ExtrasParams),
						new NameValuePair("imei", com.ledi.util.Conetq.imei2),
						new NameValuePair("package", com.ledi.util.Conetq.sdkTypes),
						new NameValuePair("productCode", com.ledi.util.Conetq.productCode),
						new NameValuePair("productKey", com.ledi.util.Conetq.ProductKey),
						new NameValuePair("login_uid", com.ledi.util.Conetq.login_uid),//登录后服务器返回的UID，用户唯一标识
						new NameValuePair("orderStaus",orderStaus),
						new NameValuePair("phonetel", com.ledi.util.Conetq.phoneTel)
						
				};
				Log.i("Payment", roleInfo_pair+"");
				String _roleInfo = HttpUtilq.getData(com.ledi.util.Conetq.payinfo, roleInfo_pair);
				
				
				try {
					JSONObject json = new JSONObject(_roleInfo);//将返回的数据转为json对象
					int status1 = json.getInt("status");	
					if(_roleInfo.equals("") || status1 != 1){//如果返回参数为空或者status1不等于1的，开始for循环请求
						for (int i = 0; i < 7; i++) {//7次请求开始
							
							_roleInfo = HttpUtilq.getData(com.ledi.util.Conetq.payinfo, roleInfo_pair);//发起请求获取返回数据
							if(!_roleInfo.equals("")){//判断循环中的请求是否为空，如果不为空
								try {
									JSONObject jsonObject = new JSONObject(_roleInfo);//将返回的数据转为json对象
									int status = jsonObject.getInt("status");//解析status数据
									if(status == 1){//如果数据为1，将i赋值为7，结束循环
										i = 7;
									}
									
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
				
				
				Log.i("Payment1", _roleInfo+"成功支付");

				
			}
		}).start();
	}
	
	public static void dologin(///////////////111111111111111111111111 
			Activity context, String UID,String UserName){
		activity = context;
		Log.i("sjj","奇天乐地");
		com.ledi.util.Conetq.UID = UID;
		com.ledi.util.Conetq.Username =UserName;
////		String pkName = activity.getPackageName();
//        String[] array =  Conet.packageName.split("\\.");// 截取最后小说点后面的字母
//        Log.i("pkname", array.toString());
//        String s = array[array.length - 1];
//		if(s.equals("vivo") ){
//			Conetq.vivoChannelInfo = UserName.split("@@@")[1];
//		}else{
//			Conetq.vivoChannelInfo = "";
//		}
//		Log.i("sjj", "vivo渠道来源信息"+Conetq.vivoChannelInfo);
		
		com.ledi.util.Conetq.productCode = sp_get("productCode", "").toString();
		com.ledi.util.Conetq.sdkTypes = sp_get("sdkTypes", "").toString();
		com.ledi.util.Conetq.imei2 = sp_get("imei", "").toString();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				NameValuePair[] roleInfo_pair = {						
						new NameValuePair("uID", com.ledi.util.Conetq.UID ),
						new NameValuePair("username", UtilOther.gbEncoding(com.ledi.util.Conetq.Username)),
						new NameValuePair("imei", com.ledi.util.Conetq.imei2),
						new NameValuePair("package", com.ledi.util.Conetq.sdkTypes),
						new NameValuePair("productCode", com.ledi.util.Conetq.productCode),
						new NameValuePair("productKey", com.ledi.util.Conetq.ProductKey),
						new NameValuePair("hwpps_tracking_id", com.ledi.util.Conetq.channelInfo),
						new NameValuePair("hwpps_install_timestamp", com.ledi.util.Conetq.installTimestamp+""),
//						new NameValuePair("vivoChannelInfo",Conetq.vivoChannelInfo),
				};
				Log.i("CCCCC", "hai");
				Log.i("dologin", roleInfo_pair+""); 
				String _roleInfo = HttpUtilq.getData(com.ledi.util.Conetq.logininfo, roleInfo_pair);
				Log.i("dologin", _roleInfo+"登录成功");
				if(null != _roleInfo){
					try {
						JSONObject obj = new JSONObject(_roleInfo);
						if(obj.has("status" ) && obj.getInt("status") == 1){
							com.ledi.util.Conetq.login_uid = obj.getString("login_uid");
							com.ledi.util.Conetq.login_username = obj.getString("login_username");
							if (!obj.getString("phone").isEmpty()) {
								com.ledi.util.Conetq.phoneTel = obj.getString("phone");
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	//这是sdk里面的类，， Context  这个是上下文  因为这个是一个单纯的类，也没有定义上下文，所以，我早这里传了一个上下文  Context context  我测试一下 啥情况
    public static void getChannelInfo(Context context){
    	String huaweippsflag = MetaDataUtil.getMetaDataValue("huaweippsflag", context);
    	Log.i("sjj","奇天乐地"+ huaweippsflag);
    	if(huaweippsflag.equals("false")){
    		huaweiflag = false;
    	}else{
    		huaweiflag = true;
    	}
        try {
        	AdChannelInfoClient.Info info = AdChannelInfoClient.getAdChannelInfo(context, huaweiflag);
        	if (info == null) {
				Log.i("CCCCK", "kong");
			}
            Log.i("CCCCC", "wolaolea");
            if (info != null){
            	Log.i("CCCCC", "llllllllll");
                String channelInfo = info.getChannelInfo(); // 创建广告任务时填入或生成的转化跟踪参数
                long installTimestamp = info.getInstallTimestamp(); // App安装时间
                com.ledi.util.Conetq.channelInfo = channelInfo;
                com.ledi.util.Conetq.installTimestamp = installTimestamp;
                Log.i("CCCCC", channelInfo+":"+installTimestamp+"");
                Log.i("CCCC", com.ledi.util.Conetq.channelInfo+"===="+ Conetq.installTimestamp);
//                Log.i("MainActivity", "getChannelInfo, channelInfo:" + channelInfo + ", installTime:" + installTimestamp);
                // TODO成功获取转化跟踪参数后，在激活消息中携带该参数发送给广告主分析平台，用于广告主转化归因，一定要确保上报成功。
            }
        } catch (Exception e) {
            Log.i("", "getChannelInfo exception", e);
        }
    }
    
    public static void sp_put(String k, Object v){
        sp = activity.getSharedPreferences("installtip", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if(v instanceof String){
            edit.putString(k, (String) v);
        }else if(v instanceof Integer){
            edit.putInt(k, (Integer) v);
        }else if(v instanceof Long) {
            edit.putLong(k, (Long) v);
        }else if(v instanceof Boolean) {
            edit.putBoolean(k, (Boolean) v);
        }else if(v instanceof Float) {
            edit.putFloat(k, (Float) v);
        }else {
            return;
        }
        edit.commit();
    }

    public static Object sp_get(String k, Object v){
        sp = activity.getSharedPreferences("installtip", Context.MODE_PRIVATE);
        if(v instanceof String){
            return sp.getString(k, (String) v);
        }else if(v instanceof Integer) {
            return sp.getInt(k, (Integer) v);
        }else if(v instanceof Long) {
            return sp.getLong(k, (Long) v);
        }else if(v instanceof Boolean) {
            return sp.getBoolean(k, (Boolean) v);
        }else if(v instanceof Float) {
            return sp.getFloat(k, (Float) v);
        }
        return null;
    }

}
