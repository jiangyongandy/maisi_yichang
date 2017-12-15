package com.maisi.video.obj.video;

import java.io.Serializable;

/**
 * 功能
 * Created by Jiang on 2017/12/11.
 */

public class ChargeRequestEntity implements Serializable {

    /**
     * uid : 1111
     * points : 12
     * commendno : sdafgag
     * amount : 15
     */

    private String uid;
    private double points;
    private String commendno;
    private double amount;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getCommendno() {
        return commendno;
    }

    public void setCommendno(String commendno) {
        this.commendno = commendno;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
