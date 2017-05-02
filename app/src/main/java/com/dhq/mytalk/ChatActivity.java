package com.dhq.mytalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * DESC
 * Created by douhaoqiang on 2017/5/2.
 */

public class ChatActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Fragment> fragList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_lay);

        mViewPager= (ViewPager) findViewById(R.id.chat_pager);

        mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.general_20dp));

        mViewPager.setOffscreenPageLimit(3);

        initFragment();

        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),fragList));
    }


    private void initFragment(){
        for(int i=0;i<5;i++){
            //use EaseChatFratFragment
            ChatFragment chatFragment = new ChatFragment();
            //pass parameters to chat fragment
            Bundle bundle=new Bundle();
            bundle.putInt(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
            bundle.putString(Constant.EXTRA_USER_ID, "156");
            chatFragment.setArguments(bundle);
            fragList.add(chatFragment);
        }
    }

    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        //数据源
        private List<Fragment> fragList = null;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragList) {
            super(fm);
            this.fragList = fragList;

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "标题";
        }

        @Override
        public Fragment getItem(int position) {
            return fragList.get(position);
        }

        @Override
        public int getCount() {
            return fragList.size();
        }
    }

}
