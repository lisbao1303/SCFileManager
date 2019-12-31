package com.metaconsultoria.root.scfilemanager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyTabAdapter extends FragmentPagerAdapter {

    private Context context;

    public MyTabAdapter(Context mContext, FragmentManager fm){
        super(fm);
        this.context= mContext;
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0){
          return new FragmentTab1();
        }
        if(i==1){
          return new FragmentTab2();
        }else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int i){
        if (i == 0) {
            return context.getString(R.string.tab_1_title);
        }
        if (i == 1) {
            return context.getString(R.string.tab_1_title);
        }
        else{
            return null;
        }
    }
}
