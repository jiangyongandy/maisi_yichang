package com.maisi.video.obj.video;

import java.io.Serializable;

/**
 * 功能
 * Created by Jiang on 2017/12/15.
 */

public class WechatTipsEntity implements Serializable {


    /**
     * id : 1
     * remarkKey : cash
     * remarkName : null
     * remarkValue : 提现请加微信：634762028，零门槛提现，微信红包发放。
     * enable : 1
     */

    private int id;
    private String remarkKey;
    private Object remarkName;
    private String remarkValue;
    private int enable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemarkKey() {
        return remarkKey;
    }

    public void setRemarkKey(String remarkKey) {
        this.remarkKey = remarkKey;
    }

    public Object getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(Object remarkName) {
        this.remarkName = remarkName;
    }

    public String getRemarkValue() {
        return remarkValue;
    }

    public void setRemarkValue(String remarkValue) {
        this.remarkValue = remarkValue;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }
}
