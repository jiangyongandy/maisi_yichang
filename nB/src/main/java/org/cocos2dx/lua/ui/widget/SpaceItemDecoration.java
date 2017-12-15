package org.cocos2dx.lua.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Jiang on 2017/3/13.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    public static final int SPAN_COUNT = 3;
    private int mSpace;

    public SpaceItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.top = mSpace;
        outRect.bottom = mSpace;
    }
}