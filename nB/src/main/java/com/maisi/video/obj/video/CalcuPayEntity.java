package com.maisi.video.obj.video;

import java.io.Serializable;

/**
 * 功能
 * Created by Jiang on 2017/12/11.
 */

public class CalcuPayEntity implements Serializable {

    /**
     * points : 12
     * amount : 15
     */

    private double points;
    private double amount;

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
