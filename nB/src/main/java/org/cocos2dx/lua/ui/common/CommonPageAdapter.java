package org.cocos2dx.lua.ui.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 功能 viewPager adapter 适配器
 * Created by Jiang on 2017/11/28.
 */

public class CommonPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public CommonPageAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getItem(position).getClass().getSimpleName();
    }
}
