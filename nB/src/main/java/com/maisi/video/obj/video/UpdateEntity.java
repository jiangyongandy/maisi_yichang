package com.maisi.video.obj.video;

import java.io.Serializable;

/**
 * 功能
 * Created by Jiang on 2017/11/29.
 */

public class UpdateEntity implements Serializable {


    /**
     * value2 : http://39.108.151.95:8000/MyApp/
     * value1 : v1.0.1
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
