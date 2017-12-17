package org.cocos2dx.lua.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.maisi.video.obj.video.CashRequestEntity;
import com.maisi.video.obj.video.MsbRateEntity;
import com.maisi.video.obj.video.RecommendEntity;
import com.maisi.video.obj.video.UserInfoEntity;
import com.maisi.video.obj.video.WechatTipsEntity;
import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.BoyiRxUtils;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.service.Service;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class DaiLiActivity extends BaseActivity {

    @BindView(R.id.tv_recommend_no)
    TextView mTvRecommendNo;
    @BindView(R.id.tv_recommend_left)
    TextView mTvRecommendLeft;
    @BindView(R.id.tv_wechat_uid)
    TextView mTvWechatUid;
    @BindView(R.id.tv_wechat_tips)
    TextView mTvWechatTips;
    @BindView(R.id.tv_maisibi_tips)
    TextView mTvMaisibiTips;
    @BindView(R.id.tv_msb_rate)
    TextView mTvMsbRate;
    @BindView(R.id.tv_know)
    TextView mTvKnow;
    @BindView(R.id.rl_guide)
    RelativeLayout mRlGuide;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.rl_ti_xian)
    RelativeLayout rlTiXian;

    private RecommendEntity result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_daili);
        ButterKnife.bind(this);


        Service.getComnonService().getWechatTips()
                .compose(BoyiRxUtils.<WechatTipsEntity>applySchedulers())
                .subscribe(new Subscriber<WechatTipsEntity>() {
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
                    public void onNext(final WechatTipsEntity result) {

                        mTvWechatTips.setText(result.getRemarkValue());
                    }
                });

        Service.getComnonService().getMaiSiBiTips()
                .compose(BoyiRxUtils.<WechatTipsEntity>applySchedulers())
                .subscribe(new Subscriber<WechatTipsEntity>() {
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
                    public void onNext(final WechatTipsEntity result) {

                        mTvMaisibiTips.setText(result.getRemarkValue());
                    }
                });

        Service.getComnonService().getMaisibiRate()
                .compose(BoyiRxUtils.<MsbRateEntity>applySchedulers())
                .subscribe(new Subscriber<MsbRateEntity>() {
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
                    public void onNext(final MsbRateEntity result) {

                        mTvMsbRate.setText(result.getValue2());
                    }
                });

        if (!VipHelperUtils.getInstance().isWechatLogin())
            return;

        /*
        * 规则如下:迈思币达到360，或者达到360-1079之间，获得系统赠送迈思币50  个
        * 迈思币达到1080，或者达到1080-1799，获得系统赠送迈思币200个
          迈思币达到1800，或者达到1800-2879，获得系统赠送迈思币400个
        * */
        Service.getComnonService().requestLogin(VipHelperUtils.getInstance().getVipUserInfo().getUid())
                .compose(BoyiRxUtils.<UserInfoEntity>applySchedulers())
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
                    public void onNext(final UserInfoEntity result) {

                        if (result.getGiveAmount() != 0) {
                            mRlGuide.setVisibility(View.VISIBLE);
                            tvTip.setText("您的迈思币数额已达到" + result.getCommendLeft() + "，恭喜您获得额外迈思币奖励" + result.getGiveAmount() + "～");
                            mTvKnow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mRlGuide.setVisibility(View.GONE);

                                    Service.getComnonService().getMaisibi(VipHelperUtils.getInstance().getVipUserInfo().getUid(), result.getGiveAmount())
                                            .compose(BoyiRxUtils.<String>applySchedulers())
                                            .subscribe(new Subscriber<String>() {
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
                                                public void onNext(final String result) {
                                                    Toast.makeText(
                                                            APPAplication.instance,
                                                            "已领取奖励",
                                                            Toast.LENGTH_SHORT).show();

                                                }
                                            });


                                }
                            });
                        }

                    }
                });


        mTvWechatUid.setText(VipHelperUtils.getInstance().getVipUserInfo().getUid());
        Service.getComnonService().getRecommendNo(VipHelperUtils.getInstance().getUserInfo().getUnionid())
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
                .subscribe(new Subscriber<RecommendEntity>() {
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
                    public void onNext(RecommendEntity result) {
                        DaiLiActivity.this.result = result;
                        if (result.getCommendNo() != null)
                            mTvRecommendNo.setText(result.getCommendNo());
//                        if (result.getCommendLeft() != null)
                        mTvRecommendLeft.setText(result.getCommendLeft() + "迈思币\n当前兑换人民币为：" + result.getCommend2cash() + "元");
                    }
                });

    }

    @OnClick(R.id.rl_ti_xian)
    public void onClick() {

        if (!VipHelperUtils.getInstance().isWechatLogin()) {
            Toast.makeText(
                    APPAplication.instance,
                    "请先登录~~",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(DaiLiActivity.this.result == null) {
            Toast.makeText(
                    APPAplication.instance,
                    "请稍候再试~~",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        new MaterialDialog.Builder(this)
                .title("输入支付宝账号")
                .content("请确认输入正确哦")
                .input("请填写正确", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        if(input == null || input.length() == 0) {
                            Toast.makeText(
                                    APPAplication.instance,
                                    "请先输入提现账号~~",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        CashRequestEntity  entity = new CashRequestEntity ();
                        entity.setUid(VipHelperUtils.getInstance().getVipUserInfo().getUid());
                        entity.setAmount(DaiLiActivity.this.result.getCommend2cash());
                        entity.setMaisibi(VipHelperUtils.getInstance().getVipUserInfo().getCommendLeft());
                        entity.setPayeeAccount(input.toString());
                        Gson gson = new Gson();
                        String toJson = gson.toJson(entity);
                        RequestBody body = RequestBody.create(MediaType.parse("application/json"), toJson);
                        Service.getComnonService().alitransfer(body)
                                .compose(BoyiRxUtils.<String>applySchedulers())
                                .subscribe(new BoyiRxUtils.MySubscriber<String>() {

                                    @Override
                                    public void onNext(String result) {
                                        Toast.makeText(
                                                APPAplication.instance,
                                                "提现申请已提交，请耐心等待",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).show();

    }
}
