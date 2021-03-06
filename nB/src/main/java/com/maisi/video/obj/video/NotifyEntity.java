package com.maisi.video.obj.video;

import java.io.Serializable;

/**
 * 功能
 * Created by Jiang on 2017/12/4.
 */

public class NotifyEntity implements Serializable {

    /**
     * id : 39
     * groupId : 38
     * key : gonggao
     * value1 : 卡顿，播放不了，不清晰，试试右上角切换线路（自备八条线路）！！！
     * value2 : null
     * description : null
     * enable : 1
     * created_time : null
     */

    private int id;
    private int groupId;
    private String key;
    private String value1;
    private Object value2;
    private Object description;
    private int enable;
    private Object created_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public Object getValue2() {
        return value2;
    }

    public void setValue2(Object value2) {
        this.value2 = value2;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public Object getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Object created_time) {
        this.created_time = created_time;
    }
}
