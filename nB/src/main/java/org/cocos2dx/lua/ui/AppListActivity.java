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
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.maisi.video.obj.video.AppInfo;
import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.AppHelperUtils;
import org.cocos2dx.lua.BoyiRxUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by JIANG on 2017/9/16.
 */

public class AppListActivity extends BaseActivity {

    public static final int RESULT_CODE_CHOOSE_APP_SUCCESS = 1;
    public static final int RESULT_CODE_CHOOSE_APP_CANCEL = 2;
    public List<AppInfo> appsInfos = new ArrayList<>();

    @BindView(R.id.gv_list)
    GridView mGvList;
    @BindView(R.id.tv_loading)
    TextView mTvLoading;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        setContentView(R.layout.activity_app_list);
        mUnbinder = ButterKnife.bind(this);
        BoyiRxUtils.makeObservable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                for (AppUtils.AppInfo appInfo : AppUtils.getAppsInfo()) {
                    AppInfo appInfo1 = new AppInfo(appInfo.getPackageName(), appInfo.getName(), appInfo.getIcon(), appInfo.getPackagePath(), appInfo.getVersionName(), appInfo.getVersionCode(), appInfo.isSystem());
                    appsInfos.add(appInfo1);
                }
                return true;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //showloading
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        ListAdapter listAdapter = new ListAdapter(AppListActivity.this, appsInfos);
                        mGvList.setAdapter(listAdapter);
                        mGvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                boolean isSuccess = AppHelperUtils.saveCollectAPPdata(AppListActivity.this, appsInfos.get(position));
                                if (isSuccess) {

                                    Toast.makeText(
                                            APPAplication.instance,
                                            "已加入收藏",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    setResult(RESULT_CODE_CHOOSE_APP_SUCCESS, intent);
                                    finish();
                                } else {
                                    Toast.makeText(
                                            APPAplication.instance,
                                            "已经收藏过该APP了",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        //隐藏loding
                        mTvLoading.setVisibility(View.GONE);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(
                                APPAplication.instance,
                                throwable.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public class ListAdapter extends BaseAdapter {

        private final List<AppInfo> names;
        private final LayoutInflater inflater;
        private final Context context;

        public ListAdapter(Context context, List<AppInfo> names) {
            this.context = context;
            this.names = names;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return names.size();
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
            ViewHolder holder = null;
            if (convertView == null) {
                rootView = inflater.inflate(R.layout.list_menu_item, parent, false);
                holder = new ViewHolder();
                holder.textView = (TextView) rootView.findViewById(R.id.iv_icon);
                holder.ivLogo = (ImageView) rootView.findViewById(R.id.iv_logo);
                rootView.setTag(holder);
            } else {
                holder = (ViewHolder) rootView.getTag();
            }
            holder.textView.setText(names.get(position).getName());
            Drawable drawable = AppUtils.getAppIcon(names.get(position).getPackageName());
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
