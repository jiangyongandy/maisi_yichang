package org.cocos2dx.lua.ui.widget;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.cocos2dx.lua.ui.widget.holder.CBViewHolderCreator;
import org.cocos2dx.lua.ui.widget.holder.Holder;

import java.util.List;

/**
 * 功能
 * Created by Jiang on 2017/11/29.
 */

public class BannerPageAdapter<T>  extends PagerAdapter  {

    private final List<T> mDatas;
    private final CBViewHolderCreator holderCreator;

    public BannerPageAdapter(CBViewHolderCreator holderCreator, List datas) {
        this.holderCreator = holderCreator;
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = getView(position, null, container);
//        if(onItemClickListener != null) view.setOnClickListener(onItemClickListener);
        container.addView(view);
        return view;
    }


    public View getView(int position, View view, ViewGroup container) {
        Holder holder = null;
        if (view == null) {
            holder = (Holder) holderCreator.createHolder();
            view = holder.createView(container.getContext());
            view.setTag(holder);
        } else {
            holder = (Holder<T>) view.getTag();
        }
        if (mDatas != null && !mDatas.isEmpty())
            holder.UpdateUI(container.getContext(), position, mDatas.get(position));
        return view;
    }
}
