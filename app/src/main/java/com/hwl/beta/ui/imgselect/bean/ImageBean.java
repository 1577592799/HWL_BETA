package com.hwl.beta.ui.imgselect.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/1/20.
 */
public class ImageBean implements Parcelable {
    private String path;
    private boolean isSelect = false;

    public ImageBean() {
    }

    public ImageBean(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "path='" + path + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel in) {
            ImageBean bean = new ImageBean();
            bean.path = in.readString();
            //1: true  0:false
            bean.isSelect = in.readByte() != 0;
            return bean;
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        //1: true  0:false
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return true;
        ImageBean ib = (ImageBean) obj;
        if (ib.getPath().equals(this.getPath()))
            return true;
        return false;
    }
}
