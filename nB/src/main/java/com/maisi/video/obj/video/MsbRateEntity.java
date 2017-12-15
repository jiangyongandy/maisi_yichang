package com.maisi.video.obj.video;

import java.io.Serializable;

/**
 * 功能
 * Created by Jiang on 2017/12/15.
 */

public class MsbRateEntity implements Serializable {


    /**
     * value2 : 1迈思币=3圆
     * value1 : 3
     */

    private String value2;
    private String value1;

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }
}
