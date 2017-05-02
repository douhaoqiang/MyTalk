package com.dhq.mytalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EMMessageListener msgListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListener();
        initEMListener();
    }


    /**
     *
     */
    private void initListener(){


        findViewById(R.id.regedit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regedit();
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        findViewById(R.id.chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ChatActivity.class);
                intent.putExtra("userId", "1235");
                startActivity(intent);
            }
        });
    }

    private void regedit(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("数据请求中");
        pd.show();

        new Thread(new Runnable() {
            public void run() {
                try {
                    // call method in SDK
                    EMClient.getInstance().createAccount("18310631593","123456");//同步方法
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!MainActivity.this.isFinishing())
                                pd.dismiss();
                            Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();
//                            finish();
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!MainActivity.this.isFinishing())
                                pd.dismiss();
                            int errorCode=e.getErrorCode();
                            if(errorCode== EMError.NETWORK_ERROR){
                                Toast.makeText(getApplicationContext(), "网络失败", Toast.LENGTH_SHORT).show();
                            }else if(errorCode == EMError.USER_ALREADY_EXIST){
                                Toast.makeText(getApplicationContext(), "该用户已经存在", Toast.LENGTH_SHORT).show();
                            }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
                                Toast.makeText(getApplicationContext(), "没有注册权限", Toast.LENGTH_SHORT).show();
                            }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
                                Toast.makeText(getApplicationContext(), "非法参数",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();

    }

    /**
     * 登录
     */
    private void login(){
        EMClient.getInstance().login("18310631593","123456",new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
            }
        });
    }


    /**
     * 退出登录
     */
    private void loginOut(){
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {


            }

            @Override
            public void onProgress(int progress, String status) {


            }

            @Override
            public void onError(int code, String message) {


            }
        });
    }


    /**
     *  连接状态监听
     */
    private void initEMListener(){
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());


        //消息监听
        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);



    }


    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {

        }
        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除

                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录

                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)) {
                            //连接不到聊天服务器

                        }else {
                            //当前网络不可用，请检查网络设置

                        }
                    }
                }
            });
        }
    }

    /**
     * 发送消息（一对一消息 单聊）
     * @param content       消息内容
     * @param chatUserId    聊天人id
     */
    private void sendUserMsg(String content,String chatUserId){
        //创建一条文本消息，content为消息文字内容，chatUserId为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, chatUserId);
        //如果是群聊，设置chattype，默认是单聊
        message.setChatType(EMMessage.ChatType.Chat);
        //消息发送状态监听
        message.setMessageStatusCallback(new EMCallBack(){
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

    }

    /**
     * 发送消息（一对多消息 群聊）
     * @param content       消息内容
     * @param chatUserId    群聊id
     */
    private void sendGroupMsg(String content,String chatUserId){
        //创建一条文本消息，content为消息文字内容，chatUserId为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, chatUserId);
        //如果是群聊，设置chattype，默认是单聊
        message.setChatType(EMMessage.ChatType.GroupChat);
        //消息发送状态监听
        message.setMessageStatusCallback(new EMCallBack(){
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
    }


    @Override
    protected void onDestroy() {
        //记得在不需要的时候移除listener，如在activity的onDestroy()时
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        super.onDestroy();
    }
}
