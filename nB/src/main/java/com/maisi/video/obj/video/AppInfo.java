package com.maisi.video.obj.video;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * 功能
 * Created by Jiang on 2017/12/6.
 */

public class AppInfo implements Serializable {


    private String   name;
    private String   packageName;
    private String   packagePath;
    private String   versionName;
    private int      versionCode;
    private boolean  isSystem;

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(final boolean isSystem) {
        this.isSystem = isSystem;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(final String packagePath) {
        this.packagePath = packagePath;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(final int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(final String versionName) {
        this.versionName = versionName;
    }

    /**
     * @param name        名称
     * @param icon        图标
     * @param packageName 包名
     * @param packagePath 包路径
     * @param versionName 版本号
     * @param versionCode 版本码
     * @param isSystem    是否系统应用
     */
    public AppInfo(String packageName, String name, Drawable icon, String packagePath,
                   String versionName, int versionCode, boolean isSystem) {
        this.setName(name);
        this.setPackageName(packageName);
        this.setPackagePath(packagePath);
        this.setVersionName(versionName);
        this.setVersionCode(versionCode);
        this.setSystem(isSystem);
    }

    @Override
    public String toString() {
        return "pkg name: " + getPackageName() +
                "\napp name: " + getName() +
                "\napp path: " + getPackagePath() +
                "\napp v name: " + getVersionName() +
                "\napp v code: " + getVersionCode() +
                "\nis system: " + isSystem();
    }

}
