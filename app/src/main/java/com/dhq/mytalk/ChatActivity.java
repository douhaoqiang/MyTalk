package com.dhq.mytalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.util.EasyUtils;

/**
 * DESC 聊天界面
 * Created by douhaoqiang on 2017/5/2.
 */

public class ChatActivity extends EaseBaseActivity {

    public static ChatActivity activityInstance;
    private ChatBaseFragment chatFragment;
    String chatUserId;//用户的注册id

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);

        EaseUI.getInstance().init(this,new EMOptions());

        activityInstance = this;
        chatUserId = getIntent().getExtras().getString("userId");
        //聊天界面
        chatFragment = new ChatFragment();
        //添加参数
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 确保只能有一个聊天界面
        String userId = intent.getStringExtra("userId");
        if (chatUserId.equals(userId))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public String getChatUserId(){
        return chatUserId;
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
//        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }


}
