package org.cocos2dx.lua.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zuiai.nn.R;

import org.cocos2dx.lua.CommonConstant;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.model.UserModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class MoreCatalogActivity extends BaseActivity {

    @BindView(R.id.gv_list)
    GridView  mGvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_more_catalog);
        ButterKnife.bind(this);

        ListAdapter listAdapter = new ListAdapter(this, VipHelperUtils.getInstance().getNames());
        mGvList.setAdapter(listAdapter);
        mGvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wechatLogin(position);
            }
        });
    }

    private void wechatLogin(int position) {
        VipHelperUtils.getInstance().changeCurrentSite(position);
        //test
        if (CommonConstant.isDebug) {

            Intent testIntent = new Intent(this, BrowserActivity.class);
            this.startActivity(testIntent);
            return;
        }
        UserModel.getInstance().launchActivity(MoreCatalogActivity.this, BrowserActivity.class, true);

    }


    public class ListAdapter extends BaseAdapter {

        private final String[] names;
        private final LayoutInflater inflater;
        private final Context context;

        public ListAdapter(Context context, String[] names) {
            this.context = context;
            this.names = names;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootView = convertView;
            ListAdapter.ViewHolder holder = null;
            if (convertView == null) {
                rootView = inflater.inflate(R.layout.list_menu_item2, parent, false);
                holder = new ListAdapter.ViewHolder();
                holder.textView = (TextView) rootView.findViewById(R.id.iv_icon);
                holder.ivLogo = (ImageView) rootView.findViewById(R.id.iv_logo);
                rootView.setTag(holder);
            } else {
                holder = (ListAdapter.ViewHolder) rootView.getTag();
            }
            holder.textView.setText(VipHelperUtils.getInstance().getNames()[position]);
            Drawable drawable = context.getResources().getDrawable(VipHelperUtils.getInstance().getIcons()[position]);
//                holder.textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            holder.ivLogo.setImageDrawable(drawable);
            return rootView;
        }

        class ViewHolder {
            TextView textView;
            ImageView ivLogo;
        }

    }
}
