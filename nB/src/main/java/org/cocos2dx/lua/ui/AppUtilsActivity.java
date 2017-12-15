package org.cocos2dx.lua.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.umeng.socialize.UMShareAPI;
import com.zuiai.nn.R;

import org.cocos2dx.lua.ui.common.CommonPageAdapter;
import org.cocos2dx.lua.ui.fragment.AppUtilsFragment;
import org.cocos2dx.lua.ui.fragment.HomeFragment;
import org.cocos2dx.lua.ui.fragment.PersonFragment2;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by JIANG on 2017/9/16.
 */

public class AppUtilsActivity extends BaseActivity {

    @BindView(R.id.rb_niuying)
    RadioButton mRbNiuying;
    @BindView(R.id.rb_person)
    RadioButton mRbPerson;
    @BindView(R.id.rb)
    RadioGroup mRb;
    @BindView(R.id.fragment_container)
    ViewPager mFragmentContainer;
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
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init() {
        setContentView(R.layout.activity_utils);
        mUnbinder = ButterKnife.bind(this);

        mHomeFragment = new AppUtilsFragment ();
        mPersonFragment = new PersonFragment2 ();
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
    }

    private void setCurrentTabState(int index) {
        rbBtns[index].setChecked(true);
        rbBtns[currentTabIndex].setChecked(false);
        currentTabIndex = index;
    }

}
