package org.cocos2dx.lua.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.AppUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.maisi.video.obj.video.AppInfo;
import com.zuiai.nn.R;

import org.cocos2dx.lua.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jiang on 2017/3/13.
 */

public class SortableNinePhotoLayout extends RecyclerView {
    public static final int MAX_ITEM_COUNT = 9;
    private static final int MAX_SPAN_COUNT = 5;

    private PhotoAdapter mPhotoAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private Delegate mDelegate;
    private GridLayoutManager mGridLayoutManager;
    private boolean mIsPlusSwitchOpened = true;
    private boolean mIsSortable = true;
    private Activity mActivity;

    public PhotoAdapter getmPhotoAdapter() {
        return mPhotoAdapter;
    }

    public SortableNinePhotoLayout(Context context) {
        this(context, null);
    }

    public SortableNinePhotoLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SortableNinePhotoLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setOverScrollMode(OVER_SCROLL_NEVER);

        initAttrs(context, attrs);

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback());
        mItemTouchHelper.attachToRecyclerView(this);

        mGridLayoutManager = new GridLayoutManager(context, MAX_SPAN_COUNT);
        setLayoutManager(mGridLayoutManager);
        addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.sort_photo_divider)));

        mPhotoAdapter = new PhotoAdapter(R.layout.list_item_write_nphoto, null);
        setAdapter(mPhotoAdapter);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SortableNinePhotoLayout);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.SortableNinePhotoLayout_bga_snpl_isPlusSwitchOpened) {
            mIsPlusSwitchOpened = typedArray.getBoolean(attr, mIsPlusSwitchOpened);
        } else if (attr == R.styleable.SortableNinePhotoLayout_bga_snpl_isSortable) {
            mIsSortable = typedArray.getBoolean(attr, mIsSortable);
        }
    }

    public void init(Activity activity) {
        mActivity = activity;
    }

    /**
     * 设置是否可拖拽排序
     *
     * @param isSortable
     */
    public void setIsSortable(boolean isSortable) {
        mIsSortable = isSortable;
    }

    /**
     * 更新图片路径数据集合
     *
     * @param photos
     */
    public void setData(List<AppInfo> photos) {
        if (mActivity == null) {
            throw new RuntimeException("请先调用init方法进行初始化");
        }

        mPhotoAdapter.setNewData(photos);
        updateHeight();
    }

    private void updateHeight() {
        if (mPhotoAdapter.getItemCount() > 0 && mPhotoAdapter.getItemCount() < MAX_SPAN_COUNT) {
            mGridLayoutManager.setSpanCount(mPhotoAdapter.getItemCount());
        } else {
            mGridLayoutManager.setSpanCount(MAX_SPAN_COUNT);
        }
        int itemWidth = (UiUtils.getScreenWidth() - UiUtils.dip2px(getContext(), 10) - UiUtils.dip2px(getContext(), 5))/ (MAX_SPAN_COUNT );
        int width = itemWidth * mGridLayoutManager.getSpanCount();
        int height = 0;
        if (mPhotoAdapter.getItemCount() != 0) {
            int rowCount = (mPhotoAdapter.getItemCount() - 1) / mGridLayoutManager.getSpanCount() + 1;
            height = itemWidth * rowCount;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        setLayoutParams(layoutParams);
    }

    /**
     * 获取图片路劲数据集合
     *
     * @return
     */
    public ArrayList<AppInfo > getData() {
        return (ArrayList<AppInfo >) mPhotoAdapter.getData();
    }

    /**
     * 删除指定索引位置的图片
     *
     * @param position
     */
    public void removeItem(int position) {
        mPhotoAdapter.remove(position);
        updateHeight();
    }

    public void setIsPlusSwitchOpened(boolean isPlusSwitchOpened) {
        mIsPlusSwitchOpened = isPlusSwitchOpened;
        updateHeight();
    }

    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    public class PhotoAdapter extends BaseQuickAdapter<AppInfo, SortableNinePhotoItem> {

        public PhotoAdapter(int layoutResId, List<AppInfo> data) {
            super(layoutResId, data);
        }

        public boolean isPlusItem(int position) {
            return mIsPlusSwitchOpened && super.getItemCount() < MAX_ITEM_COUNT && position == getItemCount() - 1;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }


        @Override
        public void onBindViewHolder(SortableNinePhotoItem holder, int positions) {
            int viewType = holder.getItemViewType();

            switch (viewType) {
                case 0:
                    convert(holder, getItem(positions));
                    break;
                default:
                    convert(holder, mData.get(holder.getLayoutPosition() - getHeaderLayoutCount()));
                    break;
            }
        }

        @Override
        protected SortableNinePhotoItem createBaseViewHolder(ViewGroup parent, int layoutResId) {
            return new SortableNinePhotoItem(getItemView(layoutResId, parent));
        }

        @Override
        protected void convert(final SortableNinePhotoItem helper, final AppInfo  item) {

            if (isPlusItem(helper.getAdapterPosition())) {
                helper.ivItemNinePhotoFlag.setVisibility(View.GONE);
                helper.ivItemNinePhotoPhoto.setImageResource(R.drawable.community_write_plus);
                helper.ivItemNinePhotoPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
                helper.ivItemNinePhotoPhoto.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDelegate != null) {
                            mDelegate.onClickAddNinePhotoItem(SortableNinePhotoLayout.this, v, helper.getAdapterPosition(), (ArrayList<AppInfo >) mPhotoAdapter.getData());
                        }
                    }
                });
                return;
            }else {
                helper.ivItemNinePhotoFlag.setVisibility(View.VISIBLE);
                Drawable drawable = AppUtils.getAppIcon(item.getPackageName());
//                holder.textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                helper.ivItemNinePhotoPhoto.setImageDrawable(drawable);
            }
            helper.ivItemNinePhotoFlag.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDelegate != null) {
                        mDelegate.onClickDeleteNinePhotoItem( v, helper.getAdapterPosition(),item, (ArrayList<AppInfo >) mPhotoAdapter.getData());
                    }
                }
            });

            helper.ivItemNinePhotoPhoto.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDelegate.onClickNinePhotoItem( SortableNinePhotoLayout.this, v, helper.getAdapterPosition(), item, (ArrayList<AppInfo >) mPhotoAdapter.getData());
                }
            });

        }

        @Override
        public int getItemCount() {
            if (mIsPlusSwitchOpened && super.getItemCount() < MAX_ITEM_COUNT) {
                return super.getItemCount() + 1;
            }

            return super.getItemCount();
        }

        @Override
        public AppInfo  getItem(int position) {
            if (isPlusItem(position)) {
                return null;
            }

            return super.getItem(position);
        }

        /**
         * 移动数据条目的位置
         *
         * @param fromPosition
         * @param toPosition
         */
        public void moveItem(int fromPosition, int toPosition) {
            mData.add(toPosition, mData.remove(fromPosition));
            notifyItemMoved(fromPosition, toPosition);
        }

    }

    public static class SortableNinePhotoItem extends BaseViewHolder {
        @BindView(R.id.iv_item_nine_photo_photo)
        SquareImageView ivItemNinePhotoPhoto;
        @BindView(R.id.iv_item_nine_photo_flag)
        ImageView ivItemNinePhotoFlag;

        SortableNinePhotoItem(View itemView) {
            super( itemView);
            ButterKnife.bind(this , itemView);
        }
    }


    private class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

        @Override
        public boolean isLongPressDragEnabled() {
            return mIsSortable && mPhotoAdapter.getData().size() > 1;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            if (mPhotoAdapter.isPlusItem(viewHolder.getAdapterPosition())) {
                return ItemTouchHelper.ACTION_STATE_IDLE;
            }

            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
            int swipeFlags = dragFlags;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder source, ViewHolder target) {
            if (source.getItemViewType() != target.getItemViewType() || mPhotoAdapter.isPlusItem(target.getAdapterPosition())) {
                return false;
            }
            mPhotoAdapter.moveItem(source.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(ViewHolder viewHolder, int direction) {
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                ViewCompat.setScaleX(viewHolder.itemView, 1.2f);
                ViewCompat.setScaleY(viewHolder.itemView, 1.2f);
//                ((SortableNinePhotoItem ) viewHolder).ivItemNinePhotoPhoto.setColorFilter(getResources().getColor(R.color.bga_pp_photo_selected_mask));
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
            ViewCompat.setScaleX(viewHolder.itemView, 1.0f);
            ViewCompat.setScaleY(viewHolder.itemView, 1.0f);
            ((SortableNinePhotoItem ) viewHolder).ivItemNinePhotoPhoto.setColorFilter(null);
            super.clearView(recyclerView, viewHolder);
        }
    }

    public interface Delegate {
        void onClickAddNinePhotoItem(SortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<AppInfo > models);

        void onClickDeleteNinePhotoItem(View view, int position, AppInfo  model, ArrayList<AppInfo > models);

        void onClickNinePhotoItem(SortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, AppInfo  model, ArrayList<AppInfo > models);
    }
}