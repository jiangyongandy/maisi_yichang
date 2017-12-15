package org.cocos2dx.lua.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.maisi.video.obj.video.UpdateEntity;
import com.umeng.socialize.UMShareAPI;
import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.DataHelper;
import org.cocos2dx.lua.DownLoadUtil;
import org.cocos2dx.lua.service.Service;
import org.cocos2dx.lua.ui.common.CommonPageAdapter;
import org.cocos2dx.lua.ui.fragment.HomeFragment;
import org.cocos2dx.lua.ui.fragment.PersonFragment2;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by JIANG on 2017/9/16.
 */

public class MainActivity extends BaseActivity {

    private static final String IS_FIRST_RUN_TIPS = "IS_FIRST_RUN_TIPS";
    @BindView(R.id.rb_niuying)
    RadioButton mRbNiuying;
    @BindView(R.id.rb_person)
    RadioButton mRbPerson;
    @BindView(R.id.rb)
    RadioGroup mRb;
    @BindView(R.id.fragment_container)
    ViewPager mFragmentContainer;
    @BindView(R.id.tv_know)
    TextView mTvKnow;
    @BindView(R.id.rl_guide)
    RelativeLayout mRlGuide;
    private Unbinder mUnbinder;
    private final String mAboutUrl = "file:///android_asset/about/index.html";
    private Fragment mHomeFragment;
    private Fragment mPersonFragment;
    private int currentTabIndex;
    private Fragment[] fragments;
    private RadioButton[] rbBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
        }
        return true;
    }

    private void init() {
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        mHomeFragment = new HomeFragment();
        mPersonFragment = new PersonFragment2();
        fragments = new Fragment[]{mHomeFragment, mPersonFragment};
        rbBtns = new RadioButton[]{mRbNiuying, mRbPerson};
        mFragmentContainer.setAdapter(new CommonPageAdapter(getSupportFragmentManager(), Arrays.asList(fragments)));
        mFragmentContainer.setOffscreenPageLimit(fragments.length);
        mFragmentContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentTabState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_niuying:
                        mFragmentContainer.setCurrentItem(0);
                        break;
                    case R.id.rb_person:
                        mFragmentContainer.setCurrentItem(1);
                        break;
                }
            }
        });
        currentTabIndex = 0;
//guide
        boolean isFirstRun = DataHelper.getBoolSp(this, IS_FIRST_RUN_TIPS, true);
        if(isFirstRun) {
            mRlGuide.setVisibility(View.VISIBLE);
            mTvKnow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRlGuide.setVisibility(View.GONE);
                }
            });
        }
        DataHelper.setBoolSF(this, IS_FIRST_RUN_TIPS, false);

        //版本更新
        Service.getComnonService().versionUpdate()
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
                .subscribe(new Subscriber<UpdateEntity>() {
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
                    public void onNext(final UpdateEntity result) {
                        if (Integer.parseInt(result.getValue1()) > AppUtils.getAppVersionCode()) {

                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("有最新版本是否立即下载？（强烈推荐下载获取更稳定体验！！）")
                                    .setPositiveButton("马上下载",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    if (result.getValue2() != null) {
                                                        DownLoadUtil downLoadUtil = new DownLoadUtil(MainActivity.this);
                                                        downLoadUtil.downloadAPK(result.getValue2(), "迈思最新版");
                                                    }
                                                }
                                            })
                                    .setNegativeButton("取消",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {

                                                }
                                            })
                                    .setOnCancelListener(
                                            new DialogInterface.OnCancelListener() {

                                                @Override
                                                public void onCancel(DialogInterface dialog) {

                                                }
                                            }).show();

                        }

                    }
                });
    }

    private void setCurrentTabState(int index) {
        rbBtns[index].setChecked(true);
        rbBtns[currentTabIndex].setChecked(false);
        currentTabIndex = index;
    }

}
