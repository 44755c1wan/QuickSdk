package com.qt.quicksdk.demo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.ledi.activity_393.QuickDialog;
import com.ledi.util.Operatee;
import com.quicksdk.QuickSDK;
import com.quicksdk.Sdk;
import com.quicksdk.entity.GameRoleInfo;
import com.quicksdk.entity.OrderInfo;
import com.quicksdk.entity.UserInfo;
import com.quicksdk.notifier.ExitNotifier;
import com.quicksdk.notifier.InitNotifier;
import com.quicksdk.notifier.LoginNotifier;
import com.quicksdk.notifier.LogoutNotifier;
import com.quicksdk.notifier.PayNotifier;
import com.quicksdk.notifier.SwitchAccountNotifier;

import java.util.UUID;

public class MainActivity extends Activity implements OnClickListener {

    private TextView infoTv;
    public static GameRoleInfo roleInfo;
    public static OrderInfo orderInfo;
    boolean isLandscape = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        infoTv = (TextView) findViewById(R.id.tv_userInfo);

//		获取转化跟踪参数，需要使用子线程获取  11111111111111111111111111111    
        new Thread(new Runnable() {
            public void run() {
                Operatee.getChannelInfo(getApplicationContext());//在这里我传入的当前的上下文  BaseQuickLifecycleActivity.this   //1111111111111111111111
            }
        }).start();


        // 设置横竖屏，游戏横屏为true，游戏竖屏为false(必接)
        QuickSDK.getInstance().setIsLandScape(isLandscape);

        try {

            //check权限
            if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                    || (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                //没有 ，  申请权限  权限数组
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                intiQuick();
            }
        } catch (Exception e) {
            //异常  继续申请
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        // 生命周期接口调用(必接)
        com.quicksdk.Sdk.getInstance().onCreate(this);
    }

    private void intiQuick() {
        // 有 则执行初始化
        // 设置通知，用于监听初始化，登录，注销，支付及退出功能的返回值(必接)
        initQkNotifiers();
        // 请将下面语句中的第二与第三个参数，替换成QuickSDK后台申请的productCode和productKey值，目前的值仅作为示例
        Sdk.getInstance().init(MainActivity.this, "24620274650134332748862293180252", "72585222");

    }

    //申请权限的回调（结果）这是一个类似生命周期的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //申请成功
            intiQuick();
        } else {
            //失败  这里逻辑以游戏为准 这里只是模拟申请失败 cp方可改为继续申请权限 或者退出游戏 或者其他逻辑
            //失败  这里逻辑以游戏为准 这里只是模拟申请失败 退出游戏    cp方可改为继续申请 或者其他逻辑
            System.exit(0);
            finish();
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        // 登录
        if (id == R.id.btn_login) {
            com.quicksdk.User.getInstance().login(MainActivity.this);
        }

        // 提交角色信息
        if (id == R.id.btn_uploadUserInfo) {
            setUserInfo();
        }

        // 注销
        if (id == R.id.btn_logout) {
            com.quicksdk.User.getInstance().logout(MainActivity.this);
        }

        // 支付
        if (id == R.id.btn_pay) {
            pay();
        }

        // 退出
        if (id == R.id.btn_finish) {
            exit();
        }
    }

    /**
     * 支付
     */
    private void pay() {
        roleInfo = new GameRoleInfo();
        roleInfo.setServerID("1");// 服务器ID，其值必须为数字字符串
        roleInfo.setServerName("火星5服");// 服务器名称
        roleInfo.setGameRoleName("裁决之剑");// 角色名称
        roleInfo.setGameRoleID("1121121");// 角色ID
        roleInfo.setGameUserLevel("12");// 等级
        roleInfo.setVipLevel("Vip4");// VIP等级
        roleInfo.setGameBalance("5000");// 角色现有金额
        roleInfo.setPartyName("");// 公会名字
        roleInfo.setRoleCreateTime("2020-02-09");

        orderInfo = new OrderInfo();
        orderInfo.setCpOrderID(UUID.randomUUID().toString().replace("-", ""));// 游戏订单号
        orderInfo.setGoodsName("元宝");// 产品名称
        // orderInfo.setGoodsName("月卡");
        orderInfo.setCount(10);// 购买数量，如购买"10元宝"则传10
        // orderInfo.setCount(1);// 购买数量，如购买"月卡"则传1
        orderInfo.setAmount(10); // 总金额（单位为元）
        orderInfo.setPrice(10);
        orderInfo.setGoodsID("101"); // 产品ID，用来识别购买的产品
        orderInfo.setGoodsDesc("11");

        orderInfo.setExtrasParams("透传参数"); // 透传参数

        com.quicksdk.Payment.getInstance().pay(MainActivity.this, orderInfo, roleInfo);
    }

    /**
     * 退出
     */
    private void exit() {
        // 先判断渠道是否有退出框，如果有则直接调用quick的exit接口
        if (QuickSDK.getInstance().isShowExitDialog()) {
            Sdk.getInstance().exit(MainActivity.this);
        } else {
            // 游戏调用自身的退出对话框，点击确定后，调用quick的exit接口
            new AlertDialog.Builder(MainActivity.this).setTitle("退出").setMessage("是否退出游戏?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Sdk.getInstance().exit(MainActivity.this);
                }
            }).setNegativeButton("取消", null).show();
        }
    }

    /**
     * 向渠道提交用户信息。 在创建游戏角色、进入游戏和角色升级3个地方调用此接口，当创建角色时最后一个参数值为true，其他两种情况为false。
     * GameRoleInfo所有字段均不能传null，游戏没有的字段请传一个默认值或空字符串。
     */
    public void setUserInfo() {
        roleInfo = new GameRoleInfo();
        roleInfo.setServerID("1");// 服务器ID
        roleInfo.setServerName("火星38服");// 服务器名称
        roleInfo.setGameRoleName("裁决之剑");// 角色名称
        roleInfo.setGameRoleID("1121121");// 角色ID
        roleInfo.setGameUserLevel("12");// 等级
        roleInfo.setVipLevel("9"); // 设置当前用户vip等级，必须为整型字符串
        roleInfo.setGameBalance("5000"); // 角色现有金额
        roleInfo.setGameUserLevel("12"); // 设置游戏角色等级
        roleInfo.setPartyName("无敌联盟"); // 设置帮派，公会名称
        roleInfo.setRoleCreateTime("1473141432"); // UC与1881渠道必传，值为10位数时间戳
        roleInfo.setPartyId("1100"); // 360渠道参数，设置帮派id，必须为整型字符串
        roleInfo.setGameRoleGender("男"); // 360渠道参数
        roleInfo.setGameRolePower("38"); // 360渠道参数，设置角色战力，必须为整型字符串
        roleInfo.setPartyRoleId("11"); // 360渠道参数，设置角色在帮派中的id
        roleInfo.setPartyRoleName("帮主"); // 360渠道参数，设置角色在帮派中的名称
        roleInfo.setProfessionId("38"); // 360渠道参数，设置角色职业id，必须为整型字符串
        roleInfo.setProfession("法师"); // 360渠道参数，设置角色职业名称
        roleInfo.setFriendlist("无"); // 360渠道参数，设置好友关系列表，格式请参考：http://open.quicksdk.net/help/detail/aid/190
        com.quicksdk.User.getInstance().setGameRoleInfo(this, roleInfo, true);

        //1~服务器id 2~服务器名称 3~游戏角色名称 4~游戏角色id 5~游戏角色等级 6~vip等级 7~角色创建时间
        Operatee.roleInfo1(MainActivity.this, roleInfo.getServerID(), roleInfo.getServerName(), roleInfo.getGameRoleName(),
                roleInfo.getGameRoleID(), roleInfo.getGameRoleLevel(), roleInfo.getVipLevel(),
                roleInfo.getRoleCreateTime());///111111111111111111111111111
        System.out.println("角色:" + roleInfo.getServerName());

    }

    /**
     * 设置通知，用于监听初始化，登录，注销，支付及退出功能的返回值
     */
    private void initQkNotifiers() {
        QuickSDK.getInstance()
                // 1.设置初始化通知(必接)
                .setInitNotifier(new InitNotifier() {

                    public void onSuccess() {
                        infoTv.setText("初始化成功");
                        //参数：1.当前的上下文  2.productCode 3.ProductKey
                        QuickDialog.quick(MainActivity.this, "24620274650134332748862293180252", "72585222");//11111111111111
                    }

                    public void onFailed(String message, String trace) {
                        infoTv.setText("初始化失败:" + message);
                    }
                })
                // 2.设置登录通知(必接)
                .setLoginNotifier(new LoginNotifier() {
                    public void onSuccess(UserInfo userInfo) {
                        if (userInfo != null) {
                            infoTv.setText("登陆成功" + "\n\r" + "UserID:  " + userInfo.getUID() + "\n\r" + "UserName:  " + userInfo.getUserName() + "\n\r"
                                    + "Token:  " + userInfo.getToken());
//							1~用户id 2~用户名称
                            Operatee.dologin(MainActivity.this, userInfo.getUID(), userInfo.getUserName());
                            Log.i("UIDDDDDDDDDDDDDDDDD", userInfo.getUID());
                            setUserInfo();
                        }
                    }

                    public void onCancel() {
                        infoTv.setText("取消登陆");
                    }

                    public void onFailed(final String message, String trace) {
                        infoTv.setText("登陆失败:" + message);
                    }

                })
                // 3.设置注销通知(必接)
                .setLogoutNotifier(new LogoutNotifier() {

                    public void onSuccess() {
                        infoTv.setText("注销成功");
                    }

                    public void onFailed(String message, String trace) {
                        infoTv.setText("注销失败:" + message);
                    }
                })
                // 4.设置切换账号通知(必接)
                .setSwitchAccountNotifier(new SwitchAccountNotifier() {

                    public void onSuccess(UserInfo userInfo) {
                        if (userInfo != null) {
                            infoTv.setText("切换账号成功" + "\n\r" + "UserID:  " + userInfo.getUID() + "\n\r" + "UserName:  " + userInfo.getUserName() + "\n\r"
                                    + "Token:  " + userInfo.getToken());


                        }
                    }

                    public void onFailed(String message, String trace) {
                        infoTv.setText("切换账号失败:" + message);
                    }

                    public void onCancel() {
                        infoTv.setText("取消切换账号");
                    }
                })
                // 5.设置支付通知(必接)
                .setPayNotifier(new PayNotifier() {

                    public void onSuccess(String sdkOrderID, String cpOrderID, String extrasParams) {
                        infoTv.setText("支付成功，sdkOrderID:" + sdkOrderID + ",cpOrderID:" + cpOrderID);

                        //1~ServerID-服务器ID、 2~ServerName-服务器名称、3~GameRoleName-角色名称、4~GameRoleID-角色ID、
                        //5~订单id 6~商品名称 7~商品数量 8~金额（单位元） 9~商品id 10~透传参数  11~支付状态 成功为success失败或取消为fail
                        Operatee.Payment(MainActivity.this, roleInfo.getServerID(), roleInfo.getServerName(), roleInfo.getGameRoleName(),
                                roleInfo.getGameRoleID(), orderInfo.getCpOrderID(), orderInfo.getGoodsName(), orderInfo.getCount(),
                                orderInfo.getAmount(), orderInfo.getGoodsID(), orderInfo.getExtrasParams(), "success"
                        );//11111111111111111111111111111111
                    }

                    public void onCancel(String cpOrderID) {
                        infoTv.setText("支付取消，cpOrderID:" + cpOrderID);
                        Operatee.Payment(MainActivity.this, roleInfo.getServerID(), roleInfo.getServerName(), roleInfo.getGameRoleName(),
                                roleInfo.getGameRoleID(), orderInfo.getCpOrderID(), orderInfo.getGoodsName(), orderInfo.getCount(),
                                orderInfo.getAmount(), orderInfo.getGoodsID(), orderInfo.getExtrasParams(), "fail"
                        );//11111111111111111111111111111111
                    }

                    public void onFailed(String cpOrderID, String message, String trace) {
                        infoTv.setText("支付失败:" + "pay failed,cpOrderID:" + cpOrderID + ",message:" + message);
                        Operatee.Payment(MainActivity.this, roleInfo.getServerID(), roleInfo.getServerName(), roleInfo.getGameRoleName(),
                                roleInfo.getGameRoleID(), orderInfo.getCpOrderID(), orderInfo.getGoodsName(), orderInfo.getCount(),
                                orderInfo.getAmount(), orderInfo.getGoodsID(), orderInfo.getExtrasParams(), "fail"
                        );//11111111111111111111111111111111
                    }
                })
                // 6.设置退出通知(必接)
                .setExitNotifier(new ExitNotifier() {

                    public void onSuccess() {
                        // 进行游戏本身的退出操作，下面的finish()只是示例
                        finish();
                    }

                    public void onFailed(String message, String trace) {
                        infoTv.setText("退出失败：" + message);
                    }
                });
    }


}
