package com.hwl.beta.ui.imgselect.bean;

/**
 * Created by Administrator on 2018/4/4.
 */

public class ImageSelectBean {
    private int selectType;
    private String dirName;
    private String dirFullName;
    private String imageCount;

    public ImageSelectBean(int selectType) {
        this.selectType = selectType;
    }

    public int getSelectType() {
        return selectType;
    }

    public void setSelectType(int selectType) {
        this.selectType = selectType;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getDirFullName() {
        return dirFullName;
    }

    public void setDirFullName(String dirFullName) {
        this.dirFullName = dirFullName;
    }

    public String getImageCount() {
        return imageCount;
    }

    public void setImageCount(String imageCount) {
        this.imageCount = imageCount;
    }
}
