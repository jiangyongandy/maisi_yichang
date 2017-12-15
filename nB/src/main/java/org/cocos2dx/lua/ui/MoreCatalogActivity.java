package org.cocos2dx.lua.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.maisi.video.obj.video.UserInfoEntity;
import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.CommonConstant;
import org.cocos2dx.lua.EventBusTag;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.model.UserModel;
import org.cocos2dx.lua.service.Service;
import org.cocos2dx.lua.ui.fragment.HomeFragment;
import org.cocos2dx.lua.ui.widget.NoScrollGridView;
import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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

        if (VipHelperUtils.getInstance().isWechatLogin()) {

            Service.getComnonService().requestLogin(VipHelperUtils.getInstance().getVipUserInfo().getUid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Toast.makeText(
                                    APPAplication.instance,
                                    "错误:" + throwable.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .subscribe(new Subscriber<UserInfoEntity>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(
                                    APPAplication.instance,
                                    e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(UserInfoEntity result) {
                            result.setWeiChatUserInfo(VipHelperUtils.getInstance().getVipUserInfo().getWeiChatUserInfo());
                            VipHelperUtils.getInstance().setVipUserInfo(result);
                            EventBus.getDefault().post(result, EventBusTag.TAG_LOGIN_SUCCESS);
                            if (result.getVipLeft() > 0) {
                                VipHelperUtils.getInstance().setValidVip(true);
                                Intent testIntent = new Intent(MoreCatalogActivity.this, BrowserActivity.class);
                                MoreCatalogActivity.this.startActivity(testIntent);

                            } else {
                                VipHelperUtils.getInstance().setValidVip(false);
                                new AlertDialog.Builder(MoreCatalogActivity.this)
                                        .setTitle("VIP已过期不能愉快的观看啦，是否前往充值？")
                                        .setPositiveButton("立即充值",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        UserModel.getInstance().launchActivity(MoreCatalogActivity.this, ChargeActivity.class);
                                                    }
                                                })
                                        .setNegativeButton("稍等",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        Toast.makeText(
                                                                MoreCatalogActivity.this,
                                                                "取消...",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                        .setOnCancelListener(
                                                new DialogInterface.OnCancelListener() {

                                                    @Override
                                                    public void onCancel(DialogInterface dialog) {
                                                        Toast.makeText(
                                                                MoreCatalogActivity.this,
                                                                "取消...",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }).show();
                            }
                        }
                    });
        } else {

            if (!CommonConstant.isRelease) {
                VipHelperUtils.getInstance().setWechatLogin(true);
                VipHelperUtils.getInstance().setValidVip(true);
                Intent intent = new Intent(this, BrowserActivity.class);
                MoreCatalogActivity.this.startActivity(intent);
                return;
            }
            new AlertDialog.Builder(this)
                    .setTitle("需要登陆")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    UserModel.getInstance().login(MoreCatalogActivity.this);
                                }
                            })
                    .setNegativeButton("否",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Toast.makeText(
                                            MoreCatalogActivity.this,
                                            "拒绝登录无法观看...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .setOnCancelListener(
                            new DialogInterface.OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    Toast.makeText(
                                            MoreCatalogActivity.this,
                                            "取消...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }).show();

        }
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
